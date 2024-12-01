package com.fptaptech.s4.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShuttleBookingDTO {
    private Long id;
    private LocalDate shuttleCheckInDate;
    private LocalDate shuttleCheckOutDate;
    private String bookingConfirmationCode;
    private BigDecimal totalPrice;
<<<<<<< HEAD
    private String userEmail;
=======
>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
    private UserDTO user;
    private ShuttleDTO shuttle;
    private Long branchId;
}
