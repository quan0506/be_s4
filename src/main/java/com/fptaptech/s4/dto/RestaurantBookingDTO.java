package com.fptaptech.s4.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestaurantBookingDTO {
    private Long id;
    private LocalDate dayCheckIn;
    private int numOfAdults;
    private int numOfChildren;
    private String name;
    private String phone;
    private BigDecimal totalPrice;
    private UserDTO user;
    private String userEmail;
    private RestaurantDTO restaurant;
    private Long branchId;
}
