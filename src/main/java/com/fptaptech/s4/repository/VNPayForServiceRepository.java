package com.fptaptech.s4.repository;

import com.fptaptech.s4.entity.ServicePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VNPayForServiceRepository extends JpaRepository<ServicePayment, Long> {

    List<ServicePayment> findByShuttleBookingIsNotNull();

    List<ServicePayment> findByRestaurantBookingIsNotNull();

    List<ServicePayment> findBySpaBookingIsNotNull();

    Optional<ServicePayment> findById(Long id);

    List<ServicePayment> findAll();

    <S extends ServicePayment> S save(S entity);

    @Query("SELECT p FROM ServicePayment p WHERE YEAR(p.paymentDate) = :year AND (p.restaurantBooking IS NOT NULL OR p.shuttleBooking IS NOT NULL) AND (:bookingType IS NULL OR (:bookingType = 'restaurant' AND p.restaurantBooking IS NOT NULL) OR (:bookingType = 'shuttle' AND p.shuttleBooking IS NOT NULL))")
    List<ServicePayment> findAllByYearAndBookingType(@Param("year") int year, @Param("bookingType") String bookingType);

    @Query("SELECT p FROM ServicePayment p WHERE (p.restaurantBooking IS NOT NULL OR p.shuttleBooking IS NOT NULL) AND (:bookingType IS NULL OR (:bookingType = 'restaurant' AND p.restaurantBooking IS NOT NULL) OR (:bookingType = 'shuttle' AND p.shuttleBooking IS NOT NULL))")
    List<ServicePayment> findAllByBookingType(@Param("bookingType") String bookingType);
}
