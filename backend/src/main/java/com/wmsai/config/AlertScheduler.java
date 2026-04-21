package com.wmsai.config;

import com.wmsai.entity.Product;
import com.wmsai.repository.ProductRepository;
import com.wmsai.service.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * Scheduled job to check stock thresholds daily.
 * Covers T048, ALERT-08.
 */
@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class AlertScheduler {

    private final ProductRepository productRepository;
    private final AlertService alertService;

    /** Runs daily at 8 AM to check all product thresholds. */
    @Scheduled(cron = "0 0 8 * * *")
    public void dailyThresholdCheck() {
        log.info("🔄 Running daily threshold check...");

        // Low stock check
        List<Product> lowStock = productRepository.findLowStockProducts();
        for (Product p : lowStock) {
            alertService.checkThresholds(p);
        }

        // Overstock check — uses per-product overstockDays [ALERT-08]
        List<Product> allProducts = productRepository.findAll();
        long overstockCount = 0;
        for (Product p : allProducts) {
            if (!p.getIsDeleted() && p.getQuantity() > p.getMaxThreshold()) {
                int days = p.getOverstockDays() != null ? p.getOverstockDays() : 30;
                if (p.getLastSaleDate() == null || p.getLastSaleDate().isBefore(LocalDate.now().minusDays(days))) {
                    alertService.checkThresholds(p);
                    overstockCount++;
                }
            }
        }

        log.info("✅ Daily threshold check complete: {} low-stock, {} overstock", lowStock.size(), overstockCount);
    }
}
