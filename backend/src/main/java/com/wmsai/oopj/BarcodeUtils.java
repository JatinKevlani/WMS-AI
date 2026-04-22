package com.wmsai.oopj;

/**
 * Binary-to-Decimal utility with NumberFormatException handling.
 * Covers OOPJ Practical #26 — bin2Dec with NumberFormatException.
 */
public class BarcodeUtils {

    /**
     * Converts a binary string to its decimal integer value.
     * Throws NumberFormatException if the input is not a valid binary string.
     *
     * @param binaryString a string of '0' and '1' characters
     * @return the decimal equivalent
     * @throws NumberFormatException if the string contains non-binary characters
     */
    public static int bin2Dec(String binaryString) {
        if (binaryString == null || binaryString.isEmpty()) {
            throw new NumberFormatException("Binary string cannot be null or empty");
        }

        for (char c : binaryString.toCharArray()) {
            if (c != '0' && c != '1') {
                throw new NumberFormatException(
                        "Invalid binary character '" + c + "' in string: " + binaryString);
            }
        }

        int decimal = 0;
        for (int i = 0; i < binaryString.length(); i++) {
            int bit = binaryString.charAt(i) - '0';
            decimal = decimal * 2 + bit;
        }
        return decimal;
    }

    /**
     * Converts a decimal integer to its binary string representation.
     *
     * @param decimal the integer to convert
     * @return binary string
     */
    public static String dec2Bin(int decimal) {
        if (decimal == 0) return "0";
        StringBuilder sb = new StringBuilder();
        int n = Math.abs(decimal);
        while (n > 0) {
            sb.insert(0, n % 2);
            n /= 2;
        }
        return (decimal < 0 ? "-" : "") + sb.toString();
    }
}
