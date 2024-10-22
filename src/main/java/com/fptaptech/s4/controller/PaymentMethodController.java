package com.fptaptech.s4.controller;


import com.fptaptech.s4.entity.PaymentMethod;
import com.fptaptech.s4.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-methods")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping
    public List<PaymentMethod> getAllPaymentMethods() {
        return paymentMethodService.getAllPaymentMethods();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethod> getPaymentMethodById(@PathVariable int id) {
        PaymentMethod paymentMethod = paymentMethodService.getPaymentMethodById(id);
        if (paymentMethod != null) {
            return ResponseEntity.ok(paymentMethod);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public PaymentMethod createPaymentMethod(@RequestBody PaymentMethod paymentMethod) {
        return paymentMethodService.createPaymentMethod(paymentMethod);
    }


    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethod> updatePaymentMethod(@PathVariable int id, @RequestBody PaymentMethod paymentMethodDetails) {
        PaymentMethod updatedPaymentMethod = paymentMethodService.updatePaymentMethod(id, paymentMethodDetails);
        if (updatedPaymentMethod != null) {
            return ResponseEntity.ok(updatedPaymentMethod);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable int id) {
        paymentMethodService.deletePaymentMethod(id);
        return ResponseEntity.noContent().build();
    }
}

