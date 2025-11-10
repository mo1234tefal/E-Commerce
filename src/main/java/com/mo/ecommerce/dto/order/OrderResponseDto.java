package com.mo.ecommerce.dto.order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private String username;
    private List<OrderItemsDto> orderItems;
    private Double totalPrice;
    private String orderStatus;
    private LocalDateTime orderDate;
    private String trackingNumber;
    private String shippingAddress;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;

}
