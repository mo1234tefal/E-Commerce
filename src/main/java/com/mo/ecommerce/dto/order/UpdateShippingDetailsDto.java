package com.mo.ecommerce.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateShippingDetailsDto {
    private Long orderId;
    private String shippingAddress;
    private String trackingNumber;
}
