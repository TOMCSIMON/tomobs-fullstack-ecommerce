package com.tomobs.ecommerce.controller.User;


import com.tomobs.ecommerce.service.OrderService;
import com.tomobs.ecommerce.service.UserAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/order_details")
@Slf4j
public class OrderDetailsController {

    private final OrderService orderService;


    public OrderDetailsController(OrderService orderService, UserAddressService userAddressService) {
        this.orderService = orderService;
    }

    @GetMapping()
    public String viewOrderDetails(
            Model model

    ){
      return "order-details";
    }
}
