package com.tecnm.merida.videogame_store_backend.persistence;

import com.tecnm.merida.videogame_store_backend.domain.Videogame;
import com.tecnm.merida.videogame_store_backend.domain.repository.VideogameRepository;
import com.tecnm.merida.videogame_store_backend.persistence.crud.ConsolaCrudRepository;
import com.tecnm.merida.videogame_store_backend.persistence.crud.VideojuegoCrudRepository;
import com.tecnm.merida.videogame_store_backend.persistence.entity.Videojuego;
import com.tecnm.merida.videogame_store_backend.persistence.mapper.VideogameMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public class VideojuegoRepository implements VideogameRepository {
    private final VideojuegoCrudRepository crud;
    private final ConsolaCrudRepository consoles;
    private final VideogameMapper mapper;
    public VideojuegoRepository(VideojuegoCrudRepository crud, ConsolaCrudRepository consoles, VideogameMapper mapper) {
        this.crud = crud; this.consoles = consoles; this.mapper = mapper;
    }
    public List<Videogame> getAll() { return mapper.toVideogames(crud.findAll()); }
    public List<Videogame> getByConsole(int id) { return mapper.toVideogames(crud.findByIdConsolaOrderByTituloAsc(id)); }
    public boolean existsByConsole(int id) { return crud.existsByIdConsola(id); }
    public Optional<Videogame> getVideogame(int id) { return crud.findById(id).map(mapper::toVideogame); }
    public Videogame save(Videogame value) {
        Videojuego entity = mapper.toVideojuego(value);
        entity.setConsola(consoles.getReferenceById(value.getConsoleId()));
        return mapper.toVideogame(crud.saveAndFlush(entity));
    }
    public void delete(int id) { crud.deleteById(id); crud.flush(); }
}
