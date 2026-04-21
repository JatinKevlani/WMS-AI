/*
 * SumDemo.c
 * OOPJ Practical #3 — Sum of two numbers with parameters.
 * Demonstrates the C equivalent of Java static method with parameters.
 * Part of WMS-AI OOPJ coverage.
 */
#include <stdio.h>

int sumWithParams(int a, int b) {
    return a + b;
}

int main() {
    int a, b;
    printf("=== Sum Demo (with parameters) ===\n");
    printf("Enter two integers: ");
    scanf("%d %d", &a, &b);
    printf("Sum of %d and %d = %d\n", a, b, sumWithParams(a, b));
    return 0;
}
