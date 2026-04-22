package com.wmsai.service;

import com.wmsai.dto.ProductDTO;
import com.wmsai.entity.*;
import com.wmsai.exception.*;
import com.wmsai.repository.*;
import com.wmsai.util.ProductCounter;
import com.wmsai.util.SKUValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Product CRUD service.
 * Covers T027, PROD-01 through PROD-10.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final AIRecommendationService aiRecommendationService;

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findByIsDeletedFalse(pageable);
    }

    public Product getProductById(int id) {
        return productRepository.findById(id)
                .filter(p -> !p.getIsDeleted())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    @Transactional
    public Product createProduct(ProductDTO dto) {
        // Validate SKU format
        if (!SKUValidator.isValidSKU(dto.getSku())) {
            throw new InvalidSKUException("Invalid SKU format: " + dto.getSku() + ". Must be 4-12 alphanumeric uppercase characters.");
        }

        // Check for duplicate SKU
        if (productRepository.existsBySku(dto.getSku())) {
            throw new DuplicateSKUException("SKU already exists: " + dto.getSku());
        }

        Product product = Product.builder()
                .sku(dto.getSku().toUpperCase())
                .name(dto.getName())
                .unitPrice(dto.getUnitPrice())
                .quantity(dto.getQuantity() != null ? dto.getQuantity() : 0)
                .minThreshold(dto.getMinThreshold() != null ? dto.getMinThreshold() : 50)
                .maxThreshold(dto.getMaxThreshold() != null ? dto.getMaxThreshold() : 500)
                .overstockDays(dto.getOverstockDays() != null ? dto.getOverstockDays() : 30)
                .build();

        if (dto.getCategoryId() != null) {
            product.setCategory(categoryRepository.findById(dto.getCategoryId()).orElse(null));
        }
        if (dto.getSupplierId() != null) {
            product.setSupplier(supplierRepository.findById(dto.getSupplierId()).orElse(null));
        }

        Product saved = productRepository.save(product);
        
        // Ensure relations are fully populated for the response
        if (saved.getCategory() != null) {
            saved.setCategory(categoryRepository.findById(saved.getCategory().getId()).orElse(null));
        }
        if (saved.getSupplier() != null) {
            saved.setSupplier(supplierRepository.findById(saved.getSupplier().getId()).orElse(null));
        }

        ProductCounter.increment();
        log.info("Product created: {} (SKU: {})", saved.getName(), saved.getSku());

        // Trigger AI bundle recommendation asynchronously [PROD-09]
        try {
            aiRecommendationService.getBundleRecommendation(saved.getId());
            log.info("AI bundle recommendation triggered for product: {}", saved.getSku());
        } catch (Exception e) {
            log.warn("AI recommendation failed for {}: {}", saved.getSku(), e.getMessage());
        }

        return saved;
    }

    @Transactional
    public Product updateProduct(int id, ProductDTO dto) {
        Product product = getProductById(id);

        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getUnitPrice() != null) product.setUnitPrice(dto.getUnitPrice());
        if (dto.getQuantity() != null) product.setQuantity(dto.getQuantity());
        if (dto.getMinThreshold() != null) product.setMinThreshold(dto.getMinThreshold());
        if (dto.getMaxThreshold() != null) product.setMaxThreshold(dto.getMaxThreshold());
        if (dto.getOverstockDays() != null) product.setOverstockDays(dto.getOverstockDays());
        if (dto.getCategoryId() != null) {
            product.setCategory(categoryRepository.findById(dto.getCategoryId()).orElse(null));
        }
        if (dto.getSupplierId() != null) {
            product.setSupplier(supplierRepository.findById(dto.getSupplierId()).orElse(null));
        }

        return productRepository.save(product);
    }

    @Transactional
    public void softDeleteProduct(int id) {
        Product product = getProductById(id);
        product.setIsDeleted(true);
        productRepository.save(product);
        log.info("Product soft-deleted: {} (SKU: {})", product.getName(), product.getSku());
    }

    /** Overloaded search methods [OOPJ-7]. */
    public List<Product> search(String query) {
        return productRepository.searchByNameOrSku(query);
    }

    public List<Product> search(int id) {
        return List.of(getProductById(id));
    }

    public List<Product> search(String name, String category) {
        return productRepository.searchByNameAndCategory(name, category);
    }
}
