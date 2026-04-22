package com.wmsai.controller;

import com.wmsai.repository.SalesRepository;
import com.wmsai.repository.ProductRepository;
import com.wmsai.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * Analytics endpoints.
 * Covers T057 — dashboard stats, period comparisons.
 */
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final SalesRepository salesRepository;
    private final ProductRepository productRepository;
    private final AlertRepository alertRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime now = LocalDateTime.now();

        long totalProducts = productRepository.count();
        BigDecimal monthlyRevenue = salesRepository.findRevenueByPeriod(startOfMonth, now);
        long activeAlerts = alertRepository.countByIsReadFalse();

        // Calculate total inventory value [ANA-01]
        BigDecimal inventoryValue = productRepository.findAll().stream()
                .filter(p -> !p.getIsDeleted())
                .map(p -> (p.getUnitPrice() != null ? p.getUnitPrice() : BigDecimal.ZERO)
                        .multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalProducts", totalProducts);
        stats.put("inventoryValue", inventoryValue);
        stats.put("monthlyRevenue", monthlyRevenue);
        stats.put("activeAlerts", activeAlerts);

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/sales")
    public ResponseEntity<Map<String, Object>> getSalesAnalytics(
            @RequestParam(defaultValue = "MOM") String period,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {

        LocalDateTime currentStart, currentEnd, previousStart, previousEnd;
        LocalDateTime now = LocalDateTime.now();

        switch (period.toUpperCase()) {
            case "QOQ" -> {
                currentStart = now.with(now.getMonth().firstMonthOfQuarter()).withDayOfMonth(1).withHour(0).withMinute(0);
                currentEnd = now;
                previousStart = currentStart.minusMonths(3);
                previousEnd = currentStart;
            }
            case "YOY" -> {
                currentStart = now.withDayOfYear(1).withHour(0).withMinute(0);
                currentEnd = now;
                previousStart = currentStart.minusYears(1);
                previousEnd = currentEnd.minusYears(1);
            }
            case "CUSTOM" -> {
                currentStart = from != null ? LocalDateTime.parse(from) : now.minusDays(30);
                currentEnd = to != null ? LocalDateTime.parse(to) : now;
                long daysBetween = java.time.Duration.between(currentStart, currentEnd).toDays();
                previousStart = currentStart.minusDays(daysBetween);
                previousEnd = currentStart;
            }
            default -> { // MOM
                currentStart = now.withDayOfMonth(1).withHour(0).withMinute(0);
                currentEnd = now;
                previousStart = currentStart.minusMonths(1);
                previousEnd = currentStart;
            }
        }

        BigDecimal currentRevenue = salesRepository.findRevenueByPeriod(currentStart, currentEnd);
        BigDecimal previousRevenue = salesRepository.findRevenueByPeriod(previousStart, previousEnd);
        List<Object[]> topProducts = salesRepository.findTopSellingProducts(currentStart, currentEnd);
        List<Object[]> categoryBreakdown = salesRepository.findRevenueByCategory(currentStart, currentEnd);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("period", period);
        result.put("currentRevenue", currentRevenue);
        result.put("previousRevenue", previousRevenue);
        result.put("topProducts", topProducts.stream().limit(5).toList());
        result.put("categoryBreakdown", categoryBreakdown);

        return ResponseEntity.ok(result);
    }
}
