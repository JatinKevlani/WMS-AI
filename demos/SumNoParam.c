/*
 * SumNoParam.c
 * OOPJ Practical #4 — Sum of two numbers without parameters (uses global variables).
 * Demonstrates the C equivalent of Java instance method without parameters.
 * Part of WMS-AI OOPJ coverage.
 */
#include <stdio.h>

int a, b; /* Global variables — equivalent to Java instance fields */

int sumWithoutParams() {
    return a + b;
}

int main() {
    printf("=== Sum Demo (without parameters) ===\n");
    printf("Enter two integers: ");
    scanf("%d %d", &a, &b);
    printf("Sum of %d and %d = %d\n", a, b, sumWithoutParams());
    return 0;
}
