package com.wmsai.oopj;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Sales average calculator using different numeric types.
 * Covers T045, OOPJ-6 (average of N numbers using different types).
 */
public class SalesAverageCalculator {

    /** Average using int. */
    public static double averageInt(int[] values) {
        if (values.length == 0) return 0;
        int sum = 0;
        for (int v : values) sum += v;
        return (double) sum / values.length;
    }

    /** Average using double. */
    public static double averageDouble(double[] values) {
        if (values.length == 0) return 0;
        double sum = 0;
        for (double v : values) sum += v;
        return sum / values.length;
    }

    /** Average using BigDecimal for precision [OOPJ-6]. */
    public static BigDecimal averageBigDecimal(List<BigDecimal> values) {
        if (values == null || values.isEmpty()) return BigDecimal.ZERO;
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal v : values) {
            sum = sum.add(v);
        }
        return sum.divide(BigDecimal.valueOf(values.size()), 2, RoundingMode.HALF_UP);
    }
}
