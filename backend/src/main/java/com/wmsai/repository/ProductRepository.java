package com.wmsai.repository;

import com.wmsai.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    Page<Product> findByIsDeletedFalse(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isDeleted = false AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(p.sku) LIKE LOWER(CONCAT('%', :q, '%')))")
    List<Product> searchByNameOrSku(@Param("q") String query);

    @Query("SELECT p FROM Product p WHERE p.isDeleted = false AND p.category.name = :category AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    List<Product> searchByNameAndCategory(@Param("name") String name, @Param("category") String category);

    List<Product> findBySupplierId(Integer supplierId);

    /** Products with stock below minimum threshold. */
    @Query("SELECT p FROM Product p WHERE p.isDeleted = false AND p.quantity < p.minThreshold")
    List<Product> findLowStockProducts();

    /** Products with stock above max threshold and no recent sales. */
    @Query("SELECT p FROM Product p WHERE p.isDeleted = false AND p.quantity > p.maxThreshold AND " +
           "(p.lastSaleDate IS NULL OR p.lastSaleDate < :cutoffDate)")
    List<Product> findOverstockProducts(@Param("cutoffDate") LocalDate cutoffDate);
}
