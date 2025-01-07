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
    private String branchName;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String description;

    // Constructors, Getters and Setters
    public BookingResponseDTO(Long bookingId, Long userId, Long roomId, String branchName
            ,LocalDate checkInDate, LocalDate checkOutDate, int adults, int children,
                              BigDecimal totalPrice, String confirmBookingCode,
                              String status, String email, String phone,String firstName, String lastName, String description) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.roomId = roomId;
        this. branchName= branchName;
        this.checkInDate = checkInDate.toString();
        this.checkOutDate = checkOutDate.toString();
        this.adults = adults;
        this.children = children;
        this.totalPrice = totalPrice;
        this.confirmBookingCode = confirmBookingCode;
        this.status = status;
        this.email = email;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
    }

    public BookingResponseDTO(Long bookingId, Long userId, Long roomId, String branchName, LocalDate checkInDate, LocalDate checkOutDate, int adults, int children, BigDecimal totalPrice, String confirmBookingCode, String status, String description) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.roomId = roomId;
        this.branchName = branchName;
        this.checkInDate = checkInDate.toString();
        this.checkOutDate = checkOutDate.toString();
        this.adults = adults;
        this.children = children;
        this.totalPrice = totalPrice;
        this.confirmBookingCode = confirmBookingCode;
        this.status = status;
        this.description = description;
    }
}