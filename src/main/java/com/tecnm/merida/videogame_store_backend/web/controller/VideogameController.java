package com.tecnm.merida.videogame_store_backend.web.controller;

import com.tecnm.merida.videogame_store_backend.domain.Videogame;
import com.tecnm.merida.videogame_store_backend.domain.service.VideogameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/videogames")
@Tag(name = "Videogames", description = "Videogame store catalog")
@SecurityRequirement(name = "bearerAuth")
@ApiResponse(responseCode = "400", description = "Invalid input")
@ApiResponse(responseCode = "401", description = "Authentication required")
@ApiResponse(responseCode = "403", description = "Access denied")
public class VideogameController {
    private final VideogameService service;
    public VideogameController(VideogameService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "List videogames")
    @ApiResponse(responseCode = "200", description = "Catalog returned")
    public ResponseEntity<List<Videogame>> getAll() { return ResponseEntity.ok(service.getAll()); }

    @GetMapping("/{id}")
    @Operation(summary = "Find videogame by ID")
    @ApiResponse(responseCode = "200", description = "Record found")
    @ApiResponse(responseCode = "404", description = "Record not found")
    public ResponseEntity<Videogame> getById(
            @Parameter(description = "Videogame ID", example = "1", required = true) @PathVariable("id") int id) {
        return ResponseEntity.ok(service.getVideogame(id));
    }

    @GetMapping("/console/{consoleId}")
    @Operation(summary = "List videogames by console")
    @ApiResponse(responseCode = "200", description = "Games returned; the list may be empty")
    @ApiResponse(responseCode = "404", description = "Console not found")
    public ResponseEntity<List<Videogame>> getByConsole(
            @Parameter(description = "Console ID", example = "1", required = true) @PathVariable("consoleId") int id) {
        return ResponseEntity.ok(service.getByConsole(id));
    }

    @PostMapping
    @Operation(summary = "Create videogame", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
            {"title":"Spider-Man 2","consoleId":1,"genre":"Accion","price":1299.00,"stock":10,"active":true}
            """))))
    @ApiResponse(responseCode = "201", description = "Record created")
    @ApiResponse(responseCode = "404", description = "Related console not found")
    public ResponseEntity<Videogame> save(@RequestBody Videogame value) {
        Videogame saved = service.save(value);
        return ResponseEntity.created(URI.create("/videogames/" + saved.getVideogameId())).body(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Replace videogame", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
            {"title":"Spider-Man 2","consoleId":1,"genre":"Accion","price":1299.00,"stock":10,"active":true}
            """))))
    @ApiResponse(responseCode = "200", description = "Record updated")
    @ApiResponse(responseCode = "404", description = "Record or console not found")
    public ResponseEntity<Videogame> update(
            @Parameter(description = "Videogame ID", example = "1", required = true) @PathVariable("id") int id,
            @RequestBody Videogame value) {
        return ResponseEntity.ok(service.update(id, value));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete videogame")
    @ApiResponse(responseCode = "204", description = "Record deleted")
    @ApiResponse(responseCode = "404", description = "Record not found")
    @ApiResponse(responseCode = "409", description = "Record is referenced by other data")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Videogame ID", example = "1", required = true) @PathVariable("id") int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
