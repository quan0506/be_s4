package com.fptaptech.s4.controller;

import com.fptaptech.s4.response.Response;
//import com.fptaptech.s4.dto.Response;
import com.fptaptech.s4.dto.SpaBookingDTO;
import com.fptaptech.s4.dto.UserDTO;
import com.fptaptech.s4.entity.User;
import com.fptaptech.s4.security.user.HotelUserDetails;
import com.fptaptech.s4.service.interfaces.ISpaBookingService;
import com.fptaptech.s4.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spa-bookings")
@RequiredArgsConstructor
public class SpaBookingController {

    private final ISpaBookingService spaBookingService;
    private final IUserService userService;

    @PostMapping("/book-spa/{branchId}/{spaId}/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Response> saveSpaBooking(
            @PathVariable Long branchId,
            @PathVariable Long spaId,
            @PathVariable Long userId,
            @RequestBody SpaBookingDTO spaBookingRequest,
            Authentication authentication) {

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

        Response response = spaBookingService.saveSpaBooking(branchId, spaId, userId, spaBookingRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/cancel/{branchId}/{bookingId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Response> cancelSpaBooking(@PathVariable Long branchId, @PathVariable Long bookingId, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        String currentUsername = authentication.getName();

        UserDTO currentUser = userService.findByEmail(currentUsername);
        if (!isAdmin) {
            String bookingEmail = spaBookingService.getBookingEmail(branchId, bookingId);
            if (!currentUser.getUserName().equals(bookingEmail)) {
                Response response = new Response();
                response.setStatusCode(403);
                response.setMessage("You can only cancel your own bookings.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        }

        Response response = spaBookingService.cancelSpaBooking(branchId, bookingId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all/{branchId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> getAllSpaBookings(@PathVariable Long branchId) {
        Response response = spaBookingService.getAllSpaBookings(branchId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-by-id/{branchId}/{bookingId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> getSpaBookingById(@PathVariable Long branchId, @PathVariable Long bookingId) {
        Response response = spaBookingService.findSpaBookingById(branchId, bookingId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/user-bookings/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Response> getAllSpaBookingsByUser(@PathVariable Long userId, Authentication authentication) {
        HotelUserDetails currentUser = (HotelUserDetails) authentication.getPrincipal();

        if (currentUser.getId().equals(userId) || authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
            Response response = spaBookingService.getAllSpaBookingsByUser(userId);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } else {
            Response response = new Response();
            response.setStatusCode(403);
            response.setMessage("You do not have permission to access this user's booking history.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }

    @GetMapping("/all-bookings")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> getAllSpaBookingsGroupedByBranch() {
        Response response = spaBookingService.getAllSpasGroupedByBranch();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}

