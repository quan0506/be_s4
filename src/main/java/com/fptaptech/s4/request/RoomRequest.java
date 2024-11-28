package com.fptaptech.s4.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomRequest {
    private String photo;
    private String roomType;
    private BigDecimal roomPrice;
    private Long branchId;
    private String description;
}
