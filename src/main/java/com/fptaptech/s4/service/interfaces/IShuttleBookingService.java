package com.fptaptech.s4.service.interfaces;

import com.fptaptech.s4.dto.Response;
import com.fptaptech.s4.dto.ShuttleBookingDTO;

import java.math.BigDecimal;
import java.util.Map;

public interface IShuttleBookingService {
    Response saveShuttleBooking(Long branchId, Long shuttleId, Long userId, ShuttleBookingDTO shuttleBookingRequest);

    Response findBookingByConfirmationCode(Long branchId, String confirmationCode);

    Response getAllShuttles(Long branchId);

    Response cancelShuttleBooking(Long branchId, Long shuttleBookingId);

    Response getAllShuttleBookingsByUser(Long userId);

    String getBookingEmail(Long branchId, Long shuttleBookingId);

    Response getAllShuttlesGroupedByBranch();

    BigDecimal calculateTotalPriceForYear(int year);

    Map<String, BigDecimal> calculateTotalPriceForEachMonth(int year);
}
