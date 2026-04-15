package com.wmsai.repository;

import com.wmsai.entity.Alert;
import com.wmsai.entity.AlertType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Integer> {
    List<Alert> findByIsReadFalseOrderByCreatedAtDesc();
    long countByIsReadFalse();

    /** Checks for duplicate alert suppression (within 24h window). */
    boolean existsByProductIdAndTypeAndCreatedAtAfter(Integer productId, AlertType type, LocalDateTime after);
}
