package com.fptaptech.s4.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpaDTO {
    private Long id;
    private String spaServiceName;
    private Long branchId;
    private List<SpaBookingDTO> spaBookings;
}