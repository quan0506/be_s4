package com.fptaptech.s4.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Table(name = "Payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentID;  // Use camelCase

    @Column(nullable = false)
    private Long bookingID;

    @Column(nullable = false)
    private Long paymentMethodID;  // method : "PayPal", "VNPay", "Offline"

    @Column(nullable = false)
    private LocalDateTime paymentDate = LocalDateTime.now();

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(length = 50)
    private String paymentStatus = "Pending";  // Pending, Approved, Failed, etc.

    @Column(length = 100)
    private String transactionCode;  // This will be the PayPal paymentId

    @Column(name = "Currency", length = 10, nullable = false)
    private String currency;

    @Column(name = "Description", length = 255)
    private String description;

    public Payment() {
    }

    public Payment(Long bookingID, Long paymentMethodID, BigDecimal amount, String currency, String description, String transactionCode, String paymentStatus) {
        this.bookingID = bookingID;
        this.paymentMethodID = paymentMethodID;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.transactionCode = transactionCode;
        this.paymentStatus = paymentStatus;
        this.paymentDate = LocalDateTime.now();
    }

    public Long getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(Long paymentID) {
        this.paymentID = paymentID;
    }

    public Long getBookingID() {
        return bookingID;
    }

    public void setBookingID(Long bookingID) {
        this.bookingID = bookingID;
    }

    public Long getPaymentMethodID() {
        return paymentMethodID;
    }

    public void setPaymentMethodID(Long paymentMethodID) {
        this.paymentMethodID = paymentMethodID;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
