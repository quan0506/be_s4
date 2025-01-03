package com.fptaptech.s4.service.interfaces;

import com.fptaptech.s4.dto.Response;
import com.fptaptech.s4.dto.RestaurantBookingDTO;

public interface IRestaurantBookingService {
    Response saveRestaurantBooking(Long branchId, Long restaurantId, Long userId, RestaurantBookingDTO restaurantBookingRequest);

    Response getAllRestaurantBookings(Long branchId);

    Response cancelRestaurantBooking(Long branchId, Long restaurantBookingId);

    Response getUserRestaurantBookings(Long userId, Long branchId);

    Response getRestaurantBookingById(Long branchId, Long restaurantBookingId);

    Response getAllRestaurantBookingsByUser(Long userId);

    Response getAllRestaurantsGroupedByBranch();
}
