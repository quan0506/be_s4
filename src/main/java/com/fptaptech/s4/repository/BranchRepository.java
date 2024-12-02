package com.fptaptech.s4.repository;

import com.fptaptech.s4.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository

public interface BranchRepository extends JpaRepository<Branch, Long> {

}
