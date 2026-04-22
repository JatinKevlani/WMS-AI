package com.wmsai.config;

import com.wmsai.entity.*;
import com.wmsai.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Seeds the database with default admin user and sample categories on startup.
 * Covers T005, T006.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedAdminUser();
    }

    private void seedAdminUser() {
        if (!userRepository.existsByEmail("admin@wms.com")) {
            User admin = User.builder()
                    .email("admin@wms.com")
                    .passwordHash(passwordEncoder.encode("admin123"))
                    .fullName("Admin Director")
                    .role(Role.ADMIN)
                    .isActive(true)
                    .build();
            userRepository.save(admin);
            log.info("✅ Default admin user created: admin@wms.com / admin123");
        }

        if (!userRepository.existsByEmail("staff@wms.com")) {
            User staff = User.builder()
                    .email("staff@wms.com")
                    .passwordHash(passwordEncoder.encode("staff123"))
                    .fullName("Staff Expert")
                    .role(Role.STAFF)
                    .isActive(true)
                    .build();
            userRepository.save(staff);
            log.info("✅ Default staff user created: staff@wms.com / staff123");
        }
    }
}
