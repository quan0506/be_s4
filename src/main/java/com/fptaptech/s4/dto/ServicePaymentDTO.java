package com.fptaptech.s4.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ServicePaymentDTO {
    private Long paymentID;
    private String chooseMethod;
    private Long paymentMethodID;
    private LocalDateTime paymentDate;
    private BigDecimal amount;
    private String paymentStatus;
    private String transactionCode;
    private String currency;
    private String description;
    private Long spaBookingId;
    private Long restaurantBookingId;
    private Long userId;

    // Getters and Setters
    public Long getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(Long paymentID) {
        this.paymentID = paymentID;
    }

    public String getChooseMethod() {
        return chooseMethod;
    }

    public void setChooseMethod(String chooseMethod) {
        this.chooseMethod = chooseMethod;
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

    public Long getSpaBookingId() {
        return spaBookingId;
    }

    public void setSpaBookingId(Long spaBookingId) {
        this.spaBookingId = spaBookingId;
    }

    public Long getRestaurantBookingId() {
        return restaurantBookingId;
    }

    public void setRestaurantBookingId(Long restaurantBookingId) {
        this.restaurantBookingId = restaurantBookingId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
