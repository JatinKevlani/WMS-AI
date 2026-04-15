package com.wmsai.util;

/**
 * String utility class.
 * Covers T033, OOPJ-10 — two ways to find string length.
 */
public class StringUtils {

    /** Finds string length using the length() method. */
    public static int lengthUsingLength(String s) {
        return s == null ? 0 : s.length();
    }

    /** Finds string length using toCharArray(). */
    public static int lengthUsingToCharArray(String s) {
        return s == null ? 0 : s.toCharArray().length;
    }
}
