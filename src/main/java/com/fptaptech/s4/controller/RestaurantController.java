package com.fptaptech.s4.controller;

import com.fptaptech.s4.dto.Response;
import com.fptaptech.s4.dto.RestaurantBookingDTO;
import com.fptaptech.s4.dto.UserDTO;
import com.fptaptech.s4.service.IRestaurantBookingService;
import com.fptaptech.s4.service.IRestaurantService;
import com.fptaptech.s4.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    @Autowired
    private final IRestaurantService restaurantService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> addNewRestaurant(
            @RequestParam(value = "branchId") Long branchId,
            @RequestParam(value = "restaurantType") String restaurantType,
            @RequestParam(value = "time") String time,
            @RequestParam(value = "restaurantAdultPrice") BigDecimal restaurantAdultPrice,
            @RequestParam(value = "restaurantChildrenPrice") BigDecimal restaurantChildrenPrice,
            @RequestParam(value = "restaurantPhotoUrl") String restaurantPhotoUrl,
            @RequestParam(value = "restaurantDescription") String restaurantDescription
    ) {
        Response response = restaurantService.addNewRestaurant(branchId, restaurantType, time, restaurantAdultPrice, restaurantChildrenPrice, restaurantPhotoUrl, restaurantDescription);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllRestaurants(@RequestParam(value = "branchId") Long branchId) {
        Response response = restaurantService.getAllRestaurants(branchId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/types")
    public List<String> getRestaurantTypes(@RequestParam(value = "branchId") Long branchId) {
        return restaurantService.getAllRestaurantTypes(branchId);
    }

    @GetMapping("/restaurant-by-id/{restaurantId}")
    public ResponseEntity<Response> getRestaurantById(@PathVariable Long restaurantId,
                                                      @RequestParam(value = "branchId") Long branchId) {
        Response response = restaurantService.getRestaurantById(branchId, restaurantId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update/{restaurantId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> updateRestaurant(
            @PathVariable Long restaurantId,
            @RequestParam(value = "branchId") Long branchId,
            @RequestParam(value = "restaurantType") String restaurantType,
            @RequestParam(value = "time") String time,
            @RequestParam(value = "restaurantAdultPrice") BigDecimal restaurantAdultPrice,
            @RequestParam(value = "restaurantChildrenPrice") BigDecimal restaurantChildrenPrice,
            @RequestParam(value = "restaurantPhotoUrl") String restaurantPhotoUrl,
            @RequestParam(value = "restaurantDescription") String restaurantDescription) {
        Response response = restaurantService.updateRestaurant(branchId, restaurantId, restaurantType, time, restaurantAdultPrice, restaurantChildrenPrice, restaurantPhotoUrl, restaurantDescription);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{restaurantId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> deleteRestaurant(@PathVariable Long restaurantId,
                                                     @RequestParam(value = "branchId") Long branchId) {
        Response response = restaurantService.deleteRestaurant(branchId, restaurantId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
