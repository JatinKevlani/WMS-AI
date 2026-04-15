package com.wmsai.exception;

/**
 * Checked exception thrown when a stock-out operation requests more
 * units than are available in inventory [OOPJ-22].
 */
public class InsufficientStockException extends Exception {
    public InsufficientStockException(String message) {
        super(message);
    }
}
