package com.tomobs.ecommerce.controller.Admin;

import com.tomobs.ecommerce.enums.OrderStatus;
import com.tomobs.ecommerce.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderRestController {

    private final OrderDetailService orderDetailService;

    @PatchMapping("/update-status/{id}")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {

        try {
            orderDetailService.updateStatus(id, status);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}
