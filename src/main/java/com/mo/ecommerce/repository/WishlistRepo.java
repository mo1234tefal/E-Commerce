package com.mo.ecommerce.repository;

import com.mo.ecommerce.entity.User;
import com.mo.ecommerce.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistRepo extends JpaRepository<Wishlist , Long> {

    Optional<Wishlist> findByUser(User user);
}

