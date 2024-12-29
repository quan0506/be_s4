package com.fptaptech.s4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomsDTO {
    private Long id;
    private String name;
    private String description;
    private String roomStatus;
    private LocalDateTime updatedAt;
}

