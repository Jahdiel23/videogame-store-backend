package com.tecnm.merida.videogame_store_backend.domain.repository;

import com.tecnm.merida.videogame_store_backend.domain.Videogame;
import java.util.Optional;
public interface PurchaseInventoryRepository {
    boolean clientExists(String clientId);
    Optional<Videogame> lockVideogame(int id);
    void updateStock(int id, int stock);
}
