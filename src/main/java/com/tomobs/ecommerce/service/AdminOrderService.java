package com.tomobs.ecommerce.service;

import com.tomobs.ecommerce.dto.AdminOrderListDTO;
import com.tomobs.ecommerce.dto.AdminSalesDTO;
import org.springframework.data.domain.Page;
import java.time.LocalDate;
import java.util.List;


public interface AdminOrderService {

    Page<AdminOrderListDTO> getAllOrdersPaginated(int page, int size);

    Page<AdminOrderListDTO> getOrdersFiltered(String keyword, String status, String sort, int page, int size);

    Page<AdminSalesDTO> getSalesSummary();

    List<AdminSalesDTO> getAllSalesForReport(LocalDate startDate, LocalDate endDate);
}
