package com.tecnm.merida.videogame_store_backend.domain.service;

import com.tecnm.merida.videogame_store_backend.domain.GameConsole;
import com.tecnm.merida.videogame_store_backend.domain.Videogame;
import com.tecnm.merida.videogame_store_backend.domain.exception.CatalogException;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class CatalogValidationTest {
    private Videogame game() {
        Videogame value = new Videogame();
        value.setTitle("Spider-Man 2"); value.setGenre("Accion");
        value.setConsoleId(1); value.setPrice(new BigDecimal("1299.00"));
        value.setStock(10); value.setActive(true);
        return value;
    }
    @Test void missingStockIsRejectedInsteadOfBecomingZero() {
        Videogame value = game(); value.setStock(null);
        assertEquals(400, assertThrows(CatalogException.class, () -> CatalogValidation.videogame(value)).getStatus());
    }
    @Test void negativePriceIsRejected() {
        Videogame value = game(); value.setPrice(new BigDecimal("-0.01"));
        assertThrows(CatalogException.class, () -> CatalogValidation.videogame(value));
    }
    @Test void moreThanTwoMonetaryDecimalsAreRejected() {
        Videogame value = game(); value.setPrice(new BigDecimal("19.999"));
        assertThrows(CatalogException.class, () -> CatalogValidation.videogame(value));
    }
    @Test void priceExceedingDatabasePrecisionIsRejected() {
        Videogame value = game(); value.setPrice(new BigDecimal("10000000000"));
        assertThrows(CatalogException.class, () -> CatalogValidation.videogame(value));
    }
    @Test void missingActiveIsRejectedInsteadOfBecomingFalse() {
        Videogame value = game(); value.setActive(null);
        assertThrows(CatalogException.class, () -> CatalogValidation.videogame(value));
    }
    @Test void missingConsoleIdIsRejected() {
        Videogame value = game(); value.setConsoleId(null);
        assertThrows(CatalogException.class, () -> CatalogValidation.videogame(value));
    }
    @Test void zeroStockAndInactiveAreValid() {
        Videogame value = game(); value.setStock(0); value.setActive(false);
        assertDoesNotThrow(() -> CatalogValidation.videogame(value));
    }
    @Test void manufacturerIsRequired() {
        GameConsole value = new GameConsole(); value.setName("PS5"); value.setActive(true);
        assertThrows(CatalogException.class, () -> CatalogValidation.console(value));
    }
    @Test void trimsConsoleNameAndManufacturer() {
        GameConsole value = new GameConsole(); value.setName(" PS5 "); value.setManufacturer(" Sony "); value.setActive(true);
        CatalogValidation.console(value);
        assertEquals("PS5", value.getName()); assertEquals("Sony", value.getManufacturer());
    }
}
