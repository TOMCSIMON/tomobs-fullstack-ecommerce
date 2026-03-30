package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.dto.AdminDashboardDTO;
import com.tomobs.ecommerce.dto.DailyEarningMapping;
import com.tomobs.ecommerce.enums.PaymentStatus;
import com.tomobs.ecommerce.repository.OrdersRepository;
import com.tomobs.ecommerce.service.AdminDashboardService;
import com.tomobs.ecommerce.service.OrderService;
import com.tomobs.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final UserService userService;
    private final OrderService orderService;
    private final OrdersRepository ordersRepository;

    @Override
    public AdminDashboardDTO getDashboardSummary(LocalDate startDate, LocalDate endDate) {

        AdminDashboardDTO dto = new AdminDashboardDTO();
        dto.setTotalUsers(userService.findTotalUsers());
        dto.setTotalOrders(orderService.findOrders());
        dto.setTotalSale(ordersRepository.sumTotalRevenueByStatus(PaymentStatus.SUCCESS));

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        List<DailyEarningMapping> earnings = ordersRepository.getDailyEarnings(start, end, PaymentStatus.SUCCESS.name());

        Map<String, BigDecimal> earningMap = new HashMap<>();
        for (DailyEarningMapping e : earnings) {
            earningMap.put(e.getDate(), e.getAmount());
        }

        List<String> labels = new ArrayList<>();
        List<BigDecimal> data = new ArrayList<>();

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            String dbDateKey = currentDate.toString();
            labels.add(currentDate.format(DateTimeFormatter.ofPattern("dd MMM")));
            data.add(earningMap.getOrDefault(dbDateKey, BigDecimal.ZERO));
            currentDate = currentDate.plusDays(1);
        }
        dto.setLabels(labels);
        dto.setChartData(data);
        return dto;
    }
}
