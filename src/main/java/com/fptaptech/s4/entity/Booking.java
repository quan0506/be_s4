package com.fptaptech.s4.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;

    @Column(name = "adults", nullable = false)
    private int adults;

    @Column(name = "children", nullable = false)
    private int children;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "confirm_booking_code", nullable = false, unique = true, length = 5)
    private String confirmBookingCode;

    @Column(name = "status", nullable = false)
    private String status;

    @PrePersist
    @PreUpdate
    public void updateStatus() {
        LocalDate now = LocalDate.now();
        if (status == null || !status.equals("Đã hủy")) {
            if (now.isAfter(checkOutDate) || now.isBefore(checkInDate)) {
                this.status = "Ngoài thời gian sử dụng phòng";
            } else {
                this.status = "Đang sử dụng";
            }
        }
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }
}

