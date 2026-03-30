package com.tomobs.ecommerce.controller.Admin;

import com.tomobs.ecommerce.dto.AdminDashboardDTO;
import com.tomobs.ecommerce.dto.AdminSalesDTO;
import com.tomobs.ecommerce.service.AdminDashboardService;
import com.tomobs.ecommerce.service.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;
    private final AdminOrderService adminOrderService;

    @GetMapping("/dashboard")
    public String adminDashboard(
            Model model) {

        AdminDashboardDTO dashboard = adminDashboardService.getDashboardSummary();
        Page<AdminSalesDTO> salesDetails = adminOrderService.getSalesSummary();
        model.addAttribute("dashboard", dashboard);
        model.addAttribute("salesDetails", salesDetails);
        return "/admin/dashboard";
    }
}
