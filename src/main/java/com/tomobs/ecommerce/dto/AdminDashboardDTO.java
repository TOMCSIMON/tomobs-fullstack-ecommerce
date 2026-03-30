package com.tomobs.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminDashboardDTO {

    private long totalUsers;
    private long totalOrders;
    private BigDecimal totalSale;
    private List<AdminSalesDTO> salesTable;
}
