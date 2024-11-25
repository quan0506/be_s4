package com.fptaptech.s4.repository;

import com.fptaptech.s4.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findAllByBranchId(Long branchId);
}
