package com.wmsai.service;

import com.wmsai.dto.OrderDTO;
import com.wmsai.entity.*;
import com.wmsai.exception.ProductNotFoundException;
import com.wmsai.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Purchase order management.
 * Covers T040, ORD-01 through ORD-04.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;

    @Transactional
    public PurchaseOrder createOrder(OrderDTO dto, User createdBy) {
        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found: " + dto.getSupplierId()));

        PurchaseOrder order = PurchaseOrder.builder()
                .supplier(supplier)
                .status(OrderStatus.PENDING)
                .notes(dto.getNotes())
                .createdBy(createdBy)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (OrderDTO.OrderItemDTO itemDTO : dto.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found: " + itemDTO.getProductId()));

            OrderItem item = OrderItem.builder()
                    .product(product)
                    .quantity(itemDTO.getQuantity())
                    .unitPrice(itemDTO.getUnitPrice() != null ? itemDTO.getUnitPrice() : product.getUnitPrice())
                    .build();

            order.addItem(item);
            BigDecimal lineTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(lineTotal);
        }

        order.setTotalAmount(total);
        PurchaseOrder saved = orderRepository.save(order);
        log.info("Purchase order created: #{} for supplier {} (₹{})", saved.getId(), supplier.getName(), total);
        return saved;
    }

    @Transactional
    public PurchaseOrder updateStatus(int orderId, String newStatus) {
        PurchaseOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        OrderStatus status = OrderStatus.valueOf(newStatus.toUpperCase());
        order.setStatus(status);

        // If order is received, stock-in all items
        if (status == OrderStatus.RECEIVED) {
            for (OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                product.setQuantity(product.getQuantity() + item.getQuantity());
                productRepository.save(product);
                log.info("Stock received: {} units of {} from PO#{}", item.getQuantity(), product.getSku(), orderId);
            }
        }

        return orderRepository.save(order);
    }

    public List<PurchaseOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    public PurchaseOrder getOrderById(int id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
    }

    public List<PurchaseOrder> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(OrderStatus.valueOf(status.toUpperCase()));
    }
}
