package com.fptaptech.s4.repository;

import com.fptaptech.s4.entity.Spa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpaRepository extends JpaRepository<Spa, Long> {
    @Query("SELECT DISTINCT s.spaServiceName FROM Spa s")
    List<String> findSpaServiceNames();

    Optional<Spa> findBySpaServiceName(String spaServiceName);

    @Query("SELECT s FROM Spa s WHERE s.branch.id = :branchId")
    List<Spa> findByBranchId(@Param("branchId") Long branchId);

    @Query("SELECT s FROM Spa s WHERE s.id = :spaId AND s.branch.id = :branchId")
    Optional<Spa> findByIdAndBranchId(@Param("spaId") Long spaId, @Param("branchId") Long branchId);
}
