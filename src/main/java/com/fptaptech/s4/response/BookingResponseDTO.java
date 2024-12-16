package com.fptaptech.s4.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDTO {
    private Long bookingId;
    private Long userId;
    private Long roomId;
    private String checkInDate;
    private String checkOutDate;
    private int adults;
    private int children;
    private BigDecimal totalPrice;
    private String confirmBookingCode;
    private String status;

    // Constructors, Getters and Setters
    public BookingResponseDTO(Long bookingId, Long userId, Long roomId, LocalDate checkInDate, LocalDate checkOutDate, int adults, int children, BigDecimal totalPrice, String confirmBookingCode, String status) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.roomId = roomId;
        this.checkInDate = checkInDate.toString();
        this.checkOutDate = checkOutDate.toString();
        this.adults = adults;
        this.children = children;
        this.totalPrice = totalPrice;
        this.confirmBookingCode = confirmBookingCode;
        this.status = status;
    }
}