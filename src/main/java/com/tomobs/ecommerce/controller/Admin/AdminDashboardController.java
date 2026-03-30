package com.tomobs.ecommerce.controller.Admin;

import com.tomobs.ecommerce.dto.AdminDashboardDTO;
import com.tomobs.ecommerce.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @GetMapping("/dashboard")
    public String adminDashboard(
            Model model) {

        AdminDashboardDTO dashboard = adminDashboardService.getDashboardSummary();
        model.addAttribute("dashboard", dashboard);
        return "/admin/dashboard";
    }
}
