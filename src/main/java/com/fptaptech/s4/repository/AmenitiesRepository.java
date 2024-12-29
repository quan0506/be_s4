package com.fptaptech.s4.repository;

import com.fptaptech.s4.entity.Amenities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface AmenitiesRepository extends JpaRepository<Amenities, Long> {

    @Query("SELECT a FROM Amenities a WHERE a.room.id = :roomId")
    List<Amenities> findAllByRoomId(@Param("roomId") Long roomId);
}
