package com.fptaptech.s4.controller;

import com.fptaptech.s4.entity.BookedRoom;
import com.fptaptech.s4.entity.Room;
import com.fptaptech.s4.exception.InvalidBookingRequestException;
import com.fptaptech.s4.exception.ResourceNotFoundException;
import com.fptaptech.s4.response.BookingResponse;
import com.fptaptech.s4.response.RoomResponse;
import com.fptaptech.s4.service.interfaces.IBookedService;
import com.fptaptech.s4.service.interfaces.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/booked")
public class BookedController {
    private final IBookedService bookedService;
    private final IRoomService roomService;

    @PostMapping("/create/{roomId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> createBooking(@PathVariable Long roomId, @RequestBody BookedRoom bookingRequest) {
        String confirmationCode = bookedService.saveBooking(roomId, bookingRequest);
        return ResponseEntity.ok("Booking created with confirmation code: " + confirmationCode);
    }

    @DeleteMapping("/cancel/{bookingId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) {
        bookedService.cancelBooking(bookingId);
        return ResponseEntity.ok("Booking canceled with ID: " + bookingId);
    }


    @GetMapping("/all-bookings")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<BookingResponse>> getAllBookings(){
        List<BookedRoom> bookings = bookedService.getAllBookings();
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for (BookedRoom booking : bookings){
            BookingResponse bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }

    @PostMapping("/room/{roomId}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable Long roomId,
                                         @RequestBody BookedRoom bookingRequest){
        try{
            String confirmationCode = bookedService.saveBooking(roomId, bookingRequest);
            return ResponseEntity.ok(
                    "Room booked successfully, Your booking confirmation code is :"+confirmationCode);

        }catch (InvalidBookingRequestException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode){
        try{
            BookedRoom booking = bookedService.findByBookingConfirmationCode(confirmationCode);
            BookingResponse bookingResponse = getBookingResponse(booking);
            return ResponseEntity.ok(bookingResponse);
        }catch (ResourceNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/user/{email}/bookings")
    public ResponseEntity<List<BookingResponse>> getBookingsByUserEmail(@PathVariable String email) {
        List<BookedRoom> bookings = bookedService.getBookingsByUserEmail(email);
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for (BookedRoom booking : bookings) {
            BookingResponse bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }

    private BookingResponse getBookingResponse(BookedRoom booking) {
        Room theRoom = roomService.getRoomById(booking.getRoom().getId()).get();
        RoomResponse room = new RoomResponse(
                theRoom.getId(),
                theRoom.getRoomType(),
                theRoom.getRoomPrice(),
                theRoom.getPhotos(),
                theRoom.getDescription());
        return new BookingResponse(
                booking.getBookingId(), booking.getCheckInDate(),
                booking.getCheckOutDate(),booking.getGuestFullName(),
                booking.getGuestEmail(), booking.getNumOfAdults(),
                booking.getNumOfChildren(), booking.getTotalNumOfGuest(),
                booking.getBookingConfirmationCode(), room);
    }
}