package com.tomobs.ecommerce.repository;

import com.tomobs.ecommerce.model.OrderItems;
import com.tomobs.ecommerce.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {

  List<OrderItems> findByOrders(Orders orders);
}
