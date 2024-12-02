package com.fptaptech.s4.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewId;

//    @ManyToOne
//    @JoinColumn(name = "customerId", nullable = false)
//    private Customer customer;

//    @ManyToOne
//    @JoinColumn(name = "serviceId", nullable = false)
//    private Service service;

    @Column(nullable = false)
    private Integer rating;

    @Column(length = 500)
    private String reviewText;

    @Column(columnDefinition = "LONGBLOB")
    private byte[] reviewImageURL;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();


}
