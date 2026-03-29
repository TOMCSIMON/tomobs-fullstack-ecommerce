package com.tomobs.ecommerce.controller.User;

import com.tomobs.ecommerce.dto.OrderDetailsDTO;
import com.tomobs.ecommerce.model.Orders;
import com.tomobs.ecommerce.service.InvoiceService;
import com.tomobs.ecommerce.service.OrderDetailService;
import com.tomobs.ecommerce.service.OrderService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;
import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderDetailsController {

    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final InvoiceService invoiceService;

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

    @GetMapping("/download-invoice/{orderId}")
    public void downloadInvoice(
            @PathVariable Long orderId,
            HttpServletResponse response,
            Principal principal) throws IOException {

        Orders order = orderService.getOrderById(orderId);
        if (!order.getUser().getEmail().equals(principal.getName())) {
            throw new AccessDeniedException("Unauthorized access");
        }
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=invoice_" + orderId + ".pdf";
        response.setHeader(headerKey, headerValue);
        invoiceService.generate(order, response);
    }
}
