package com.fptaptech.s4.repository;

import com.fptaptech.s4.entity.Restaurant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findAllByBranchId(Long branchId);

    @Query("SELECT s FROM Restaurant s WHERE s.id = :restaurantId AND s.branch.id = :branchId")
    Optional<Restaurant> findByIdAndBranchId(@Param("restaurantId") Long restaurantId, @Param("branchId") Long branchId);
}
