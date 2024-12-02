package com.fptaptech.s4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    private Long bookingId;
    private String userName;        // Tên người dùng (lấy từ User)
    private String roomName;        // Tên phòng (lấy từ Room)
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int adults;
    private int children;
    private BigDecimal totalPrice;
    private String paymentMethod;
    private String confirmBookingCode;
    private String status;
}
