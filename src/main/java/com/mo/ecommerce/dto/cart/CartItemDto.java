package com.mo.ecommerce.dto.cart;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemDto {
    private Long id;
    private Integer quantity;
    private Double price;
    private String productName;

}
