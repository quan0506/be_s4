package com.fptaptech.s4.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestaurantDTO {
    private Long id;
    private String restaurantType;
    private String time;
    private BigDecimal restaurantAdultPrice;
    private BigDecimal restaurantChildrenPrice;
    private String restaurantPhotoUrl;
    private String restaurantDescription;
    private Long branchId; // Added branch connection
}
