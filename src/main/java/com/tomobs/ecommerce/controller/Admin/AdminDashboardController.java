package com.tomobs.ecommerce.controller.Admin;

import com.tomobs.ecommerce.dto.AdminDashboardDTO;
import com.tomobs.ecommerce.dto.AdminSalesDTO;
import com.tomobs.ecommerce.service.AdminDashboardService;
import com.tomobs.ecommerce.service.AdminOrderService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;
    private final AdminOrderService adminOrderService;

    @GetMapping()
    public String adminDashboard(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            Model model) {

        if (startDate == null || endDate == null) {
            endDate = LocalDate.now();
            startDate = endDate.minusDays(6);
        }

        AdminDashboardDTO dashboard = adminDashboardService.getDashboardSummary(startDate, endDate);
        Page<AdminSalesDTO> salesDetails = adminOrderService.getSalesSummary();
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("dashboard", dashboard);
        model.addAttribute("salesDetails", salesDetails);
        return "admin/dashboard";
    }

    @GetMapping("/export-pdf")
    public void exportPdf(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            HttpServletResponse response) throws IOException {

        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ToMobs_Sales_Report_" + startDate + "_to_" + endDate + ".pdf";
        response.setHeader(headerKey, headerValue);

        if (startDate == null || endDate == null) {
            endDate = LocalDate.now();
            startDate = endDate.minusDays(6);
        }

        AdminDashboardDTO summary = adminDashboardService.getDashboardSummary(startDate, endDate);
        List<AdminSalesDTO> allSalesList = adminOrderService.getAllSalesForReport(startDate, endDate);
        adminDashboardService.generatePdfReport(response, allSalesList, summary, startDate, endDate);
    }
}
