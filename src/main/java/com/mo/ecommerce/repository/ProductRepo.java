package com.mo.ecommerce.repository;


import com.mo.ecommerce.entity.Category;
import com.mo.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product , Long> {
    List<Product> findByNameContainingIgnoreCase(String keyword);
    List<Product> findByCategory(Category category);
}
