package com.wmsai.controller;

import com.wmsai.dto.*;
import com.wmsai.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication endpoints.
 * Covers T014 — POST /api/auth/login, POST /api/auth/register.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    /** POST /api/auth/logout — signals frontend to clear JWT [AUTH-03]. */
    @PostMapping("/logout")
    public ResponseEntity<java.util.Map<String, String>> logout() {
        // JWT is stateless — the frontend clears localStorage.
        // This endpoint exists to satisfy REST API contract.
        return ResponseEntity.ok(java.util.Map.of("message", "Logged out successfully"));
    }
}
