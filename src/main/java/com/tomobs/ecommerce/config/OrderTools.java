package com.tomobs.ecommerce.config;

import com.tomobs.ecommerce.model.Orders;
import com.tomobs.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OrderTools {

    private final OrderService orderService;

    @Tool(description = "Check the status of a customer's order by order ID")
    public String checkOrderStatus(String orderId) {
        Orders order = orderService.getOrderById(Long.parseLong(orderId));
        return "Order %s is currently: %s".formatted(orderId, order.getStatus());
    }

    @Tool(description = "Submit a cancellation request for a customer's order. This does not immediately cancel the order.")
    public String cancelOrder(Long orderId, String cancelReason) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            orderService.saveCancelRequest(email, orderId, cancelReason);
            return "Cancellation request for order %d has been submitted.".formatted(orderId);
        } catch (RuntimeException e) {
            return "Could not submit cancellation for order %d: %s".formatted(orderId, e.getMessage());
        }
    }
}
