package com.fptaptech.s4.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RoomPaymentDTO {
    private Long bookingId;
    private Long userId;
    private Long paymentID;
    private String modeOfPayment;
    private String chooseMethod;
    private Long paymentMethodId;
    private BigDecimal amount;
    private String transactionCode;
    private LocalDateTime paymentDate;
    private String paymentStatus;
    private String currency;
    private String description;

}
