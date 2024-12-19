package com.fptaptech.s4.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"branch", "rooms"})
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "room_type", columnDefinition = "TEXT")
    private String roomType;

    @Column(name = "room_price")
    private BigDecimal roomPrice;

    @Column(name = "is_booked")
    private boolean isBooked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    @CollectionTable(name = "room_photos", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "photo_url")
    private List<String> photos;

    @Column(name = "status", columnDefinition = "TEXT")
    private String status;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Booking> bookings;

    public Room() {
        this.bookings = new ArrayList<>();
    }

    public void addBooking(Booking booking) {
        if (bookings == null) {
            bookings = new ArrayList<>();
        }
        bookings.add(booking);
        booking.setRoom(this);
        isBooked = true;
        String bookingCode = RandomStringUtils.randomNumeric(10);
        booking.setConfirmBookingCode(bookingCode);
        updateStatus();
    }

    public void updateStatus() {
        boolean booked = this.bookings.stream()
                .anyMatch(booking -> booking.getCheckInDate().isBefore(LocalDate.now()) &&
                        booking.getCheckOutDate().isAfter(LocalDate.now()));
        this.status = booked ? "Đang sử dụng" : "Còn trống";
    }

    public Long getBranchId() {
        return branch.getId();
    }

    public String getBranchName() {
        return branch.getBranchName();
    }
}

