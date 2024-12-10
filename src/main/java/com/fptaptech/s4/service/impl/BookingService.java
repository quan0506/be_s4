package com.fptaptech.s4.service.impl;

import com.fptaptech.s4.entity.Booking;
import com.fptaptech.s4.entity.Room;
import com.fptaptech.s4.exception.ResourceNotFoundException;
import com.fptaptech.s4.entity.BookedRoom;
import com.fptaptech.s4.exception.RoomNotAvailableException;
import com.fptaptech.s4.repository.BookingRepository;
import com.fptaptech.s4.repository.UserRepository;
import com.fptaptech.s4.response.BookingResponseDTO;
import com.fptaptech.s4.service.interfaces.IBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {

    private final BookingRepository bookingRepository;
    private final RoomService roomService;
    private final UserRepository userRepository;

    @Override
    public BookingResponseDTO createBooking(Booking booking) {
        // Tìm kiếm Room với id
        Room room = roomService.getRoomById(booking.getRoom().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        // Kiểm tra tình trạng phòng trước khi tạo booking
        if (!roomService.isRoomAvailable(room.getId(), booking.getCheckInDate(), booking.getCheckOutDate())) {
            throw new RoomNotAvailableException("Phòng đã được đặt hoặc đang trong thời gian sử dụng");
        }

        // Đảm bảo Room có Branch không bị null
        if (room.getBranch() == null) {
            throw new ResourceNotFoundException("Branch not found for the given room");
        }

        booking.setRoom(room);
        booking.setConfirmBookingCode(generateConfirmCode());
        var userId = booking.getUser().getId();
        var newUser = userRepository.findById(userId);
        booking.getUser().setFirstName(newUser.get().getFirstName());
        booking.getUser().setLastName(newUser.get().getLastName());
        booking.getUser().setEmail(newUser.get().getEmail());
        booking.getUser().setPhone(newUser.get().getPhone());

        BigDecimal totalPrice = calculateTotalPrice(booking);
        booking.setTotalPrice(totalPrice);

        Booking savedBooking = bookingRepository.save(booking);

        return new BookingResponseDTO(
                savedBooking.getBookingId(),
                savedBooking.getUser().getId(),
                savedBooking.getRoom().getId(),
                savedBooking.getCheckInDate(),
                savedBooking.getCheckOutDate(),
                savedBooking.getAdults(),
                savedBooking.getChildren(),
                savedBooking.getTotalPrice(),
                savedBooking.getPaymentMethod(),
                savedBooking.getConfirmBookingCode(),
                savedBooking.getStatus()
        );
    }



    private BigDecimal calculateTotalPrice(Booking booking) {
        Room room = booking.getRoom();
        BigDecimal basePrice = room.getRoomPrice();
        int adults = booking.getAdults();
        int children = booking.getChildren();

        if (adults > 2) {
            basePrice = basePrice.add(basePrice.multiply(BigDecimal.valueOf(0.30)));
        }

        if (children > 0) {
            basePrice = basePrice.add(basePrice.multiply(BigDecimal.valueOf(0.15)));
        }

        return basePrice;
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
