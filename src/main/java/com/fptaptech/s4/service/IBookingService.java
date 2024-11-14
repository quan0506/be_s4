package com.fptaptech.s4.service;

import com.fptaptech.s4.entity.BookedRoom;
import com.fptaptech.s4.entity.Booking;

import java.util.List;

public interface IBookingService {
    Booking createBooking(Booking booking);
    Booking updateBooking(Long bookingId, Booking booking);
    void deleteBooking(Long bookingId);
    Booking getBookingById(Long bookingId);
    List<Booking> getAllBookings();
    void processPayment(Long bookingId, String paymentMethod);
    List<BookedRoom> getAllBookingsByRoomId(Long roomId);
}


