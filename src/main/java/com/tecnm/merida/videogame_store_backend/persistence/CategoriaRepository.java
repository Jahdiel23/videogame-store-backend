package com.tecnm.merida.videogame_store_backend.persistence;

import com.tecnm.merida.videogame_store_backend.domain.GameConsole;
import com.tecnm.merida.videogame_store_backend.domain.repository.CategoryRepository;
import com.tecnm.merida.videogame_store_backend.persistence.crud.CategoriaCrudRepository;
import com.tecnm.merida.videogame_store_backend.persistence.entity.Categoria;
import com.tecnm.merida.videogame_store_backend.persistence.mapper.CategoryMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoriaRepository implements CategoryRepository {

    private final CategoriaCrudRepository categoriaCrudRepository;
    private final CategoryMapper mapper;

    public CategoriaRepository(
            CategoriaCrudRepository categoriaCrudRepository,
            CategoryMapper mapper) {
        this.categoriaCrudRepository = categoriaCrudRepository;
        this.mapper = mapper;
    }

    @Override
    public List<GameConsole> getAll() {
        List<Categoria> categorias =
                (List<Categoria>) categoriaCrudRepository.findAll();

        return categorias.stream()
                .map(mapper::toCategory)
                .toList();
    }

    @Override
    public Optional<GameConsole> getCategory(int categoryId) {
        return categoriaCrudRepository.findById(categoryId)
                .map(mapper::toCategory);
    }

    @Override
    public GameConsole save(GameConsole category) {
        Categoria categoria = mapper.toCategoria(category);

        return mapper.toCategory(
                categoriaCrudRepository.save(categoria)
        );
    }

    @Override
    public void delete(int categoryId) {
        categoriaCrudRepository.deleteById(categoryId);
    }
}
