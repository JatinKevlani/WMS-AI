package com.wmsai.controller;

import com.wmsai.oopj.*;
import com.wmsai.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * OOPJ demonstration controller — exposes all practical utilities via API.
 * This endpoint allows the frontend "OOPJ Demo" page to call and display results
 * from all 30 OOPJ concepts.
 */
@RestController
@RequestMapping("/api/oopj")
public class OOPJController {

    /** OOPJ-1: Insert element into sorted array. */
    @PostMapping("/array/insert-sorted")
    public ResponseEntity<Map<String, Object>> insertSorted(@RequestBody Map<String, Object> body) {
        List<Integer> arr = ((List<Number>) body.get("array")).stream().map(Number::intValue).toList();
        int element = ((Number) body.get("element")).intValue();
        int[] input = arr.stream().mapToInt(i -> i).toArray();
        int[] result = ArrayUtils.insertSorted(input, element);
        return ResponseEntity.ok(Map.of("input", arr, "element", element, "result", Arrays.stream(result).boxed().toList()));
    }

    /** OOPJ-2: Matrix multiplication. */
    @PostMapping("/matrix/multiply")
    public ResponseEntity<Map<String, Object>> matrixMultiply(@RequestBody Map<String, int[][]> body) {
        int[][] a = body.get("matrixA");
        int[][] b = body.get("matrixB");
        int[][] result = ArrayUtils.multiplyMatrices(a, b);
        return ResponseEntity.ok(Map.of("matrixA", a, "matrixB", b, "result", result != null ? result : "incompatible"));
    }

    /** OOPJ-3,4: Sum with and without parameters. */
    @PostMapping("/math/sum")
    public ResponseEntity<Map<String, Object>> sum(@RequestBody Map<String, Integer> body) {
        int a = body.get("a");
        int b = body.get("b");
        return ResponseEntity.ok(Map.of(
                "sumWithParams", MathUtils.sumWithParams(a, b),
                "sumWithoutParams", new MathUtils(a, b).sumWithoutParams()
        ));
    }

    /** OOPJ-5,8: Factorial (recursive & iterative). */
    @GetMapping("/math/factorial/{n}")
    public ResponseEntity<Map<String, Object>> factorial(@PathVariable int n) {
        return ResponseEntity.ok(Map.of(
                "number", n,
                "recursive", FactorialCalculator.factorialRecursive(n),
                "iterative", FactorialCalculator.factorialIterative(n),
                "display", FactorialCalculator.factorialDisplay(n)
        ));
    }

    /** OOPJ-6: Average of N numbers. */
    @PostMapping("/math/average")
    public ResponseEntity<Map<String, Object>> average(@RequestBody Map<String, List<Double>> body) {
        List<Double> values = body.get("values");
        double[] arr = values.stream().mapToDouble(d -> d).toArray();
        return ResponseEntity.ok(Map.of(
                "values", values,
                "average", SalesAverageCalculator.averageDouble(arr)
        ));
    }

    /** OOPJ-9: Area calculations (overloading). */
    @PostMapping("/area/calculate")
    public ResponseEntity<Map<String, Object>> calculateArea(@RequestBody Map<String, Double> body) {
        AreaCalculator calc = new AreaCalculator();
        double l = body.getOrDefault("length", 10.0);
        double w = body.getOrDefault("width", 5.0);
        double r = body.getOrDefault("radius", 3.0);
        return ResponseEntity.ok(Map.of(
                "rectangle", calc.calculateArea(l, w),
                "circle", calc.calculateArea(r),
                "triangle", calc.calculateTriangleArea(l, w)
        ));
    }

    /** OOPJ-10: Two ways to find string length. */
    @PostMapping("/string/length")
    public ResponseEntity<Map<String, Object>> stringLength(@RequestBody Map<String, String> body) {
        String s = body.get("text");
        return ResponseEntity.ok(Map.of(
                "text", s,
                "lengthUsingLength", StringUtils.lengthUsingLength(s),
                "lengthUsingToCharArray", StringUtils.lengthUsingToCharArray(s)
        ));
    }

    /** OOPJ-11,12: Constructor overloading + copy constructor. */
    @PostMapping("/zone/create")
    public ResponseEntity<Map<String, Object>> createZone(@RequestBody Map<String, Object> body) {
        String type = (String) body.getOrDefault("type", "cube");
        WarehouseZone zone;
        if ("cube".equals(type)) {
            zone = new WarehouseZone(((Number) body.get("side")).doubleValue());
        } else if ("cuboid".equals(type)) {
            zone = new WarehouseZone(
                    ((Number) body.get("length")).doubleValue(),
                    ((Number) body.get("breadth")).doubleValue(),
                    ((Number) body.get("height")).doubleValue());
        } else {
            zone = new WarehouseZone(
                    ((Number) body.get("radius")).doubleValue(),
                    ((Number) body.get("height")).doubleValue(), true);
        }
        WarehouseZone copy = new WarehouseZone(zone);
        return ResponseEntity.ok(Map.of(
                "zone", zone.toString(),
                "copy", copy.toString(),
                "volume", zone.getVolume()
        ));
    }

    /** OOPJ-13: Vowel/Consonant counter. */
    @PostMapping("/text/analyze")
    public ResponseEntity<Map<String, Object>> analyzeText(@RequestBody Map<String, String> body) {
        String text = body.get("text");
        Map<String, Integer> counts = TextAnalyzer.countVowelsAndConsonants(text);
        int capitalWords = TextAnalyzer.countCapitalStartWords(text);
        Map<String, Object> result = new LinkedHashMap<>(counts);
        result.put("capitalStartWords", capitalWords);
        result.put("text", text);
        return ResponseEntity.ok(result);
    }

    /** OOPJ-15: Static product counter. */
    @GetMapping("/counter")
    public ResponseEntity<Map<String, Integer>> getCounter() {
        return ResponseEntity.ok(Map.of("productCount", ProductCounter.getCount()));
    }

    /** OOPJ-17: SKU palindrome check. */
    @GetMapping("/sku/palindrome/{sku}")
    public ResponseEntity<Map<String, Object>> checkPalindrome(@PathVariable String sku) {
        return ResponseEntity.ok(Map.of(
                "sku", sku,
                "isPalindrome", SKUValidator.isPalindrome(sku),
                "isValidSKU", SKUValidator.isValidSKU(sku)
        ));
    }

    /** OOPJ-18,19,20: Inheritance + Interface demo. */
    @GetMapping("/hierarchy")
    public ResponseEntity<Map<String, Object>> hierarchy() {
        AdminUser admin = new AdminUser("Alice", "alice@wms.com", "123", 1, "FULL");
        StaffUser staff = new StaffUser("Bob", "bob@wms.com", "456", 2, "Morning");

        return ResponseEntity.ok(Map.of(
                "admin", admin.getDisplayInfo(),
                "staff", staff.getDisplayInfo(),
                "adminSearchById", admin.searchById(1) != null ? "Found" : "Not found",
                "staffSearchByName", staff.searchByName("Bob") != null ? "Found" : "Not found"
        ));
    }

    /** OOPJ-26: Binary to decimal conversion with NumberFormatException. */
    @PostMapping("/barcode/convert")
    public ResponseEntity<Map<String, Object>> barcodeConvert(@RequestBody Map<String, String> body) {
        String binary = body.get("binary");
        try {
            int decimal = BarcodeUtils.bin2Dec(binary);
            return ResponseEntity.ok(Map.of(
                    "binary", binary,
                    "decimal", decimal,
                    "backToBinary", BarcodeUtils.dec2Bin(decimal)
            ));
        } catch (NumberFormatException e) {
            return ResponseEntity.ok(Map.of(
                    "binary", binary != null ? binary : "",
                    "error", e.getMessage()
            ));
        }
    }

    /** OOPJ-16ext: Vowel tracker with running totals. */
    @PostMapping("/vowel/track")
    public ResponseEntity<Map<String, Object>> vowelTrack(@RequestBody Map<String, Object> body) {
        VowelTracker tracker = new VowelTracker();
        Object sentences = body.get("sentences");
        if (sentences instanceof java.util.List<?> list) {
            for (Object s : list) {
                if ("quit".equalsIgnoreCase(String.valueOf(s).trim())) break;
                tracker.processSentence(String.valueOf(s));
            }
        } else {
            tracker.processSentence(String.valueOf(body.getOrDefault("text", "")));
        }
        return ResponseEntity.ok(tracker.getRunningTotals());
    }

    /** OOPJ-27: Recursive find smallest. */
    @PostMapping("/recursive/smallest")
    public ResponseEntity<Map<String, Object>> recursiveSmallest(@RequestBody Map<String, java.util.List<Integer>> body) {
        java.util.List<Integer> values = body.get("values");
        int[] arr = values.stream().mapToInt(i -> i).toArray();
        return ResponseEntity.ok(Map.of(
                "values", values,
                "smallest", RecursiveUtils.findSmallest(arr),
                "largest", RecursiveUtils.findLargest(arr, arr.length - 1)
        ));
    }

    /** OOPJ-27ext: Write random stock log and find smallest. */
    @GetMapping("/inventory-log/demo")
    public ResponseEntity<Map<String, Object>> inventoryLogDemo() {
        try {
            String path = "./wms-data/oopj_demo_stock.txt";
            int[] values = InventoryLogWriter.writeRandomStockLog(path);
            int smallest = RecursiveUtils.findSmallest(values);
            return ResponseEntity.ok(Map.of(
                    "count", values.length,
                    "smallest", smallest,
                    "filePath", path,
                    "first10", java.util.Arrays.stream(values).limit(10).boxed().toList()
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("error", e.getMessage()));
        }
    }

    /** OOPJ-29: Tag deduplication sorted descending. */
    @PostMapping("/tags/deduplicate")
    public ResponseEntity<Map<String, Object>> deduplicate(@RequestBody Map<String, java.util.List<String>> body) {
        java.util.List<String> words = body.get("words");
        String[] arr = words.toArray(new String[0]);
        java.util.List<String> result = TagDeduplicator.deduplicateAndSort(arr);
        return ResponseEntity.ok(Map.of(
                "input", words,
                "deduplicated", result,
                "uniqueCount", result.size()
        ));
    }
}
