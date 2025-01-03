package com.fptaptech.s4.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShuttleBookingDTO {
    private Long id;
    private LocalDate shuttleCheckInDate;
    private LocalDate shuttleCheckOutDate;
    private String phone;
    private String bookingConfirmationCode;
    private BigDecimal totalPrice;

//    private Long carId;
//    private String carType;
//    private BigDecimal carPrice;
//    private List<String> photos;
//    private String carDescription;


    private String userEmail;
    private UserDTO user;
    private ShuttleDTO shuttle;
//    private Long branchId;
//    private String branchName;
}