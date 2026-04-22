package com.wmsai.service;

import com.wmsai.entity.Product;
import com.wmsai.entity.SalesTransaction;
import com.wmsai.repository.ProductRepository;
import com.wmsai.repository.SalesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * File export service — CSV, text file generation.
 * Covers T058-T060, FILE-01 through FILE-05.
 * Uses FileWriter/PrintWriter [OOPJ-26], BufferedReader [OOPJ-27].
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FileExportService {

    private final ProductRepository productRepository;
    private final SalesRepository salesRepository;

    @Value("${app.files.base.path:./wms-data}")
    private String basePath;

    /**
     * Exports all products to CSV file [FILE-01].
     * Uses PrintWriter [OOPJ-26].
     */
    public String exportProductsCsv() throws IOException {
        String dir = basePath + "/exports";
        Files.createDirectories(Path.of(dir));

        String filename = "products_" + timestamp() + ".csv";
        String filePath = dir + "/" + filename;

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("ID,SKU,Name,Category,Supplier,Price,Quantity,MinThreshold,MaxThreshold,LastSaleDate,CreatedAt");

            List<Product> products = productRepository.findAll();
            for (Product p : products) {
                writer.printf("%d,%s,%s,%s,%s,%.2f,%d,%d,%d,%s,%s%n",
                        p.getId(),
                        escapeCsv(p.getSku()),
                        escapeCsv(p.getName()),
                        p.getCategory() != null ? escapeCsv(p.getCategory().getName()) : "",
                        p.getSupplier() != null ? escapeCsv(p.getSupplier().getName()) : "",
                        p.getUnitPrice() != null ? p.getUnitPrice() : 0,
                        p.getQuantity(),
                        p.getMinThreshold(),
                        p.getMaxThreshold(),
                        p.getLastSaleDate() != null ? p.getLastSaleDate() : "",
                        p.getCreatedAt());
            }

            log.info("📄 Exported {} products to {}", products.size(), filePath);
        }

        return filePath;
    }

    /**
     * Exports sales transactions to CSV [FILE-02].
     */
    public String exportSalesCsv() throws IOException {
        String dir = basePath + "/exports";
        Files.createDirectories(Path.of(dir));

        String filename = "sales_" + timestamp() + ".csv";
        String filePath = dir + "/" + filename;

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("ID,ProductSKU,ProductName,QuantitySold,SalePrice,TotalAmount,SoldBy,SaleDate");

            List<SalesTransaction> sales = salesRepository.findAll();
            for (SalesTransaction s : sales) {
                writer.printf("%d,%s,%s,%d,%.2f,%.2f,%s,%s%n",
                        s.getId(),
                        s.getProduct().getSku(),
                        escapeCsv(s.getProduct().getName()),
                        s.getQuantitySold(),
                        s.getSalePrice(),
                        s.getTotalAmount(),
                        s.getSoldBy() != null ? s.getSoldBy().getEmail() : "",
                        s.getSaleDate());
            }

            log.info("📄 Exported {} sales to {}", sales.size(), filePath);
        }

        return filePath;
    }

    /**
     * Generates inventory summary report as text [FILE-03].
     */
    public String generateInventoryReport() throws IOException {
        String dir = basePath + "/reports";
        Files.createDirectories(Path.of(dir));

        String filename = "inventory_report_" + timestamp() + ".txt";
        String filePath = dir + "/" + filename;

        List<Product> products = productRepository.findAll();

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("=".repeat(60));
            writer.println("        WMS-AI INVENTORY REPORT");
            writer.println("        Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println("=".repeat(60));
            writer.println();

            writer.printf("Total Products: %d%n", products.size());
            writer.printf("Total Stock Value: ₹%.2f%n",
                    products.stream()
                            .mapToDouble(p -> p.getUnitPrice() != null ?
                                    p.getUnitPrice().doubleValue() * p.getQuantity() : 0)
                            .sum());
            writer.println();

            writer.println("-".repeat(60));
            writer.printf("%-6s %-12s %-20s %8s %8s%n", "ID", "SKU", "Name", "Qty", "Value");
            writer.println("-".repeat(60));

            for (Product p : products) {
                double value = p.getUnitPrice() != null ? p.getUnitPrice().doubleValue() * p.getQuantity() : 0;
                writer.printf("%-6d %-12s %-20s %8d %8.2f%n",
                        p.getId(), p.getSku(), truncate(p.getName(), 20), p.getQuantity(), value);
            }

            writer.println("-".repeat(60));
            log.info("📊 Generated inventory report: {}", filePath);
        }

        return filePath;
    }

    private String timestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String truncate(String s, int maxLen) {
        return s != null && s.length() > maxLen ? s.substring(0, maxLen - 3) + "..." : s;
    }
}
