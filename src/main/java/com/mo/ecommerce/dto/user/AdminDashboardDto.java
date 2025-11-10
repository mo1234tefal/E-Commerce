package com.mo.ecommerce.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminDashboardDto {
    private long totalUsers;
    private long totalOrders;
    private long totalProducts;
    private double totalRevenue;
}
