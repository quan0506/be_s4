package com.fptaptech.s4.controller;

import com.fptaptech.s4.entity.Hotel;
import com.fptaptech.s4.service.impl.HotelServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelServices hotelService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<Hotel> addHotel(@RequestBody Hotel hotel) {
        Hotel newHotel = hotelService.addHotel(hotel);
        return ResponseEntity.status(HttpStatus.CREATED).body(newHotel);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<Hotel> updateHotel(@PathVariable Long id, @RequestBody Hotel hotel) {
        Hotel updatedHotel = hotelService.updateHotel(id, hotel);
        return ResponseEntity.ok(updatedHotel);
    }

   /* @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.ok("Hotel deleted successfully.");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Hotel>> getAllHotels() {
        List<Hotel> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(hotels);
    }*/
}




/*@RestController
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelServices hotelService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Hotel> addHotel(@RequestBody Hotel hotel) {
        Hotel newHotel = hotelService.addHotel(hotel);
        return ResponseEntity.status(HttpStatus.CREATED).body(newHotel);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Hotel> updateHotel(@PathVariable Long id, @RequestBody Hotel hotel) {
        Hotel updatedHotel = hotelService.updateHotel(id, hotel);
        return ResponseEntity.ok(updatedHotel);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.ok("Hotel deleted successfully.");
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'EMPLOYEE')")
    public ResponseEntity<List<Hotel>> getAllHotels() {
        List<Hotel> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(hotels);
    }
}*/


/*@RestController
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class HotelController {

    private final HotelServices hotelService;

    @PostMapping("/add")
    public ResponseEntity<Hotel> addHotel(@RequestBody Hotel hotel) {
        Hotel newHotel = hotelService.addHotel(hotel);
        return ResponseEntity.status(HttpStatus.CREATED).body(newHotel);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable Long id, @RequestBody Hotel hotel) {
        Hotel updatedHotel = hotelService.updateHotel(id, hotel);
        return ResponseEntity.ok(updatedHotel);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.ok("Hotel deleted successfully.");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Hotel>> getAllHotels() {
        List<Hotel> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(hotels);
    }
}*/
