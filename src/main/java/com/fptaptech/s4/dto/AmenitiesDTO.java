package com.fptaptech.s4.dto;

import lombok.Data;


import java.util.List;

@Data
public class AmenitiesDTO {
    private Long id;
    private String name;
    private List<String> photos;
    private String description;
    private Long roomId;
    private String roomType;
}
