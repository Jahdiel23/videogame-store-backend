package com.tecnm.merida.videogame_store_backend.domain.repository;

import com.tecnm.merida.videogame_store_backend.domain.GameConsole;
import java.util.List;
import java.util.Optional;
public interface ConsoleRepository {
    List<GameConsole> getAll();
    Optional<GameConsole> getConsole(int consoleId);
    GameConsole save(GameConsole console);
    void delete(int consoleId);
}
