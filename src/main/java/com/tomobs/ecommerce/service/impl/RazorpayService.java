package com.tomobs.ecommerce.service.impl;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class RazorpayService {

    private final RazorpayClient razorpayClient;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    public JSONObject createRazorpayOrder(BigDecimal totalAmount) throws RazorpayException {
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", totalAmount.multiply(new java.math.BigDecimal("100")).intValue());
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "txn_" + System.currentTimeMillis());
        Order razorpayOrder = razorpayClient.orders.create(orderRequest);
        return razorpayOrder.toJson();
    }

    public boolean verifySignature(String razorpay_order_id, String razorpay_payment_id, String razorpay_signature) {
        try {
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", razorpay_order_id);
            options.put("razorpay_payment_id", razorpay_payment_id);
            options.put("razorpay_signature", razorpay_signature);
            return Utils.verifyPaymentSignature(options, razorpayKeySecret);

        } catch (Exception e) {
            log.error("Signature verification failed due to exception: ", e);
            return false;
        }
    }
}
