package com.fptaptech.s4.repository;

import com.fptaptech.s4.entity.Branch;
import com.fptaptech.s4.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface BranchRepository extends JpaRepository<Branch, Long> {
    List<Branch> findByHotel(Hotel hotel);
}
