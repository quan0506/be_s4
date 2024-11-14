package com.fptaptech.s4.repository;

import com.fptaptech.s4.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Optional<Hotel> findById(Long id);
}
