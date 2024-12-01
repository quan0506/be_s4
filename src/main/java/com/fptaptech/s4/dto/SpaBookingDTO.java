package com.fptaptech.s4.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpaBookingDTO {
    private Long id;
    private LocalDateTime appointmentTime; // Combination of date and time
    private String spaServiceTime; // Duration in minutes (e.g., "45", "60")
    private String spaServiceName;
    private String phone;
    private int numberOfPeople;
    private String fullName;
    private String description;
<<<<<<< HEAD
    private String userEmail;
=======
>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
    private UserDTO user;
    private SpaDTO spa;
}
