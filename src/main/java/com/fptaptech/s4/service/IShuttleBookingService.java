package com.fptaptech.s4.service;

import com.fptaptech.s4.dto.Response;
import com.fptaptech.s4.dto.ShuttleBookingDTO;

public interface IShuttleBookingService {
    Response saveShuttleBooking(Long branchId, Long shuttleId, Long userId, ShuttleBookingDTO shuttleBookingRequest);

    Response findBookingByConfirmationCode(Long branchId, String confirmationCode);

    Response getAllShuttles(Long branchId);

    Response cancelShuttleBooking(Long branchId, Long shuttleBookingId);

    Response getAllShuttleBookingsByUser(Long userId);
    String getBookingEmail(Long branchId, Long shuttleBookingId);


}
