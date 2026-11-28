package com.tecnm.merida.videogame_store_backend.persistence;

import com.tecnm.merida.videogame_store_backend.domain.Purchase;
import com.tecnm.merida.videogame_store_backend.domain.PurchaseItem;
import com.tecnm.merida.videogame_store_backend.domain.exception.PurchaseException;
import com.tecnm.merida.videogame_store_backend.domain.service.PurchaseService;
import com.tecnm.merida.videogame_store_backend.persistence.crud.*;
import com.tecnm.merida.videogame_store_backend.persistence.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

// Separate in-memory database: these tests NEVER use the user's PostgreSQL data.
@SpringBootTest(properties = {
    "spring.profiles.active=test",
    "spring.datasource.url=jdbc:h2:mem:purchase_tests;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.open-in-view=false",
    "spring.sql.init.mode=never",
    "app.jwt.secret=MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY="
})
class PurchasePersistenceTest {
    @Autowired PurchaseService service;
    @Autowired CompraCrudRepository purchases;
    @Autowired ClienteCrudRepository clients;
    @Autowired ConsolaCrudRepository consoles;
    @Autowired VideojuegoCrudRepository games;
    @Autowired JdbcTemplate jdbc;
    private int firstId;
    private int secondId;

    @BeforeEach void prepare() {
        purchases.deleteAll(); games.deleteAll(); consoles.deleteAll(); clients.deleteAll();
        Cliente client = new Cliente(); client.setId("2"); client.setNombre("Jahdiel");
        client.setApellidos("Tzec"); client.setCorreoElectronico("test@example.invalid");
        clients.save(client);
        Consola console = new Consola(); console.setNombre("PS5"); console.setFabricante("Sony"); console.setEstado(true);
        console = consoles.saveAndFlush(console);
        firstId = game(console.getIdConsola(), "Game A", "19.95");
        secondId = game(console.getIdConsola(), "Game B", "50.00");
    }
    private int game(int consoleId, String name, String price) {
        Videojuego game = new Videojuego(); game.setTitulo(name); game.setIdConsola(consoleId);
        game.setGenero("Accion"); game.setPrecio(new BigDecimal(price)); game.setStock(10); game.setEstado(true);
        return games.saveAndFlush(game).getIdVideojuego();
    }
    private PurchaseItem item(int id, int quantity) {
        PurchaseItem item = new PurchaseItem(); item.setVideogameId(id); item.setQuantity(quantity); return item;
    }
    private Purchase request(PurchaseItem... items) {
        Purchase p = new Purchase(); p.setClientId("2"); p.setPaymentMethod("CASH"); p.setItems(List.of(items)); return p;
    }
    private int count(String table) { return jdbc.queryForObject("SELECT COUNT(*) FROM " + table, Integer.class); }
    private int stock(int id) { return jdbc.queryForObject("SELECT stock FROM videojuegos WHERE id_videojuego = ?", Integer.class, id); }

    @Test void cascadePersistsHeaderAndTwoDetailsWithGeneratedKey() {
        Purchase saved = service.save(request(item(firstId, 2), item(secondId, 1)));
        assertNotNull(saved.getPurchaseId());
        assertEquals(1, count("compras")); assertEquals(2, count("compras_productos"));
        assertEquals(new BigDecimal("89.90"), saved.getTotal());
        assertEquals(8, stock(firstId)); assertEquals(9, stock(secondId));
        Purchase loaded = service.getPurchase(saved.getPurchaseId());
        assertEquals(2, loaded.getItems().size()); assertEquals(saved.getTotal(), loaded.getTotal());
        assertNotNull(loaded.getDate());
        assertEquals("COMPLETED", loaded.getState());
    }
    @Test void insufficientStockOnSecondLineRollsBackEarlierStockChange() {
        assertEquals(409, assertThrows(PurchaseException.class,
                () -> service.save(request(item(firstId, 2), item(secondId, 11)))).getStatus());
        assertEquals(0, count("compras")); assertEquals(0, count("compras_productos"));
        assertEquals(10, stock(firstId)); assertEquals(10, stock(secondId));
    }
    @Test void detailFailureRollsBackAlreadyInsertedHeaderAndDetails() {
        // Fail during cascade insertion after the header has obtained its generated ID.
        jdbc.execute("ALTER TABLE compras_productos ADD CONSTRAINT reject_test_detail CHECK (cantidad <> 2)");
        try {
            assertThrows(DataIntegrityViolationException.class,
                    () -> service.save(request(item(firstId, 1), item(secondId, 2))));
            assertEquals(0, count("compras")); assertEquals(0, count("compras_productos"));
            assertEquals(10, stock(firstId)); assertEquals(10, stock(secondId));
        } finally {
            jdbc.execute("ALTER TABLE compras_productos DROP CONSTRAINT reject_test_detail");
        }
    }
    @Test void unknownGameDoesNotLeaveAPartialPurchase() {
        assertEquals(404, assertThrows(PurchaseException.class,
                () -> service.save(request(item(firstId, 1), item(Integer.MAX_VALUE, 1)))).getStatus());
        assertEquals(0, count("compras")); assertEquals(0, count("compras_productos"));
        assertEquals(10, stock(firstId));
    }
    @Test void historicalPriceSurvivesLaterCatalogChanges() {
        Purchase saved = service.save(request(item(firstId, 2)));
        Videojuego game = games.findById(firstId).orElseThrow(); game.setPrecio(new BigDecimal("99.00")); games.saveAndFlush(game);
        Purchase loaded = service.getPurchase(saved.getPurchaseId());
        assertEquals(new BigDecimal("19.95"), loaded.getItems().getFirst().getUnitPrice());
        assertEquals(new BigDecimal("39.90"), loaded.getTotal());
    }
}
