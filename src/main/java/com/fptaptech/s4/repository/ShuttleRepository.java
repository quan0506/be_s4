package com.fptaptech.s4.repository;

import com.fptaptech.s4.entity.Shuttle;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ShuttleRepository extends JpaRepository<Shuttle, Long> {
    @Query("SELECT DISTINCT s.carType FROM Shuttle s WHERE s.branch.id = :branchId")
    List<String> findDistinctCarTypes(@Param("branchId") Long branchId);

    @Query("SELECT s FROM Shuttle s WHERE s.branch.id = :branchId AND s.carType LIKE %:carType% AND s.id NOT IN (SELECT sb.shuttle.id FROM ShuttleBooking sb WHERE (sb.shuttleCheckInDate <= :shuttleCheckOutDate AND sb.shuttleCheckOutDate >= :shuttleCheckInDate))")
    List<Shuttle> findAvailableCarsByDatesAndTypes(@Param("branchId") Long branchId, @Param("shuttleCheckInDate") LocalDate shuttleCheckInDate, @Param("shuttleCheckOutDate") LocalDate shuttleCheckOutDate, @Param("carType") String carType);

    @Query("SELECT s FROM Shuttle s WHERE s.branch.id = :branchId")
    List<Shuttle> findByBranchId(@Param("branchId") Long branchId, Sort sort);

    @Query("SELECT s FROM Shuttle s WHERE s.branch.id = :branchId AND s.id NOT IN (SELECT sb.shuttle.id FROM ShuttleBooking sb)")
    List<Shuttle> getAllAvailableCars(@Param("branchId") Long branchId);

    @Query("SELECT s FROM Shuttle s WHERE s.id = :shuttleId AND s.branch.id = :branchId")
    Optional<Shuttle> findByIdAndBranchId(@Param("shuttleId") Long shuttleId, @Param("branchId") Long branchId);
}
