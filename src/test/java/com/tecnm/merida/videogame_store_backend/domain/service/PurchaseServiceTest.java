package com.tecnm.merida.videogame_store_backend.domain.service;

import com.tecnm.merida.videogame_store_backend.domain.*;
import com.tecnm.merida.videogame_store_backend.domain.exception.PurchaseException;
import com.tecnm.merida.videogame_store_backend.domain.repository.PurchaseInventoryRepository;
import com.tecnm.merida.videogame_store_backend.persistence.PurchaseRepository;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class PurchaseServiceTest {
    private final Inventory inventory = new Inventory();
    private final Purchases purchases = new Purchases();
    private final PurchaseService service = new PurchaseService(purchases, inventory);
    private Purchase request() {
        Purchase value = new Purchase(); value.setClientId("2"); value.setPaymentMethod("CASH");
        PurchaseItem item = new PurchaseItem(); item.setVideogameId(1); item.setQuantity(2);
        value.setItems(List.of(item)); return value;
    }
    @Test void serverReplacesSubmittedTotalsAndMetadata() {
        Purchase value = request(); value.setPurchaseId(999); value.setState("FAKE");
        value.getItems().getFirst().setTotal(new BigDecimal("0.01"));
        value.getItems().getFirst().setUnitPrice(new BigDecimal("0.01"));
        Purchase saved = service.save(value);
        assertEquals(new BigDecimal("39.90"), saved.getTotal());
        assertEquals(new BigDecimal("19.95"), saved.getItems().getFirst().getUnitPrice());
        assertEquals("COMPLETED", saved.getState()); assertEquals(1, saved.getPurchaseId());
        assertEquals(8, inventory.games.get(1).getStock());
    }
    @Test void rejectsMissingQuantityInsteadOfUnboxingNull() {
        Purchase value = request(); value.getItems().getFirst().setQuantity(null);
        assertEquals(400, assertThrows(PurchaseException.class, () -> service.save(value)).getStatus());
        assertEquals(0, purchases.data.size());
    }
    @Test void rejectsDuplicateGameBeforeSaving() {
        Purchase value = request(); value.setItems(List.of(value.getItems().getFirst(), value.getItems().getFirst()));
        assertEquals(400, assertThrows(PurchaseException.class, () -> service.save(value)).getStatus());
        assertEquals(0, purchases.data.size());
    }
    @Test void rejectsEmptyDetails() {
        Purchase value = request(); value.setItems(List.of());
        assertEquals(400, assertThrows(PurchaseException.class, () -> service.save(value)).getStatus());
    }
    @Test void rejectsNullDetail() {
        Purchase value = request(); value.setItems(Arrays.asList((PurchaseItem) null));
        assertEquals(400, assertThrows(PurchaseException.class, () -> service.save(value)).getStatus());
    }
    @Test void rejectsUnknownClient() {
        Purchase value = request(); value.setClientId("missing");
        assertEquals(404, assertThrows(PurchaseException.class, () -> service.save(value)).getStatus());
        assertEquals(0, purchases.data.size());
    }
    @Test void rejectsUnknownGame() {
        Purchase value = request(); value.getItems().getFirst().setVideogameId(999);
        assertEquals(404, assertThrows(PurchaseException.class, () -> service.save(value)).getStatus());
        assertEquals(0, purchases.data.size());
    }
    @Test void rejectsInsufficientStock() {
        Purchase value = request(); value.getItems().getFirst().setQuantity(11);
        assertEquals(409, assertThrows(PurchaseException.class, () -> service.save(value)).getStatus());
        assertEquals(10, inventory.games.get(1).getStock());
    }
    @Test void rejectsInactiveGame() {
        inventory.games.get(1).setActive(false);
        assertEquals(409, assertThrows(PurchaseException.class, () -> service.save(request())).getStatus());
    }
    @Test void rejectsInactiveConsole() {
        inventory.games.get(1).getGameConsole().setActive(false);
        assertEquals(409, assertThrows(PurchaseException.class, () -> service.save(request())).getStatus());
    }
    @Test void rejectsUnknownPaymentMethod() {
        Purchase value = request(); value.setPaymentMethod("INVALID");
        assertEquals(400, assertThrows(PurchaseException.class, () -> service.save(value)).getStatus());
    }
    @Test void locksGamesInSortedOrder() {
        Purchase value = request();
        PurchaseItem second = new PurchaseItem(); second.setVideogameId(2); second.setQuantity(1);
        value.setItems(List.of(second, value.getItems().getFirst()));
        service.save(value);
        assertEquals(List.of(1, 2), inventory.locked);
        assertEquals(new BigDecimal("59.85"), value.getTotal());
    }
    private static class Inventory implements PurchaseInventoryRepository {
        final Map<Integer, Videogame> games = new HashMap<>();
        final List<Integer> locked = new ArrayList<>();
        Inventory() {
            GameConsole console = new GameConsole(); console.setActive(true);
            for (int id : List.of(1, 2)) {
                Videogame game = new Videogame(); game.setVideogameId(id); game.setPrice(new BigDecimal("19.95"));
                game.setStock(10); game.setActive(true); game.setGameConsole(console); games.put(id, game);
            }
        }
        public boolean clientExists(String id) { return "2".equals(id); }
        public Optional<Videogame> lockVideogame(int id) { locked.add(id); return Optional.ofNullable(games.get(id)); }
        public void updateStock(int id, int stock) { games.get(id).setStock(stock); }
    }
    private static class Purchases implements PurchaseRepository {
        final Map<Integer, Purchase> data = new HashMap<>();
        public List<Purchase> getAll() { return new ArrayList<>(data.values()); }
        public Optional<Purchase> getPurchase(int id) { return Optional.ofNullable(data.get(id)); }
        public List<Purchase> getByClient(String id) { return data.values().stream().filter(p -> id.equals(p.getClientId())).toList(); }
        public Purchase save(Purchase value) { value.setPurchaseId(data.size() + 1); data.put(value.getPurchaseId(), value); return value; }
    }
}
