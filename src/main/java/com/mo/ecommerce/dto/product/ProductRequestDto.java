package com.mo.ecommerce.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDto {
    @NotNull(message ="Product name is required ")
    private String name ;
    @NotNull(message = "Price is required")
    private Double price;
    @NotNull(message = "Description is required")
    private String description;
    @NotNull(message = "Image Url is required")
    private String imageUrl;
    @Min(0)
    @NotNull(message = "Stock is required")
    private Integer stock;
    @NotNull(message = "Category ID is required")
    private Long categoryId;


}
