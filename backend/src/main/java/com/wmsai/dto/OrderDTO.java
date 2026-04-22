package com.wmsai.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDTO {
    private Integer supplierId;
    private String notes;
    private List<OrderItemDTO> items;

    @Data
    public static class OrderItemDTO {
        private Integer productId;
        private Integer quantity;
        private BigDecimal unitPrice;
    }
}
