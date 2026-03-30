package com.tomobs.ecommerce.service;

import com.tomobs.ecommerce.dto.AdminDashboardDTO;
import java.time.LocalDate;

public interface AdminDashboardService {

    AdminDashboardDTO getDashboardSummary(LocalDate startDate, LocalDate endDate);
}
