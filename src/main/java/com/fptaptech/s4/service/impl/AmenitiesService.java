package com.fptaptech.s4.service.impl;

import com.fptaptech.s4.dto.AmenitiesDTO;
import com.fptaptech.s4.dto.Response;
import com.fptaptech.s4.entity.Amenities;
import com.fptaptech.s4.entity.Room;
import com.fptaptech.s4.exception.OurException;
import com.fptaptech.s4.repository.AmenitiesRepository;
import com.fptaptech.s4.repository.RoomRepository;
import com.fptaptech.s4.service.interfaces.IAmenitiesService;
import com.fptaptech.s4.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AmenitiesService implements IAmenitiesService {

    private final AmenitiesRepository amenitiesRepository;
    private final RoomRepository roomRepository;
    private final CloudinaryService cloudinaryService;

    // Create Amenity
    @Override
    public Response createAmenity(Long roomId, List<MultipartFile> photos, String name, String description) {
        Response response = new Response();
        try {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room Not Found"));

            List<String> imageUrls = uploadPhotos(photos); // Upload photos to Cloudinary

            Amenities amenity = new Amenities();
            amenity.setRoom(room);
            amenity.setPhotos(imageUrls);
            amenity.setName(name);
            amenity.setDescription(description);
            Amenities savedAmenity = amenitiesRepository.save(amenity);

            AmenitiesDTO amenitiesDTO = Utils.mapAmenitiesEntityToAmenitiesDTO(savedAmenity);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setData(amenitiesDTO);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving amenity: " + e.getMessage());
        }
        return response;
    }

    // Get All Amenities
    @Override
    public Response getAllAmenities() {
        Response response = new Response();
        try {
            List<Amenities> amenities = amenitiesRepository.findAll();
            List<AmenitiesDTO> amenitiesDTOList = Utils.mapAmenitiesListEntityToAmenitiesListDTO(amenities);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setData(amenitiesDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving amenities: " + e.getMessage());
        }
        return response;
    }

    // Get All Amenities by Room ID
    @Override
    public Response getAllAmenitiesByRoomId(Long roomId) {
        Response response = new Response();
        try {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
            List<Amenities> amenities = amenitiesRepository.findAllByRoomId(roomId);
            List<AmenitiesDTO> amenitiesDTOList = Utils.mapAmenitiesListEntityToAmenitiesListDTO(amenities);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setData(amenitiesDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving amenities by room ID: " + e.getMessage());
        }
        return response;
    }

    // Get Amenity By ID
    @Override
    public Response getAmenityById(Long id) {
        Response response = new Response();
        try {
            Amenities amenity = amenitiesRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Amenity not found"));
            AmenitiesDTO amenitiesDTO = Utils.mapAmenitiesEntityToAmenitiesDTO(amenity);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setData(amenitiesDTO);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving amenity: " + e.getMessage());
        }
        return response;
    }

    // Update Amenity
    @Override
    public Response updateAmenity(Long id, Long roomId, List<MultipartFile> photos, String name, String description) {
        Response response = new Response();
        try {
            Amenities existingAmenity = amenitiesRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Amenity not found"));

            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room Not Found"));

            List<String> imageUrls = uploadPhotos(photos); // Upload photos to Cloudinary

            existingAmenity.setRoom(room);
            existingAmenity.setPhotos(imageUrls);
            existingAmenity.setName(name);
            existingAmenity.setDescription(description);
            Amenities updatedAmenity = amenitiesRepository.save(existingAmenity);

            AmenitiesDTO amenitiesDTO = Utils.mapAmenitiesEntityToAmenitiesDTO(updatedAmenity);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setData(amenitiesDTO);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating amenity: " + e.getMessage());
        }
        return response;
    }

    // Delete Amenity
    @Override
    public Response deleteAmenity(Long id) {
        Response response = new Response();
        try {
            Amenities amenity = amenitiesRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Amenity not found"));
            amenitiesRepository.delete(amenity);
            response.setStatusCode(200);
            response.setMessage("Amenity deleted successfully");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting amenity: " + e.getMessage());
        }
        return response;
    }

    // Helper method to upload photos to Cloudinary
    private List<String> uploadPhotos(List<MultipartFile> photos) {
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile photo : photos) {
            if (photo != null && !photo.isEmpty()) {
                try {
                    Map uploadResult = cloudinaryService.upload(photo);
                    String imageUrl = (String) uploadResult.get("url");
                    imageUrls.add(imageUrl);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to upload photo", e);
                }
            }
        }
        return imageUrls;
    }
}


