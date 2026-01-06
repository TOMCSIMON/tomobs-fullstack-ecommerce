package com.tomobs.ecommerce.repository;

import com.tomobs.ecommerce.model.Address;
import com.tomobs.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserAddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUser(User user);


    Optional<Address> findByUserAndIsDefaultTrue(User user);

    boolean existsByUser(User user);
}

