package com.fptaptech.s4.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "hotel")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "HotelName", nullable = false, length = 100)
    private String hotelName;

    @Column(name = "Address", nullable = false, length = 200)
    private String address;

    @Column(name = "City", nullable = false, length = 100)
    private String city;
}
