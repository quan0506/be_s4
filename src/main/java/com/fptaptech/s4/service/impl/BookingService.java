package com.fptaptech.s4.service.impl;

import com.fptaptech.s4.entity.Booking;
import com.fptaptech.s4.exception.ResourceNotFoundException;
import com.fptaptech.s4.entity.BookedRoom;
import com.fptaptech.s4.repository.BookingRepository;
import com.fptaptech.s4.service.IBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {

    private final BookingRepository bookingRepository;

    @Override
    public Booking createBooking(Booking booking) {
        // Tạo mã xác nhận cho booking
        booking.setConfirmBookingCode(generateConfirmCode());
        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateBooking(Long bookingId, Booking booking) {
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        existingBooking.setCheckInDate(booking.getCheckInDate());
        existingBooking.setCheckOutDate(booking.getCheckOutDate());
        existingBooking.setAdults(booking.getAdults());
        existingBooking.setChildren(booking.getChildren());
        existingBooking.setPaymentMethod(booking.getPaymentMethod());
        existingBooking.setStatus(booking.getStatus());
        return bookingRepository.save(existingBooking);
    }

    @Override
    public void deleteBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        bookingRepository.delete(booking);
    }

    @Override
    public Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public void processPayment(Long bookingId, String paymentMethod) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        // Xử lý thanh toán theo phương thức (Online/Offline)
        booking.setPaymentMethod(paymentMethod);
        booking.setStatus("Confirmed");
        bookingRepository.save(booking);
    }

    @Override
    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return List.of();
    }

    private String generateConfirmCode() {
        // Generate random booking confirmation code
        return "BOOK" + System.currentTimeMillis();
    }
}