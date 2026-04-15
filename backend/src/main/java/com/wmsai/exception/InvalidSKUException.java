package com.wmsai.exception;

/**
 * Unchecked exception for invalid SKU format [OOPJ-23].
 */
public class InvalidSKUException extends RuntimeException {
    public InvalidSKUException(String message) {
        super(message);
    }
}
