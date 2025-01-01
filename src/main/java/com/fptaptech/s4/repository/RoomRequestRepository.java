package com.fptaptech.s4.repository;


import com.fptaptech.s4.request.RoomRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface RoomRequestRepository extends JpaRepository<RoomRequest, Long> {
    List<RoomRequest> findByRoomIdOrderByCreatedAtDesc(Long roomId);
}
