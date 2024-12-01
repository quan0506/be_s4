package com.fptaptech.s4.controller;

<<<<<<< HEAD
import com.fptaptech.s4.dto.Response;
import com.fptaptech.s4.dto.ShuttleBookingDTO;
import com.fptaptech.s4.dto.UserDTO;
import com.fptaptech.s4.service.IShuttleBookingService;
import com.fptaptech.s4.service.IShuttleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
=======
import com.fptaptech.s4.response.Response;
import com.fptaptech.s4.service.interfaces.IShuttleService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

import java.math.BigDecimal;
import java.time.LocalDate;
@RestController
@RequestMapping("/shuttles")
@RequiredArgsConstructor
public class ShuttleController {

    private final IShuttleService shuttleService;

<<<<<<< HEAD
    // them xe
=======

>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> addNewCar(
            @RequestParam("branchId") Long branchId,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "carType", required = false) String carType,
            @RequestParam(value = "carPrice", required = false) BigDecimal carPrice,
            @RequestParam(value = "carDescription", required = false) String carDescription
    ) {

        if (photo == null || photo.isEmpty() || carType == null || carType.isBlank() || carPrice == null) {
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide values for all fields (branchId, photo, carType, carPrice)");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        Response response = shuttleService.addNewCar(branchId, photo, carType, carPrice, carDescription);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

<<<<<<< HEAD
    // lay tat ca dich vu xe
=======
>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
    @GetMapping("/all")
    public ResponseEntity<Response> getAllCars(@RequestParam("branchId") Long branchId) {
        Response response = shuttleService.getAllCars(branchId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

<<<<<<< HEAD
    // xem tat ca cac loai xe
=======
>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
    @GetMapping("/types")
    public List<String> getCarTypes(@RequestParam("branchId") Long branchId) {
        return shuttleService.getAllCarTypes(branchId);
    }

<<<<<<< HEAD
    // xem dich vu xe theo id
=======
>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
    @GetMapping("/car-by-id/{carId}")
    public ResponseEntity<Response> getCarById(@PathVariable Long carId, @RequestParam("branchId") Long branchId) {
        Response response = shuttleService.getCarById(branchId, carId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

<<<<<<< HEAD
    // xem tat ca cac xe hien co ( theo thoi gian ) (giong chon ngay booking khach san)
=======
>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
    @GetMapping("/all-available-cars")
    public ResponseEntity<Response> getAvailableCars(@RequestParam("branchId") Long branchId) {
        Response response = shuttleService.getAllAvailableCars(branchId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

<<<<<<< HEAD
    // search theo loai xe va ngay thue xe
=======
>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
    @GetMapping("/available-cars-by-date-and-type")
    public ResponseEntity<Response> getAvailableCarsByDateAndType(
            @RequestParam("branchId") Long branchId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam(required = false) String carType
    ) {
        Response response;

        if (checkInDate == null || carType == null || carType.isBlank() || checkOutDate == null) {
            response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide values for all fields (branchId, checkInDate, carType, checkOutDate)");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }

        response = shuttleService.getAvailableCarsByDateAndType(branchId, checkInDate, checkOutDate, carType);

        if (response == null) {
            response = new Response();
            response.setStatusCode(500);
            response.setMessage("Internal server error: response is null");
        }

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

<<<<<<< HEAD
    // update dich vu xe
=======
>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
    @PutMapping("/update/{carId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> updateCar(@PathVariable Long carId,
                                              @RequestParam("branchId") Long branchId,
                                              @RequestParam(value = "photo", required = false) MultipartFile photo,
                                              @RequestParam(value = "carType", required = false) String carType,
                                              @RequestParam(value = "carPrice", required = false) BigDecimal carPrice,
                                              @RequestParam(value = "carDescription", required = false) String carDescription
    ) {
        Response response = shuttleService.updateCar(branchId, carId, carDescription, carType, carPrice, photo);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

<<<<<<< HEAD
    // xoa 1 dich vu xe
=======
>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
    @DeleteMapping("/delete/{carId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> deleteCar(@PathVariable Long carId, @RequestParam("branchId") Long branchId) {
        Response response = shuttleService.deleteCar(branchId, carId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


}
