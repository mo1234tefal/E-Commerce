package com.mo.ecommerce.enums;

public enum OrderStatus {
    PENDING,        // Just placed, waiting for confirmation
    CONFIRMED,      // Seller/admin confirms the order
    SHIPPED,        // Handed over to delivery service
    OUT_FOR_DELIVERY, // Currently being delivered
    DELIVERED,      // Successfully delivered to customer
    CANCELED,       // Canceled by user/admin
    RETURN_REQUESTED, // Customer requested return
    RETURNED
}
