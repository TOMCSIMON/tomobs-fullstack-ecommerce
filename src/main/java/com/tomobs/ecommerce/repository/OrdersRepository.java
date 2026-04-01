package com.tomobs.ecommerce.repository;

import com.tomobs.ecommerce.dto.DailyEarningMapping;
import com.tomobs.ecommerce.dto.OrderListDTO;
import com.tomobs.ecommerce.enums.OrderStatus;
import com.tomobs.ecommerce.enums.PaymentStatus;
import com.tomobs.ecommerce.model.Orders;
import com.tomobs.ecommerce.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

  @Query("""
      SELECT new com.tomobs.ecommerce.dto.OrderListDTO(
              o.id,
              MIN(pv.variantName),
              SUM(oi.quantity),
              o.totalAmount,
              o.status,
              MIN(vi.fileName)
              )
              FROM Orders o
              JOIN o.user u
              JOIN o.orderItems oi
              JOIN oi.productVariant pv
              LEFT JOIN pv.images vi ON vi.isPrimary = true
              WHERE u = :user
              GROUP BY o.id, o.totalAmount, o.status, o.createdAt
              ORDER BY o.createdAt DESC
      """)
  Page<OrderListDTO> findByUser(@Param("user") User user, Pageable pageable);

  @Query("""
      SELECT new com.tomobs.ecommerce.dto.OrderListDTO(
              o.id,
              MIN(pv.variantName),
              SUM(oi.quantity),
              o.totalAmount,
              o.status,
              MIN(vi.fileName)
              )
              FROM Orders o
              JOIN o.user u
              JOIN o.orderItems oi
              JOIN oi.productVariant pv
              LEFT JOIN pv.images vi ON vi.isPrimary = true
              WHERE u = :user
              AND (LOWER(pv.variantName) LIKE LOWER(CONCAT('%', :search, '%')))
              GROUP BY o.id, o.totalAmount, o.status, o.createdAt
              ORDER BY o.createdAt DESC
      """)
  Page<OrderListDTO> findByUserAndSearch(@Param("user") User user,
                                         @Param("search") String search,
                                         Pageable pageable);

  long countByStatus(OrderStatus status);

  @Query("""
        SELECT o FROM Orders o
        LEFT JOIN o.user u
        WHERE
        (:keyword IS NULL OR LOWER(CAST(u.userName AS string)) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')))
        AND (:status IS NULL OR UPPER(CAST(o.status AS string)) = UPPER(CAST(:status AS string)))
        """)
  Page<Orders> findFilteredOrders(@Param("keyword") String keyword, @Param("status") String status, Pageable pageable);

  @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Orders o WHERE o.paymentStatus = :status")
  BigDecimal sumTotalRevenueByStatus(@Param("status")PaymentStatus status);

  @Query(value = "SELECT TO_CHAR(DATE(created_at), 'YYYY-MM-DD') as date, SUM(total_amount) as amount " +
          "FROM orders " +
          "WHERE created_at >= :startDate AND created_at <= :endDate " +
          "AND payment_status = :status " +
          "GROUP BY DATE(created_at) " +
          "ORDER BY DATE(created_at)", nativeQuery = true)
  List<DailyEarningMapping> getDailyEarnings(
          @Param("startDate") LocalDateTime startDate,
          @Param("endDate") LocalDateTime endDate,
          @Param("status") String status
  );

  List<Orders> findByCreatedAtBetweenAndPaymentStatusOrderByCreatedAtDesc(
          LocalDateTime startDate,
          LocalDateTime endDate,
          PaymentStatus status
  );
}