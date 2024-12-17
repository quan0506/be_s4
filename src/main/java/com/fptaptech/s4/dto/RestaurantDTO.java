package com.fptaptech.s4.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestaurantDTO {
    private Long id;
    private String restaurantType;
    private String time;
    private BigDecimal restaurantAdultPrice;
    private BigDecimal restaurantChildrenPrice;
    private List<String> photos;
    private String restaurantDescription;

    private Long branchId;
    private String branchName;
    private String branchAddress;
}
