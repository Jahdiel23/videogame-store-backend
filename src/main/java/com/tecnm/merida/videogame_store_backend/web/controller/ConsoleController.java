package com.tecnm.merida.videogame_store_backend.web.controller;

import com.tecnm.merida.videogame_store_backend.domain.GameConsole;
import com.tecnm.merida.videogame_store_backend.domain.service.ConsoleService;
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
@RequestMapping("/consoles")
@Tag(name = "Consoles", description = "Videogame store catalog")
@SecurityRequirement(name = "bearerAuth")
@ApiResponse(responseCode = "400", description = "Invalid input")
@ApiResponse(responseCode = "401", description = "Authentication required")
@ApiResponse(responseCode = "403", description = "Access denied")
public class ConsoleController {
    private final ConsoleService service;
    public ConsoleController(ConsoleService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "List consoles")
    @ApiResponse(responseCode = "200", description = "Catalog returned")
    public ResponseEntity<List<GameConsole>> getAll() { return ResponseEntity.ok(service.getAll()); }

    @GetMapping("/{id}")
    @Operation(summary = "Find console by ID")
    @ApiResponse(responseCode = "200", description = "Record found")
    @ApiResponse(responseCode = "404", description = "Record not found")
    public ResponseEntity<GameConsole> getById(
            @Parameter(description = "Console ID", example = "1", required = true) @PathVariable("id") int id) {
        return ResponseEntity.ok(service.getConsole(id));
    }

    @PostMapping
    @Operation(summary = "Create console", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
            {"name":"PlayStation 5","manufacturer":"Sony","active":true}
            """))))
    @ApiResponse(responseCode = "201", description = "Record created")
    @ApiResponse(responseCode = "404", description = "Related console not found")
    public ResponseEntity<GameConsole> save(@RequestBody GameConsole value) {
        GameConsole saved = service.save(value);
        return ResponseEntity.created(URI.create("/consoles/" + saved.getConsoleId())).body(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Replace console", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
            {"name":"PlayStation 5","manufacturer":"Sony","active":true}
            """))))
    @ApiResponse(responseCode = "200", description = "Record updated")
    @ApiResponse(responseCode = "404", description = "Record or console not found")
    public ResponseEntity<GameConsole> update(
            @Parameter(description = "Console ID", example = "1", required = true) @PathVariable("id") int id,
            @RequestBody GameConsole value) {
        return ResponseEntity.ok(service.update(id, value));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete console")
    @ApiResponse(responseCode = "204", description = "Record deleted")
    @ApiResponse(responseCode = "404", description = "Record not found")
    @ApiResponse(responseCode = "409", description = "Record is referenced by other data")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Console ID", example = "1", required = true) @PathVariable("id") int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
