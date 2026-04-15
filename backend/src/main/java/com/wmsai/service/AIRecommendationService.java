package com.wmsai.service;

import com.wmsai.entity.*;
import com.wmsai.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * AI Recommendation Engine using Claude API.
 * Covers T050-T054, REC-01 through REC-07.
 * Falls back to rule-based logic if API key is not set.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AIRecommendationService {

    private final ProductRepository productRepository;
    private final SalesRepository salesRepository;
    private final ProductBundleRepository bundleRepository;
    private final AIRecommendationLogRepository logRepository;

    @Value("${anthropic.api.key:}")
    private String apiKey;

    @Value("${anthropic.api.model:claude-sonnet-4-20250514}")
    private String model;

    @Value("${anthropic.api.base-url:https://api.anthropic.com/v1}")
    private String baseUrl;

    /**
     * Gets restock recommendation for a product [REC-01].
     */
    public Map<String, Object> getRestockRecommendation(int productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        // Build context
        String prompt = buildRestockPrompt(product);

        // Try AI, fallback to rule-based
        String response = callAI(prompt, "restock", product);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("product", product.getName());
        result.put("sku", product.getSku());
        result.put("currentStock", product.getQuantity());
        result.put("minThreshold", product.getMinThreshold());
        result.put("recommendation", response);

        return result;
    }

    /**
     * Gets bundle recommendations for a product [REC-02].
     */
    public Map<String, Object> getBundleRecommendation(int productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        List<Product> allProducts = productRepository.findAll();
        String prompt = buildBundlePrompt(product, allProducts);
        String response = callAI(prompt, "bundle", product);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("product", product.getName());
        result.put("recommendation", response);
        result.put("savedBundles", bundleRepository.findByProductId(productId));

        return result;
    }

    /**
     * Saves a bundle recommendation [REC-04].
     */
    public ProductBundle saveBundle(int productId, String bundleProductName, String reason) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        ProductBundle bundle = ProductBundle.builder()
                .product(product)
                .bundleProductName(bundleProductName)
                .reason(reason)
                .build();

        return bundleRepository.save(bundle);
    }

    /**
     * Gets demand forecast for a product [REC-03].
     */
    public Map<String, Object> getDemandForecast(int productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        List<SalesTransaction> recentSales = salesRepository.findByProductId(productId);
        String prompt = buildForecastPrompt(product, recentSales);
        String response = callAI(prompt, "forecast", product);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("product", product.getName());
        result.put("totalSalesRecords", recentSales.size());
        result.put("forecast", response);

        return result;
    }

    private String callAI(String prompt, String type, Product product) {
        String response;

        if (apiKey == null || apiKey.isBlank()) {
            // Fallback to rule-based
            response = generateRuleBasedResponse(type, product);
            log.info("🤖 AI fallback used for {} (no API key)", type);
        } else {
            try {
                response = callClaudeAPI(prompt);
                log.info("🤖 Claude AI response received for {}", type);
            } catch (Exception e) {
                log.warn("Claude API error, falling back to rule-based: {}", e.getMessage());
                response = generateRuleBasedResponse(type, product);
            }
        }

        // Log the interaction [REC-07]
        AIRecommendationLog aiLog = AIRecommendationLog.builder()
                .recommendationType(type)
                .product(product)
                .promptSent(prompt.length() > 2000 ? prompt.substring(0, 2000) + "..." : prompt)
                .responseReceived(response)
                .build();
        logRepository.save(aiLog);

        return response;
    }

    @SuppressWarnings("unchecked")
    private String callClaudeAPI(String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", apiKey);
        headers.set("anthropic-version", "2023-06-01");

        Map<String, Object> body = Map.of(
                "model", model,
                "max_tokens", 500,
                "messages", List.of(Map.of("role", "user", "content", prompt))
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl + "/messages", request, Map.class);

        if (response.getBody() != null && response.getBody().containsKey("content")) {
            List<Map<String, Object>> content = (List<Map<String, Object>>) response.getBody().get("content");
            if (!content.isEmpty()) {
                return (String) content.get(0).get("text");
            }
        }

        return "Unable to generate recommendation.";
    }

    private String generateRuleBasedResponse(String type, Product product) {
        return switch (type) {
            case "restock" -> {
                if (product.getQuantity() < product.getMinThreshold()) {
                    int suggested = product.getMaxThreshold() - product.getQuantity();
                    yield "⚠️ Stock is below minimum threshold. Recommended restock quantity: " + suggested +
                          " units. Current stock: " + product.getQuantity() + "/" + product.getMinThreshold() +
                          ". Priority: HIGH. Consider placing a purchase order immediately.";
                } else if (product.getQuantity() > product.getMaxThreshold()) {
                    yield "📦 Product is overstocked (" + product.getQuantity() + "/" + product.getMaxThreshold() +
                          "). Consider running promotions or bundling with slower-moving items to reduce excess inventory.";
                } else {
                    yield "✅ Stock levels are healthy (" + product.getQuantity() + " units). " +
                          "Min: " + product.getMinThreshold() + ", Max: " + product.getMaxThreshold() +
                          ". No immediate action required.";
                }
            }
            case "bundle" -> "📦 Bundle Suggestions for " + product.getName() + ":\n" +
                    "1. Pair with complementary items in the same category for a 10-15% discount bundle.\n" +
                    "2. Create a starter kit including this product and related accessories.\n" +
                    "3. Offer a bulk-buy bundle (3+ units) at a 20% discount for B2B customers.";
            case "forecast" -> "📈 Demand Forecast for " + product.getName() + ":\n" +
                    "Based on current stock levels (" + product.getQuantity() + " units) and threshold settings, " +
                    "estimated runway: " + (product.getQuantity() / Math.max(1, product.getMinThreshold() / 10)) + " weeks. " +
                    "Recommend monitoring sales velocity and adjusting thresholds accordingly.";
            default -> "No recommendation available for type: " + type;
        };
    }

    private String buildRestockPrompt(Product product) {
        return "You are a warehouse management AI assistant. Analyze this product and provide a restock recommendation:\n" +
                "Product: " + product.getName() + " (SKU: " + product.getSku() + ")\n" +
                "Current Stock: " + product.getQuantity() + "\n" +
                "Min Threshold: " + product.getMinThreshold() + "\n" +
                "Max Threshold: " + product.getMaxThreshold() + "\n" +
                "Category: " + (product.getCategory() != null ? product.getCategory().getName() : "N/A") + "\n" +
                "Last Sale: " + (product.getLastSaleDate() != null ? product.getLastSaleDate() : "Never") + "\n\n" +
                "Provide a concise recommendation including: urgency level, suggested restock quantity, and any notes.";
    }

    private String buildBundlePrompt(Product product, List<Product> allProducts) {
        StringBuilder sb = new StringBuilder();
        sb.append("Suggest 3 product bundles for: ").append(product.getName())
                .append(" (").append(product.getSku()).append(")\n\n");
        sb.append("Available products in inventory:\n");
        for (Product p : allProducts.stream().limit(20).toList()) {
            sb.append("- ").append(p.getName()).append(" (").append(p.getSku())
                    .append(", ₹").append(p.getUnitPrice()).append(")\n");
        }
        sb.append("\nProvide 3 bundle suggestions with names and brief reasons.");
        return sb.toString();
    }

    private String buildForecastPrompt(Product product, List<SalesTransaction> sales) {
        return "Forecast demand for: " + product.getName() + " (SKU: " + product.getSku() + ")\n" +
                "Current Stock: " + product.getQuantity() + "\n" +
                "Total Sales Records: " + sales.size() + "\n" +
                "Total Quantity Sold: " + sales.stream().mapToInt(SalesTransaction::getQuantitySold).sum() + "\n\n" +
                "Provide a brief demand forecast and inventory runway estimate.";
    }
}
