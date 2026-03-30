package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.dto.AdminDashboardDTO;
import com.tomobs.ecommerce.service.AdminDashboardService;
import com.tomobs.ecommerce.service.OrderService;
import com.tomobs.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final UserService userService;
    private final OrderService orderService;

    @Override
    public AdminDashboardDTO getDashboardSummary() {

        long users = userService.findTotalUsers();
        AdminDashboardDTO dto = new AdminDashboardDTO();
        dto.setTotalUsers(users);
        return dto;
    }
}
