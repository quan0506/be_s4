package com.fptaptech.s4.repository;

import com.fptaptech.s4.entity.ShuttleBooking;

import com.fptaptech.s4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


import java.util.Optional;

public interface ShuttleBookingRepository extends JpaRepository<ShuttleBooking, Long> {
    Optional<ShuttleBooking> findByBookingConfirmationCode(String confirmationCode);
    List<ShuttleBooking> findByUser(User user);

    @Query("SELECT b FROM ShuttleBooking b WHERE YEAR(b.shuttleCheckInDate) = :year")
    List<ShuttleBooking> findAllByYear(@Param("year") int year);
}
