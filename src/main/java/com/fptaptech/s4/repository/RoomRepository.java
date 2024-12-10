package com.fptaptech.s4.repository;

import com.fptaptech.s4.entity.Booking;
import com.fptaptech.s4.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT DISTINCT r.roomType FROM Room r")
    List<String> findDistinctRoomTypes();

    /*@Query("SELECT r FROM Room r WHERE r.roomType LIKE %:roomType% AND r.id NOT IN (" +
            "SELECT br.room.id FROM BookedRoom br WHERE " +
            "(br.checkInDate <= :checkOutDate AND br.checkOutDate >= :checkInDate))")*/

    @Query("SELECT b FROM Booking b WHERE b.room.id = :roomId AND " + "(b.checkInDate < :checkOutDate AND b.checkOutDate > :checkInDate)")
        boolean isRoomAvailable(@Param("roomId") Long roomId,
                                @Param("checkInDate") LocalDate checkInDate,
                                @Param("checkOutDate") LocalDate checkOutDate);

    List<Room> findByBranch_Id(Long branchId);
    List<Room> findByRoomTypeAndBranch_Id(String roomType, Long branchId);
    List<Room> findByRoomPriceAndBranch_Id(BigDecimal roomPrice, Long branchId);


        @Query("SELECT b FROM Booking b WHERE b.room.id = :roomId AND " +
                "(b.checkInDate < :checkOutDate AND b.checkOutDate > :checkInDate)")
        List<Booking> findBookingsByRoomIdAndDateRange(@Param("roomId") Long roomId,
                                                       @Param("checkInDate") LocalDate checkInDate,
                                                       @Param("checkOutDate") LocalDate checkOutDate);


}


