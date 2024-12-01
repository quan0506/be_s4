package com.fptaptech.s4.response;

import com.fptaptech.s4.entity.HotelServices;
import com.fptaptech.s4.entity.Room;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookedResponse {
    private  Long bookingId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private String guestFullName;
    private String guestEmail;

    private int NumOfAdults;

    private int NumOfChildren;

    private int totalNumOfGuest;

    private String bookingConfirmationCode;

    private Room room;

    private List<HotelServices> services; // Dịch vụ bổ sung
}
