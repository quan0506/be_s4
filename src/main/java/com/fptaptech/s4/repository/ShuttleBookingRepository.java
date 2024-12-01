package com.fptaptech.s4.repository;

import com.fptaptech.s4.entity.ShuttleBooking;
<<<<<<< HEAD
import com.fptaptech.s4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
=======
import org.springframework.data.jpa.repository.JpaRepository;

>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
import java.util.Optional;

public interface ShuttleBookingRepository extends JpaRepository<ShuttleBooking, Long> {
    Optional<ShuttleBooking> findByBookingConfirmationCode(String confirmationCode);
<<<<<<< HEAD
    List<ShuttleBooking> findByUser(User user);
=======
>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
}
