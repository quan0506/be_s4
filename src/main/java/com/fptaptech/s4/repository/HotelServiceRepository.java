package com.fptaptech.s4.repository;

import com.fptaptech.s4.entity.HotelServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotelServiceRepository extends JpaRepository<HotelServices, Long> {
    Optional<HotelServices> findById(Long id);
}