package com.wmsai.oopj;

/**
 * Math utility class.
 * Covers T066, OOPJ-3 (sum with params), OOPJ-4 (sum without params using instance fields).
 */
public class MathUtils {
    private int a;
    private int b;

    public MathUtils() {}

    public MathUtils(int a, int b) {
        this.a = a;
        this.b = b;
    }

    /** Sum with parameters [OOPJ-3]. */
    public static int sumWithParams(int a, int b) {
        return a + b;
    }

    /** Sum without parameters — uses instance fields [OOPJ-4]. */
    public int sumWithoutParams() {
        return this.a + this.b;
    }

    public void setA(int a) { this.a = a; }
    public void setB(int b) { this.b = b; }
    public int getA() { return a; }
    public int getB() { return b; }
}
