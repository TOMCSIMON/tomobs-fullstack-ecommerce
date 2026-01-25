package com.tomobs.ecommerce.service.impl;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.tomobs.ecommerce.model.Orders;
import com.tomobs.ecommerce.repository.OrdersRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RazorpayService {

    @Autowired
    private RazorpayClient razorpayClient;

    private final OrdersRepository ordersRepository;

    public RazorpayService(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    public JSONObject createRazorpayOrder(Orders order) throws RazorpayException {

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", order.getTotalAmount().multiply(new java.math.BigDecimal("100")).intValue()); // amount in paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "order_" + order.getId());

        Order razorpayOrder = razorpayClient.orders.create(orderRequest);

        // Save Razorpay order ID to your order
        order.setRazorpayOrderId(razorpayOrder.get("id"));
        ordersRepository.save(order);

        return razorpayOrder.toJson();
    }

    // VERIFY PAYMENT
    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    public boolean verifySignature(String orderId, String paymentId, String signature) {
        try {
            String data = orderId + "|" + paymentId;

            log.info("orderId: {}" ,orderId);
            log.info("paymentId: {}", paymentId);
            log.info("signature: {}", signature);

            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
            javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(razorpayKeySecret.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);

            byte[] hash = mac.doFinal(data.getBytes());

            // Convert byte array to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString().equals(signature);
        } catch(Exception e) {
            return false;
        }
    }
}
