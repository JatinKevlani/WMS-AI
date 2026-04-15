package com.wmsai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Audit logger service — writes activity to a log file.
 * Covers T062, FILE-09 through FILE-11.
 * Uses FileWriter with append mode [OOPJ-26].
 */
@Service
@Slf4j
public class AuditLogService {

    @Value("${app.files.base.path:./wms-data}")
    private String basePath;

    /**
     * Appends an activity log entry [FILE-09].
     */
    public void logActivity(String action, String user, String details) {
        try {
            String dir = basePath + "/logs";
            Files.createDirectories(Path.of(dir));

            String filePath = dir + "/activity_log.txt";

            try (FileWriter fw = new FileWriter(filePath, true);
                 PrintWriter writer = new PrintWriter(fw)) {
                writer.printf("[%s] User: %s | Action: %s | Details: %s%n",
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        user, action, details);
            }
        } catch (IOException e) {
            log.error("Failed to write audit log: {}", e.getMessage());
        }
    }

    /**
     * Reads the activity log file [FILE-10].
     * Uses BufferedReader [OOPJ-27].
     */
    public String readActivityLog() {
        String filePath = basePath + "/logs/activity_log.txt";
        File file = new File(filePath);
        if (!file.exists()) return "No activity log found.";

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            log.error("Failed to read audit log: {}", e.getMessage());
            return "Error reading log: " + e.getMessage();
        }
        return sb.toString();
    }

    /**
     * Read recent N lines from log [FILE-11].
     */
    public String readRecentLog(int lines) {
        String filePath = basePath + "/logs/activity_log.txt";
        try {
            var allLines = Files.readAllLines(Path.of(filePath));
            int start = Math.max(0, allLines.size() - lines);
            return String.join("\n", allLines.subList(start, allLines.size()));
        } catch (IOException e) {
            return "No log file found.";
        }
    }
}
