package com.tomobs.ecommerce.controller.Admin;

import com.tomobs.ecommerce.dto.AdminOrderListDTO;
import com.tomobs.ecommerce.dto.CategoryDTO;
import com.tomobs.ecommerce.enums.PaymentType;
import com.tomobs.ecommerce.model.Payment;
import com.tomobs.ecommerce.service.AdminOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/orders")
@Slf4j
@RequiredArgsConstructor
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @GetMapping()
    public String getOrders(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        Model model) {

        Page<AdminOrderListDTO> orderListPage = adminOrderService.getAllOrdersPaginated(page, size);
        model.addAttribute("orders", orderListPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", orderListPage.getTotalPages());
        return "admin/order-list";
      }
}
