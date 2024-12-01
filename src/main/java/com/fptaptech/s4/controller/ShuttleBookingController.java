package com.fptaptech.s4.controller;

import com.fptaptech.s4.dto.Response;
import com.fptaptech.s4.dto.ShuttleBookingDTO;
import com.fptaptech.s4.dto.UserDTO;
import com.fptaptech.s4.entity.User;
import com.fptaptech.s4.service.IShuttleBookingService;
import com.fptaptech.s4.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shuttle-bookings")
@RequiredArgsConstructor
public class ShuttleBookingController {

    private final IShuttleBookingService shuttleBookingService;
    private final IUserService userService;

    @PostMapping("/book-shuttle/{branchId}/{shuttleId}/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Response> saveShuttleBookings(
            @PathVariable Long branchId,
            @PathVariable Long shuttleId,
            @PathVariable Long userId,
            @RequestBody ShuttleBookingDTO shuttleBookingRequest,
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

        Response response = shuttleBookingService.saveShuttleBooking(branchId, shuttleId, userId, shuttleBookingRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
// huy dat dich vu xe
    @DeleteMapping("/cancel/{branchId}/{bookingId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Response> cancelShuttleBooking(@PathVariable Long branchId, @PathVariable Long bookingId, Authentication authentication) {
        // Check if the user is an admin or if the user is cancelling their own booking
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        String currentUsername = authentication.getName();

        UserDTO currentUser = userService.findByEmail(currentUsername);
        if (!isAdmin) {
            String bookingEmail = shuttleBookingService.getBookingEmail(branchId, bookingId);
            if (!currentUser.getEmail().equals(bookingEmail)) {
                Response response = new Response();
                response.setStatusCode(403);
                response.setMessage("You can only cancel your own bookings.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        }

        Response response = shuttleBookingService.cancelShuttleBooking(branchId, bookingId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // lấy tat ca loai xe trong branch
    @GetMapping("/all/{branchId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> getAllShuttles(@PathVariable Long branchId) {
        Response response = shuttleBookingService.getAllShuttles(branchId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // lay book xe qua code
    @GetMapping("/get-by-confirmation-code/{branchId}/{confirmationCode}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Response> getShuttleBookingByConfirmationCode(@PathVariable Long branchId, @PathVariable String confirmationCode) {
        Response response = shuttleBookingService.findBookingByConfirmationCode(branchId, confirmationCode);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    // get lich su booking cua user
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Response> getAllShuttleBookingsByUser(@PathVariable Long userId, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getId().equals(userId) || authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
            Response response = shuttleBookingService.getAllShuttleBookingsByUser(userId);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } else {
            Response response = new Response();
            response.setStatusCode(403);
            response.setMessage("You do not have permission to access this user's booking history.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }



}


