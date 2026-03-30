package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.dto.AdminDashboardDTO;
import com.tomobs.ecommerce.enums.PaymentStatus;
import com.tomobs.ecommerce.repository.OrdersRepository;
import com.tomobs.ecommerce.service.AdminDashboardService;
import com.tomobs.ecommerce.service.OrderService;
import com.tomobs.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final UserService userService;
    private final OrderService orderService;
    private final OrdersRepository ordersRepository;

    @Override
    public AdminDashboardDTO getDashboardSummary() {

        long users = userService.findTotalUsers();
        long orders = orderService.findOrders();
        BigDecimal totalSale = ordersRepository.sumTotalRevenueByStatus(PaymentStatus.SUCCESS);
        AdminDashboardDTO dto = new AdminDashboardDTO();
        dto.setTotalUsers(users);
        dto.setTotalOrders(orders);
        dto.setTotalSale(totalSale);
        return dto;
    }
}
