package com.mo.ecommerce.repository;

import com.mo.ecommerce.entity.Address;
import com.mo.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepo extends JpaRepository<Address , Long> {
    List<Address> findByUser(User user);
}
