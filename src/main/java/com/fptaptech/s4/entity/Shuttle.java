package com.fptaptech.s4.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "shuttles")
public class Shuttle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String carType;

    private BigDecimal carPrice;

    @ElementCollection
    @CollectionTable(name = "shuttle_photos", joinColumns = @JoinColumn(name = "shuttle_id"))
    @Column(name = "photo_url")
    private List<String> photos;

    @Column(columnDefinition = "TEXT")
    private String carDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @OneToMany(mappedBy = "shuttle", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ShuttleBooking> shuttleBookings = new ArrayList<>();

    @Override
    public String toString() {
        return "Shuttle{" +
                "id=" + id +
                ", carType='" + carType + '\'' +
                ", carPrice=" + carPrice +
                ", photos=" + photos +
                ", carDescription='" + carDescription + '\'' +
                '}';
    }
}
