package com.wmsai.exception;

/**
 * Unchecked exception thrown when a duplicate SKU is detected [OOPJ-23].
 */
public class DuplicateSKUException extends RuntimeException {
    public DuplicateSKUException(String message) {
        super(message);
    }
}
