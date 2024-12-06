package com.fptaptech.s4.service.interfaces;

import com.fptaptech.s4.response.Response;
import com.fptaptech.s4.dto.SpaBookingDTO;

public interface ISpaBookingService {
    Response saveSpaBooking(Long branchId, Long spaId, Long userId, SpaBookingDTO spaBookingRequest);
    Response findSpaBookingById(Long branchId,Long spaBookingId);
    Response getAllSpaBookings(Long branchId);

    String getBookingEmail(Long branchId, Long spaBookingId);

    Response cancelSpaBooking(Long branchId, Long spaBookingId);

    Response getAllSpaBookingsByUser(Long userId);
}
