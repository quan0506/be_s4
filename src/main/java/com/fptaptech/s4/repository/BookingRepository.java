package com.fptaptech.s4.repository;

import com.fptaptech.s4.entity.BookedRoom;
import com.fptaptech.s4.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser_Id(Long userId);
    List<Booking> findByRoomIdAndStatus(Long roomId, String status);
}
