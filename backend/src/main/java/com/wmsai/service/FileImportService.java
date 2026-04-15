package com.wmsai.service;

import com.wmsai.entity.*;
import com.wmsai.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV file import service.
 * Covers T061, FILE-06 through FILE-08.
 * Uses BufferedReader [OOPJ-27].
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FileImportService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Imports products from a CSV file [FILE-06].
     * Expected columns: SKU,Name,Category,Price,Quantity,MinThreshold,MaxThreshold
     * Uses BufferedReader [OOPJ-27].
     *
     * @return number of successfully imported products
     */
    public int importProductsCsv(MultipartFile file) throws IOException {
        List<String> errors = new ArrayList<>();
        int successCount = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String header = reader.readLine(); // skip header
            if (header == null) throw new IllegalArgumentException("Empty CSV file");

            String line;
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                try {
                    String[] cols = parseCsvLine(line);
                    if (cols.length < 5) {
                        errors.add("Line " + lineNum + ": insufficient columns");
                        continue;
                    }

                    String sku = cols[0].trim().toUpperCase();
                    String name = cols[1].trim();
                    String categoryName = cols.length > 2 ? cols[2].trim() : "";
                    BigDecimal price = new BigDecimal(cols[3].trim());
                    int quantity = Integer.parseInt(cols[4].trim());
                    int minThreshold = cols.length > 5 ? Integer.parseInt(cols[5].trim()) : 50;
                    int maxThreshold = cols.length > 6 ? Integer.parseInt(cols[6].trim()) : 500;

                    // Skip if SKU already exists
                    if (productRepository.existsBySku(sku)) {
                        errors.add("Line " + lineNum + ": SKU " + sku + " already exists, skipped");
                        continue;
                    }

                    Category category = null;
                    if (!categoryName.isEmpty()) {
                        category = categoryRepository.findByName(categoryName).orElse(null);
                    }

                    Product product = Product.builder()
                            .sku(sku)
                            .name(name)
                            .category(category)
                            .unitPrice(price)
                            .quantity(quantity)
                            .minThreshold(minThreshold)
                            .maxThreshold(maxThreshold)
                            .build();

                    productRepository.save(product);
                    successCount++;

                } catch (Exception e) {
                    errors.add("Line " + lineNum + ": " + e.getMessage());
                }
            }
        }

        log.info("📥 CSV Import completed: {} succeeded, {} errors", successCount, errors.size());
        if (!errors.isEmpty()) {
            log.warn("Import errors: {}", errors);
        }

        return successCount;
    }

    /** Simple CSV parser handling quoted fields. */
    private String[] parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());

        return result.toArray(new String[0]);
    }
}
