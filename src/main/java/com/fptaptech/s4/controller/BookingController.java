package com.fptaptech.s4.controller;

import com.fptaptech.s4.Mapper.BookingMapper;
import com.fptaptech.s4.dto.BookingDTO;
import com.fptaptech.s4.dto.Response;
import com.fptaptech.s4.entity.Booking;
import com.fptaptech.s4.response.BookingResponse;
import com.fptaptech.s4.response.BookingResponseDTO;
import com.fptaptech.s4.service.impl.BookingService;
import com.fptaptech.s4.service.impl.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final EmailService emailService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<BookingResponseDTO> createBooking(@RequestBody Booking booking) {
        BookingResponseDTO newBooking = bookingService.createBooking(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBooking);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking booking) {
        Booking updatedBooking = bookingService.updateBooking(id, booking);
        return ResponseEntity.ok(updatedBooking);
    }

 /*   @PutMapping("/update/dates/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<Booking> updateBookingDates(@PathVariable Long id, @RequestBody Booking booking) {
        Booking updatedBooking = bookingService.updateBooking(id, booking, true);
        return ResponseEntity.ok(updatedBooking);
    }*/

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok("Booking deleted successfully.");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        BookingDTO bookingDTO = BookingMapper.toDTO(booking);
        return ResponseEntity.ok(bookingDTO);
    }

    @GetMapping("user/{userId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByUserId(@PathVariable Long userId, Authentication authentication) {
        List<BookingResponseDTO> bookings = bookingService.getBookingsByUserId(userId, authentication);
        return ResponseEntity.ok(bookings);
    }

    /*@PostMapping("/cancel/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<String> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok("Booking cancelled successfully.");
    }*/

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> getAllBookings() {
        Response response = bookingService.getAllBookings();
        response.setMessage("Bookings listed successfully.");
        response.setStatusCode(200);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/payment/{id}")
    public ResponseEntity<String> processPayment(@PathVariable Long id, @RequestParam String paymentMethod) {
        bookingService.processPayment(id, paymentMethod);
        return ResponseEntity.ok("Payment processed successfully.");
    }

    @PostMapping("/cancel/request/{bookingId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<Void> requestCancellation(@PathVariable Long bookingId, @RequestParam String userEmail) {
        bookingService.requestCancellation(bookingId, userEmail);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}