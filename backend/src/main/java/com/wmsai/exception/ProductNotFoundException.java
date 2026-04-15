package com.wmsai.exception;

/**
 * Thrown when a product is not found by ID or SKU [OOPJ-23].
 */
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
