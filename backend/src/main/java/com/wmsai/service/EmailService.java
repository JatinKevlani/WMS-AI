package com.wmsai.service;

import com.wmsai.entity.Alert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service for sending SMTP emails.
 * Covers T049.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.alert.admin.email}")
    private String adminEmail;

    /**
     * Sends an email notification for a system alert asynchronously.
     */
    @Async
    public void sendAlertEmail(Alert alert) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(adminEmail);
            message.setSubject("WMS-AI Alert: " + alert.getType());
            
            String text = String.format(
                "A new system alert has been generated.\n\nType: %s\nProduct: %s (SKU: %s)\nMessage: %s\n\nPlease check the WMS-AI system for more details.",
                alert.getType(),
                alert.getProduct().getName(),
                alert.getProduct().getSku(),
                alert.getMessage()
            );
            
            message.setText(text);
            mailSender.send(message);
            log.info("📧 Alert email sent to {}", adminEmail);
        } catch (Exception e) {
            log.error("Failed to send alert email for {}: {}", alert.getProduct().getSku(), e.getMessage());
        }
    }
}
