package com.mo.ecommerce.dto.cart;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class CartItemRequest {
    private Long productId;
    private Integer quantity;
}
