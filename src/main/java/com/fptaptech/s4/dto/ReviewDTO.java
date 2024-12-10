package com.fptaptech.s4.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;


@Data
public class ReviewDTO {
    private Integer reviewId;
    private Integer rating;
    private String reviewText;
    private List<String> photos;
    private LocalDateTime createdAt;
    private Long branchId;
    private String branchName;
    private Long roomId;
    private String roomType;
    private String userEmail;
}
