package com.wmsai.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDTO {
    @NotBlank
    private String name;
    @NotBlank @Size(min = 4, max = 12)
    private String sku;
    private Integer categoryId;
    private Integer supplierId;
    @DecimalMin("0.01")
    private BigDecimal unitPrice;
    @Min(0)
    private Integer quantity;
    private Integer minThreshold;
    private Integer maxThreshold;
    private Integer overstockDays;
}
