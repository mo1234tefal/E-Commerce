package com.mo.ecommerce.repository;

import com.mo.ecommerce.entity.Cart;
import com.mo.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
