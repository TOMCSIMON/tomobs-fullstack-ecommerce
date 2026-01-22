package com.tomobs.ecommerce.controller.User;

import com.tomobs.ecommerce.dto.CartDTO;
import com.tomobs.ecommerce.dto.UserAddressListDTO;
import com.tomobs.ecommerce.model.Orders;
import com.tomobs.ecommerce.model.User;
import com.tomobs.ecommerce.service.CartService;
import com.tomobs.ecommerce.service.OrderService;
import com.tomobs.ecommerce.service.UserAddressService;
import com.tomobs.ecommerce.service.UserService;
import com.tomobs.ecommerce.service.impl.RazorpayService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

  @Autowired private RazorpayService razorpayService;

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
  public String viewCheckoutPage(Principal principal, Model model) {

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

  @Value("${razorpay.key.id}")
  private String razorpayKeyId;

  @PostMapping("/placeOrder")
  public String placeOrder(
      @RequestParam Long addressId,
      @RequestParam String paymentMethod,
      Principal principal,
      RedirectAttributes redirectAttributes,
      Model model) {

    String email = principal.getName();
    Long orderId = orderService.placeOrder(email, addressId, paymentMethod);

    if ("ONLINE_PAYMENT".equals(paymentMethod)) {
      try {
        Orders order = orderService.getOrderById(orderId);
        JSONObject razorpayOrder = razorpayService.createRazorpayOrder(order);

        model.addAttribute("orderId", orderId);
        model.addAttribute("razorpayOrderId", razorpayOrder.get("id"));
        model.addAttribute("razorpayKey", razorpayKeyId);
        model.addAttribute("amount", order.getTotalAmount());
        model.addAttribute("customerName", order.getUser().getUserName());
        model.addAttribute("customerEmail", order.getUser().getEmail());

        return "razorpay-checkout";
      } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Payment initialization failed");
        return "redirect:/checkout";
      }
    } else {
      redirectAttributes.addAttribute("orderId", orderId);
      return "redirect:/order-success";
    }
  }

  @PostMapping("/payment/verify")
  public String verifyPayment(
      @RequestParam String razorpay_payment_id,
      @RequestParam String razorpay_order_id,
      @RequestParam String razorpay_signature,
      @RequestParam Long orderId,
      RedirectAttributes redirectAttributes) {
    System.out.println("Reached Payment verification:");
    System.out.println();
    System.out.println();
    try {
      boolean isValid =
          razorpayService.verifySignature(
              razorpay_payment_id, razorpay_order_id, razorpay_signature);

      if (isValid) {
        orderService.confirmPayment(orderId, razorpay_payment_id);
        redirectAttributes.addAttribute("orderId", orderId);
        return "redirect:/order-success";
      } else {
        redirectAttributes.addAttribute("error", "Payment Verification failed!");
        return "redirect:/payment-failed";
      }
    } catch (Exception e) {
      redirectAttributes.addAttribute("error", "Payment Verification error!");
      return "redirect:/payment-failed";
    }
  }
}
