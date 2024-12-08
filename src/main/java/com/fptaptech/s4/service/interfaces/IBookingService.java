package com.fptaptech.s4.service.interfaces;

import com.fptaptech.s4.entity.BookedRoom;
import com.fptaptech.s4.entity.Booking;
import com.fptaptech.s4.response.BookingResponseDTO;

import java.util.List;

public interface IBookingService {
    BookingResponseDTO createBooking(Booking booking);
    Booking updateBooking(Long bookingId, Booking booking);
    void deleteBooking(Long bookingId);
    Booking getBookingById(Long bookingId);
    List<Booking> getAllBookings();
    void processPayment(Long bookingId, String paymentMethod);
    List<BookedRoom> getAllBookingsByRoomId(Long roomId);
}


