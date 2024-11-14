package com.fptaptech.s4.service.impl;

import com.fptaptech.s4.entity.BookedRoom;
import com.fptaptech.s4.entity.Room;
import com.fptaptech.s4.exception.InvalidBookingRequestException;
import com.fptaptech.s4.exception.ResourceNotFoundException;
import com.fptaptech.s4.repository.BookedRepository;
import com.fptaptech.s4.repository.RoomRepository;
import com.fptaptech.s4.service.IBookedService;
import com.fptaptech.s4.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookedService implements IBookedService {
    private final BookedRepository bookedRepository;
    private final IRoomService roomService;
    private final RoomRepository roomRepository;
    @Override
    public List<BookedRoom> getAllBookings() {
        return bookedRepository.findAll();
    }
    @Override
    public List<BookedRoom> getBookingsByUserEmail(String email) {
        return bookedRepository.findByGuestEmail(email);
    }
    @Override
    public void cancelBooking(Long bookingId) {
        Optional<BookedRoom> booking = bookedRepository.findById(bookingId);
        if (booking.isPresent()) {
            bookedRepository.deleteById(bookingId);
        } else {
            throw new ResourceNotFoundException("Booking not found with ID: " + bookingId);
        }
    }

    @Override
    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);
        if (room.isPresent()) {
            return bookedRepository.findByRoomId(roomId);
        } else {
            throw new ResourceNotFoundException("Room not found with ID: " + roomId);
        }
    }
    @Override
    public String saveBooking(Long roomId, BookedRoom bookingRequest) {
        if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
            throw new InvalidBookingRequestException("Check-in date must come before check-out date");
        }
        Room room = roomService.getRoomById(roomId).get();
        List<BookedRoom> existingBookings = room.getBookings();
        boolean roomIsAvailable = roomIsAvailable(bookingRequest,existingBookings);
        if (roomIsAvailable){
            room.addBooking(bookingRequest);
            bookedRepository.save(bookingRequest);
        }else{
            throw  new InvalidBookingRequestException("Sorry, This room is not available for the selected dates;");
        }
        return bookingRequest.getBookingConfirmationCode();
    }
    @Override
    public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
        return bookedRepository.findByBookingConfirmationCode(confirmationCode)
                .orElseThrow(() -> new ResourceNotFoundException("No booking found with booking code :"+confirmationCode));
    }
    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))
                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }
}
