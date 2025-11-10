package com.mo.ecommerce.repository;

import com.mo.ecommerce.entity.Order;
import com.mo.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order , Long> {
    List<Order> findAllByUser(User user);
}
