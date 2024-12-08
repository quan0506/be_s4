package com.fptaptech.s4.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "hotel")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "HotelName", nullable = false, columnDefinition = "TEXT")
    private String hotelName;

    @Column(name = "StarRating", nullable = false, length = 200)
    private String starRating;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    /*@Column(name = "City", nullable = false, length = 100)
    private String city;*/
}
