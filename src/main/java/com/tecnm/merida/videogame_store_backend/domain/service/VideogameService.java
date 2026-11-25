package com.tecnm.merida.videogame_store_backend.domain.service;

import com.tecnm.merida.videogame_store_backend.domain.Videogame;
import com.tecnm.merida.videogame_store_backend.domain.exception.CatalogException;
import com.tecnm.merida.videogame_store_backend.domain.repository.ConsoleRepository;
import com.tecnm.merida.videogame_store_backend.domain.repository.VideogameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class VideogameService {
    private final VideogameRepository videogames;
    private final ConsoleRepository consoles;
    public VideogameService(VideogameRepository videogames, ConsoleRepository consoles) {
        this.videogames = videogames;
        this.consoles = consoles;
    }
    public List<Videogame> getAll() { return videogames.getAll(); }
    public Videogame getVideogame(int id) {
        CatalogValidation.validId(id);
        return videogames.getVideogame(id).orElseThrow(() -> new CatalogException(404, "Videojuego no encontrado."));
    }
    public List<Videogame> getByConsole(int id) {
        requireConsole(id);
        return videogames.getByConsole(id);
    }
    private void requireConsole(int id) {
        CatalogValidation.validId(id);
        if (consoles.getConsole(id).isEmpty()) throw new CatalogException(404, "Consola no encontrada.");
    }
    @Transactional
    public Videogame save(Videogame value) {
        CatalogValidation.videogame(value);
        if (value.getVideogameId() != null) throw new CatalogException(400, "Omite videogameId al crear un juego.");
        requireConsole(value.getConsoleId());
        value.setGameConsole(null);
        return videogames.save(value);
    }
    @Transactional
    public Videogame update(int id, Videogame value) {
        getVideogame(id);
        CatalogValidation.videogame(value);
        if (value.getVideogameId() != null && value.getVideogameId() != id)
            throw new CatalogException(400, "videogameId no coincide con la URL.");
        requireConsole(value.getConsoleId());
        value.setVideogameId(id);
        value.setGameConsole(null);
        return videogames.save(value);
    }
    @Transactional
    public void delete(int id) {
        getVideogame(id);
        videogames.delete(id);
    }
}
