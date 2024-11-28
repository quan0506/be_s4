package com.fptaptech.s4.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private  Long id;

    @Column(name = "room_type")
    private String roomType;

    @Column(name = "room_price")
    private BigDecimal roomPrice;

    @Column(name = "is_booked")
    private boolean isBooked = false;

    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    private Branch branch;

    @Column(name = "description")
    private String description;

    @Column(name = "photo", length = 15000)
    private String photo;

    @OneToMany(mappedBy="room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BookedRoom> bookings;

    public Room() {
        this.bookings = new ArrayList<>();
    }

    public void addBooking(BookedRoom booking){
        if (bookings == null){
            bookings = new ArrayList<>();
        }
        bookings.add(booking);
        booking.setRoom(this);
        isBooked = true;
        String bookingCode = RandomStringUtils.randomNumeric(10);
        booking.setBookingConfirmationCode(bookingCode);
    }

    public Long getBranchId() {
        return branch.getId();
    }

}
