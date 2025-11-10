package com.mo.ecommerce.dto.wishlist;

import com.mo.ecommerce.dto.product.ProductResponse;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistDto {
    private Long id;
    private Integer userId;
    private List<ProductResponse> products;
}
