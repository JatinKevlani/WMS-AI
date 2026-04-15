package com.wmsai;

import com.wmsai.oopj.*;
import com.wmsai.util.ProductCounter;
import com.wmsai.util.SKUValidator;
import com.wmsai.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Executes a startup demonstration of all OOPJ utility components.
 * Fulfills task T098.
 */
@Component
@Slf4j
public class DemoRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("===========================================");
        log.info("🚀 STARTING OOPJ UTILITIES VERIFICATION 🚀");
        log.info("===========================================");

        // 1. ArrayUtils (Prac 1 & 2)
        int[] arr = {1, 3, 5, 7, 9};
        int[] newArr = ArrayUtils.insertSorted(arr, 4);
        log.info("[OOPJ-1] Array insertion sorted: {}", Arrays.toString(newArr));
        
        int[][] matA = {{1, 2}, {3, 4}};
        int[][] matB = {{5, 6}, {7, 8}};
        int[][] matC = ArrayUtils.multiplyMatrices(matA, matB);
        log.info("[OOPJ-2] Matrix multiplication completed successfully.");

        // 2. MathUtils (Prac 3 & 4)
        MathUtils math = new MathUtils();
        log.info("[OOPJ-3] static sumWithParams(10, 20) = {}", MathUtils.sumWithParams(10, 20));
        math.setA(15);
        math.setB(25);
        log.info("[OOPJ-4] instance sumWithoutParams() = {}", math.sumWithoutParams());

        // 3. FactorialCalculator (Prac 5 & 8)
        log.info("[OOPJ-5/8] Factorial iterative (5): {}", FactorialCalculator.factorialIterative(5));
        log.info("[OOPJ-5/8] Factorial recursive (5): {}", FactorialCalculator.factorialRecursive(5));

        // 4. SalesAverageCalculator (Prac 6)
        double[] doubleSales = {100.0, 200.0, 150.0, 250.0};
        log.info("[OOPJ-6] Sales average (double[]): {}", SalesAverageCalculator.averageDouble(doubleSales));

        // 5. AreaCalculator (Prac 9)
        AreaCalculator areaCalc = new AreaCalculator();
        log.info("[OOPJ-9] Area Overloading (Circle r=5): {}", areaCalc.calculateArea(5.0));
        log.info("[OOPJ-9] Area Overloading (Rectangle 5x10): {}", areaCalc.calculateArea(5, 10));

        // 6. StringUtils (Prac 10)
        log.info("[OOPJ-10] String length (normal): {}", StringUtils.lengthUsingLength("WMS-AI"));
        log.info("[OOPJ-10] String length (char array): {}", StringUtils.lengthUsingToCharArray("WMS-AI"));

        // 7. WarehouseZone (Prac 11 & 12)
        WarehouseZone z1 = new WarehouseZone(10);
        WarehouseZone z2 = new WarehouseZone(z1); // Copy constructor
        log.info("[OOPJ-11/12] WarehouseZone copy constructor Volume: {}", z2.getVolume());

        // 8. TextAnalyzer (Prac 13 & 14)
        String text = "The Quick Brown fox";
        java.util.Map<String, Integer> vc = TextAnalyzer.countVowelsAndConsonants(text);
        log.info("[OOPJ-13] Vowels: {}, Consonants: {}", vc.get("vowels"), vc.get("consonants"));
        log.info("[OOPJ-14] Capital-start words: {}", TextAnalyzer.countCapitalStartWords(text));

        // 9. ProductCounter (Prac 15)
        ProductCounter.increment();
        log.info("[OOPJ-15] Static ProductCounter: {}", ProductCounter.getCount());

        // 10. InventoryItem (Prac 16)
        InventoryItem item = new InventoryItem("LAPTOP");
        log.info("[OOPJ-16] InventoryItem this() chaining -> {}", item.getName());

        // 11. SKUValidator (Prac 17)
        log.info("[OOPJ-17] SKUValidator isPalindrome('RADAR'): {}", SKUValidator.isPalindrome("RADAR"));

        log.info("===========================================");
        log.info("✅ ALL OOPJ DEMOS COMPLETED SUCCESSFULLY ✅");
        log.info("===========================================");
    }
}
