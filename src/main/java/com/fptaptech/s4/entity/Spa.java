package com.fptaptech.s4.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "spas")
public class Spa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String spaServiceName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @OneToMany(mappedBy = "spa", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SpaBooking> spaBookings = new ArrayList<>();

    @Override
    public String toString() {
        return "Spa{" +
                "id=" + id +
                ", spaServiceName='" + spaServiceName + '\'' +
                ", branch=" + branch +
                '}';
    }
}
