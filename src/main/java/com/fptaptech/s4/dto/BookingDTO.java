package com.fptaptech.s4.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    private Long bookingId;
    private String roomType;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int adults;
//    private int children;
    private BigDecimal totalPrice;
    private String confirmBookingCode;
    private String status;
    private String description;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
