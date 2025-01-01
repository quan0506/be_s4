package com.fptaptech.s4.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequestDTO {
    private Long id;
    private Long roomId;
    private String requestType;
    private String description;
    private LocalDateTime createdAt;
}
