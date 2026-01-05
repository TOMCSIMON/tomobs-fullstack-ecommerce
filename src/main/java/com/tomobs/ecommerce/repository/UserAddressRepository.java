package com.tomobs.ecommerce.repository;

import com.tomobs.ecommerce.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<Address, Long> {}
