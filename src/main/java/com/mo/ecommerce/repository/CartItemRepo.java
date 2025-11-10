package com.mo.ecommerce.repository;

import com.mo.ecommerce.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CartItem ,Long> {
}
