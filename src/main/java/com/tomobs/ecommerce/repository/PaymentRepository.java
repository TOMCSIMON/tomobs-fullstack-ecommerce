package com.tomobs.ecommerce.repository;

import com.tomobs.ecommerce.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentRepository extends JpaRepository<Payment, Long> { }
