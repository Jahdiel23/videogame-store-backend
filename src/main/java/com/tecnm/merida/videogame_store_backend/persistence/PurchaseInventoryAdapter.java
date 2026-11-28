package com.tecnm.merida.videogame_store_backend.persistence;

import com.tecnm.merida.videogame_store_backend.domain.Videogame;
import com.tecnm.merida.videogame_store_backend.domain.repository.PurchaseInventoryRepository;
import com.tecnm.merida.videogame_store_backend.persistence.crud.ClienteCrudRepository;
import com.tecnm.merida.videogame_store_backend.persistence.crud.VideojuegoCrudRepository;
import com.tecnm.merida.videogame_store_backend.persistence.mapper.VideogameMapper;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public class PurchaseInventoryAdapter implements PurchaseInventoryRepository {
    private final ClienteCrudRepository clients;
    private final VideojuegoCrudRepository videogames;
    private final VideogameMapper mapper;
    public PurchaseInventoryAdapter(ClienteCrudRepository clients, VideojuegoCrudRepository videogames, VideogameMapper mapper) {
        this.clients = clients; this.videogames = videogames; this.mapper = mapper;
    }
    public boolean clientExists(String id) { return clients.existsById(id); }
    public Optional<Videogame> lockVideogame(int id) { return videogames.findLockedById(id).map(mapper::toVideogame); }
    public void updateStock(int id, int stock) {
        var entity = videogames.findById(id).orElseThrow();
        entity.setStock(stock);
        videogames.save(entity);
    }
}
