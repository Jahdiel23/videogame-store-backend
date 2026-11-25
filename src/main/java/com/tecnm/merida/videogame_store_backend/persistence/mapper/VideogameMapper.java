package com.tecnm.merida.videogame_store_backend.persistence.mapper;

import com.tecnm.merida.videogame_store_backend.domain.Videogame;
import com.tecnm.merida.videogame_store_backend.persistence.entity.Videojuego;
import org.mapstruct.*;
import java.util.List;
@Mapper(componentModel = "spring", uses = ConsoleMapper.class, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface VideogameMapper {
    @Mapping(source = "idVideojuego", target = "videogameId")
    @Mapping(source = "titulo", target = "title")
    @Mapping(source = "idConsola", target = "consoleId")
    @Mapping(source = "genero", target = "genre")
    @Mapping(source = "precio", target = "price")
    @Mapping(source = "stock", target = "stock")
    @Mapping(source = "estado", target = "active")
    @Mapping(source = "consola", target = "gameConsole")
    Videogame toVideogame(Videojuego videojuego);
    List<Videogame> toVideogames(List<Videojuego> videojuegos);
    @InheritInverseConfiguration
    @Mapping(target = "consola", ignore = true)
    Videojuego toVideojuego(Videogame videogame);
}
