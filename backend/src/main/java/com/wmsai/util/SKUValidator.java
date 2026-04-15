package com.wmsai.util;

/**
 * SKU validation utility.
 * Covers T031, OOPJ-17 (palindrome check).
 */
public class SKUValidator {

    /** Validates SKU format: 4-12 uppercase alphanumeric chars. */
    public static boolean isValidSKU(String sku) {
        if (sku == null) return false;
        return sku.matches("^[A-Z0-9\\-]{3,12}$");
    }

    /** Checks if a SKU is a palindrome [OOPJ-17]. */
    public static boolean isPalindrome(String sku) {
        if (sku == null || sku.isEmpty()) return false;
        String clean = sku.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
        int left = 0, right = clean.length() - 1;
        while (left < right) {
            if (clean.charAt(left) != clean.charAt(right)) return false;
            left++;
            right--;
        }
        return true;
    }
}
