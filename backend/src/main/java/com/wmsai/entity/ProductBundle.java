package com.wmsai.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Saved bundle recommendation for a product.
 * Covers T022, REC-04, REC-05.
 */
@Entity
@Table(name = "product_bundles")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ProductBundle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "bundle_product_name", nullable = false)
    private String bundleProductName;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "saved_at", nullable = false)
    @Builder.Default
    private LocalDateTime savedAt = LocalDateTime.now();
}
