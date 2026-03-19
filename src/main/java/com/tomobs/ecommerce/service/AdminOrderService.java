package com.tomobs.ecommerce.service;

import com.tomobs.ecommerce.dto.AdminOrderListDTO;
import org.springframework.data.domain.Page;


public interface AdminOrderService {

    Page<AdminOrderListDTO> getAllOrdersPaginated(int page, int size);

    Page<AdminOrderListDTO> getOrdersFiltered(String keyword, int page, int size);
}
