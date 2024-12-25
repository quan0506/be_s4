package com.fptaptech.s4.controller;

import com.fptaptech.s4.dto.Response;
import com.fptaptech.s4.dto.RoomPaymentDTO;
import com.fptaptech.s4.entity.Booking;
import com.fptaptech.s4.entity.Payment;
import com.fptaptech.s4.repository.BookingRepository;
import com.fptaptech.s4.repository.PaymentRepository;
import com.fptaptech.s4.service.impl.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
@RequiredArgsConstructor
@RequestMapping("/api")
public class VNPayController {

    private final VNPayService vnPayService;
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;


    // THANH TOAN PHONG
    @PostMapping("/submitOrder")
    public ResponseEntity<Map<String, String>> submitOrder(
            @RequestParam("bookingId") Long bookingId,
            @RequestParam("modeOfPayment") String modeOfPayment,
            HttpServletRequest request) {

        // Retrieve booking details
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Calculate total based on mode of payment
        BigDecimal totalAmount = booking.getTotalPrice();
        if ("Deposit".equalsIgnoreCase(modeOfPayment)) {
            totalAmount = totalAmount.multiply(BigDecimal.valueOf(0.50)); // 50% deposit
        } else {
            totalAmount = totalAmount.multiply(BigDecimal.valueOf(0.95)); // 95% full payment
        }

        // Automatically determine chooseMethod
        String chooseMethod = "Deposit".equalsIgnoreCase(modeOfPayment) ? "HalfPaid" : "Paid";

        // Create order info string
        String orderInfo = bookingId + "-" + booking.getUser().getId() + "-" + modeOfPayment + "-" + chooseMethod;

        // Generate VNPAY URL
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(totalAmount.intValue(), orderInfo, baseUrl);

        Map<String, String> response = new HashMap<>();
        response.put("vnpayUrl", vnpayUrl);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vnpay-payment")
    public ResponseEntity<?> getPaymentStatus(HttpServletRequest request) {
        int paymentStatus = vnPayService.orderReturn(request);

        // Retrieve necessary parameters from the request
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        // Print all parameters for debugging
        Map<String, String[]> params = request.getParameterMap();
        params.forEach((key, value) -> {
            System.out.println("Key: " + key + ", Value: " + Arrays.toString(value));
        });

        // Extract details from orderInfo
        String[] orderDetails = orderInfo.split("-");
        Long bookingId = Long.parseLong(orderDetails[0]);
        Long userId = Long.parseLong(orderDetails[1]);
        String modeOfPayment = orderDetails[2];
        String chooseMethod = orderDetails[3];

        // Retrieve booking details
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Create and save the payment record
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setUser(booking.getUser());
        payment.setAmount(new BigDecimal(totalPrice).divide(BigDecimal.valueOf(100)));
        payment.setTransactionCode(transactionId);
        payment.setPaymentMethodID(2L);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentStatus("Pending");
        payment.setModeOfPayment(modeOfPayment);
        payment.setCurrency("VND");
        payment.setChooseMethod(chooseMethod);
        payment.setDescription("Thanh toán qua VNPay");

        if (paymentStatus == 1) {
            payment.setPaymentStatus("Approved");
        }

        paymentRepository.save(payment);

        // Add transaction details to the response
        Map<String, Object> response = new HashMap<>();
        response.put("totalPrice", totalPrice);
        response.put("paymentTime", paymentTime);
        response.put("transactionId", transactionId);
        response.put("paymentStatus", paymentStatus);
        response.put("bookingId", bookingId);


        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, "http://localhost:5173/thank/" + bookingId)
                .body(response);
    }

    @GetMapping("/payments")
    public ResponseEntity<Response> getAllPayments() {
        return ResponseEntity.ok(vnPayService.getAllPayments());
    }

    @GetMapping("/payments/{id}")
    public ResponseEntity<Response> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(vnPayService.getPaymentById(id));
    }

    @PutMapping("/payments/{id}")
    public ResponseEntity<Response> updatePayment(@PathVariable Long id, @RequestBody RoomPaymentDTO paymentDetails) {
        return ResponseEntity.ok(vnPayService.updatePayment(id, paymentDetails));
    }
    @DeleteMapping("/payments/{id}")
    public ResponseEntity<Response> deletePayment(@PathVariable Long id){
        return ResponseEntity.ok(vnPayService.deletePayment(id));
    }

    @GetMapping("/monthly-total-price")
    public ResponseEntity<Map<String, BigDecimal>> getMonthlyTotalPrice(@RequestParam int year) {
        Map<String, BigDecimal> monthlyTotalPrice = vnPayService.calculateTotalPriceForEachMonth(year);
        return ResponseEntity.ok(monthlyTotalPrice);
    }


    @GetMapping("/yearly-total-price")
    public ResponseEntity<Map<Integer, BigDecimal>> getYearlyTotalPrice() {
        Map<Integer, BigDecimal> yearlyTotalPrice = vnPayService.calculateTotalPriceForEachYear();
        return ResponseEntity.ok(yearlyTotalPrice);
    }

}

