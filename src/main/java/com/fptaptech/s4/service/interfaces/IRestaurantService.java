package com.fptaptech.s4.service.interfaces;

import com.fptaptech.s4.response.Response;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface IRestaurantService {
    Response addNewRestaurant(Long branchId, String restaurantType, String time, BigDecimal restaurantAdultPrice, BigDecimal restaurantChildrenPrice, MultipartFile restaurantPhoto, String restaurantDescription);

    List<String> getAllRestaurantTypes(Long branchId);

    Response getAllRestaurants(Long branchId);

    Response deleteRestaurant(Long branchId, Long restaurantId);

    Response updateRestaurant(Long branchId, Long restaurantId, String restaurantType, String time, BigDecimal restaurantAdultPrice, BigDecimal restaurantChildrenPrice, MultipartFile restaurantPhoto, String restaurantDescription);

    Response getRestaurantById(Long branchId, Long restaurantId);
}
