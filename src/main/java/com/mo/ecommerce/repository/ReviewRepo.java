package com.mo.ecommerce.repository;

import com.mo.ecommerce.entity.Product;
import com.mo.ecommerce.entity.Review;
import com.mo.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepo extends JpaRepository<Review , Long> {
    List<Review> findByUser(User user);

    List<Review> findByProduct(Product product);
}
