package com.fptaptech.s4.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class ServicePayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentID;

    private String chooseMethod;
    private Long paymentMethodID;
    private LocalDateTime paymentDate;
    private BigDecimal amount;
    private String paymentStatus;
    private String transactionCode;
    private String currency;
    private String description;

    @ManyToOne
    @JoinColumn(name = "spa_booking_id")
    private SpaBooking spaBooking;

    @ManyToOne
    @JoinColumn(name = "restaurant_booking_id")
    private RestaurantBooking restaurantBooking;

    @ManyToOne
    @JoinColumn(name = "shuttle_booking_id")
    private ShuttleBooking shuttleBooking;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public ServicePayment(Long paymentID, String chooseMethod, Long paymentMethodID, LocalDateTime paymentDate, BigDecimal amount, String paymentStatus, String transactionCode, String currency, String description, SpaBooking spaBooking, RestaurantBooking restaurantBooking, ShuttleBooking shuttleBooking, User user) {
        this.paymentID = paymentID;
        this.chooseMethod = chooseMethod;
        this.paymentMethodID = paymentMethodID;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.transactionCode = transactionCode;
        this.currency = currency;
        this.description = description;
        this.spaBooking = spaBooking;
        this.restaurantBooking = restaurantBooking;
        this.shuttleBooking = shuttleBooking;
        this.user = user;
    }

    public ServicePayment() {

    }

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

    public SpaBooking getSpaBooking() {
        return spaBooking;
    }

    public void setSpaBooking(SpaBooking spaBooking) {
        this.spaBooking = spaBooking;
    }

    public RestaurantBooking getRestaurantBooking() {
        return restaurantBooking;
    }

    public void setRestaurantBooking(RestaurantBooking restaurantBooking) {
        this.restaurantBooking = restaurantBooking;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ShuttleBooking getShuttleBooking() {
        return shuttleBooking;
    }

    public void setShuttleBooking(ShuttleBooking shuttleBooking) {
        this.shuttleBooking = shuttleBooking;
    }
}
