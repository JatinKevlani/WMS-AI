package com.wmsai;

import com.wmsai.entity.Product;
import com.wmsai.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class WmsAiBackendApplicationTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void contextLoads() {
        assertNotNull(productRepository, "ProductRepository should be loaded");
    }

    @Test
    void testProductCreation() {
        Product p = Product.builder()
                .sku("TSKU-001")
                .name("Integration Test Check")
                .unitPrice(new BigDecimal("10.00"))
                .quantity(50)
                .minThreshold(10)
                .maxThreshold(100)
                .build();
                
        Product saved = productRepository.save(p);
        assertNotNull(saved.getId());
        
        Product retrieved = productRepository.findById(saved.getId()).orElse(null);
        assertNotNull(retrieved);
        assertEquals("TSKU-001", retrieved.getSku());
    }
}
