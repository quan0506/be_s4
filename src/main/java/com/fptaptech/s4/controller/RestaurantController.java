package com.fptaptech.s4.controller;

import com.fptaptech.s4.response.Response;
import com.fptaptech.s4.service.interfaces.IRestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final IRestaurantService restaurantService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> addNewRestaurant(
            @RequestParam(value = "branchId") Long branchId,
            @RequestParam(value = "restaurantType") String restaurantType,
            @RequestParam(value = "time") String time,
            @RequestParam(value = "restaurantAdultPrice") BigDecimal restaurantAdultPrice,
            @RequestParam(value = "restaurantChildrenPrice") BigDecimal restaurantChildrenPrice,
            @RequestParam(value = "photos", required = false) List<MultipartFile> photos,
            @RequestParam(value = "restaurantDescription") String restaurantDescription
    ) {
        if (restaurantType == null || restaurantType.isBlank() || restaurantAdultPrice == null || restaurantChildrenPrice == null) {
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide values for all fields (branchId, restaurantType, restaurantAdultPrice, restaurantChildrenPrice)");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        Response response = restaurantService.addNewRestaurant(branchId, photos, restaurantType, time, restaurantAdultPrice, restaurantChildrenPrice, restaurantDescription);
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> updateRestaurant(
            @PathVariable Long restaurantId,
            @RequestParam("branchId") Long branchId,
            @RequestParam(value = "photos", required = false) List<MultipartFile> photos,
            @RequestParam(value = "restaurantType", required = false) String restaurantType,
            @RequestParam(value = "time", required = false) String time,
            @RequestParam(value = "restaurantAdultPrice", required = false) BigDecimal restaurantAdultPrice,
            @RequestParam(value = "restaurantChildrenPrice", required = false) BigDecimal restaurantChildrenPrice,
            @RequestParam(value = "restaurantDescription", required = false) String restaurantDescription
    ) {
        Response response = restaurantService.updateRestaurant(branchId, restaurantId, restaurantType, time, restaurantAdultPrice, restaurantChildrenPrice, restaurantDescription, photos);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{restaurantId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> deleteRestaurant(@PathVariable Long restaurantId) {
        Response response = restaurantService.deleteRestaurant( restaurantId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-all-restaurants")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> getAllRestaurants() {
        Response response = restaurantService.getAllRestaurantsNoBranch();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
