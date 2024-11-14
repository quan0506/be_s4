package com.fptaptech.s4.controller;

import com.fptaptech.s4.repository.PaymentRepository;
import com.fptaptech.s4.service.impl.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequiredArgsConstructor
@Slf4j

@RequestMapping("/api")
public class PaypalController {

    private final PaypalService paypalService;
    @Autowired
    private final PaymentRepository paymentRepository;

    @PostMapping("/payment/create")
    public ResponseEntity<?> createPayment(
            @RequestParam("method") String method,
            @RequestParam("amount") String amount,
            @RequestParam("currency") String currency,
            @RequestParam("description") String description,
            @RequestParam("bookingId") Long bookingId,  // Add bookingId
            @RequestParam("paymentMethodId") Long paymentMethodId // Add paymentMethodId
    ) {
        try {
            String cancelUrl = "http://localhost:8080/api/payment/cancel";
            String successUrl = "http://localhost:8080/api/payment/success";
            Payment payment = paypalService.createPayment(
                    Double.valueOf(amount),
                    currency,
                    method,
                    "sale",
                    description,
                    cancelUrl,
                    successUrl,
                    bookingId,
                    paymentMethodId
            );

            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return ResponseEntity.ok(links.getHref()); // Return approval URL
                }
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred:: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during payment creation");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment creation failed");
    }


    @GetMapping("/payment/success")
    public ResponseEntity<?> paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId,
            @RequestParam("bookingID") Long bookingID,
            @RequestParam("paymentMethodID") Long paymentMethodID
    ) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId, bookingID, paymentMethodID);
            if (payment.getState().equals("approved")) {
                return ResponseEntity.ok("Payment approved");
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred:: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during payment execution");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment execution failed");
    }

    @GetMapping("/payment/cancel")
    public ResponseEntity<?> paymentCancel() {
        return ResponseEntity.ok("Payment cancelled");
    }

    @GetMapping("/payment/error")
    public ResponseEntity<?> paymentError() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during the payment process");
    }
}
