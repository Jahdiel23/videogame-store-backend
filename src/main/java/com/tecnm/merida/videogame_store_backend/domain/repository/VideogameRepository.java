package com.tecnm.merida.videogame_store_backend.domain.repository;

import com.tecnm.merida.videogame_store_backend.domain.Videogame;
import java.util.List;
import java.util.Optional;
public interface VideogameRepository {
    List<Videogame> getAll();
    List<Videogame> getByConsole(int consoleId);
    boolean existsByConsole(int consoleId);
    Optional<Videogame> getVideogame(int videogameId);
    Videogame save(Videogame videogame);
    void delete(int videogameId);
}
