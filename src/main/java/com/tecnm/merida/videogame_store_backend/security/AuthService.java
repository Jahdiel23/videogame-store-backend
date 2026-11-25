package com.tecnm.merida.videogame_store_backend.security;

import com.tecnm.merida.videogame_store_backend.persistence.crud.ClienteCrudRepository;
import com.tecnm.merida.videogame_store_backend.persistence.entity.Cliente;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final ClienteCrudRepository clienteCrudRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(
            ClienteCrudRepository clienteCrudRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil
    ) {
        this.clienteCrudRepository = clienteCrudRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String login(String correoElectronico, String password) {

        Cliente cliente = clienteCrudRepository
                .findByCorreoElectronico(correoElectronico)
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));

        if (!passwordEncoder.matches(password, cliente.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        return jwtUtil.generateToken(cliente.getId());
    }
}
