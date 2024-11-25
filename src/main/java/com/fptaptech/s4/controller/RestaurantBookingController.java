package com.fptaptech.s4.controller;

import com.fptaptech.s4.dto.Response;
import com.fptaptech.s4.dto.RestaurantBookingDTO;
import com.fptaptech.s4.dto.UserDTO;
import com.fptaptech.s4.entity.RestaurantBooking;
import com.fptaptech.s4.service.IRestaurantBookingService;
import com.fptaptech.s4.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurant-bookings")
@RequiredArgsConstructor
public class RestaurantBookingController {

    @Autowired
    private final IRestaurantBookingService restaurantBookingService;
    private final IUserService userService;

    @PostMapping("/book-restaurant/{branchId}/{restaurantId}/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Response> saveRestaurantBooking(
            @PathVariable Long branchId,
            @PathVariable Long restaurantId,
            @PathVariable Long userId,
            @RequestBody RestaurantBookingDTO restaurantBookingRequest,
            Authentication authentication) {

        // Check if the user is an admin or if the user is booking for themselves
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        String currentUsername = authentication.getName();

        UserDTO currentUser = userService.findByEmail(currentUsername);
        if (!isAdmin && !currentUser.getId().equals(userId)) {
            Response response = new Response();
            response.setStatusCode(403);
            response.setMessage("You can only book for yourself.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        Response response = restaurantBookingService.saveRestaurantBooking(branchId, restaurantId, userId, restaurantBookingRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> getAllRestaurantBookings(@RequestParam(value = "branchId") Long branchId) {
        Response response = restaurantBookingService.getAllRestaurantBookings(branchId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/cancel/{branchId}/{bookingId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Response> cancelRestaurantBooking(
            @PathVariable Long branchId,
            @PathVariable Long bookingId) {
        Response response = restaurantBookingService.cancelRestaurantBooking(branchId, bookingId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/user-bookings/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Response> getUserRestaurantBookings(
            @PathVariable Long userId,
            @RequestParam(value = "branchId") Long branchId) {
        Response response = restaurantBookingService.getUserRestaurantBookings(userId, branchId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/booking-by-id/{bookingId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> getRestaurantBookingById(
            @PathVariable Long bookingId,
            @RequestParam(value = "branchId") Long branchId) {
        Response response = restaurantBookingService.getRestaurantBookingById(branchId, bookingId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
