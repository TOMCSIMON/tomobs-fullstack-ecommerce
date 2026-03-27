package com.tomobs.ecommerce.controller.User;

import com.tomobs.ecommerce.dto.OrderListDTO;
import com.tomobs.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;


@Slf4j
@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping()
    public String viewOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            Model model){

        Page<OrderListDTO> orderPage = orderService.findOrders(page, size);
        model.addAttribute("orderItems", orderPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("pageSize", size);
        log.info("orderItems: {}", orderPage.getContent());
        return "order";
    }

    @GetMapping("/search")
    public String searchOrders(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            Model model){

        Page<OrderListDTO> orderPage = orderService.findOrdersWithSearch(page, size, search);
        model.addAttribute("orderItems", orderPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("pageSize", size);
        return "order :: order-fragment";
    }

    @ResponseBody
    @PostMapping("/cancel/{id}")
    public ResponseEntity<Map<String, Boolean>> cancelOrder(
            @PathVariable("id") Long orderId,
            @RequestParam(value = "cancelReason") String cancelReason,
            Principal principal) {

        orderService.saveCancelRequest(principal.getName(), orderId, cancelReason);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @ResponseBody
    @PostMapping("/return/{id}")
    public ResponseEntity<Map<String, Boolean>> returnOrder(
            @PathVariable("id") Long orderId,
            @RequestParam(value = "returnReason") String returnReason,
            Principal principal) {

        orderService.saveReturnRequest(principal.getName(), orderId, returnReason);
        return ResponseEntity.ok(Map.of("success", true));
    }
}
