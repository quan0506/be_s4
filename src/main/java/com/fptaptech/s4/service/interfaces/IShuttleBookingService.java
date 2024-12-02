package com.fptaptech.s4.service.interfaces;

import com.fptaptech.s4.response.Response;
import com.fptaptech.s4.dto.ShuttleBookingDTO;

public interface IShuttleBookingService {
    Response saveShuttleBooking(Long branchId, Long shuttleId, Long userId, ShuttleBookingDTO shuttleBookingRequest);

    Response findBookingByConfirmationCode(Long branchId, String confirmationCode);


    Response cancelShuttleBooking(Long branchId, Long shuttleBookingId);

    String getBookingEmail(Long branchId, Long shuttleBookingId);

    Response getAllShuttleBookings(Long branchId);

    Response getAllShuttleBookingsByUser(Long userId);

    Response getAllShuttles(Long branchId);
}
