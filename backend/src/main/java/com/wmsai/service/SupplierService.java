package com.wmsai.service;

import com.wmsai.entity.Supplier;
import com.wmsai.oopj.Searchable;
import com.wmsai.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Supplier service implementing Searchable.
 * Covers T037, OOPJ-21 (List\<Supplier\> in method signatures).
 */
@Service
@RequiredArgsConstructor
public class SupplierService implements Searchable {

    private final SupplierRepository supplierRepository;

    /** Uses List\<Supplier\> [OOPJ-21]. */
    public List<Supplier> getSupplierList() {
        return supplierRepository.findAll();
    }

    public Supplier createSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @Override
    public Object searchById(int id) {
        return supplierRepository.findById(id).orElse(null);
    }

    @Override
    public Object searchByName(String name) {
        return supplierRepository.findByNameContainingIgnoreCase(name);
    }
}
