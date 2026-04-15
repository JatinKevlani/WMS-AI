package com.wmsai.service;

import com.wmsai.entity.Product;
import com.wmsai.exception.InsufficientStockException;
import com.wmsai.exception.ProductNotFoundException;
import com.wmsai.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles stock-in and stock-out operations.
 * Covers T029, PROD-06 through PROD-08.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {

    private final ProductRepository productRepository;
    private final AlertService alertService;

    /** Increases product stock quantity. */
    @Transactional
    public Product stockIn(int productId, int qty) {
        if (qty <= 0) throw new IllegalArgumentException("Stock-in quantity must be positive");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productId));

        product.setQuantity(product.getQuantity() + qty);
        Product saved = productRepository.save(product);

        // Check alert thresholds after stock change
        alertService.checkThresholds(saved);

        log.info("Stock-in: {} units added to {} (new qty: {})", qty, product.getSku(), saved.getQuantity());
        return saved;
    }

    /**
     * Decreases product stock quantity.
     * Throws InsufficientStockException (checked) if qty > available [OOPJ-22].
     */
    @Transactional
    public Product stockOut(int productId, int qty) throws InsufficientStockException {
        if (qty <= 0) throw new IllegalArgumentException("Stock-out quantity must be positive");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productId));

        if (qty > product.getQuantity()) {
            throw new InsufficientStockException(
                    "Insufficient stock for " + product.getSku() + ": requested " + qty + ", available " + product.getQuantity());
        }

        product.setQuantity(product.getQuantity() - qty);
        Product saved = productRepository.save(product);

        // Check alert thresholds after stock change
        alertService.checkThresholds(saved);

        log.info("Stock-out: {} units removed from {} (new qty: {})", qty, product.getSku(), saved.getQuantity());
        return saved;
    }
}
