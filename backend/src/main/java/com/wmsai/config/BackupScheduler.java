package com.wmsai.config;

import com.wmsai.entity.Product;
import com.wmsai.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Daily backup scheduler — writes inventory snapshot to a dated text file.
 * Covers FILE-05 — daily inventory backup to backup/inventory_YYYY-MM-DD.txt.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BackupScheduler {

    private final ProductRepository productRepository;

    @Value("${app.files.base.path:./wms-data}")
    private String basePath;

    /** Runs daily at 2 AM to create inventory backup. */
    @Scheduled(cron = "0 0 2 * * *")
    public void dailyInventoryBackup() {
        String date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String fileName = "inventory_" + date + ".txt";
        Path backupDir = Path.of(basePath, "backup");

        try {
            Files.createDirectories(backupDir);
            Path filePath = backupDir.resolve(fileName);

            List<Product> products = productRepository.findAll().stream()
                    .filter(p -> !p.getIsDeleted())
                    .toList();

            try (PrintWriter writer = new PrintWriter(new FileWriter(filePath.toFile()))) {
                writer.println("=== WMS-AI INVENTORY BACKUP ===");
                writer.println("Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                writer.println("Total Products: " + products.size());
                writer.println("=================================");
                writer.println();
                writer.printf("%-12s | %-30s | %-15s | %8s | %10s%n",
                        "SKU", "NAME", "CATEGORY", "QTY", "PRICE");
                writer.println("-".repeat(85));

                for (Product p : products) {
                    writer.printf("%-12s | %-30s | %-15s | %8d | %10s%n",
                            p.getSku(),
                            truncate(p.getName(), 30),
                            p.getCategory() != null ? truncate(p.getCategory().getName(), 15) : "N/A",
                            p.getQuantity(),
                            p.getUnitPrice() != null ? p.getUnitPrice().toString() : "0.00");
                }

                writer.println();
                writer.println("=== END OF BACKUP ===");
            }

            log.info("✅ Daily inventory backup created: {}", filePath);
        } catch (IOException e) {
            log.error("❌ Failed to create daily backup: {}", e.getMessage());
        }
    }

    private String truncate(String s, int maxLen) {
        if (s == null) return "";
        return s.length() > maxLen ? s.substring(0, maxLen - 3) + "..." : s;
    }
}
