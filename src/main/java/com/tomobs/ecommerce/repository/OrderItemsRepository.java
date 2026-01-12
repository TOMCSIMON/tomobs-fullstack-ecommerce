package com.tomobs.ecommerce.repository;

import com.tomobs.ecommerce.model.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {}
