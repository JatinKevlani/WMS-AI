package com.wmsai.controller;

import com.wmsai.dto.AuthResponse;
import com.wmsai.dto.LoginRequest;
import com.wmsai.dto.RegisterRequest;
import com.wmsai.dto.UserSummaryDTO;
import com.wmsai.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Authentication endpoints.
 * Covers T014 â€” POST /api/auth/login, POST /api/auth/register.
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

    @GetMapping("/users")
    public ResponseEntity<List<UserSummaryDTO>> getVisibleUsers(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(authService.getVisibleUsers(userDetails.getUsername()));
    }

    /** POST /api/auth/logout â€” signals frontend to clear JWT [AUTH-03]. */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        // JWT is stateless â€” the frontend clears localStorage.
        // This endpoint exists to satisfy REST API contract.
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}
