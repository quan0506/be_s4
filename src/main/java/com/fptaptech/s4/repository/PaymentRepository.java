package com.fptaptech.s4.repository;


import com.fptaptech.s4.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByTransactionCode(String transactionCode);
    @Query("SELECT p FROM Payment p WHERE YEAR(p.paymentDate) = :year")
    List<Payment> findAllByYear(@Param("year") int year);
}
