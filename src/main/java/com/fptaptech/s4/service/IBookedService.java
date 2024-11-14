package com.fptaptech.s4.service;


import com.fptaptech.s4.entity.BookedRoom;

import java.util.List;

public interface IBookedService {
    void cancelBooking(Long bookingId);
    List<BookedRoom> getAllBookingsByRoomId(Long roomId);
    String saveBooking(Long roomId, BookedRoom bookingRequest);
    BookedRoom findByBookingConfirmationCode(String confirmationCode);
    List<BookedRoom> getAllBookings();
    List<BookedRoom> getBookingsByUserEmail(String email);

}
