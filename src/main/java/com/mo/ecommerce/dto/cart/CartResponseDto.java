package com.mo.ecommerce.dto.cart;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CartResponseDto {
    private Long id;
    private String username;
    private List<CartItemDto> cartItems;
    private Double totalprice;
}
