package com.fptaptech.s4.repository;

import com.fptaptech.s4.entity.ShuttleBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShuttleBookingRepository extends JpaRepository<ShuttleBooking, Long> {
    Optional<ShuttleBooking> findByBookingConfirmationCode(String confirmationCode);
}
