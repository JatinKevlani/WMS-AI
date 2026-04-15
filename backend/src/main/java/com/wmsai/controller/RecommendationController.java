package com.wmsai.controller;

import com.wmsai.entity.ProductBundle;
import com.wmsai.service.AIRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * AI Recommendations REST endpoints.
 * Covers T054, REC-01 through REC-07.
 */
@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final AIRecommendationService aiService;

    @GetMapping("/restock/{productId}")
    public ResponseEntity<Map<String, Object>> getRestockRecommendation(@PathVariable int productId) {
        return ResponseEntity.ok(aiService.getRestockRecommendation(productId));
    }

    @GetMapping("/bundle/{productId}")
    public ResponseEntity<Map<String, Object>> getBundleRecommendation(@PathVariable int productId) {
        return ResponseEntity.ok(aiService.getBundleRecommendation(productId));
    }

    @GetMapping("/forecast/{productId}")
    public ResponseEntity<Map<String, Object>> getDemandForecast(@PathVariable int productId) {
        return ResponseEntity.ok(aiService.getDemandForecast(productId));
    }

    @PostMapping("/bundle/save")
    public ResponseEntity<ProductBundle> saveBundle(@RequestBody Map<String, String> body) {
        int productId = Integer.parseInt(body.get("productId"));
        String bundleName = body.get("bundleProductName");
        String reason = body.get("reason");
        return ResponseEntity.ok(aiService.saveBundle(productId, bundleName, reason));
    }
}
