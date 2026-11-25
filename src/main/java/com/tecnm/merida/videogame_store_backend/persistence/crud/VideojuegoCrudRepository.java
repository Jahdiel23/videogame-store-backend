package com.tecnm.merida.videogame_store_backend.persistence.crud;

import com.tecnm.merida.videogame_store_backend.persistence.entity.Videojuego;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface VideojuegoCrudRepository extends JpaRepository<Videojuego, Integer> {
    List<Videojuego> findByIdConsolaOrderByTituloAsc(int idConsola);
    boolean existsByIdConsola(int idConsola);
}
