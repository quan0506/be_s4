package com.fptaptech.s4.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Data
@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String restaurantType;
    private String time;
    @Column(nullable = false)
    private BigDecimal restaurantAdultPrice;
    @Column(nullable = false)
    private BigDecimal restaurantChildrenPrice;

    @ElementCollection
    @CollectionTable(name = "restaurant_photos", joinColumns = @JoinColumn(name = "restaurant_id"))
    @Column(name = "photo_url")
    private List<String> photos;

    @Column(columnDefinition = "TEXT")
    private String restaurantDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RestaurantBooking> restaurantBookings = new ArrayList<>();

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", restaurantType='" + restaurantType + '\'' +
                ", time='" + time + '\'' +
                ", restaurantAdultPrice=" + restaurantAdultPrice +
                ", restaurantChildrenPrice=" + restaurantChildrenPrice +
                ", photos=" + photos +
                ", restaurantDescription='" + restaurantDescription + '\'' +
                '}';
    }
}
