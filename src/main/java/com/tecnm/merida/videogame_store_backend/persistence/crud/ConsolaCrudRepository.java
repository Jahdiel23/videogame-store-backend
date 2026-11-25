package com.tecnm.merida.videogame_store_backend.persistence.crud;

import com.tecnm.merida.videogame_store_backend.persistence.entity.Consola;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ConsolaCrudRepository extends JpaRepository<Consola, Integer> {}
