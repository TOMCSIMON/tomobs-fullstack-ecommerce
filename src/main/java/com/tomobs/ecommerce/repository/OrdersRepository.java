package com.tomobs.ecommerce.repository;

import com.tomobs.ecommerce.dto.OrderListDTO;
import com.tomobs.ecommerce.model.Orders;
import com.tomobs.ecommerce.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

  @Query("""
        SELECT new com.tomobs.ecommerce.dto.OrderListDTO(
                o.id,
                pv.variantName,
                SUM(oi.quantity),
                o.totalAmount,
                o.status,
                vi.fileName
                )
                FROM Orders o
                JOIN o.user u
                JOIN o.orderItems oi
                JOIN oi.productVariant pv
                LEFT JOIN pv.images vi ON vi.isPrimary = true
                WHERE u = :user
                GROUP BY o.id, pv.variantName, o.totalAmount, o.status, vi.fileName
                ORDER BY o.createdAt DESC
        """)
  Page<OrderListDTO> findByUser(
          @Param("user") User user,
          Pageable pageable
  );
}