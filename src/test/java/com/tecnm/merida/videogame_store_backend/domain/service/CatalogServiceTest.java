package com.tecnm.merida.videogame_store_backend.domain.service;

import com.tecnm.merida.videogame_store_backend.domain.GameConsole;
import com.tecnm.merida.videogame_store_backend.domain.Videogame;
import com.tecnm.merida.videogame_store_backend.domain.exception.CatalogException;
import com.tecnm.merida.videogame_store_backend.domain.repository.ConsoleRepository;
import com.tecnm.merida.videogame_store_backend.domain.repository.VideogameRepository;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class CatalogServiceTest {
    private final Consoles consoles = new Consoles();
    private final Games games = new Games();
    private final ConsoleService consoleService = new ConsoleService(consoles, games);
    private final VideogameService gameService = new VideogameService(games, consoles);

    private GameConsole console() {
        GameConsole value = new GameConsole();
        value.setName("PS5"); value.setManufacturer("Sony"); value.setActive(true);
        return value;
    }
    private Videogame game(int consoleId) {
        Videogame value = new Videogame();
        value.setTitle("Spider-Man 2"); value.setGenre("Accion"); value.setConsoleId(consoleId);
        value.setPrice(new BigDecimal("1299.00")); value.setStock(10); value.setActive(true);
        return value;
    }
    @Test void cannotSaveAGameForAMissingConsole() {
        assertEquals(404, assertThrows(CatalogException.class, () -> gameService.save(game(99))).getStatus());
        assertEquals(0, games.data.size());
    }
    @Test void cannotDeleteAConsoleWithGames() {
        int id = consoleService.save(console()).getConsoleId();
        gameService.save(game(id));
        assertEquals(409, assertThrows(CatalogException.class, () -> consoleService.delete(id)).getStatus());
        assertEquals(1, consoles.data.size());
    }
    @Test void canDeleteGameThenItsConsole() {
        int consoleId = consoleService.save(console()).getConsoleId();
        int gameId = gameService.save(game(consoleId)).getVideogameId();
        gameService.delete(gameId); consoleService.delete(consoleId);
        assertEquals(0, consoles.data.size()); assertEquals(0, games.data.size());
    }
    @Test void updateMissingGameDoesNotCreateARecord() {
        assertEquals(404, assertThrows(CatalogException.class, () -> gameService.update(99, game(1))).getStatus());
        assertEquals(0, games.data.size());
    }
    @Test void rejectPostWithAnExistingId() {
        GameConsole value = console(); value.setConsoleId(10);
        assertEquals(400, assertThrows(CatalogException.class, () -> consoleService.save(value)).getStatus());
        assertEquals(0, consoles.data.size());
    }
    @Test void rejectBodyIdThatDiffersFromPath() {
        int id = consoleService.save(console()).getConsoleId();
        GameConsole replacement = console(); replacement.setConsoleId(id + 1);
        assertEquals(400, assertThrows(CatalogException.class, () -> consoleService.update(id, replacement)).getStatus());
    }
    @Test void existingConsoleWithoutGamesReturnsEmptyList() {
        int id = consoleService.save(console()).getConsoleId();
        assertEquals(List.of(), gameService.getByConsole(id));
    }
    private static class Consoles implements ConsoleRepository {
        final Map<Integer, GameConsole> data = new HashMap<>();
        private int nextId = 1;
        public List<GameConsole> getAll() { return new ArrayList<>(data.values()); }
        public Optional<GameConsole> getConsole(int id) { return Optional.ofNullable(data.get(id)); }
        public GameConsole save(GameConsole value) {
            if (value.getConsoleId() == null) value.setConsoleId(nextId++);
            data.put(value.getConsoleId(), value); return value;
        }
        public void delete(int id) { data.remove(id); }
    }
    private static class Games implements VideogameRepository {
        final Map<Integer, Videogame> data = new HashMap<>();
        private int nextId = 1;
        public List<Videogame> getAll() { return new ArrayList<>(data.values()); }
        public List<Videogame> getByConsole(int id) {
            return data.values().stream().filter(v -> v.getConsoleId() == id).toList();
        }
        public boolean existsByConsole(int id) { return !getByConsole(id).isEmpty(); }
        public Optional<Videogame> getVideogame(int id) { return Optional.ofNullable(data.get(id)); }
        public Videogame save(Videogame value) {
            if (value.getVideogameId() == null) value.setVideogameId(nextId++);
            data.put(value.getVideogameId(), value); return value;
        }
        public void delete(int id) { data.remove(id); }
    }
}
