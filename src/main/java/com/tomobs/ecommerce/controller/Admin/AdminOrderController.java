package com.tomobs.ecommerce.controller.Admin;

import com.tomobs.ecommerce.dto.AdminOrderDetailsDTO;
import com.tomobs.ecommerce.dto.AdminOrderListDTO;
import com.tomobs.ecommerce.enums.OrderStatus;
import com.tomobs.ecommerce.service.AdminOrderService;
import com.tomobs.ecommerce.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/orders")
@Slf4j
@RequiredArgsConstructor
public class AdminOrderController {

    private final AdminOrderService adminOrderService;
    private final OrderDetailService orderDetailService;

    @GetMapping()
    public String getOrders(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "6") int size,
        Model model) {

        Page<AdminOrderListDTO> orderListPage = adminOrderService.getAllOrdersPaginated(page, size);
        long pendingCount = adminOrderService.getOrderCountByStatus(OrderStatus.PENDING);
        long cancelCount = adminOrderService.getOrderCountByStatus(OrderStatus.CANCELLED);
        model.addAttribute("orders", orderListPage.getContent());
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("cancelCount", cancelCount);
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

        Page<AdminOrderListDTO> orderListPage = adminOrderService.getOrdersFiltered(keyword, status, sort, page, size);

        model.addAttribute("orders", orderListPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", orderListPage.getTotalPages());
        model.addAttribute("keyword", keyword);

        return "admin/order-list :: order-grid-fragment";
    }

    @GetMapping("/edit/{id}")
    public String viewOrderDetails(
            @PathVariable("id") Long orderId,
            Model model) {

        AdminOrderDetailsDTO orderDetails = orderDetailService.findOrderDetailsForAdmin(orderId);
        model.addAttribute("order", orderDetails);
        return "admin/admin-order-details";
    }
}
