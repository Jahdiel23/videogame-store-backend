package com.tecnm.merida.videogame_store_backend.persistence;

import com.tecnm.merida.videogame_store_backend.domain.Purchase;
import com.tecnm.merida.videogame_store_backend.persistence.crud.CompraCrudRepository;
import com.tecnm.merida.videogame_store_backend.persistence.entity.Compra;
import com.tecnm.merida.videogame_store_backend.persistence.mapper.PurchaseMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public class CompraRepository implements PurchaseRepository {
    private final CompraCrudRepository crud;
    private final PurchaseMapper mapper;
    public CompraRepository(CompraCrudRepository crud, PurchaseMapper mapper) { this.crud = crud; this.mapper = mapper; }
    public List<Purchase> getAll() { return mapper.toPurchases(crud.findAll()); }
    public Optional<Purchase> getPurchase(int id) { return crud.findById(id).map(mapper::toPurchase); }
    public List<Purchase> getByClient(String id) { return mapper.toPurchases(crud.findByIdCliente(id)); }
    public Purchase save(Purchase value) {
        Compra entity = mapper.toCompra(value);
        entity.getProductos().forEach(item -> item.setCompra(entity));
        // CascadeType.ALL on Compra.productos persists details; no individual detail saves.
        return mapper.toPurchase(crud.saveAndFlush(entity));
    }
}
