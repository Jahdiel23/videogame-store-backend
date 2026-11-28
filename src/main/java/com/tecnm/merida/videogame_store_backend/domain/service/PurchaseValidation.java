package com.tecnm.merida.videogame_store_backend.domain.service;

import com.tecnm.merida.videogame_store_backend.domain.Purchase;
import com.tecnm.merida.videogame_store_backend.domain.PurchaseItem;
import com.tecnm.merida.videogame_store_backend.domain.exception.PurchaseException;
import java.util.HashSet;
import java.util.Set;

public final class PurchaseValidation {
    private PurchaseValidation() {}
    public static void validate(Purchase value) {
        if (value == null) throw new PurchaseException(400, "La compra es obligatoria.");
        validClientId(value.getClientId());
        if (!Set.of("CASH", "CARD", "TRANSFER").contains(value.getPaymentMethod() == null ? "" : value.getPaymentMethod()))
            throw new PurchaseException(400, "paymentMethod debe ser CASH, CARD o TRANSFER.");
        if (value.getComment() != null && value.getComment().length() > 500)
            throw new PurchaseException(400, "comment admite hasta 500 caracteres.");
        if (value.getItems() == null || value.getItems().isEmpty() || value.getItems().size() > 100)
            throw new PurchaseException(400, "items debe contener entre 1 y 100 videojuegos.");
        Set<Integer> ids = new HashSet<>();
        for (PurchaseItem item : value.getItems()) {
            if (item == null || item.getVideogameId() == null || item.getVideogameId() <= 0)
                throw new PurchaseException(400, "Cada detalle requiere un videogameId positivo.");
            if (item.getQuantity() == null || item.getQuantity() < 1 || item.getQuantity() > 1000)
                throw new PurchaseException(400, "quantity debe estar entre 1 y 1000.");
            if (!ids.add(item.getVideogameId()))
                throw new PurchaseException(400, "No repitas un videojuego; usa una sola linea con su cantidad total.");
        }
    }
    public static void validClientId(String id) {
        if (id == null || id.isBlank() || id.length() > 20)
            throw new PurchaseException(400, "clientId es obligatorio y admite hasta 20 caracteres.");
    }
    public static void validId(int id) {
        if (id <= 0) throw new PurchaseException(400, "El identificador debe ser positivo.");
    }
}
