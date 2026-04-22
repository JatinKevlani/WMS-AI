package com.wmsai.controller;

import com.wmsai.entity.SystemSetting;
import com.wmsai.entity.User;
import com.wmsai.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsController {

    @PersistenceContext
    private EntityManager em;

    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<SystemSetting>> getAll() {
        List<SystemSetting> settings = em.createQuery("SELECT s FROM SystemSetting s", SystemSetting.class)
                .getResultList();
        return ResponseEntity.ok(settings);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<Map<String, String>> updateSettings(
            @RequestBody Map<String, String> settings,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        for (Map.Entry<String, String> entry : settings.entrySet()) {
            SystemSetting setting = em.find(SystemSetting.class, entry.getKey());
            if (setting == null) {
                setting = SystemSetting.builder()
                        .key(entry.getKey())
                        .value(entry.getValue())
                        .updatedBy(user)
                        .updatedAt(LocalDateTime.now())
                        .build();
            } else {
                setting.setValue(entry.getValue());
                setting.setUpdatedBy(user);
                setting.setUpdatedAt(LocalDateTime.now());
            }
            em.merge(setting);
        }

        return ResponseEntity.ok(Map.of("message", "Settings updated"));
    }
}
