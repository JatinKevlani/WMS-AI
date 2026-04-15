package com.wmsai.controller;

import com.wmsai.entity.Supplier;
import com.wmsai.entity.Product;
import com.wmsai.repository.SupplierRepository;
import com.wmsai.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<Supplier>> getAll() {
        return ResponseEntity.ok(supplierRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable int id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found: " + id));
        List<Product> products = productRepository.findBySupplierId(id);
        return ResponseEntity.ok(Map.of("supplier", supplier, "products", products));
    }

    @PostMapping
    public ResponseEntity<Supplier> create(@RequestBody Supplier supplier) {
        return ResponseEntity.ok(supplierRepository.save(supplier));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Supplier> update(@PathVariable int id, @RequestBody Supplier dto) {
        Supplier s = supplierRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found: " + id));
        if (dto.getName() != null) s.setName(dto.getName());
        if (dto.getContactPerson() != null) s.setContactPerson(dto.getContactPerson());
        if (dto.getEmail() != null) s.setEmail(dto.getEmail());
        if (dto.getPhone() != null) s.setPhone(dto.getPhone());
        if (dto.getAddress() != null) s.setAddress(dto.getAddress());
        return ResponseEntity.ok(supplierRepository.save(s));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Supplier>> search(@RequestParam String q) {
        return ResponseEntity.ok(supplierRepository.findByNameContainingIgnoreCase(q));
    }
}
