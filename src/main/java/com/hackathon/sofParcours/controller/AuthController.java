package com.hackathon.sofParcours.controller;

import com.hackathon.sofParcours.controller.dto.AuthResponse;
import com.hackathon.sofParcours.controller.dto.LoginRequest;
import com.hackathon.sofParcours.controller.dto.RegisterRequest;
import com.hackathon.sofParcours.model.User;
import com.hackathon.sofParcours.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints pour l'authentification utilisateur")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Inscription d'un nouvel utilisateur")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            User user = authService.register(request.getUsername(), request.getEmail(), request.getPassword());
            String token = authService.login(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getUsername()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Connexion utilisateur")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            String token = authService.login(request.getEmail(), request.getPassword());
            User user = authService.getUserById(authService.login(request.getEmail(), request.getPassword()));
            return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getUsername()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(e.getMessage()));
        }
    }
}
