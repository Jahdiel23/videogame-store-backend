package com.tecnm.merida.videogame_store_backend.persistence.crud;

import com.tecnm.merida.videogame_store_backend.persistence.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CompraCrudRepository extends JpaRepository<Compra, Integer> {
    List<Compra> findByIdCliente(String idCliente);
}
