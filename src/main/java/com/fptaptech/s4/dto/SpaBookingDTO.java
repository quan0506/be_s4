package com.fptaptech.s4.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpaBookingDTO {
    private Long id;
    private LocalDateTime appointmentTime;
    private String spaServiceTime;
    private String phone;
    private int numberOfPeople;
    private String fullName;
    private String description;


    private String userEmail;

    private UserDTO user;
    private SpaDTO spa;
}
