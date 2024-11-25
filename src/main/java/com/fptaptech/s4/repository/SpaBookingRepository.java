package com.fptaptech.s4.repository;

import com.fptaptech.s4.entity.SpaBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpaBookingRepository extends JpaRepository<SpaBooking, Long> {
    Optional<SpaBooking> findById(Long id);

    @Query("SELECT b FROM SpaBooking b ORDER BY b.appointmentTime ASC")
    List<SpaBooking> findAllOrderByAppointmentTime();

    @Query("SELECT b FROM SpaBooking b WHERE b.user.id = :userId ORDER BY b.appointmentTime ASC")
    List<SpaBooking> findByUserIdOrderByAppointmentTimeAsc(@Param("userId") Long userId);

    @Query("SELECT b FROM SpaBooking b WHERE b.spa.id = :spaId ORDER BY b.appointmentTime ASC")
    List<SpaBooking> findBySpaIdOrderByAppointmentTimeAsc(@Param("spaId") Long spaId);

    @Query("SELECT b FROM SpaBooking b WHERE b.spa.branch.id = :branchId ORDER BY b.appointmentTime ASC")
    List<SpaBooking> findByBranchIdOrderByAppointmentTimeAsc(@Param("branchId") Long branchId);
}
