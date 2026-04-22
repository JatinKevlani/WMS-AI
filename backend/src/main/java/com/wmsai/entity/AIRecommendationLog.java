package com.wmsai.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * AI recommendation audit log.
 * Covers T022, REC-07.
 */
@Entity
@Table(name = "ai_recommendation_log")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AIRecommendationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "recommendation_type", nullable = false)
    private String recommendationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "prompt_sent", columnDefinition = "TEXT")
    private String promptSent;

    @Column(name = "response_received", columnDefinition = "TEXT")
    private String responseReceived;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
