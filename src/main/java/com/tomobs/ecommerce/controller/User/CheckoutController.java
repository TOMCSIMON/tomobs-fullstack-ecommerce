package com.tomobs.ecommerce.controller.User;

import com.tomobs.ecommerce.dto.CartDTO;
import com.tomobs.ecommerce.dto.UserAddressListDTO;
import com.tomobs.ecommerce.model.User;
import com.tomobs.ecommerce.service.CartService;
import com.tomobs.ecommerce.service.OrderService;
import com.tomobs.ecommerce.service.UserAddressService;
import com.tomobs.ecommerce.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/checkout")
public class CheckoutController {

  private final UserAddressService userAddressService;
  private final UserService userService;
  private final CartService cartService;
  private final OrderService orderService;

  public CheckoutController(
      UserAddressService userAddressService,
      OrderService orderService,
      UserService userService,
      CartService cartService) {

    this.userAddressService = userAddressService;
    this.userService = userService;
    this.cartService = cartService;
    this.orderService = orderService;
  }

  @GetMapping()
  public String viewCheckoutPage(
          Principal principal,
          Model model) {

    List<UserAddressListDTO> addressList = userAddressService.getAddress();

    String email = principal.getName();
    User user = userService.findByEmail(email);
    List<CartDTO> cartItems = cartService.findCart(user.getId());
    Double totalAmount = cartService.calculateTotal(user.getId());

    model.addAttribute("addressList", addressList);
    model.addAttribute("cartItems", cartItems);
    model.addAttribute("totalPrice", totalAmount);

    return "checkout";
  }

  @PostMapping("/placeOrder")
  public String placeOrder(
          @RequestParam Long addressId,
          @RequestParam String paymentMethod,
          Principal principal,
          RedirectAttributes redirectAttributes) {
    String email = principal.getName();
    Long orderId = orderService.placeOrder(email, addressId, paymentMethod);
    redirectAttributes.addAttribute("orderId", orderId);
    return "order-success.html";
  }
}
