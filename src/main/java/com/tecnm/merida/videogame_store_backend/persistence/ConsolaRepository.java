package com.tecnm.merida.videogame_store_backend.persistence;

import com.tecnm.merida.videogame_store_backend.domain.GameConsole;
import com.tecnm.merida.videogame_store_backend.domain.repository.ConsoleRepository;
import com.tecnm.merida.videogame_store_backend.persistence.crud.ConsolaCrudRepository;
import com.tecnm.merida.videogame_store_backend.persistence.mapper.ConsoleMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public class ConsolaRepository implements ConsoleRepository {
    private final ConsolaCrudRepository crud;
    private final ConsoleMapper mapper;
    public ConsolaRepository(ConsolaCrudRepository crud, ConsoleMapper mapper) { this.crud = crud; this.mapper = mapper; }
    public List<GameConsole> getAll() { return crud.findAll().stream().map(mapper::toConsole).toList(); }
    public Optional<GameConsole> getConsole(int id) { return crud.findById(id).map(mapper::toConsole); }
    public GameConsole save(GameConsole value) { return mapper.toConsole(crud.saveAndFlush(mapper.toConsola(value))); }
    public void delete(int id) { crud.deleteById(id); crud.flush(); }
}
