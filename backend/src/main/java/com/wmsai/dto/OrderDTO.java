package com.wmsai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDTO {
    private Integer supplierId;
    private String notes;
    private List<OrderItemDTO> items;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderItemDTO {
        private Integer productId;
        private Integer quantity;
        private BigDecimal unitPrice;
    }
}
