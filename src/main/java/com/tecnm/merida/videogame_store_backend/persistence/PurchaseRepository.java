package com.tecnm.merida.videogame_store_backend.persistence;

import com.tecnm.merida.videogame_store_backend.domain.Purchase;
import java.util.List;
import java.util.Optional;
public interface PurchaseRepository {
    List<Purchase> getAll();
    Optional<Purchase> getPurchase(int id);
    List<Purchase> getByClient(String clientId);
    Purchase save(Purchase purchase);
}
