package com.fptaptech.s4.repository;


import com.fptaptech.s4.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    // add method search if need
}
