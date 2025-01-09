package com.fptaptech.s4.service.impl;


import com.fptaptech.s4.dto.RestaurantBookingDTO;
import com.fptaptech.s4.dto.UserDTO;
import com.fptaptech.s4.entity.Branch;
import com.fptaptech.s4.entity.Restaurant;
import com.fptaptech.s4.entity.RestaurantBooking;
import com.fptaptech.s4.entity.User;
import com.fptaptech.s4.exception.OurException;
import com.fptaptech.s4.repository.BranchRepository;
import com.fptaptech.s4.repository.RestaurantBookingRepository;
import com.fptaptech.s4.repository.RestaurantRepository;
import com.fptaptech.s4.repository.UserRepository;

import com.fptaptech.s4.dto.Response;
import com.fptaptech.s4.service.interfaces.IRestaurantBookingService;
import com.fptaptech.s4.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.util.List;

import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RestaurantBookingService implements IRestaurantBookingService {
    private final RestaurantBookingRepository restaurantBookingRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final EmailService emailService;

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


            Context emailContext = new Context();
            emailContext.setVariable("userName", user.getEmail());
            emailContext.setVariable("RestaurantType", restaurant.getRestaurantType());
            emailContext.setVariable("DayCheckIn", restaurantBooking.getDayCheckIn());
            emailContext.setVariable("NumOfChildren", restaurantBooking.getNumOfChildren());
            emailContext.setVariable("NumOfAdults", restaurantBooking.getNumOfAdults());
            emailContext.setVariable("totalPrice", totalPrice);


            emailService.sendHtmlMessage(
                    user.getEmail(),
                    "Shuttle Booking Confirmation",
                    "shuttle-booking-confirmation",
                    emailContext
            );

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

            List<RestaurantBooking> restaurantBookingList = restaurantBookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                    .filter(booking -> booking.getRestaurant().getBranch().getId().equals(branchId))
                    .collect(Collectors.toList());

//            List<RestaurantBooking> restaurantBookingList = restaurantBookingRepository.findAllBookingsByBranchIdSortedByCheckIn(branchId);
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

            User user = restaurantBooking.getUser();
            Context emailContext = new Context();
            emailContext.setVariable("userName", user.getEmail());
            emailContext.setVariable("restaurantName", restaurantBooking.getName());
            emailContext.setVariable("dayCheckIn", restaurantBooking.getDayCheckIn());

            emailService.sendHtmlMessage(
                    user.getEmail(),
                    "Xác Nhận Hủy Đặt Bàn",
                    "restaurant-booking-cancel-confirmation",
                    emailContext
            );


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
    public Response getAllRestaurantBookingsByUser(Long userId) {
        Response response = new Response();
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));
            List<RestaurantBooking> restaurantBookingList = restaurantBookingRepository.findByUser(user);

            if (restaurantBookingList.isEmpty()) {
                response.setStatusCode(404);
                response.setMessage("No restaurant bookings found for the specified user.");
                return response;
            }

            List<RestaurantBookingDTO> restaurantBookingDTOList = restaurantBookingList.stream()
                    .map(Utils::mapRestaurantBookingEntityToRestaurantBookingDTO)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage("User restaurant bookings retrieved successfully.");
            response.setRestaurantBookingList(restaurantBookingDTOList);
            response.setEmail(user.getEmail());
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching restaurant bookings: " + e.getMessage());
        }
        return response;
    }


    @Override
    public Response getUserRestaurantBookings (Long userId, Long branchId){
        Response response1 = new Response();
        try {
            List<RestaurantBooking> restaurantBookingList = restaurantBookingRepository.findByUserIdOrderByDayCheckInAsc(userId);
            List<RestaurantBookingDTO> restaurantBookingDTOList = Utils.mapRestaurantBookingListEntityToRestaurantBookingListDTO(restaurantBookingList);
            response1.setStatusCode(200);
            response1.setMessage("User restaurant bookings retrieved successfully");
            response1.setRestaurantBookingList(restaurantBookingDTOList);
        } catch (Exception ex) {
            response1.setStatusCode(500);
            response1.setMessage("Error fetching user restaurant bookings: " + ex.getMessage());
        }
        return response1;
    }
    @Override
    public Response getRestaurantBookingById (Long branchId, Long restaurantBookingId){
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


    public Response getAllRestaurantsGroupedByBranch() {
        Response response = new Response();
        try {
            List<RestaurantBooking> restaurantBookingList = restaurantBookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

            // Group by branch
            Map<Long, List<RestaurantBookingDTO>> groupedByBranch = restaurantBookingList.stream()
                    .collect(Collectors.groupingBy(
                            booking -> booking.getRestaurant().getBranch().getId(),
                            Collectors.mapping(booking -> Utils.mapRestaurantBookingEntityToRestaurantBookingDTO(booking), Collectors.toList())
                    ));

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setData(groupedByBranch);

            List<String> userEmails = restaurantBookingList.stream()
                    .map(booking -> booking.getUser().getEmail())
                    .distinct()
                    .collect(Collectors.toList());
            response.setEmail(String.join(", ", userEmails));
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching restaurant bookings: " + e.getMessage());
        }
        return response;
    }
}


