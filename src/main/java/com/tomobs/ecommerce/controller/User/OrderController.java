package com.tomobs.ecommerce.controller.User;

import com.tomobs.ecommerce.dto.OrderListDTO;
import com.tomobs.ecommerce.model.User;
import com.tomobs.ecommerce.service.OrderService;
import com.tomobs.ecommerce.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
    }

    @GetMapping()
    public String viewOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size,
            Model model){

        Page<OrderListDTO> orderPage = orderService.findOrders(page, size);
        model.addAttribute("orderItems", orderPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("pageSize", size);
        return "order";
    }
}
