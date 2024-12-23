package com.fptaptech.s4.repository;

import com.fptaptech.s4.entity.ServicePayment;
import org.springframework.data.jpa.repository.JpaRepository;
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
}

