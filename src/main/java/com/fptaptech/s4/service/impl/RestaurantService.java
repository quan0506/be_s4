package com.fptaptech.s4.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fptaptech.s4.dto.RestaurantDTO;
import com.fptaptech.s4.entity.Branch;
import com.fptaptech.s4.entity.Restaurant;
import com.fptaptech.s4.exception.OurException;
import com.fptaptech.s4.repository.BranchRepository;
import com.fptaptech.s4.repository.RestaurantRepository;
import com.fptaptech.s4.response.Response;
import com.fptaptech.s4.service.interfaces.IRestaurantService;
import com.fptaptech.s4.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RestaurantService implements IRestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final BranchRepository branchRepository;
    private final Cloudinary cloudinary;

    @Override
    public Response addNewRestaurant(Long branchId, String restaurantType, String time, BigDecimal restaurantAdultPrice, BigDecimal restaurantChildrenPrice, MultipartFile restaurantPhoto, String restaurantDescription) {
        Response response = new Response();
        try {
            Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new OurException("Branch Not Found"));
            Map uploadResult = cloudinary.uploader().upload(restaurantPhoto.getBytes(), ObjectUtils.emptyMap());

            Restaurant restaurant = new Restaurant();
            restaurant.setRestaurantType(restaurantType);
            restaurant.setTime(time);
            restaurant.setRestaurantAdultPrice(restaurantAdultPrice);
            restaurant.setRestaurantChildrenPrice(restaurantChildrenPrice);
            restaurant.setRestaurantPhotoUrl(uploadResult.get("url").toString());
            restaurant.setRestaurantDescription(restaurantDescription);
            restaurant.setBranch(branch);

            restaurantRepository.save(restaurant);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRestaurant(Utils.mapRestaurantEntityToRestaurantDTO(restaurant));
        } catch (IOException e) {
            response.setStatusCode(500);
            response.setMessage("Error uploading photo: " + e.getMessage());
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
    public Response deleteRestaurant(Long branchId, Long restaurantId) {
        Response response = new Response();
        try {
            Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new OurException("Restaurant Not Found"));
            if (!restaurant.getBranch().getId().equals(branchId)) {
                response.setStatusCode(403);
                response.setMessage("You don't have permission to delete this restaurant.");
                return response;
            }
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
    public Response updateRestaurant(Long branchId, Long restaurantId, String restaurantType, String time, BigDecimal restaurantAdultPrice, BigDecimal restaurantChildrenPrice, MultipartFile restaurantPhoto, String restaurantDescription) {
        Response response = new Response();
        try {
            Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new OurException("Restaurant Not Found"));
            if (!restaurant.getBranch().getId().equals(branchId)) {
                response.setStatusCode(403);
                response.setMessage("You don't have permission to update this restaurant.");
                return response;
            }

            Map uploadResult = cloudinary.uploader().upload(restaurantPhoto.getBytes(), ObjectUtils.emptyMap());
            restaurant.setRestaurantType(restaurantType);
            restaurant.setTime(time);
            restaurant.setRestaurantAdultPrice(restaurantAdultPrice);
            restaurant.setRestaurantChildrenPrice(restaurantChildrenPrice);
            restaurant.setRestaurantPhotoUrl(uploadResult.get("url").toString());
            restaurant.setRestaurantDescription(restaurantDescription);

            restaurantRepository.save(restaurant);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRestaurant(Utils.mapRestaurantEntityToRestaurantDTO(restaurant));
        } catch (IOException e) {
            response.setStatusCode(500);
            response.setMessage("Error uploading photo: " + e.getMessage());
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating restaurant: " + e.getMessage());
        }
        return response;
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

