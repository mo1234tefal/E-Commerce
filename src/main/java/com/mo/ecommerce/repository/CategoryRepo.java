package com.mo.ecommerce.repository;

import com.mo.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category , Long> {

    Optional<Category> findByNameIgnoreCase(String categoryName);
}
