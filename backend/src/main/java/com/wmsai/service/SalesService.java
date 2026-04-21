package com.wmsai.service;

import com.wmsai.dto.SalesDTO;
import com.wmsai.entity.*;
import com.wmsai.exception.InsufficientStockException;
import com.wmsai.repository.*;
import com.wmsai.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Sales transaction management.
 * Covers T043, SALES-01 through SALES-04.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SalesService {

    private final SalesRepository salesRepository;
    private final ProductRepository productRepository;
    private final StockService stockService;
    private final AuditLogService auditLogService;

    /** Records a sale, triggers stock-out, and updates last sale date. */
    @Transactional
    public SalesTransaction recordSale(SalesDTO dto, User soldBy) throws InsufficientStockException {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + dto.getProductId()));

        // Stock-out operation
        stockService.stockOut(product.getId(), dto.getQuantitySold());

        // Update last sale date
        product.setLastSaleDate(LocalDate.now());
        productRepository.save(product);

        // Calculate total and save transaction
        BigDecimal total = dto.getSalePrice().multiply(BigDecimal.valueOf(dto.getQuantitySold()));

        SalesTransaction txn = SalesTransaction.builder()
                .product(product)
                .quantitySold(dto.getQuantitySold())
                .salePrice(dto.getSalePrice())
                .totalAmount(total)
                .soldBy(soldBy)
                .build();

        SalesTransaction saved = salesRepository.save(txn);
        log.info("Sale recorded: {} x{} = ₹{}", product.getSku(), dto.getQuantitySold(), total);

        // Log to activity file [SALES-03]
        auditLogService.logActivity("SALE_RECORDED",
                soldBy != null ? soldBy.getEmail() : "SYSTEM",
                String.format("Product: %s (SKU: %s), Qty: %d, Price: ₹%s, Total: ₹%s",
                        product.getName(), product.getSku(), dto.getQuantitySold(), dto.getSalePrice(), total));

        return saved;
    }

    public List<SalesTransaction> getTransactions(LocalDateTime from, LocalDateTime to) {
        return salesRepository.findBySaleDateBetween(from, to);
    }

    public List<SalesTransaction> getTransactionsByProduct(int productId) {
        return salesRepository.findByProductId(productId);
    }

    public BigDecimal getTotalRevenue(LocalDateTime from, LocalDateTime to) {
        return salesRepository.findRevenueByPeriod(from, to);
    }

    public List<SalesTransaction> getAllTransactions() {
        return salesRepository.findAll();
    }
}
