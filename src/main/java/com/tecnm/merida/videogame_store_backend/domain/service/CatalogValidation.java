package com.tecnm.merida.videogame_store_backend.domain.service;

import com.tecnm.merida.videogame_store_backend.domain.GameConsole;
import com.tecnm.merida.videogame_store_backend.domain.Videogame;
import com.tecnm.merida.videogame_store_backend.domain.exception.CatalogException;
import java.math.BigDecimal;

public final class CatalogValidation {
    private CatalogValidation() {}
    public static void validId(int id) {
        if (id <= 0) throw new CatalogException(400, "El identificador debe ser mayor que cero.");
    }
    private static void text(String value, int max, String field) {
        if (value == null || value.isBlank() || value.trim().length() > max)
            throw new CatalogException(400, field + " es obligatorio y admite hasta " + max + " caracteres.");
    }
    public static void console(GameConsole value) {
        if (value == null) throw new CatalogException(400, "La consola es obligatoria.");
        text(value.getName(), 100, "name");
        text(value.getManufacturer(), 100, "manufacturer");
        if (value.getActive() == null) throw new CatalogException(400, "active es obligatorio.");
        value.setName(value.getName().trim());
        value.setManufacturer(value.getManufacturer().trim());
    }
    public static void videogame(Videogame value) {
        if (value == null) throw new CatalogException(400, "El videojuego es obligatorio.");
        text(value.getTitle(), 150, "title");
        text(value.getGenre(), 60, "genre");
        if (value.getConsoleId() == null || value.getConsoleId() <= 0)
            throw new CatalogException(400, "consoleId debe ser mayor que cero.");
        BigDecimal price = value.getPrice();
        if (price == null || price.signum() < 0 || price.compareTo(new BigDecimal("9999999999.99")) > 0
                || price.stripTrailingZeros().scale() > 2)
            throw new CatalogException(400, "price es obligatorio, no negativo y admite dos decimales.");
        if (value.getStock() == null || value.getStock() < 0)
            throw new CatalogException(400, "stock es obligatorio y no puede ser negativo.");
        if (value.getActive() == null) throw new CatalogException(400, "active es obligatorio.");
        value.setTitle(value.getTitle().trim());
        value.setGenre(value.getGenre().trim());
    }
}
