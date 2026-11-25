package com.tecnm.merida.videogame_store_backend.web.controller;

import com.tecnm.merida.videogame_store_backend.security.AuthService;
import com.tecnm.merida.videogame_store_backend.web.controller.dto.LoginRequest;
import com.tecnm.merida.videogame_store_backend.web.controller.dto.LoginResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {

        String token = authService.login(
                request.getCorreoElectronico(),
                request.getPassword()
        );

        return ResponseEntity.ok(new LoginResponse(token));
    }
}
