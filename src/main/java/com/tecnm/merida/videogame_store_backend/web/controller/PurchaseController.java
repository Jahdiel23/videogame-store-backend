package com.tecnm.merida.videogame_store_backend.web.controller;

import com.tecnm.merida.videogame_store_backend.domain.Purchase;
import com.tecnm.merida.videogame_store_backend.domain.service.PurchaseService;
import com.tecnm.merida.videogame_store_backend.persistence.mapper.PurchaseRequestMapper;
import com.tecnm.merida.videogame_store_backend.web.controller.dto.PurchaseRequest;
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
@RequestMapping("/purchases")
@Tag(name = "Purchases", description = "Sales with nested videogame details, totals and stock control")
@SecurityRequirement(name = "bearerAuth")
@ApiResponse(responseCode = "400", description = "Invalid request")
@ApiResponse(responseCode = "401", description = "Authentication required")
@ApiResponse(responseCode = "403", description = "Access denied")
public class PurchaseController {
    private final PurchaseService service;
    private final PurchaseRequestMapper mapper;
    public PurchaseController(PurchaseService service, PurchaseRequestMapper mapper) { this.service = service; this.mapper = mapper; }
    @GetMapping
    @Operation(summary = "List purchases with their details")
    @ApiResponse(responseCode = "200", description = "Purchases returned")
    public ResponseEntity<List<Purchase>> getAll() { return ResponseEntity.ok(service.getAll()); }
    @GetMapping("/{id}")
    @Operation(summary = "Get purchase by ID")
    @ApiResponse(responseCode = "200", description = "Purchase found")
    @ApiResponse(responseCode = "404", description = "Purchase not found")
    public ResponseEntity<Purchase> getPurchase(
            @Parameter(description = "Purchase ID", example = "1", required = true) @PathVariable("id") int id) {
        return ResponseEntity.ok(service.getPurchase(id));
    }
    @GetMapping("/client/{clientId}")
    @Operation(summary = "List purchases for a client")
    @ApiResponse(responseCode = "200", description = "Purchases returned; the list may be empty")
    @ApiResponse(responseCode = "404", description = "Client not found")
    public ResponseEntity<List<Purchase>> getByClient(
            @Parameter(description = "Client ID", example = "2", required = true) @PathVariable("clientId") String id) {
        return ResponseEntity.ok(service.getByClient(id));
    }
    @PostMapping
    @Operation(summary = "Create purchase and nested details", description = "Prices, totals, date and state are calculated by the server. CASH, CARD or TRANSFER. The complete purchase and stock changes are atomic.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "clientId": "2",
                      "paymentMethod": "CASH",
                      "comment": "Compra de videojuegos",
                      "items": [
                        {"videogameId": 1, "quantity": 2},
                        {"videogameId": 2, "quantity": 1}
                      ]
                    }
                    """))))
    @ApiResponse(responseCode = "201", description = "Purchase and details created")
    @ApiResponse(responseCode = "404", description = "Client or videogame not found")
    @ApiResponse(responseCode = "409", description = "Insufficient stock, inactive catalog record or data conflict")
    public ResponseEntity<Purchase> save(@RequestBody PurchaseRequest request) {
        Purchase saved = service.save(mapper.toPurchase(request));
        return ResponseEntity.created(URI.create("/purchases/" + saved.getPurchaseId())).body(saved);
    }
}
