package com.fptaptech.s4.controller;

import com.fptaptech.s4.entity.Booking;
import com.fptaptech.s4.entity.Payment;
import com.fptaptech.s4.repository.BookingRepository;
import com.fptaptech.s4.repository.PaymentRepository;
import com.fptaptech.s4.service.impl.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
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
        Map<String, String[]> params = request.getParameterMap();
        params.forEach((key, value) -> {
            System.out.println("Key: " + key + ", Value: " + Arrays.toString(value));
        });

        int paymentStatus = vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        if (orderInfo == null || transactionId == null || paymentTime == null || totalPrice == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "Thiếu thông tin phản hồi từ VNPay",
                    "status", "error"
            ));
        }

        Payment payment = new Payment();
        payment.setBookingID(Long.parseLong(orderInfo));  // This is where the error occurs
        payment.setAmount(new BigDecimal(totalPrice).divide(BigDecimal.valueOf(100))); // VNPay trả về đơn vị VNĐ nhân 100
        payment.setTransactionCode(transactionId);
        payment.setPaymentMethodID(2L);  // Giả định VNPay là 2
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentStatus(paymentStatus == 1 ? "Approved" : "Failed");
        payment.setCurrency("VND");  // Bổ sung nếu cần
        payment.setDescription("Thanh toán qua VNPay");

        paymentRepository.save(payment);

        return ResponseEntity.ok(Map.of(
                "orderId", orderInfo,
                "totalPrice", totalPrice,
                "paymentTime", paymentTime,
                "transactionId", transactionId,
                "paymentStatus", paymentStatus
        ));
    }


}
