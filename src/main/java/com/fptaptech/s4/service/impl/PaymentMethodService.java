package com.fptaptech.s4.service.impl;


import com.fptaptech.s4.entity.PaymentMethod;
import com.fptaptech.s4.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    public List<PaymentMethod> getAllPaymentMethods() {
        return paymentMethodRepository.findAll();
    }

    public PaymentMethod getPaymentMethodById(int id) {
        return paymentMethodRepository.findById(id).orElse(null);
    }

    public PaymentMethod createPaymentMethod(PaymentMethod paymentMethod) {
        return paymentMethodRepository.save(paymentMethod);
    }

    public PaymentMethod updatePaymentMethod(int id, PaymentMethod paymentMethodDetails) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id).orElse(null);
        if (paymentMethod != null) {
            paymentMethod.setMethodName(paymentMethodDetails.getMethodName());
            paymentMethod.setDescription(paymentMethodDetails.getDescription());
            return paymentMethodRepository.save(paymentMethod);
        }
        return null;
    }

    public void deletePaymentMethod(int id) {
        paymentMethodRepository.deleteById(id);
    }
}
