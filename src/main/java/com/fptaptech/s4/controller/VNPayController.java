package com.fptaptech.s4.controller;

import com.fptaptech.s4.entity.Payment;
import com.fptaptech.s4.repository.PaymentRepository;
import com.fptaptech.s4.service.impl.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@org.springframework.stereotype.Controller

@RestController
@RequestMapping("/api")
public class VNPayController {

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private PaymentRepository paymentRepository;

    @PostMapping("/submitOrder")
    public ResponseEntity<Map<String, String>> submitOrder(@RequestParam("amount") int orderTotal,
                                                           @RequestParam("orderInfo") String orderInfo,
                                                           @RequestParam("bookingId") Long bookingId,
                                                           HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);

        Map<String, String> response = new HashMap<>();
        response.put("vnpayUrl", vnpayUrl);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vnpay-payment")
    public ResponseEntity<Map<String, Object>> getPaymentStatus(HttpServletRequest request) {
        int paymentStatus = vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");


        Payment payment = new Payment();
        payment.setBookingID(Long.parseLong(orderInfo));
        payment.setAmount(new BigDecimal(totalPrice).divide(BigDecimal.valueOf(100)));
        payment.setTransactionCode(transactionId);
        payment.setPaymentMethodID(2L);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentStatus(paymentStatus == 1 ? "Approved" : "Failed");

        paymentRepository.save(payment);  // Save to the database

        Map<String, Object> response = new HashMap<>();
        response.put("orderId", orderInfo);
        response.put("totalPrice", totalPrice);
        response.put("paymentTime", paymentTime);
        response.put("transactionId", transactionId);
        response.put("paymentStatus", paymentStatus);

        return ResponseEntity.ok(response);
    }
}

