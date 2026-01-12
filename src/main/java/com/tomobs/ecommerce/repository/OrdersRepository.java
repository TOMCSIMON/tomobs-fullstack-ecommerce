package com.tomobs.ecommerce.repository;

import com.tomobs.ecommerce.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
}
