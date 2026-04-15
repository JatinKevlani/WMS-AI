package com.wmsai.util;

/**
 * Tracks total product count using static members.
 * Covers T032, OOPJ-15.
 */
public class ProductCounter {

    private static int totalCount = 0;

    public static void increment() {
        totalCount++;
    }

    public static int getCount() {
        return totalCount;
    }

    public static void reset() {
        totalCount = 0;
    }
}
