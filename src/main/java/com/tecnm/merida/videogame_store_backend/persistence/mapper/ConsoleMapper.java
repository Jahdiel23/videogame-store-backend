package com.tecnm.merida.videogame_store_backend.persistence.mapper;

import com.tecnm.merida.videogame_store_backend.domain.GameConsole;
import com.tecnm.merida.videogame_store_backend.persistence.entity.Consola;
import org.mapstruct.*;
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ConsoleMapper {
    @Mapping(source = "idConsola", target = "consoleId")
    @Mapping(source = "nombre", target = "name")
    @Mapping(source = "fabricante", target = "manufacturer")
    @Mapping(source = "estado", target = "active")
    GameConsole toConsole(Consola consola);
    @InheritInverseConfiguration
    @Mapping(target = "videojuegos", ignore = true)
    Consola toConsola(GameConsole console);
}
