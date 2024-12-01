package com.fptaptech.s4.controller;

<<<<<<< HEAD
import com.fptaptech.s4.dto.Response;
import com.fptaptech.s4.dto.RestaurantBookingDTO;
import com.fptaptech.s4.dto.UserDTO;
import com.fptaptech.s4.entity.RestaurantBooking;
import com.fptaptech.s4.entity.User;
import com.fptaptech.s4.service.IRestaurantBookingService;
import com.fptaptech.s4.service.IUserService;
=======
import com.fptaptech.s4.response.Response;
import com.fptaptech.s4.dto.RestaurantBookingDTO;
import com.fptaptech.s4.dto.UserDTO;
import com.fptaptech.s4.service.interfaces.IRestaurantBookingService;
import com.fptaptech.s4.service.interfaces.IUserService;
>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
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
<<<<<<< HEAD
// get all lich su booking
=======

>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> getAllRestaurantBookings(@RequestParam(value = "branchId") Long branchId) {
        Response response = restaurantBookingService.getAllRestaurantBookings(branchId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
<<<<<<< HEAD
// huy book nha hang
=======

>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
    @DeleteMapping("/cancel/{branchId}/{bookingId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Response> cancelRestaurantBooking(
            @PathVariable Long branchId,
            @PathVariable Long bookingId) {
        Response response = restaurantBookingService.cancelRestaurantBooking(branchId, bookingId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

<<<<<<< HEAD
// tim lich su theo user
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Response> getAllRestaurantBookingsByUser(@PathVariable Long userId, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getId().equals(userId) || authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
            Response response = restaurantBookingService.getAllRestaurantBookingsByUser(userId);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } else {
            Response response = new Response();
            response.setStatusCode(403);
            response.setMessage("You do not have permission to access this user's booking history.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }
// tim booking theo id
=======
    @GetMapping("/user-bookings/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Response> getUserRestaurantBookings(
            @PathVariable Long userId,
            @RequestParam(value = "branchId") Long branchId) {
        Response response = restaurantBookingService.getUserRestaurantBookings(userId, branchId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
    @GetMapping("/booking-by-id/{bookingId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> getRestaurantBookingById(
            @PathVariable Long bookingId,
            @RequestParam(value = "branchId") Long branchId) {
        Response response = restaurantBookingService.getRestaurantBookingById(branchId, bookingId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
<<<<<<< HEAD

=======
>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
