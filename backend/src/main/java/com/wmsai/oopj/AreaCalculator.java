package com.wmsai.oopj;

/**
 * Demonstrates method overriding with Product area calculations.
 * Covers OOPJ-9 — runtime polymorphism with area calculation.
 */
public class AreaCalculator {

    /** Base: generic rectangle area. */
    public double calculateArea(double length, double width) {
        return length * width;
    }

    /** Overloaded: circle area [OOPJ-9]. */
    public double calculateArea(double radius) {
        return Math.PI * radius * radius;
    }

    /** Overloaded: triangle area [OOPJ-9]. */
    public double calculateTriangleArea(double base, double height) {
        return 0.5 * base * height;
    }

    /** Display all calculations. */
    public String displayCalculations(double l, double w, double r, double base, double h) {
        return String.format(
            "Rectangle (%.1f x %.1f) = %.2f sq.m\nCircle (r=%.1f) = %.2f sq.m\nTriangle (b=%.1f, h=%.1f) = %.2f sq.m",
            l, w, calculateArea(l, w),
            r, calculateArea(r),
            base, h, calculateTriangleArea(base, h)
        );
    }
}
