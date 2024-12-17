//package com.fptaptech.s4.service.impl;
//
//import com.fptaptech.s4.repository.PaymentRepository;
//import com.paypal.api.payments.*;
//import com.paypal.base.rest.APIContext;
//import com.paypal.base.rest.PayPalRESTException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class PaypalService {
//
//    private final APIContext apiContext;
//    @Autowired
//    private final PaymentRepository paymentRepository;
//
//    public List<com.fptaptech.s4.entity.Payment> getAllPayments() {
//        return paymentRepository.findAll();
//    }
//
//    public Payment createPayment(
//            Double total,
//            String currency,
//            String method,
//            String intent,
//            String description,
//            String cancelUrl,
//            String successUrl,
//            Long bookingId,
//            Long paymentMethodId
//    ) throws PayPalRESTException {
//
//        Amount amount = new Amount();
//        amount.setCurrency(currency);
//        amount.setTotal(String.format(Locale.forLanguageTag(currency), "%.2f", total));
//
//        Transaction transaction = new Transaction();
//        transaction.setDescription(description);
//        transaction.setAmount(amount);
//
//        List<Transaction> transactions = new ArrayList<>();
//        transactions.add(transaction);
//
//        Payer payer = new Payer();
//        payer.setPaymentMethod(method);
//
//        Payment payment = new Payment();
//        payment.setIntent(intent);
//        payment.setPayer(payer);
//        payment.setTransactions(transactions);
//
//        RedirectUrls redirectUrls = new RedirectUrls();
//        redirectUrls.setCancelUrl(cancelUrl);
//        redirectUrls.setReturnUrl(successUrl);
//
//        payment.setRedirectUrls(redirectUrls);
//
//        Payment createdPayment = payment.create(apiContext);
//
//        com.fptaptech.s4.entity.Payment savedPayment = new com.fptaptech.s4.entity.Payment(
//                bookingId,
//                paymentMethodId,
//                BigDecimal.valueOf(total),
//                currency,
//                description,
//                createdPayment.getId(),
//                "Pending"
//        );
//        paymentRepository.save(savedPayment);
//
//        return createdPayment;
//    }
//
//    public Payment executePayment(String paymentId, String payerId, Long bookingID, Long paymentMethodID) throws PayPalRESTException {
//        Payment payment = new Payment();
//        payment.setId(paymentId);
//
//        PaymentExecution paymentExecution = new PaymentExecution();
//        paymentExecution.setPayerId(payerId);
//
//        Payment executedPayment = payment.execute(apiContext, paymentExecution);
//
//
//        Optional<com.fptaptech.s4.entity.Payment> paymentEntity = paymentRepository.findByTransactionCode(paymentId);
//        if (paymentEntity.isPresent()) {
//            com.fptaptech.s4.entity.Payment p = paymentEntity.get();
//            p.setPaymentStatus("Approved");
//            paymentRepository.save(p);
//        }
//
//        return executedPayment;
//    }
//}
