package com.tecnm.merida.videogame_store_backend.persistence.crud;

import com.tecnm.merida.videogame_store_backend.persistence.entity.Videojuego;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface VideojuegoCrudRepository extends JpaRepository<Videojuego, Integer> {
    List<Videojuego> findByIdConsolaOrderByTituloAsc(int idConsola);
    boolean existsByIdConsola(int idConsola);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select v from Videojuego v where v.idVideojuego = :id")
    Optional<Videojuego> findLockedById(@Param("id") int id);
}
