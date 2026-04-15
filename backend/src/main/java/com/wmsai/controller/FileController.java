package com.wmsai.controller;

import com.wmsai.service.FileExportService;
import com.wmsai.service.FileImportService;
import com.wmsai.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * File I/O endpoints for export, import, and log viewing.
 * Covers T063, FILE-01 through FILE-11.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileController {

    private final FileExportService fileExportService;
    private final FileImportService fileImportService;
    private final AuditLogService auditLogService;

    @GetMapping("/export/products")
    public ResponseEntity<Resource> exportProducts() throws IOException {
        String filePath = fileExportService.exportProductsCsv();
        return downloadFile(filePath, "products.csv");
    }

    @GetMapping("/export/sales")
    public ResponseEntity<Resource> exportSales() throws IOException {
        String filePath = fileExportService.exportSalesCsv();
        return downloadFile(filePath, "sales.csv");
    }

    @GetMapping("/export/report")
    public ResponseEntity<Resource> exportReport() throws IOException {
        String filePath = fileExportService.generateInventoryReport();
        return downloadFile(filePath, "inventory_report.txt");
    }

    @PostMapping("/import/products")
    public ResponseEntity<Map<String, Object>> importProducts(@RequestParam("file") MultipartFile file) throws IOException {
        int count = fileImportService.importProductsCsv(file);
        return ResponseEntity.ok(Map.of(
                "message", "Import complete",
                "importedCount", count
        ));
    }

    @GetMapping("/logs/activity")
    public ResponseEntity<Map<String, String>> getActivityLog(
            @RequestParam(defaultValue = "50") int lines) {
        String content = auditLogService.readRecentLog(lines);
        return ResponseEntity.ok(Map.of("log", content));
    }

    private ResponseEntity<Resource> downloadFile(String filePath, String downloadName) {
        File file = new File(filePath);
        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
