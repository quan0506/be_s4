package com.fptaptech.s4.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fptaptech.s4.dto.*;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private int statusCode;
    private String message;
    private Object data;

    private String token;
    private String role;
    private String expirationTime;
    private String bookingConfirmationCode;
    private String Email;

    private Long branchId;
    private UserDTO user;
    private List<UserDTO> userList;


//    // Shuttle Response
//    private ShuttleDTO shuttle;
//    private ShuttleBookingDTO shuttleBooking;
//    private List<ShuttleDTO> shuttleList;
//    private List<ShuttleBookingDTO> shuttleBookingList;
//
//    // Spa Response
//    private SpaDTO spa;
//    private SpaBookingDTO spaBooking;
//    private List<SpaDTO> spaList;
//    private List<SpaBookingDTO> spaBookingList;
//
//    // Restaurant Response
//    private RestaurantDTO restaurant;
//    private RestaurantBookingDTO restaurantBooking;
//    private List<RestaurantDTO> restaurantList;
//    private List<RestaurantBookingDTO> restaurantBookingList;

}
