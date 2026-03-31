package com.tomobs.ecommerce.service.impl;

import com.tomobs.ecommerce.dto.AdminOrderListDTO;
import com.tomobs.ecommerce.dto.AdminSalesDTO;
import com.tomobs.ecommerce.enums.PaymentStatus;
import com.tomobs.ecommerce.model.Orders;
import com.tomobs.ecommerce.repository.OrdersRepository;
import com.tomobs.ecommerce.service.AdminOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrdersRepository ordersRepository;

    @Override
    public Page<AdminOrderListDTO> getAllOrdersPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Orders> orderPage = ordersRepository.findAll(pageable);
        return orderPage.map(this::convertToDTO);
    }

    @Override
    public Page<AdminOrderListDTO> getOrdersFiltered(String keyword, String status, String sort, int page, int size) {

        Sort jpaSort = Sort.by(Sort.Direction.DESC, "createdAt");
        if ("date_asc".equalsIgnoreCase(sort)) {
            jpaSort = Sort.by(Sort.Direction.ASC, "createdAt");
        }
        if ("amount_desc".equalsIgnoreCase(sort)) {
            jpaSort = Sort.by(Sort.Direction.DESC, "totalAmount");
        }
        if ("amount_asc".equalsIgnoreCase(sort)) {
            jpaSort = Sort.by(Sort.Direction.ASC, "totalAmount");
        }
        Pageable pageable = PageRequest.of(page, size, jpaSort);
        Page<Orders> orderPage = ordersRepository.findFilteredOrders(keyword, status, pageable);
        return orderPage.map(this::convertToDTO);
    }

    @Override
    public Page<AdminSalesDTO> getSalesSummary() {

        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Orders> orderPage = ordersRepository.findAll(pageable);
        return orderPage.map(order -> {
            AdminSalesDTO dto = new AdminSalesDTO();
            dto.setId(order.getId());
            dto.setUserName(order.getUser().getUserName());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setStatus(order.getStatus());
            dto.setCreatedAt(order.getCreatedAt());
            return dto;
        });
    }

    @Override
    public List<AdminSalesDTO> getAllSalesForReport(LocalDate startDate, LocalDate endDate) {

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        List<Orders> ordersList = ordersRepository.findByCreatedAtBetweenAndPaymentStatusOrderByCreatedAtDesc(
                start, end, PaymentStatus.SUCCESS
        );
        return ordersList.stream().map(order -> {
            AdminSalesDTO dto = new AdminSalesDTO();
            dto.setId(order.getId());
            dto.setUserName(order.getUser().getUserName());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setStatus(order.getStatus());
            dto.setCreatedAt(order.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());
    }

    private AdminOrderListDTO convertToDTO(Orders order) {
        AdminOrderListDTO dto = new AdminOrderListDTO();
        dto.setId(order.getId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setPaymentType(order.getPaymentType());
        dto.setStatus(order.getStatus());
        dto.setTotalItems(order.getOrderItems().size());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUserName(order.getUser().getUserName());
        return dto;
    }
}