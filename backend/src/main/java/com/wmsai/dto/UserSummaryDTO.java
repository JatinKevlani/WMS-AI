package com.wmsai.dto;

import com.wmsai.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserSummaryDTO {
    private String id;
    private String email;
    private String fullName;
    private String role;
    private boolean active;
    private LocalDateTime createdAt;

    public static UserSummaryDTO from(User user) {
        return UserSummaryDTO.builder()
                .id(user.getId() != null ? user.getId().toString() : null)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole() != null ? user.getRole().name() : null)
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
