package com.wmsai.service;

import com.wmsai.dto.*;
import com.wmsai.entity.Role;
import com.wmsai.entity.User;
import com.wmsai.repository.UserRepository;
import com.wmsai.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Authentication service handling login and user registration.
 * Covers T013.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    /** Authenticates user and returns JWT token. */
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return AuthResponse.builder()
                .id(user.getId().toString())
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .active(user.isActive())
                .build();
    }

    /** Registers a new user (ADMIN-only operation). */
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + request.getEmail());
        }

        Role role = (request.getRole() != null && request.getRole().equalsIgnoreCase("ADMIN"))
                ? Role.ADMIN : Role.STAFF;

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(role)
                .isActive(true)
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return AuthResponse.builder()
                .id(user.getId().toString())
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .active(user.isActive())
                .build();
    }

    public java.util.List<UserSummaryDTO> getVisibleUsers(String requesterEmail) {
        User requester = userRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + requesterEmail));

        if (requester.getRole() == Role.ADMIN) {
            return userRepository.findAll().stream()
                    .sorted(java.util.Comparator.comparing(User::getFullName, String.CASE_INSENSITIVE_ORDER))
                    .map(UserSummaryDTO::from)
                    .toList();
        }

        return java.util.List.of(UserSummaryDTO.from(requester));
    }
}
