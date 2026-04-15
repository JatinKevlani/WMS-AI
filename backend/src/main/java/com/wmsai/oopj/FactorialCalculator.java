package com.wmsai.oopj;

/**
 * Factorial Calculator — recursive and iterative.
 * Covers T069, OOPJ-5 (factorial), OOPJ-8 (recursion vs iteration).
 */
public class FactorialCalculator {

    /** Recursive factorial [OOPJ-8]. */
    public static long factorialRecursive(int n) {
        if (n < 0) throw new IllegalArgumentException("Negative number: " + n);
        if (n <= 1) return 1;
        return n * factorialRecursive(n - 1);
    }

    /** Iterative factorial [OOPJ-8]. */
    public static long factorialIterative(int n) {
        if (n < 0) throw new IllegalArgumentException("Negative number: " + n);
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    /** Returns factorial as a formatted string [OOPJ-5]. */
    public static String factorialDisplay(int n) {
        return n + "! = " + factorialRecursive(n);
    }
}
