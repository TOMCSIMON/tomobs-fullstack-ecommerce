package com.tomobs.ecommerce.controller.Admin;

import com.tomobs.ecommerce.dto.AdminOrderListDTO;
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
        @RequestParam(defaultValue = "6") int size,
        Model model) {

        Page<AdminOrderListDTO> orderListPage = adminOrderService.getAllOrdersPaginated(page, size);
        model.addAttribute("orders", orderListPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalOrders", orderListPage.getTotalElements());
        model.addAttribute("totalPage", orderListPage.getTotalPages());
        return "admin/order-list";
      }

    @GetMapping("/filter")
    public String getOrderFiltered(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "sort", defaultValue = "date_desc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            Model model) {

        Page<AdminOrderListDTO> orderListPage = adminOrderService.getOrdersFiltered(keyword, page, size);

        model.addAttribute("orders", orderListPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", orderListPage.getTotalPages());
        model.addAttribute("keyword", keyword);

        return "admin/order-list :: order-grid-fragment";
    }
}
