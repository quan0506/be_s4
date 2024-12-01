package com.fptaptech.s4.controller;

<<<<<<< HEAD
import com.fptaptech.s4.dto.Response;
import com.fptaptech.s4.dto.SpaBookingDTO;
import com.fptaptech.s4.dto.UserDTO;
import com.fptaptech.s4.entity.User;
import com.fptaptech.s4.service.ISpaBookingService;
import com.fptaptech.s4.service.IUserService;
=======
import com.fptaptech.s4.response.Response;
import com.fptaptech.s4.dto.SpaBookingDTO;
import com.fptaptech.s4.dto.UserDTO;
import com.fptaptech.s4.service.interfaces.ISpaBookingService;
import com.fptaptech.s4.service.interfaces.IUserService;
>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
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
<<<<<<< HEAD
    private final IUserService userService;

    // book 1 dich vu spa
=======
    private final IUserService userService; // Assuming you have a IUserService to handle user-related operations

>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
    @PostMapping("/book-spa/{branchId}/{spaId}/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Response> saveSpaBooking(
            @PathVariable Long branchId,
            @PathVariable Long spaId,
            @PathVariable Long userId,
            @RequestBody SpaBookingDTO spaBookingRequest,
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

        Response response = spaBookingService.saveSpaBooking(branchId, spaId, userId, spaBookingRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
<<<<<<< HEAD
    // huy book spa
=======

>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
    @DeleteMapping("/cancel/{branchId}/{bookingId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Response> cancelSpaBooking(@PathVariable Long branchId, @PathVariable Long bookingId, Authentication authentication) {
        // Check if the user is an admin or if the user is cancelling their own booking
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        String currentUsername = authentication.getName();

        UserDTO currentUser = userService.findByEmail(currentUsername);
        if (!isAdmin) {
            String bookingEmail = spaBookingService.getBookingEmail(branchId, bookingId);
            if (!currentUser.getUsername().equals(bookingEmail)) {
                Response response = new Response();
                response.setStatusCode(403);
                response.setMessage("You can only cancel your own bookings.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        }

        Response response = spaBookingService.cancelSpaBooking(branchId, bookingId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
<<<<<<< HEAD
    // get All lich su booking Spa theo branch
=======

>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
    @GetMapping("/all/{branchId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> getAllSpaBookings(@PathVariable Long branchId) {
        Response response = spaBookingService.getAllSpaBookings(branchId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
<<<<<<< HEAD
    // get book theo id
    @GetMapping("/get-by-id/{branchId}/{bookingId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
=======

    @GetMapping("/get-by-id/{branchId}/{bookingId}")
>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
    public ResponseEntity<Response> getSpaBookingById(@PathVariable Long branchId, @PathVariable Long bookingId) {
        Response response = spaBookingService.findSpaBookingById(branchId, bookingId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
<<<<<<< HEAD
    // check lich su book cua user
    @GetMapping("/get-by-user/{branchId}/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Response> getUserSpaBookings(@PathVariable Long branchId, @PathVariable Long userId, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getId().equals(userId) || authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
            Response response = spaBookingService.getUserSpaBookings(userId, branchId);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } else {
            Response response = new Response();
            response.setStatusCode(403);
            response.setMessage("You do not have permission to access this user's booking history.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }
}

=======

    @GetMapping("/get-by-user/{branchId}/{userId}")
    public ResponseEntity<Response> getUserSpaBookings(@PathVariable Long branchId, @PathVariable Long userId) {
        Response response = spaBookingService.getUserSpaBookings(userId, branchId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
