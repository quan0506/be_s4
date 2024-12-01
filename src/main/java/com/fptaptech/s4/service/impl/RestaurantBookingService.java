package com.fptaptech.s4.service.impl;

<<<<<<< HEAD
import com.fptaptech.s4.dto.Response;
=======
import com.fptaptech.s4.response.Response;
>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
import com.fptaptech.s4.dto.RestaurantBookingDTO;
import com.fptaptech.s4.entity.Branch;
import com.fptaptech.s4.entity.Restaurant;
import com.fptaptech.s4.entity.RestaurantBooking;
import com.fptaptech.s4.entity.User;
import com.fptaptech.s4.exception.OurException;
import com.fptaptech.s4.repository.BranchRepository;
import com.fptaptech.s4.repository.RestaurantBookingRepository;
import com.fptaptech.s4.repository.RestaurantRepository;
import com.fptaptech.s4.repository.UserRepository;
<<<<<<< HEAD
import com.fptaptech.s4.service.IRestaurantBookingService;
import com.fptaptech.s4.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
=======
import com.fptaptech.s4.service.interfaces.IRestaurantBookingService;
import com.fptaptech.s4.utils.Utils;
import lombok.RequiredArgsConstructor;
>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
<<<<<<< HEAD
import java.util.stream.Collectors;
=======
>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1

@Service
@RequiredArgsConstructor
public class RestaurantBookingService implements IRestaurantBookingService {
    private final RestaurantBookingRepository restaurantBookingRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository; // Assuming you have a BranchRepository

    @Override
    public Response saveRestaurantBooking(Long branchId, Long restaurantId, Long userId, RestaurantBookingDTO restaurantBookingRequest) {
        Response response = new Response();
        try {
            Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new OurException("Branch Not Found"));
            Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new OurException("Restaurant Not Found"));
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));

            RestaurantBooking restaurantBooking = new RestaurantBooking();
            restaurantBooking.setRestaurant(restaurant);
            restaurantBooking.setUser(user);
            restaurantBooking.setDayCheckIn(restaurantBookingRequest.getDayCheckIn());
            restaurantBooking.setNumOfAdults(restaurantBookingRequest.getNumOfAdults());
            restaurantBooking.setNumOfChildren(restaurantBookingRequest.getNumOfChildren());
            restaurantBooking.setName(restaurantBookingRequest.getName());
            restaurantBooking.setPhone(restaurantBookingRequest.getPhone());

            // Calculate the total price
            BigDecimal totalPrice = restaurant.getRestaurantAdultPrice().multiply(BigDecimal.valueOf(restaurantBooking.getNumOfAdults()))
                    .add(restaurant.getRestaurantChildrenPrice().multiply(BigDecimal.valueOf(restaurantBooking.getNumOfChildren())));
            restaurantBooking.setTotalPrice(totalPrice);

            restaurantBookingRepository.save(restaurantBooking);

            RestaurantBookingDTO restaurantBookingDTO = Utils.mapRestaurantBookingEntityToRestaurantBookingDTO(restaurantBooking);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRestaurantBooking(restaurantBookingDTO);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving restaurant booking: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllRestaurantBookings(Long branchId) {
        Response response = new Response();
        try {
<<<<<<< HEAD
            List<RestaurantBooking> restaurantBookingList = restaurantBookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                    .filter(booking -> booking.getRestaurant().getBranch().getId().equals(branchId))
                    .collect(Collectors.toList());
=======
            List<RestaurantBooking> restaurantBookingList = restaurantBookingRepository.findAllBookingsByBranchIdSortedByCheckIn(branchId);
>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
            List<RestaurantBookingDTO> restaurantBookingDTOList = Utils.mapRestaurantBookingListEntityToRestaurantBookingListDTO(restaurantBookingList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRestaurantBookingList(restaurantBookingDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching restaurant bookings: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response cancelRestaurantBooking(Long branchId, Long restaurantBookingId) {
        Response response = new Response();
        try {
            RestaurantBooking restaurantBooking = restaurantBookingRepository.findById(restaurantBookingId).orElseThrow(() -> new OurException("Booking Not Found"));
            restaurantBookingRepository.deleteById(restaurantBookingId);
            response.setStatusCode(200);
            response.setMessage("successful");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error canceling restaurant booking: " + e.getMessage());
        }
        return response;
    }

    @Override
<<<<<<< HEAD
    public Response getAllRestaurantBookingsByUser(Long userId) {
        Response response = new Response();
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));
            List<RestaurantBooking> restaurantBookingList = restaurantBookingRepository.findByUser(user);
            List<RestaurantBookingDTO> restaurantBookingDTOList = Utils.mapRestaurantBookingListEntityToRestaurantBookingListDTO(restaurantBookingList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRestaurantBookingList(restaurantBookingDTOList);
            response.setEmail(user.getEmail());  // Include the user's email in the response
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching restaurant bookings: " + e.getMessage());
=======
    public Response getUserRestaurantBookings(Long userId, Long branchId) {
        Response response = new Response();
        try {
            List<RestaurantBooking> restaurantBookingList = restaurantBookingRepository.findByUserIdOrderByDayCheckInAsc(userId);
            List<RestaurantBookingDTO> restaurantBookingDTOList = Utils.mapRestaurantBookingListEntityToRestaurantBookingListDTO(restaurantBookingList);
            response.setStatusCode(200);
            response.setMessage("User restaurant bookings retrieved successfully");
            response.setRestaurantBookingList(restaurantBookingDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching user restaurant bookings: " + e.getMessage());
>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
        }
        return response;
    }

    @Override
    public Response getRestaurantBookingById(Long branchId, Long restaurantBookingId) {
        Response response = new Response();
        try {
            RestaurantBooking restaurantBooking = restaurantBookingRepository.findById(restaurantBookingId)
                    .orElseThrow(() -> new OurException("Booking Not Found"));
            RestaurantBookingDTO restaurantBookingDTO = Utils.mapRestaurantBookingEntityToRestaurantBookingDTO(restaurantBooking);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRestaurantBooking(restaurantBookingDTO);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching restaurant booking: " + e.getMessage());
        }
        return response;
    }
}
