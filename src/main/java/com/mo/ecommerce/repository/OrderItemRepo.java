package com.mo.ecommerce.repository;

import com.mo.ecommerce.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepo extends JpaRepository<OrderItem , Long> {
}
