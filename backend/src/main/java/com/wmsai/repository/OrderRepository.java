package com.wmsai.repository;

import com.wmsai.entity.PurchaseOrder;
import com.wmsai.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<PurchaseOrder, Integer> {
    List<PurchaseOrder> findBySupplierId(Integer supplierId);
    List<PurchaseOrder> findByStatus(OrderStatus status);
}
