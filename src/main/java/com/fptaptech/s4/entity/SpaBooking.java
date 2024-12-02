package com.fptaptech.s4.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "spa_bookings")
public class SpaBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Appointment time is required")
    private LocalDateTime appointmentTime; // Combination of date and time

    private String spaServiceTime; // Duration in minutes (e.g., "45", "60")
    private String phone;
    private int numberOfPeople;
    private String fullName;
    private String description;

    private String spaServiceName;
    private BigDecimal spaServicePrice;
    private String spaPhotoUrl;
    private String spaDescription;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spa_id")
    private Spa spa;

    @Override
    public String toString() {
        return "SpaBooking{" +
                "id=" + id +
                ", appointmentTime=" + appointmentTime +
                ", spaServiceTime='" + spaServiceTime + '\'' +
                ", phone='" + phone + '\'' +
                ", numberOfPeople=" + numberOfPeople +
                ", fullName='" + fullName + '\'' +
                ", description='" + description + '\'' +
                ", spaServiceName='" + spaServiceName + '\'' +
                ", spaServicePrice=" + spaServicePrice +
                ", spaPhotoUrl='" + spaPhotoUrl + '\'' +
                ", spaDescription='" + spaDescription + '\'' +
                ", user=" + user +
                ", spa=" + spa +
                '}';
    }
}
