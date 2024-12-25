package com.fptaptech.s4.controller;

import com.fptaptech.s4.dto.Response;
import com.fptaptech.s4.dto.ServicePaymentDTO;
import com.fptaptech.s4.entity.*;
import com.fptaptech.s4.repository.RestaurantBookingRepository;
import com.fptaptech.s4.repository.ShuttleBookingRepository;
import com.fptaptech.s4.repository.SpaBookingRepository;
import com.fptaptech.s4.repository.VNPayForServiceRepository;
import com.fptaptech.s4.service.impl.VNPayForService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service-payments")
public class VNPayForServiceController {

    private final VNPayForService vnPayForService;
    private final VNPayForServiceRepository vnPayForServiceRepository;
    private final SpaBookingRepository spaBookingRepository;
    private final RestaurantBookingRepository restaurantBookingRepository;
    private final ShuttleBookingRepository shuttleBookingRepository;

    @PostMapping("/submitOrder")
    public ResponseEntity<Map<String, String>> submitOrder(
            @RequestParam(value = "restaurantBookingId", required = false) Long restaurantBookingId,
            @RequestParam(value = "shuttleBookingId", required = false) Long shuttleBookingId,
            HttpServletRequest request) {
        try {
            if ((restaurantBookingId == null && shuttleBookingId == null) || (restaurantBookingId != null && shuttleBookingId != null)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Please provide only one booking ID (either restaurant or shuttle)."));
            }

            BigDecimal totalPrice;
            String orderInfo;

            if (restaurantBookingId != null) {
                RestaurantBooking restaurantBooking = restaurantBookingRepository.findById(restaurantBookingId)
                        .orElseThrow(() -> new RuntimeException("Restaurant booking not found"));
                totalPrice = restaurantBooking.getTotalPrice();
                orderInfo = "restaurant-" + restaurantBookingId;
            } else {
                ShuttleBooking shuttleBooking = shuttleBookingRepository.findById(shuttleBookingId)
                        .orElseThrow(() -> new RuntimeException("Shuttle booking not found"));
                totalPrice = shuttleBooking.getTotalPrice();
                orderInfo = "shuttle-" + shuttleBookingId;
            }

            // Convert the total price to the format expected by VNPay
            int orderTotal = totalPrice.multiply(BigDecimal.valueOf(100)).intValue();

            // Generate the VNPay payment URL
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String returnUrl = baseUrl + "/api/service-payments/vnpay-payment"; // Redirect here after payment
            String vnpayUrl = vnPayForService.createOrder(orderTotal, orderInfo, returnUrl);

            // Return the VNPay URL to the client
            Map<String, String> response = new HashMap<>();
            response.put("vnpayUrl", vnpayUrl);
            response.put("message", "Proceed to VNPay for payment.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Return error details in case of failure
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error submitting order: " + e.getMessage()));
        }
    }


    // Handle VNPay payment callback
    @GetMapping("/vnpay-payment")
    public ResponseEntity<Map<String, Object>> getPaymentStatus(HttpServletRequest request) {
        try {
            int paymentStatus = vnPayForService.orderReturn(request);

            // Extract necessary parameters from the request
            String orderInfo = request.getParameter("vnp_OrderInfo");
            String paymentTime = request.getParameter("vnp_PayDate");
            String transactionId = request.getParameter("vnp_TransactionNo");
            String totalPrice = request.getParameter("vnp_Amount");

            // Debugging: Log all parameters
            request.getParameterMap().forEach((key, value) -> {
                System.out.println("Key: " + key + ", Value: " + Arrays.toString(value));
            });

            // Extract details from orderInfo
            String[] orderDetails = orderInfo.split("-");
            if (orderDetails.length != 2) {
                throw new IllegalArgumentException("Invalid order info format.");
            }

            String bookingType = orderDetails[0];
            Long bookingId = Long.parseLong(orderDetails[1]);

            // Initialize user variable
            User user = null;

            // Retrieve booking details based on booking type and set the user
            ServicePayment servicePayment = new ServicePayment();
            if ("restaurant".equals(bookingType)) {
                RestaurantBooking restaurantBooking = restaurantBookingRepository.findById(bookingId)
                        .orElseThrow(() -> new RuntimeException("Restaurant booking not found"));
                servicePayment.setRestaurantBooking(restaurantBooking);
                user = restaurantBooking.getUser();
            } else if ("shuttle".equals(bookingType)) {
                ShuttleBooking shuttleBooking = shuttleBookingRepository.findById(bookingId)
                        .orElseThrow(() -> new RuntimeException("Shuttle booking not found"));
                servicePayment.setShuttleBooking(shuttleBooking);
                user = shuttleBooking.getUser();
            } else {
                throw new IllegalArgumentException("Invalid booking type.");
            }

            // Create and save the payment record
            servicePayment.setAmount(new BigDecimal(totalPrice).divide(BigDecimal.valueOf(100)));
            servicePayment.setTransactionCode(transactionId);
            servicePayment.setPaymentMethodID(2L);
            servicePayment.setPaymentDate(LocalDateTime.now());
            servicePayment.setPaymentStatus(paymentStatus == 1 ? "Approved" : "Failed");
            servicePayment.setCurrency("VND");
            servicePayment.setDescription("Payment through VNPay");
            servicePayment.setUser(user);
            vnPayForServiceRepository.save(servicePayment);

            // Build response
            Map<String, Object> response = new HashMap<>();
            response.put("orderId", bookingId);
            response.put("userId", user != null ? user.getId() : null);
            response.put("totalPrice", totalPrice);
            response.put("paymentTime", paymentTime);
            response.put("transactionId", transactionId);
            response.put("paymentStatus", paymentStatus);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error processing payment: " + e.getMessage()));
        }
    }


    @GetMapping
    public ResponseEntity<Response> getAllServicePayments() {
        return ResponseEntity.ok(vnPayForService.getAllServicePayments());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateServicePayment(@PathVariable Long id, @RequestBody ServicePaymentDTO paymentDetails) {
        return ResponseEntity.ok(vnPayForService.updateServicePayment(id, paymentDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteServicePayment(@PathVariable Long id) {
        return ResponseEntity.ok(vnPayForService.deleteServicePayment(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getServicePaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(vnPayForService.getServicePaymentById(id));
    }

    @GetMapping("/shuttle-payments")
    public ResponseEntity<Response> getAllShuttlePayments() {
        return ResponseEntity.ok(vnPayForService.getAllShuttlePayments());
    }

    @GetMapping("/restaurant-payments")
    public ResponseEntity<Response> getAllRestaurantPayments() {
        return ResponseEntity.ok(vnPayForService.getAllRestaurantPayments());
    }

//    @GetMapping("/spa-payments")
//    public ResponseEntity<Response> getAllSpaPayments() {
//        return ResponseEntity.ok(vnPayForService.getAllSpaPayments());
//    }


    @GetMapping("/monthly-total-price")
    public ResponseEntity<Map<String, BigDecimal>> getMonthlyTotalPrice(@RequestParam int year, @RequestParam String bookingType) {
        Map<String, BigDecimal> monthlyTotalPrice = vnPayForService.calculateTotalPriceForEachMonth(year, bookingType);
        return ResponseEntity.ok(monthlyTotalPrice);
    }

    @GetMapping("/yearly-total-price")
    public ResponseEntity<Map<Integer, BigDecimal>> getYearlyTotalPrice(@RequestParam String bookingType) {
        Map<Integer, BigDecimal> yearlyTotalPrice = vnPayForService.calculateTotalPriceForEachYear(bookingType);
        return ResponseEntity.ok(yearlyTotalPrice);
    }
}
