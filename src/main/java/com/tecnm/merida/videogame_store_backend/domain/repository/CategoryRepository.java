package com.tecnm.merida.videogame_store_backend.domain.repository;

import com.tecnm.merida.videogame_store_backend.domain.GameConsole;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    List<GameConsole> getAll();

    Optional<GameConsole> getCategory(int categoryId);

    GameConsole save(GameConsole category);

    void delete(int categoryId);
}
