package com.wmsai.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class SalesDTO {
    @NotNull
    private Integer productId;
    @Min(1)
    private Integer quantitySold;
    @DecimalMin("0.01")
    private BigDecimal salePrice;
}
