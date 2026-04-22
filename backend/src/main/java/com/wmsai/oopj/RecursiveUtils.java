package com.wmsai.oopj;

/**
 * Recursive utility methods.
 * Covers OOPJ Practical #27 — recursive findSmallest method.
 */
public class RecursiveUtils {

    /**
     * Recursively finds the smallest integer in an array.
     *
     * @param arr   the integer array to search
     * @param index the current index being compared (start from arr.length - 1)
     * @return the smallest integer in the array
     * @throws IllegalArgumentException if array is null or empty
     */
    public static int findSmallest(int[] arr, int index) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Array must not be null or empty");
        }

        // Base case: only one element
        if (index == 0) {
            return arr[0];
        }

        // Recursive case: compare current element with smallest of the rest
        int smallestOfRest = findSmallest(arr, index - 1);
        return Math.min(arr[index], smallestOfRest);
    }

    /**
     * Convenience method — finds smallest in the entire array.
     *
     * @param arr the integer array
     * @return the smallest element
     */
    public static int findSmallest(int[] arr) {
        return findSmallest(arr, arr.length - 1);
    }

    /**
     * Recursively finds the largest integer in an array.
     *
     * @param arr   the integer array
     * @param index current index (start from arr.length - 1)
     * @return the largest integer
     */
    public static int findLargest(int[] arr, int index) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Array must not be null or empty");
        }
        if (index == 0) return arr[0];
        return Math.max(arr[index], findLargest(arr, index - 1));
    }
}
