package com.fptaptech.s4.repository;

import com.fptaptech.s4.entity.RestaurantBooking;
import com.fptaptech.s4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RestaurantBookingRepository extends JpaRepository<RestaurantBooking, Long> {

    @Query("SELECT rb FROM RestaurantBooking rb WHERE rb.restaurant.branch.id = :branchId ORDER BY rb.dayCheckIn ASC")
    List<RestaurantBooking> findAllBookingsByBranchIdSortedByCheckIn(@Param("branchId") Long branchId);

    @Query("SELECT rb FROM RestaurantBooking rb WHERE rb.user.id = :userId ORDER BY rb.dayCheckIn ASC")
    List<RestaurantBooking> findByUserIdOrderByDayCheckInAsc(@Param("userId") Long userId);

    List<RestaurantBooking> findByUser(User user);
}
