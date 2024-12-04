package com.fptaptech.s4.repository;


import com.fptaptech.s4.entity.Branch;
import com.fptaptech.s4.entity.Review;
import com.fptaptech.s4.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByBranchAndRoomIsNull(Branch branch);
    List<Review> findByRoom(Room room);
}
