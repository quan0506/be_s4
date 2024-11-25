package com.fptaptech.s4.service;

import com.fptaptech.s4.dto.Response;
import com.fptaptech.s4.dto.ShuttleBookingDTO;
import com.fptaptech.s4.entity.ShuttleBooking;

public interface IShuttleBookingService {
    Response saveShuttleBooking(Long branchId, Long shuttleId, Long userId, ShuttleBookingDTO shuttleBookingRequest);

    Response findBookingByConfirmationCode(Long branchId, String confirmationCode);

    Response getAllShuttleBookings(Long branchId);

    Response cancelShuttleBooking(Long branchId, Long shuttleBookingId);

    String getBookingEmail(Long branchId, Long shuttleBookingId);
}
