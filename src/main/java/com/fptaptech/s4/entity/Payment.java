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
    private String chooseMethod;

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

    @Column(name = "ModeOfPayment", length = 50)
    private String modeOfPayment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Payment() {
    }


    public Payment(Long paymentID, String chooseMethod, Long paymentMethodID, LocalDateTime paymentDate, BigDecimal amount, String paymentStatus, String transactionCode, String currency, String description, String modeOfPayment, Booking booking, User user) {
        this.paymentID = paymentID;
        this.chooseMethod = chooseMethod;
        this.paymentMethodID = paymentMethodID;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.transactionCode = transactionCode;
        this.currency = currency;
        this.description = description;
        this.modeOfPayment = modeOfPayment;
        this.booking = booking;
        this.user = user;
    }

    public Long getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(Long paymentID) {
        this.paymentID = paymentID;
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

    public String getChooseMethod() {
        return chooseMethod;
    }

    public void setChooseMethod(String chooseMethod) {
        this.chooseMethod = chooseMethod;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
