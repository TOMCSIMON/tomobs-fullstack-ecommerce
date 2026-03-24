package com.tomobs.ecommerce.controller.User;

import com.tomobs.ecommerce.dto.OrderDetailsDTO;
import com.tomobs.ecommerce.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderDetailsController {

    private final OrderDetailService orderDetailService;

    @GetMapping("/details/{id}")
    public String viewOrderDetails(
            @PathVariable("id") Long orderId,
            Principal principal,
            Model model) {

        String email = principal.getName();
        OrderDetailsDTO orderDetails = orderDetailService.findOrderDetails(email, orderId);
        model.addAttribute("order", orderDetails);
        return "order-details";
    }
}
