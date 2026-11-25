package com.tecnm.merida.videogame_store_backend.domain.service;

import com.tecnm.merida.videogame_store_backend.domain.GameConsole;
import com.tecnm.merida.videogame_store_backend.domain.exception.CatalogException;
import com.tecnm.merida.videogame_store_backend.domain.repository.ConsoleRepository;
import com.tecnm.merida.videogame_store_backend.domain.repository.VideogameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ConsoleService {
    private final ConsoleRepository consoles;
    private final VideogameRepository videogames;
    public ConsoleService(ConsoleRepository consoles, VideogameRepository videogames) {
        this.consoles = consoles;
        this.videogames = videogames;
    }
    public List<GameConsole> getAll() { return consoles.getAll(); }
    public GameConsole getConsole(int id) {
        CatalogValidation.validId(id);
        return consoles.getConsole(id).orElseThrow(() -> new CatalogException(404, "Consola no encontrada."));
    }
    @Transactional
    public GameConsole save(GameConsole value) {
        CatalogValidation.console(value);
        if (value.getConsoleId() != null) throw new CatalogException(400, "Omite consoleId al crear una consola.");
        return consoles.save(value);
    }
    @Transactional
    public GameConsole update(int id, GameConsole value) {
        getConsole(id);
        CatalogValidation.console(value);
        if (value.getConsoleId() != null && value.getConsoleId() != id)
            throw new CatalogException(400, "consoleId no coincide con la URL.");
        value.setConsoleId(id);
        return consoles.save(value);
    }
    @Transactional
    public void delete(int id) {
        getConsole(id);
        if (videogames.existsByConsole(id))
            throw new CatalogException(409, "La consola tiene videojuegos asociados; elimina o reasigna esos juegos primero.");
        consoles.delete(id);
    }
}
