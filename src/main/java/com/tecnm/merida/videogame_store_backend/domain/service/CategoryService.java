package com.tecnm.merida.videogame_store_backend.domain.service;

import com.tecnm.merida.videogame_store_backend.domain.GameConsole;
import com.tecnm.merida.videogame_store_backend.domain.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<GameConsole> getAll() {
        return categoryRepository.getAll();
    }

    public Optional<GameConsole> getCategory(int categoryId) {
        return categoryRepository.getCategory(categoryId);
    }

    public GameConsole save(GameConsole category) {
        return categoryRepository.save(category);
    }

    public Optional<GameConsole> update(int categoryId, GameConsole category) {
        return categoryRepository.getCategory(categoryId)
                .map(existingCategory -> {
                    category.setCategoryId(categoryId);
                    return categoryRepository.save(category);
                });
    }

    public boolean delete(int categoryId) {
        if (categoryRepository.getCategory(categoryId).isPresent()) {
            categoryRepository.delete(categoryId);
            return true;
        }
        return false;
    }
}
