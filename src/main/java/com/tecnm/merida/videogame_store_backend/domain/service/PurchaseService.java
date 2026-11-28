package com.tecnm.merida.videogame_store_backend.domain.service;

import com.tecnm.merida.videogame_store_backend.domain.Purchase;
import com.tecnm.merida.videogame_store_backend.domain.PurchaseItem;
import com.tecnm.merida.videogame_store_backend.domain.Videogame;
import com.tecnm.merida.videogame_store_backend.domain.exception.PurchaseException;
import com.tecnm.merida.videogame_store_backend.domain.repository.PurchaseInventoryRepository;
import com.tecnm.merida.videogame_store_backend.persistence.PurchaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class PurchaseService {
    private final PurchaseRepository purchases;
    private final PurchaseInventoryRepository inventory;
    public PurchaseService(PurchaseRepository purchases, PurchaseInventoryRepository inventory) {
        this.purchases = purchases; this.inventory = inventory;
    }
    public List<Purchase> getAll() { return purchases.getAll(); }
    public Purchase getPurchase(int id) {
        PurchaseValidation.validId(id);
        return purchases.getPurchase(id).orElseThrow(() -> new PurchaseException(404, "Compra no encontrada."));
    }
    public List<Purchase> getByClient(String id) {
        PurchaseValidation.validClientId(id);
        if (!inventory.clientExists(id)) throw new PurchaseException(404, "Cliente no encontrado.");
        return purchases.getByClient(id);
    }
    @Transactional
    public Purchase save(Purchase purchase) {
        PurchaseValidation.validate(purchase);
        if (!inventory.clientExists(purchase.getClientId())) throw new PurchaseException(404, "Cliente no encontrado.");
        // Acquire all row locks in the same order to avoid opposite-order deadlocks.
        Map<Integer, Videogame> games = new TreeMap<>();
        purchase.getItems().stream().map(PurchaseItem::getVideogameId).sorted().forEach(id ->
                games.put(id, inventory.lockVideogame(id)
                        .orElseThrow(() -> new PurchaseException(404, "Videojuego no encontrado: " + id))));
        LocalDateTime now = LocalDateTime.now();
        for (PurchaseItem item : purchase.getItems()) {
            Videogame game = games.get(item.getVideogameId());
            if (!Boolean.TRUE.equals(game.getActive()) || game.getGameConsole() == null
                    || !Boolean.TRUE.equals(game.getGameConsole().getActive()))
                throw new PurchaseException(409, "El videojuego o su consola esta inactivo: " + game.getVideogameId());
            if (game.getPrice() == null || game.getPrice().signum() < 0)
                throw new PurchaseException(409, "El videojuego no tiene un precio valido.");
            if (game.getStock() == null || game.getStock() < item.getQuantity())
                throw new PurchaseException(409, "Existencias insuficientes para el videojuego " + game.getVideogameId());
            BigDecimal unitPrice;
            try {
                unitPrice = game.getPrice().setScale(2, RoundingMode.UNNECESSARY);
            } catch (ArithmeticException ex) {
                throw new PurchaseException(409, "El precio del catalogo debe tener como maximo dos decimales.");
            }
            item.setUnitPrice(unitPrice);
            item.setTotal(unitPrice.multiply(BigDecimal.valueOf(item.getQuantity())));
            item.setActive(true);
            item.setDate(now);
            inventory.updateStock(game.getVideogameId(), game.getStock() - item.getQuantity());
        }
        purchase.setPurchaseId(null);
        purchase.setDate(now);
        purchase.setState("COMPLETED");
        return purchases.save(purchase);
    }
}
