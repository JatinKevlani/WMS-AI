package com.wmsai.repository;

import com.wmsai.entity.SalesTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface SalesRepository extends JpaRepository<SalesTransaction, Integer> {

    List<SalesTransaction> findBySaleDateBetween(LocalDateTime from, LocalDateTime to);

    List<SalesTransaction> findByProductId(Integer productId);

    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM SalesTransaction s WHERE s.saleDate BETWEEN :from AND :to")
    BigDecimal findRevenueByPeriod(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("SELECT s.product.id, s.product.name, SUM(s.quantitySold) as totalQty, SUM(s.totalAmount) as totalRev " +
           "FROM SalesTransaction s WHERE s.saleDate BETWEEN :from AND :to " +
           "GROUP BY s.product.id, s.product.name ORDER BY totalQty DESC")
    List<Object[]> findTopSellingProducts(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("SELECT s.product.category.name, SUM(s.totalAmount) " +
           "FROM SalesTransaction s WHERE s.saleDate BETWEEN :from AND :to " +
           "GROUP BY s.product.category.name")
    List<Object[]> findRevenueByCategory(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    /** Slow-moving products: lowest total sales quantity [ANA-06]. */
    @Query("SELECT s.product.id, s.product.name, s.product.sku, SUM(s.quantitySold) as totalQty " +
           "FROM SalesTransaction s WHERE s.saleDate BETWEEN :from AND :to " +
           "GROUP BY s.product.id, s.product.name, s.product.sku ORDER BY totalQty ASC")
    List<Object[]> findSlowMovingProducts(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
