package com.fptaptech.s4.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Data
@Entity
@Table(name = "shuttle_bookings")
public class ShuttleBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Check-in date is required")
    private LocalDate shuttleCheckInDate;

    @Future(message = "Check-out date must be in the future")
    private LocalDate shuttleCheckOutDate;

    private String bookingConfirmationCode;

    private BigDecimal totalPrice;

    private String phone;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shuttle_id")
    private Shuttle shuttle;

    @Override
    public String toString() {
        return "ShuttleBooking{" +
                "id=" + id +
                ", shuttleCheckInDate=" + shuttleCheckInDate +
                ", shuttleCheckOutDate=" + shuttleCheckOutDate +
                ", bookingConfirmationCode='" + bookingConfirmationCode + '\'' +
                ", totalPrice=" + totalPrice +
                ", user=" + user +
                ", shuttle=" + shuttle +
                '}';
    }
}
