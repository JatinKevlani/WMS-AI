package com.wmsai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wmsai.entity.Category;
import com.wmsai.entity.Product;
import com.wmsai.entity.Supplier;
import com.wmsai.repository.CategoryRepository;
import com.wmsai.repository.ProductRepository;
import com.wmsai.repository.SupplierRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ApiSmokeTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Test
    void loginSupportsSeededStaffAndRejectsInvalidCredentials() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", "staff@wms.com",
                                "password", "staff123"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("staff@wms.com"))
                .andExpect(jsonPath("$.role").value("STAFF"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", "staff@wms.com",
                                "password", "wrong-password"
                        ))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }

    @Test
    @WithMockUser(username = "admin@wms.com", roles = "ADMIN")
    void adminCanViewAllUsers() throws Exception {
        mockMvc.perform(get("/api/auth/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "staff@wms.com", roles = "STAFF")
    void staffCanViewOnlySelfAndAccessAlerts() throws Exception {
        mockMvc.perform(get("/api/auth/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].email").value("staff@wms.com"));

        mockMvc.perform(get("/api/alerts/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").exists());
    }

    @Test
    @WithMockUser(username = "admin@wms.com", roles = "ADMIN")
    void productValidationSearchAndRecommendationsWork() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "sku", "TOO-LONG-SKU-123",
                                "name", "Invalid Product",
                                "unitPrice", new BigDecimal("9.99"),
                                "quantity", 2
                        ))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());

        Product product = productRepository.save(Product.builder()
                .sku("SMOKE001")
                .name("Smoke Test Product")
                .unitPrice(new BigDecimal("99.99"))
                .quantity(25)
                .minThreshold(5)
                .maxThreshold(100)
                .build());

        mockMvc.perform(get("/api/products/search")
                        .param("q", "SMOKE001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value("SMOKE001"))
                .andExpect(jsonPath("$[0].unitPrice").value(99.99));

        mockMvc.perform(get("/api/recommendations/restock/{productId}", product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("SMOKE001"))
                .andExpect(jsonPath("$.recommendation").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "admin@wms.com", roles = "ADMIN")
    void salesEndpointRecordsSaleAgainstProduct() throws Exception {
        Product product = productRepository.save(Product.builder()
                .sku("SALE0001")
                .name("Sales Smoke Product")
                .unitPrice(new BigDecimal("49.99"))
                .quantity(10)
                .minThreshold(2)
                .maxThreshold(50)
                .build());

        mockMvc.perform(post("/api/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "productId", product.getId(),
                                "quantitySold", 2,
                                "salePrice", new BigDecimal("49.99")
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product.sku").value("SALE0001"))
                .andExpect(jsonPath("$.quantitySold").value(2))
                .andExpect(jsonPath("$.totalAmount").value(99.98));
    }

    @Test
    @WithMockUser(username = "admin@wms.com", roles = "ADMIN")
    void analyticsEndpointReturnsStructuredLiveData() throws Exception {
        Category category = categoryRepository.save(Category.builder()
                .name("Analytics Category")
                .description("Analytics Category")
                .build());

        Product product = productRepository.save(Product.builder()
                .sku("ANLY001")
                .name("Analytics Product")
                .category(category)
                .unitPrice(new BigDecimal("25.00"))
                .quantity(20)
                .minThreshold(2)
                .maxThreshold(40)
                .build());

        mockMvc.perform(post("/api/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "productId", product.getId(),
                                "quantitySold", 3,
                                "salePrice", new BigDecimal("25.00")
                        ))))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/analytics/sales").param("period", "MOM"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.period").value("MOM"))
                .andExpect(jsonPath("$.periodLabel").isNotEmpty())
                .andExpect(jsonPath("$.timeSeries").isArray())
                .andExpect(jsonPath("$.topProducts[0].sku").value("ANLY001"))
                .andExpect(jsonPath("$.categoryBreakdown[0].category").value("Analytics Category"))
                .andExpect(jsonPath("$.inventoryHealth.total").value(1));
    }

    @Test
    @WithMockUser(username = "admin@wms.com", roles = "ADMIN")
    void productCreationPersistsAssignedCategoryAndSupplier() throws Exception {
        Category category = categoryRepository.save(Category.builder()
                .name("Smoke Category")
                .description("Smoke Category")
                .build());

        Supplier supplier = supplierRepository.save(Supplier.builder()
                .name("Smoke Supplier")
                .contactPerson("Verifier")
                .email("smoke-supplier@example.com")
                .phone("8888888888")
                .address("Smoke Address")
                .build());

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "sku", "REL0001",
                                "name", "Related Product",
                                "unitPrice", new BigDecimal("19.99"),
                                "quantity", 5,
                                "categoryId", category.getId(),
                                "supplierId", supplier.getId()
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category.id").value(category.getId()))
                .andExpect(jsonPath("$.supplier.id").value(supplier.getId()));

        Product savedProduct = productRepository.findBySku("REL0001").orElseThrow();
        org.junit.jupiter.api.Assertions.assertNotNull(savedProduct.getCategory());
        org.junit.jupiter.api.Assertions.assertNotNull(savedProduct.getSupplier());
        org.junit.jupiter.api.Assertions.assertEquals(category.getId(), savedProduct.getCategory().getId());
        org.junit.jupiter.api.Assertions.assertEquals(supplier.getId(), savedProduct.getSupplier().getId());
    }

    @Test
    @WithMockUser(username = "admin@wms.com", roles = "ADMIN")
    void orderEndpointsSerializeWithoutRecursiveLoop() throws Exception {
        Supplier supplier = supplierRepository.save(Supplier.builder()
                .name("Smoke Test Supplier")
                .contactPerson("Verifier")
                .email("verify@example.com")
                .phone("9999999999")
                .address("Test Address")
                .build());

        Product product = productRepository.save(Product.builder()
                .sku("ORDER001")
                .name("Order Smoke Product")
                .unitPrice(new BigDecimal("39.99"))
                .quantity(12)
                .minThreshold(3)
                .maxThreshold(40)
                .build());

        MvcResult createResult = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "supplierId", supplier.getId(),
                                "items", List.of(Map.of(
                                        "productId", product.getId(),
                                        "name", "ORDER001 - Order Smoke Product",
                                        "quantity", 4,
                                        "unitPrice", new BigDecimal("39.99")
                                ))
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.supplier.name").value("Smoke Test Supplier"))
                .andExpect(jsonPath("$.items[0].product.sku").value("ORDER001"))
                .andExpect(jsonPath("$.items[0].order").doesNotExist())
                .andReturn();

        JsonNode createdOrder = objectMapper.readTree(createResult.getResponse().getContentAsString());
        int orderId = createdOrder.get("id").asInt();

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].items[0].product.sku").value("ORDER001"));

        mockMvc.perform(put("/api/orders/{id}/status", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("status", "RECEIVED"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RECEIVED"));
    }
}
