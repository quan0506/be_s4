package com.fptaptech.s4.response;

import com.fptaptech.s4.entity.BookedRoom;
import com.fptaptech.s4.entity.Booking;
import jakarta.persistence.Column;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {

    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private LocalDate checkInDate;

    @Column(nullable = false)
    private LocalDate checkOutDate;

    @Column(nullable = false)
    private String guestName;

    @Column(nullable = false)
    private String guestEmail;

    @Column(nullable = false)
    private int numOfAdults;

    @Column(nullable = false)
    private int numOfChildren;

    @Column(nullable = false)
    private int totalNumOfGuests;

    @Column(nullable = false,length = 4,unique = true)
    @SequenceGenerator(name ="bookingConfirmationCode", allocationSize = 4)
    private String bookingConfirmationCode;

    @Column(nullable = false)
    private RoomResponse room;

    public BookingResponse(Long id, LocalDate checkInDate, LocalDate checkOutDate,
                           String bookingConfirmationCode) {
        this.id = id;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingConfirmationCode = bookingConfirmationCode;
    }

    public BookingResponse(BookedRoom bookedRoom) {
        this.id = bookedRoom.getBookingId();
        this.checkInDate = bookedRoom.getCheckInDate();
        this.checkOutDate = bookedRoom.getCheckOutDate();
        this.guestName = bookedRoom.getGuestFullName();
        this.guestEmail = bookedRoom.getGuestEmail();
        this.numOfAdults = bookedRoom.getNumOfAdults();
        this.numOfChildren = bookedRoom.getNumOfChildren();
        this.totalNumOfGuests = bookedRoom.getTotalNumOfGuest();
        this.bookingConfirmationCode = bookedRoom.getBookingConfirmationCode();
    }

    public BookingResponse(Booking booking) {
        this.id = booking.getBookingId();
        this.checkInDate = booking.getCheckInDate();
        this.checkOutDate=booking.getCheckOutDate();
        this.guestName=booking.getUser().getFirstName();
        this.guestEmail=booking.getUser().getEmail();
        this.numOfAdults = booking.getAdults();
        this.numOfChildren = booking.getChildren();
        this.totalNumOfGuests = booking.getAdults() + booking.getChildren();
        this.bookingConfirmationCode = booking.getConfirmBookingCode();
    }
}

