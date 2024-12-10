package com.fptaptech.s4.controller;

import com.fptaptech.s4.Mapper.BookingMapper;
import com.fptaptech.s4.dto.BookingDTO;
import com.fptaptech.s4.entity.Booking;
import com.fptaptech.s4.response.BookingResponseDTO;
import com.fptaptech.s4.service.interfaces.IBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final IBookingService bookingService;

    @PostMapping("/create")
    public ResponseEntity<BookingResponseDTO> createBooking(@RequestBody Booking booking) {
        BookingResponseDTO newBooking = bookingService.createBooking(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBooking);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking booking) {
        Booking updatedBooking = bookingService.updateBooking(id, booking);
        return ResponseEntity.ok(updatedBooking);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok("Booking deleted successfully.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        BookingDTO bookingDTO = BookingMapper.toDTO(booking);
        return ResponseEntity.ok(bookingDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        // Chuyển đổi từ List<Booking> sang List<BookingDTO>
        List<BookingDTO> bookingDTOs = bookings.stream()
                .map(BookingMapper::toDTO)
                .toList();
        return ResponseEntity.ok(bookingDTOs);
    }

    @PostMapping("/payment/{id}")
    public ResponseEntity<String> processPayment(@PathVariable Long id, @RequestParam String paymentMethod) {
        bookingService.processPayment(id, paymentMethod);
        return ResponseEntity.ok("Payment processed successfully.");
    }
}
