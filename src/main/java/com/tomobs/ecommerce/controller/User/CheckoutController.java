package com.tomobs.ecommerce.controller.User;

import com.tomobs.ecommerce.dto.CartDTO;
import com.tomobs.ecommerce.dto.UserAddressListDTO;
import com.tomobs.ecommerce.model.Orders;
import com.tomobs.ecommerce.model.User;
import com.tomobs.ecommerce.service.*;
import com.tomobs.ecommerce.service.impl.RazorpayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {

  private final RazorpayService razorpayService;
  private final UserAddressService userAddressService;
  private final UserService userService;
  private final CartService cartService;
  private final OrderService orderService;
  private final ProductVariantService productVariantService;

  @Value("${razorpay.key.id}")
  private String razorpayKeyId;

  @GetMapping()
  public String viewCheckoutPage(
          @RequestParam(value = "variantId", required = false) Long variantId,
          Principal principal,
          Model model) {

    String email = principal.getName();
    User user = userService.findByEmail(email);
    List<UserAddressListDTO> addressList = userAddressService.getAddress();

    List<CartDTO> checkoutItems;
    Double totalAmount;

    if (variantId != null) {
      CartDTO buyNowItem = productVariantService.getBuyNowVariant(variantId);
      checkoutItems = List.of(buyNowItem);
      totalAmount = buyNowItem.getPrice().doubleValue();
      model.addAttribute("buyNowVariantId", variantId);
    } else {
      checkoutItems = cartService.findCart(user.getId());
      totalAmount = cartService.calculateTotal(user.getId());
    }

    model.addAttribute("addressList", addressList);
    model.addAttribute("cartItems", checkoutItems);
    model.addAttribute("totalPrice", totalAmount);

    return "checkout";
  }

  @PostMapping("/placeOrder")
  public String placeOrder(
          @RequestParam(value = "variantId", required = false) Long variantId,
          @RequestParam Long addressId,
          @RequestParam String paymentMethod,
          Principal principal,
          RedirectAttributes redirectAttributes,
          Model model) {

    Long orderId;
    String email = principal.getName();
    User user = userService.findByEmail(email);
    if("CASH_ON_DELIVERY".equals(paymentMethod)){
      if (variantId != null) {
        orderId = orderService.placeOrderForBuyNow(variantId, email, addressId, paymentMethod);
      } else {
        orderId = orderService.placeOrder(email, addressId, paymentMethod);
      }
      model.addAttribute("orderId", orderId);
      return "order-success";
    }else {
      try{
        Double amount = (variantId != null) ? orderService.calculateBuyNowTotal(variantId) : cartService.calculateTotal(user.getId());
        JSONObject razorpayOrder = razorpayService.createRazorpayOrder(BigDecimal.valueOf(amount));
        model.addAttribute("razorpayOrderId", razorpayOrder.get("id"));
        model.addAttribute("amount", amount);
        model.addAttribute("razorpayKey", razorpayKeyId);
        model.addAttribute("addressId", addressId);
        model.addAttribute("variantId", variantId);
        return "razorpay-checkout";
      }catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Order failed: " + e.getMessage());
        return "redirect:/checkout" + (variantId != null ? "?variantId=" + variantId : "");
      }
    }
  }

  @PostMapping("/payment/verify")
  public String verifyPayment(
          @RequestParam String razorpay_payment_id,
          @RequestParam String razorpay_order_id,
          @RequestParam String razorpay_signature,
          @RequestParam Long addressId,
          @RequestParam(required = false) Long variantId,
          Principal principal,
          RedirectAttributes redirectAttributes) {

    try {
      boolean isValid = razorpayService.verifySignature(razorpay_order_id, razorpay_payment_id, razorpay_signature);
      if (isValid) {
        String email = principal.getName();
        Long orderId;
        if (variantId != null) {
          orderId = orderService.placeOrderForBuyNow(variantId, email, addressId, "ONLINE_PAYMENT");
        } else {
          orderId = orderService.placeOrder(email, addressId, "ONLINE_PAYMENT");
        }
        orderService.confirmPayment(orderId, razorpay_payment_id);
        redirectAttributes.addAttribute("orderId", orderId);
        return "redirect:/checkout/order-success";
      } else {
        log.error("Payment verification failed for Order ID: {}", razorpay_order_id);
        redirectAttributes.addFlashAttribute("error", "Payment verification failed. Please contact support.");
        return "redirect:/checkout/payment-failed";
      }
    } catch (Exception e) {
      log.error("Error during payment verification: ", e);
      redirectAttributes.addFlashAttribute("error", "An error occurred during verification.");
      return "redirect:/checkout/payment-failed";
    }
  }

  @GetMapping("/order-success")
  public String viewSuccessPage(@RequestParam("orderId") Long orderId, Model model) {
    try {
      Orders order = orderService.getOrderById(orderId);
      model.addAttribute("orderId", order.getId());
      model.addAttribute("totalAmount", order.getTotalAmount());
      model.addAttribute("paymentStatus", order.getPaymentStatus());
      model.addAttribute("orderStatus", order.getStatus());

      return "order-success";
    } catch (Exception e) {
      log.error("Error loading order success page: ", e);
      return "redirect:/orders";
    }
  }
}
