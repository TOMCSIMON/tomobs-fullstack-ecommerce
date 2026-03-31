package com.tomobs.ecommerce.service;

import com.tomobs.ecommerce.dto.AdminDashboardDTO;
import com.tomobs.ecommerce.dto.AdminSalesDTO;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface AdminDashboardService {

    AdminDashboardDTO getDashboardSummary(LocalDate startDate, LocalDate endDate);

    void generatePdfReport(HttpServletResponse response, List<AdminSalesDTO> salesList, AdminDashboardDTO summary, LocalDate startDate, LocalDate endDate) throws IOException;
}
