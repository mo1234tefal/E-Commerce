package com.mo.ecommerce.repository;

import com.mo.ecommerce.entity.User;
import com.mo.ecommerce.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User , Integer> {
    Optional<User> findByEmail(String email);
    User findByRole(Role role);
    List<User> findAllByRole(Role role);
}
