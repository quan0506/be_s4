package com.fptaptech.s4.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShuttleDTO {
    private Long id;
    private String carType;
    private BigDecimal carPrice;
    private String carPhotoUrl;
    private String carDescription;
    private Long branchId;
    private String branchName;
    private List<ShuttleBookingDTO> shuttleBookings;
}
