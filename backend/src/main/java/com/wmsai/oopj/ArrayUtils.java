package com.wmsai.oopj;

/**
 * Array and sorting utility.
 * Covers T068, OOPJ-1 (insert and sort array), OOPJ-2 (matrix operations).
 */
public class ArrayUtils {

    /**
     * Inserts an element into a sorted integer array [OOPJ-1].
     * Returns a new sorted array with the element inserted.
     */
    public static int[] insertSorted(int[] arr, int element) {
        int[] result = new int[arr.length + 1];
        int i = 0;
        boolean inserted = false;
        for (int j = 0; j < result.length; j++) {
            if (!inserted && (i >= arr.length || element <= arr[i])) {
                result[j] = element;
                inserted = true;
            } else {
                result[j] = arr[i++];
            }
        }
        return result;
    }

    /**
     * Multiplies two 2D integer matrices [OOPJ-2].
     * @return Resultant matrix, or null if dimensions are incompatible.
     */
    public static int[][] multiplyMatrices(int[][] a, int[][] b) {
        if (a == null || b == null || a.length == 0 || b.length == 0) return null;
        int rows = a.length;
        int cols = b[0].length;
        int common = a[0].length;
        if (common != b.length) return null;

        int[][] result = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < common; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    }

    /** Prints a matrix in a formatted way. */
    public static String matrixToString(int[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : matrix) {
            for (int j = 0; j < row.length; j++) {
                if (j > 0) sb.append("\t");
                sb.append(row[j]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
