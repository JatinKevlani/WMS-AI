package com.wmsai.controller;

import com.wmsai.dto.ProductDTO;
import com.wmsai.entity.Product;
import com.wmsai.exception.InsufficientStockException;
import com.wmsai.service.ProductService;
import com.wmsai.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Product REST controller.
 * Covers T030 — all product endpoints from PRD Section 9.
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final StockService stockService;

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String q) {
        return ResponseEntity.ok(productService.search(q));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDTO dto) {
        return ResponseEntity.ok(productService.createProduct(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @RequestBody ProductDTO dto) {
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable int id) {
        productService.softDeleteProduct(id);
        return ResponseEntity.ok(Map.of("message", "Product deleted successfully"));
    }

    @PostMapping("/{id}/stock-in")
    public ResponseEntity<Product> stockIn(@PathVariable int id, @RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(stockService.stockIn(id, body.get("quantity")));
    }

    @PostMapping("/{id}/stock-out")
    public ResponseEntity<Product> stockOut(@PathVariable int id, @RequestBody Map<String, Integer> body)
            throws InsufficientStockException {
        return ResponseEntity.ok(stockService.stockOut(id, body.get("quantity")));
    }
}
