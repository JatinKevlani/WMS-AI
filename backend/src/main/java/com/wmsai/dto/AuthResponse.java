package com.wmsai.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String id;
    private String token;
    private String email;
    private String fullName;
    private String role;
    private boolean active;
}
