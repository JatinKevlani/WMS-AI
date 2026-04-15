package com.wmsai.service;

import com.wmsai.entity.*;
import com.wmsai.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Alert service for low-stock and overstock notifications.
 * Covers T046, ALERT-01 through ALERT-07.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AlertService {

    private final AlertRepository alertRepository;
    private final EmailService emailService;

    /** Checks low-stock and overstock thresholds for a product. */
    public void checkThresholds(Product product) {
        // Low-stock check
        if (product.getQuantity() < product.getMinThreshold()) {
            createAlert(AlertType.LOW_STOCK, product,
                    "Low stock alert: " + product.getName() + " (SKU: " + product.getSku() +
                    ") has " + product.getQuantity() + " units, below minimum threshold of " + product.getMinThreshold());
        }

        // Overstock check
        if (product.getQuantity() > product.getMaxThreshold()) {
            createAlert(AlertType.OVERSTOCK, product,
                    "Overstock alert: " + product.getName() + " (SKU: " + product.getSku() +
                    ") has " + product.getQuantity() + " units, exceeding maximum threshold of " + product.getMaxThreshold());
        }
    }

    /** Creates an alert with duplicate suppression (24h window). */
    public void createAlert(AlertType type, Product product, String message) {
        // Suppress duplicate alerts within 24 hours [ALERT-07]
        if (alertRepository.existsByProductIdAndTypeAndCreatedAtAfter(
                product.getId(), type, LocalDateTime.now().minusHours(24))) {
            log.debug("Duplicate alert suppressed for {} - {}", product.getSku(), type);
            return;
        }

        Alert alert = Alert.builder()
                .type(type)
                .product(product)
                .message(message)
                .isRead(false)
                .build();

        alertRepository.save(alert);
        log.info("🔔 Alert created: {} for {}", type, product.getSku());
        
        // Send email notification [T049]
        emailService.sendAlertEmail(alert);
    }

    public List<Alert> getUnreadAlerts() {
        return alertRepository.findByIsReadFalseOrderByCreatedAtDesc();
    }

    public void markAsRead(int alertId) {
        alertRepository.findById(alertId).ifPresent(alert -> {
            alert.setIsRead(true);
            alertRepository.save(alert);
        });
    }

    public long getUnreadCount() {
        return alertRepository.countByIsReadFalse();
    }
}
