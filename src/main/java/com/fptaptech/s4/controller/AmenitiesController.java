package com.fptaptech.s4.controller;

import com.fptaptech.s4.dto.Response;
import com.fptaptech.s4.service.interfaces.IAmenitiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/amenities")
@RequiredArgsConstructor
public class AmenitiesController {

    @Autowired
    private final IAmenitiesService amenitiesService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> addNewAmenity(
            @RequestParam("roomId") Long roomId,
            @RequestParam(value = "photos", required = false) List<MultipartFile> photos,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description
    ) {
        if (photos == null || photos.isEmpty() || name == null || name.isBlank()) {
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide values for all fields (roomId, photos, name)");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        Response response = amenitiesService.createAmenity(roomId, photos, name, description);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<Response> getAllAmenities() {
        Response response = amenitiesService.getAllAmenities();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/getAllAmenitiesByRoomId/{roomId}")
    public ResponseEntity<Response> getAllAmenitiesByRoomId(@PathVariable Long roomId) {
        Response response = amenitiesService.getAllAmenitiesByRoomId(roomId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/getAmenityById/{id}")
    public ResponseEntity<Response> getAmenityById(@PathVariable Long id) {
        Response response = amenitiesService.getAmenityById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/updateAmenity/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> updateAmenity(
            @PathVariable Long id,
            @RequestParam("roomId") Long roomId,
            @RequestParam(value = "photos", required = false) List<MultipartFile> photos,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description
    ) {
        Response response = amenitiesService.updateAmenity(id, roomId, photos, name, description);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/deleteAmenity/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> deleteAmenity(@PathVariable Long id) {
        Response response = amenitiesService.deleteAmenity(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}

