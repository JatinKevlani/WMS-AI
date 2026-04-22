package com.wmsai.controller;

import com.wmsai.entity.Product;
import com.wmsai.entity.SalesTransaction;
import com.wmsai.repository.AlertRepository;
import com.wmsai.repository.ProductRepository;
import com.wmsai.repository.SalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Analytics endpoints.
 * Covers T057 â€” dashboard stats, period comparisons.
 */
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private static final DateTimeFormatter DAY_LABEL = DateTimeFormatter.ofPattern("dd MMM");
    private static final DateTimeFormatter SHORT_DAY_LABEL = DateTimeFormatter.ofPattern("EEE");
    private static final DateTimeFormatter MONTH_LABEL = DateTimeFormatter.ofPattern("MMM");

    private final SalesRepository salesRepository;
    private final ProductRepository productRepository;
    private final AlertRepository alertRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime now = LocalDateTime.now();

        long totalProducts = productRepository.countByIsDeletedFalse();
        BigDecimal monthlyRevenue = defaultAmount(salesRepository.findRevenueByPeriod(startOfMonth, now));
        long activeAlerts = alertRepository.countByIsReadFalse();

        BigDecimal inventoryValue = activeProducts().stream()
                .map(p -> defaultAmount(p.getUnitPrice()).multiply(BigDecimal.valueOf(p.getQuantity())))
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

        TimeRange range = resolveTimeRange(period, from, to);

        BigDecimal currentRevenue = defaultAmount(salesRepository.findRevenueByPeriod(range.currentStart(), range.currentEnd()));
        BigDecimal previousRevenue = defaultAmount(salesRepository.findRevenueByPeriod(range.previousStart(), range.previousEnd()));
        List<SalesTransaction> currentTransactions = salesRepository.findBySaleDateBetweenOrderBySaleDateAsc(
                range.currentStart(), range.currentEnd());
        List<SalesTransaction> previousTransactions = salesRepository.findBySaleDateBetweenOrderBySaleDateAsc(
                range.previousStart(), range.previousEnd());

        long currentUnits = currentTransactions.stream().mapToLong(SalesTransaction::getQuantitySold).sum();
        long previousUnits = previousTransactions.stream().mapToLong(SalesTransaction::getQuantitySold).sum();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("period", range.period());
        result.put("periodLabel", range.label());
        result.put("from", range.currentStart());
        result.put("to", range.currentEnd());
        result.put("currentRevenue", currentRevenue);
        result.put("previousRevenue", previousRevenue);
        result.put("currentUnits", currentUnits);
        result.put("previousUnits", previousUnits);
        result.put("transactionCount", currentTransactions.size());
        result.put("previousTransactionCount", previousTransactions.size());
        result.put("topProducts", mapProductSales(salesRepository.findTopSellingProducts(range.currentStart(), range.currentEnd()), 5));
        result.put("slowMovingProducts", mapProductSales(salesRepository.findSlowMovingProducts(range.currentStart(), range.currentEnd()), 6));
        result.put("categoryBreakdown", mapCategoryBreakdown(
                salesRepository.findRevenueByCategory(range.currentStart(), range.currentEnd())));
        result.put("timeSeries", buildTimeSeries(currentTransactions, range));
        result.put("inventoryHealth", buildInventoryHealth());

        return ResponseEntity.ok(result);
    }

    private List<Product> activeProducts() {
        return productRepository.findByIsDeletedFalse(Pageable.unpaged()).getContent();
    }

    private Map<String, Object> buildInventoryHealth() {
        List<Product> products = activeProducts();
        long lowStock = products.stream().filter(p -> p.getQuantity() <= p.getMinThreshold()).count();
        long overstock = products.stream().filter(p -> p.getQuantity() >= p.getMaxThreshold()).count();
        long normal = Math.max(0, products.size() - lowStock - overstock);

        Map<String, Object> inventoryHealth = new LinkedHashMap<>();
        inventoryHealth.put("total", products.size());
        inventoryHealth.put("normal", normal);
        inventoryHealth.put("lowStock", lowStock);
        inventoryHealth.put("overstock", overstock);
        return inventoryHealth;
    }

    private List<Map<String, Object>> mapProductSales(List<Object[]> rows, int limit) {
        return rows.stream()
                .limit(limit)
                .map(row -> {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("productId", row[0]);
                    item.put("sku", row[1]);
                    item.put("name", row[2]);
                    item.put("category", row[3]);
                    item.put("unitsSold", row[4]);
                    item.put("revenue", row.length > 5 ? defaultAmount((BigDecimal) row[5]) : BigDecimal.ZERO);
                    return item;
                })
                .toList();
    }

    private List<Map<String, Object>> mapCategoryBreakdown(List<Object[]> rows) {
        return rows.stream()
                .map(row -> {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("category", row[0] != null ? row[0] : "Uncategorized");
                    item.put("revenue", defaultAmount((BigDecimal) row[1]));
                    return item;
                })
                .toList();
    }

    private List<Map<String, Object>> buildTimeSeries(List<SalesTransaction> transactions, TimeRange range) {
        if ("WOW".equals(range.period())) {
            Map<LocalDate, MetricBucket> buckets = new LinkedHashMap<>();
            LocalDate cursor = range.currentStart().toLocalDate();
            LocalDate endDate = range.currentEnd().toLocalDate();
            while (!cursor.isAfter(endDate)) {
                buckets.put(cursor, new MetricBucket());
                cursor = cursor.plusDays(1);
            }

            for (SalesTransaction tx : transactions) {
                MetricBucket bucket = buckets.get(tx.getSaleDate().toLocalDate());
                if (bucket != null) {
                    bucket.add(defaultAmount(tx.getTotalAmount()), tx.getQuantitySold());
                }
            }

            List<Map<String, Object>> points = new ArrayList<>();
            buckets.forEach((date, bucket) -> points.add(point(SHORT_DAY_LABEL.format(date), bucket)));
            return points;
        }

        if ("YOY".equals(range.period()) || "QOQ".equals(range.period())) {
            Map<YearMonth, MetricBucket> buckets = new LinkedHashMap<>();
            YearMonth cursor = YearMonth.from(range.currentStart());
            YearMonth endMonth = YearMonth.from(range.currentEnd());
            while (!cursor.isAfter(endMonth)) {
                buckets.put(cursor, new MetricBucket());
                cursor = cursor.plusMonths(1);
            }

            for (SalesTransaction tx : transactions) {
                MetricBucket bucket = buckets.get(YearMonth.from(tx.getSaleDate()));
                if (bucket != null) {
                    bucket.add(defaultAmount(tx.getTotalAmount()), tx.getQuantitySold());
                }
            }

            List<Map<String, Object>> points = new ArrayList<>();
            buckets.forEach((month, bucket) -> points.add(point(MONTH_LABEL.format(month.atDay(1)), bucket)));
            return points;
        }

        Map<LocalDate, MetricBucket> buckets = new LinkedHashMap<>();
        LocalDate cursor = range.currentStart().toLocalDate();
        LocalDate endDate = range.currentEnd().toLocalDate();
        while (!cursor.isAfter(endDate)) {
            buckets.put(cursor, new MetricBucket());
            cursor = cursor.plusDays(1);
        }

        for (SalesTransaction tx : transactions) {
            MetricBucket bucket = buckets.get(tx.getSaleDate().toLocalDate());
            if (bucket != null) {
                bucket.add(defaultAmount(tx.getTotalAmount()), tx.getQuantitySold());
            }
        }

        List<Map<String, Object>> points = new ArrayList<>();
        buckets.forEach((date, bucket) -> points.add(point(DAY_LABEL.format(date), bucket)));
        return points;
    }

    private Map<String, Object> point(String label, MetricBucket bucket) {
        Map<String, Object> point = new LinkedHashMap<>();
        point.put("label", label);
        point.put("revenue", bucket.revenue);
        point.put("units", bucket.units);
        return point;
    }

    private BigDecimal defaultAmount(BigDecimal amount) {
        return amount != null ? amount : BigDecimal.ZERO;
    }

    private TimeRange resolveTimeRange(String rawPeriod, String from, String to) {
        String period = rawPeriod != null ? rawPeriod.toUpperCase() : "MOM";
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime currentStart;
        LocalDateTime currentEnd = now;
        LocalDateTime previousStart;
        LocalDateTime previousEnd;
        String label;

        switch (period) {
            case "WOW" -> {
                currentStart = now.minusDays(6).toLocalDate().atStartOfDay();
                previousEnd = currentStart.minusNanos(1);
                previousStart = currentStart.minusDays(7);
                label = "Last 7 Days";
            }
            case "QOQ" -> {
                currentStart = now.with(now.getMonth().firstMonthOfQuarter()).withDayOfMonth(1)
                        .toLocalDate().atStartOfDay();
                previousEnd = currentStart.minusNanos(1);
                previousStart = currentStart.minusMonths(3);
                label = "Current Quarter";
            }
            case "YOY" -> {
                currentStart = now.withDayOfYear(1).toLocalDate().atStartOfDay();
                previousEnd = currentStart.minusNanos(1);
                previousStart = currentStart.minusYears(1);
                label = "Year to Date";
            }
            case "CUSTOM" -> {
                LocalDateTime parsedFrom = from != null ? LocalDateTime.parse(from) : now.minusDays(30);
                LocalDateTime parsedTo = to != null ? LocalDateTime.parse(to) : now;
                if (parsedTo.isBefore(parsedFrom)) {
                    LocalDateTime swap = parsedFrom;
                    parsedFrom = parsedTo;
                    parsedTo = swap;
                }
                currentStart = parsedFrom;
                currentEnd = parsedTo;
                Duration duration = Duration.between(currentStart, currentEnd);
                previousEnd = currentStart.minusNanos(1);
                previousStart = previousEnd.minus(duration);
                label = "Custom Range";
            }
            default -> {
                period = "MOM";
                currentStart = now.withDayOfMonth(1).toLocalDate().atStartOfDay();
                previousEnd = currentStart.minusNanos(1);
                previousStart = currentStart.minusMonths(1);
                label = "Month to Date";
            }
        }

        return new TimeRange(period, label, currentStart, currentEnd, previousStart, previousEnd);
    }

    private record TimeRange(
            String period,
            String label,
            LocalDateTime currentStart,
            LocalDateTime currentEnd,
            LocalDateTime previousStart,
            LocalDateTime previousEnd
    ) {
    }

    private static final class MetricBucket {
        private BigDecimal revenue = BigDecimal.ZERO;
        private long units = 0;

        private void add(BigDecimal amount, int quantity) {
            revenue = revenue.add(amount);
            units += quantity;
        }
    }
}
