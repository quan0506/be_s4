package com.fptaptech.s4.utils;

import com.fptaptech.s4.dto.*;
import com.fptaptech.s4.entity.*;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    // Generate random confirmation code
    public static String generateRandomConfirmationCode(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

    public static String generateRandomVoucherAndDiscountCode(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

    // User mappings
    public static UserDTO mapUserEntityToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUserName(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPhone(user.getPhone());

        // Map roles directly from Role entity
        userDTO.setRoles(new ArrayList<>(user.getRoles()));

        return userDTO;
    }

    public static UserDTO mapUserEntityToUserDTOPlusUserBookings(User user) {
        UserDTO userDTO = mapUserEntityToUserDTO(user);

        List<ShuttleBookingDTO> shuttleBookingDTOList = user.getShuttleBookings().stream()
                .map(Utils::mapShuttleBookingEntityToShuttleBookingDTOPlusShuttle)
                .collect(Collectors.toList());
        userDTO.setShuttleBookings(shuttleBookingDTOList);

        return userDTO;
    }

    public static List<UserDTO> mapUserListEntityToUserListDTO(List<User> userList) {
        return userList.stream().map(Utils::mapUserEntityToUserDTO).collect(Collectors.toList());
    }

    // Shuttle mappings
    public static ShuttleDTO mapShuttleEntityToShuttleDTO(Shuttle shuttle) {
        ShuttleDTO shuttleDTO = new ShuttleDTO();
        shuttleDTO.setId(shuttle.getId());
        shuttleDTO.setCarType(shuttle.getCarType());
        shuttleDTO.setCarPrice(shuttle.getCarPrice());
        shuttleDTO.setCarPhotoUrl(shuttle.getCarPhotoUrl());
        shuttleDTO.setCarDescription(shuttle.getCarDescription());
        shuttleDTO.setBranchId(shuttle.getBranch().getId()); // Set branch ID
        return shuttleDTO;
    }

    public static ShuttleDTO mapShuttleEntityToShuttleDTOPlusBookings(Shuttle shuttle) {
        ShuttleDTO shuttleDTO = mapShuttleEntityToShuttleDTO(shuttle);
        if (shuttle.getShuttleBookings() != null) {
            List<ShuttleBookingDTO> shuttleBookingDTOList = shuttle.getShuttleBookings().stream()
                    .map(Utils::mapShuttleBookingEntityToShuttleBookingDTO)
                    .collect(Collectors.toList());
            shuttleDTO.setShuttleBookings(shuttleBookingDTOList);
        }
        return shuttleDTO;
    }

    public static List<ShuttleDTO> mapShuttleListEntityToShuttleListDTO(List<Shuttle> shuttleList) {
        return shuttleList.stream().map(Utils::mapShuttleEntityToShuttleDTO).collect(Collectors.toList());
    }

    // ShuttleBooking mappings
    public static ShuttleBookingDTO mapShuttleBookingEntityToShuttleBookingDTO(ShuttleBooking shuttleBooking) {
        ShuttleBookingDTO shuttleBookingDTO = new ShuttleBookingDTO();
        shuttleBookingDTO.setId(shuttleBooking.getId());
        shuttleBookingDTO.setShuttleCheckInDate(shuttleBooking.getShuttleCheckInDate());
        shuttleBookingDTO.setShuttleCheckOutDate(shuttleBooking.getShuttleCheckOutDate());
        shuttleBookingDTO.setBookingConfirmationCode(shuttleBooking.getBookingConfirmationCode());
        shuttleBookingDTO.setTotalPrice(shuttleBooking.getTotalPrice());
        shuttleBookingDTO.setUserEmail(shuttleBooking.getUser().getEmail());
        shuttleBookingDTO.setBranchId(shuttleBooking.getShuttle().getBranch().getId()); // Set branch ID
        return shuttleBookingDTO;
    }

    public static ShuttleBookingDTO mapShuttleBookingEntityToShuttleBookingDTOPlusShuttle(ShuttleBooking shuttleBooking) {
        ShuttleBookingDTO shuttleBookingDTO = mapShuttleBookingEntityToShuttleBookingDTO(shuttleBooking);
        if (shuttleBooking.getUser() != null) {
            shuttleBookingDTO.setUser(Utils.mapUserEntityToUserDTO(shuttleBooking.getUser()));
        }
        if (shuttleBooking.getShuttle() != null) {
            shuttleBookingDTO.setShuttle(Utils.mapShuttleEntityToShuttleDTO(shuttleBooking.getShuttle()));
        }
        return shuttleBookingDTO;
    }

    public static List<ShuttleBookingDTO> mapShuttleBookingListEntityToShuttleBookingListDTO(List<ShuttleBooking> shuttleBookingList) {
        return shuttleBookingList.stream().map(Utils::mapShuttleBookingEntityToShuttleBookingDTO).collect(Collectors.toList());
    }




    //Spa mappings
    public static UserDTO mapUserEntityToUserDTOPlusUserBookingsAndSpa(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setUserName(user.getEmail());
        userDTO.setPhone(user.getPhone());

        List<SpaBookingDTO> spaBookingDTOList = user.getSpaBookings().stream()
                .map(booking -> mapSpaBookingEntityToSpaBookingDTOPlusSpa(booking, false))
                .collect(Collectors.toList());
        userDTO.setSpaBookings(spaBookingDTOList);

        return userDTO;
    }

    public static SpaDTO mapSpaEntityToSpaDTO(Spa spa) {
        SpaDTO spaDTO = new SpaDTO();
        spaDTO.setId(spa.getId());
        spaDTO.setSpaServiceName(spa.getSpaServiceName());
        spaDTO.setSpaServicePrice(spa.getSpaServicePrice());
        spaDTO.setSpaPhotoUrl(spa.getSpaPhotoUrl());
        spaDTO.setSpaDescription(spa.getSpaDescription());
        spaDTO.setBranchId(spa.getBranch().getId());
        return spaDTO;
    }



    public static List<SpaDTO> mapSpaListEntityToSpaListDTO(List<Spa> spaList) {
        return spaList.stream().map(Utils::mapSpaEntityToSpaDTO).collect(Collectors.toList());
    }

    public static SpaBookingDTO mapSpaBookingEntityToSpaBookingDTO(SpaBooking spaBooking) {
        SpaBookingDTO spaBookingDTO = new SpaBookingDTO();
        spaBookingDTO.setId(spaBooking.getId());
        spaBookingDTO.setAppointmentTime(spaBooking.getAppointmentTime());
        spaBookingDTO.setSpaServiceTime(spaBooking.getSpaServiceTime());
        spaBookingDTO.setPhone(spaBooking.getPhone());
        spaBookingDTO.setNumberOfPeople(spaBooking.getNumberOfPeople());
        spaBookingDTO.setFullName(spaBooking.getFullName());
        spaBookingDTO.setDescription(spaBooking.getDescription());
        spaBookingDTO.setSpaServiceName(spaBooking.getSpaServiceName());
        spaBookingDTO.setSpaServicePrice(spaBooking.getSpaServicePrice());
        spaBookingDTO.setSpaDescription(spaBooking.getSpaDescription());
        spaBookingDTO.setUserEmail(spaBooking.getUser().getEmail());
        spaBookingDTO.setUser(mapUserEntityToUserDTO(spaBooking.getUser()));
        spaBookingDTO.setSpa(mapSpaEntityToSpaDTO(spaBooking.getSpa()));
        return spaBookingDTO;
    }
    public static List<SpaBookingDTO> mapSpaBookingListEntityToSpaBookingListDTO(List<SpaBooking> spaBookingList) {
        return spaBookingList.stream().map(Utils::mapSpaBookingEntityToSpaBookingDTO).collect(Collectors.toList());
    }

    public static SpaBookingDTO mapSpaBookingEntityToSpaBookingDTOPlusSpa(SpaBooking spaBooking, boolean mapUser) {
        SpaBookingDTO spaBookingDTO = mapSpaBookingEntityToSpaBookingDTO(spaBooking);
        if (mapUser) {
            spaBookingDTO.setUser(Utils.mapUserEntityToUserDTO(spaBooking.getUser()));
        }
        if (spaBooking.getSpa() != null) {
            SpaDTO spaDTO = new SpaDTO();
            spaDTO.setId(spaBooking.getSpa().getId());
            spaDTO.setSpaServiceName(spaBooking.getSpa().getSpaServiceName());
            spaBookingDTO.setSpa(spaDTO);
        }
        return spaBookingDTO;
    }


    // Restaurant mapping

    public static RestaurantDTO mapRestaurantEntityToRestaurantDTO(Restaurant restaurant) {
        RestaurantDTO restaurantDTO = new RestaurantDTO();
        restaurantDTO.setId(restaurant.getId());
        restaurantDTO.setRestaurantType(restaurant.getRestaurantType());
        restaurantDTO.setTime(restaurant.getTime());
        restaurantDTO.setRestaurantAdultPrice(restaurant.getRestaurantAdultPrice());
        restaurantDTO.setRestaurantChildrenPrice(restaurant.getRestaurantChildrenPrice());
        restaurantDTO.setRestaurantPhotoUrl(restaurant.getRestaurantPhotoUrl());
        restaurantDTO.setRestaurantDescription(restaurant.getRestaurantDescription());
        restaurantDTO.setBranchId(restaurant.getBranch().getId()); // Added branchId
        return restaurantDTO;
    }

    public static List<RestaurantDTO> mapRestaurantListEntityToRestaurantListDTO(List<Restaurant> restaurantList) {
        return restaurantList.stream().map(Utils::mapRestaurantEntityToRestaurantDTO).collect(Collectors.toList());
    }

    public static RestaurantBookingDTO mapRestaurantBookingEntityToRestaurantBookingDTO(RestaurantBooking restaurantBooking) {
        RestaurantBookingDTO restaurantBookingDTO = new RestaurantBookingDTO();
        restaurantBookingDTO.setId(restaurantBooking.getId());
        restaurantBookingDTO.setDayCheckIn(restaurantBooking.getDayCheckIn());
        restaurantBookingDTO.setNumOfAdults(restaurantBooking.getNumOfAdults());
        restaurantBookingDTO.setNumOfChildren(restaurantBooking.getNumOfChildren());
        restaurantBookingDTO.setName(restaurantBooking.getName());
        restaurantBookingDTO.setPhone(restaurantBooking.getPhone());
        restaurantBookingDTO.setTotalPrice(restaurantBooking.getTotalPrice());
        restaurantBookingDTO.setUserEmail(restaurantBooking.getUser().getEmail());
        restaurantBookingDTO.setUser(Utils.mapUserEntityToUserDTO(restaurantBooking.getUser()));
        restaurantBookingDTO.setRestaurant(Utils.mapRestaurantEntityToRestaurantDTO(restaurantBooking.getRestaurant()));
        restaurantBookingDTO.setBranchId(restaurantBooking.getRestaurant().getBranch().getId()); // Added branchId
        return restaurantBookingDTO;
    }

    public static List<RestaurantBookingDTO> mapRestaurantBookingListEntityToRestaurantBookingListDTO(List<RestaurantBooking> restaurantBookingList) {
        return restaurantBookingList.stream().map(Utils::mapRestaurantBookingEntityToRestaurantBookingDTO).collect(Collectors.toList());
    }


    // review
    public static ReviewDTO mapReviewEntityToReviewDTO(Review review, String userEmail) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setReviewId(review.getReviewId());
        reviewDTO.setRating(review.getRating());
        reviewDTO.setReviewText(review.getReviewText());
        reviewDTO.setReviewImageURL(review.getReviewImageURL());
        reviewDTO.setCreatedAt(review.getCreatedAt());

        if (review.getBranch() != null) {
            reviewDTO.setBranchId(review.getBranch().getId());
            reviewDTO.setBranchName(review.getBranch().getBranchName());
        }

        if (review.getRoom() != null) {
            reviewDTO.setRoomId(review.getRoom().getId());
            reviewDTO.setRoomType(review.getRoom().getRoomType());
        }

        reviewDTO.setUserEmail(userEmail); // Set the user's email

        return reviewDTO;
    }
}

