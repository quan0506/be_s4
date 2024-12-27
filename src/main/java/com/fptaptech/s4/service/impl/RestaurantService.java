package com.fptaptech.s4.service.impl;

import com.fptaptech.s4.dto.RestaurantDTO;
import com.fptaptech.s4.entity.Branch;
import com.fptaptech.s4.entity.Restaurant;
import com.fptaptech.s4.exception.OurException;
import com.fptaptech.s4.repository.BranchRepository;
import com.fptaptech.s4.repository.RestaurantRepository;
import com.fptaptech.s4.dto.Response;
import com.fptaptech.s4.service.interfaces.IRestaurantService;
import com.fptaptech.s4.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RestaurantService implements IRestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final BranchRepository branchRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public Response addNewRestaurant(Long branchId, List<MultipartFile> photos, String restaurantType, String time, BigDecimal restaurantAdultPrice, BigDecimal restaurantChildrenPrice, String restaurantDescription) {
        Response response = new Response();
        try {
            Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new OurException("Branch Not Found"));

            List<String> imageUrls = uploadPhotos(photos);

            Restaurant restaurant = new Restaurant();
            restaurant.setBranch(branch);
            restaurant.setPhotos(imageUrls);
            restaurant.setRestaurantType(restaurantType);
            restaurant.setTime(time);
            restaurant.setRestaurantAdultPrice(restaurantAdultPrice);
            restaurant.setRestaurantChildrenPrice(restaurantChildrenPrice);
            restaurant.setRestaurantDescription(restaurantDescription);
            Restaurant savedRestaurant = restaurantRepository.save(restaurant);

            RestaurantDTO restaurantDTO = Utils.mapRestaurantEntityToRestaurantDTO(savedRestaurant);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setData(restaurantDTO);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving restaurant: " + e.getMessage());
        }
        return response;
    }


    @Override
    public List<String> getAllRestaurantTypes(Long branchId) {
        return List.of(); // Implementation to get all restaurant types for a specific branch
    }

    @Override
    public Response getAllRestaurants(Long branchId) {
        Response response = new Response();
        try {
            List<Restaurant> restaurantList = restaurantRepository.findAllByBranchId(branchId);
            List<RestaurantDTO> restaurantDTOList = Utils.mapRestaurantListEntityToRestaurantListDTO(restaurantList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRestaurantList(restaurantDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching restaurants: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteRestaurant(Long restaurantId) {
        Response response = new Response();
        try {
            Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new OurException("Restaurant Not Found"));
            restaurantRepository.deleteById(restaurantId);
            response.setStatusCode(200);
            response.setMessage("successful");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting restaurant: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateRestaurant(Long branchId, Long restaurantId, String restaurantType, String time, BigDecimal restaurantAdultPrice, BigDecimal restaurantChildrenPrice, String restaurantDescription, List<MultipartFile> photos) {
        Response response = new Response();
        try {
            List<String> imageUrls = null;
            if (photos != null && !photos.isEmpty()) {
                imageUrls = uploadPhotos(photos); // Upload photos to Cloudinary
            }

            Restaurant restaurant = restaurantRepository.findByIdAndBranchId(restaurantId, branchId).orElseThrow(() -> new OurException("Restaurant Not Found"));
            if (restaurantType != null) restaurant.setRestaurantType(restaurantType);
            if (time != null) restaurant.setTime(time);
            if (restaurantAdultPrice != null) restaurant.setRestaurantAdultPrice(restaurantAdultPrice);
            if (restaurantChildrenPrice != null) restaurant.setRestaurantChildrenPrice(restaurantChildrenPrice);
            if (restaurantDescription != null) restaurant.setRestaurantDescription(restaurantDescription);
            if (imageUrls != null && !imageUrls.isEmpty()) restaurant.setPhotos(imageUrls);

            Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
            RestaurantDTO restaurantDTO = Utils.mapRestaurantEntityToRestaurantDTO(updatedRestaurant);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setData(restaurantDTO);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating restaurant: " + e.getMessage());
        }
        return response;
    }

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


    @Override
    public Response getRestaurantById(Long branchId, Long restaurantId) {
        Response response = new Response();
        try {
            Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new OurException("Restaurant Not Found"));
            if (!restaurant.getBranch().getId().equals(branchId)) {
                response.setStatusCode(403);
                response.setMessage("You don't have permission to view this restaurant.");
                return response;
            }
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRestaurant(Utils.mapRestaurantEntityToRestaurantDTO(restaurant));
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching restaurant: " + e.getMessage());
        }
        return response;
    }


    public Response getAllRestaurantsNoBranch() {
        Response response = new Response();
        try {
            List<Restaurant> restaurants = restaurantRepository.findAll();
            List<RestaurantDTO> restaurantDTOList = Utils.mapRestaurantListEntityToRestaurantListDTO(restaurants);
            response.setStatusCode(200);
            response.setMessage("Restaurants retrieved successfully.");
            response.setData(restaurantDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching restaurants: " + e.getMessage());
        }
        return response;
    }
}




