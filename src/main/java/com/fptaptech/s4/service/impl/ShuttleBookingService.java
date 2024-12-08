package com.fptaptech.s4.service.impl;


import com.fptaptech.s4.dto.ShuttleBookingDTO;
import com.fptaptech.s4.entity.Shuttle;
import com.fptaptech.s4.entity.ShuttleBooking;
import com.fptaptech.s4.entity.User;
import com.fptaptech.s4.exception.OurException;
import com.fptaptech.s4.repository.BranchRepository;
import com.fptaptech.s4.repository.ShuttleBookingRepository;
import com.fptaptech.s4.repository.ShuttleRepository;
import com.fptaptech.s4.repository.UserRepository;
import com.fptaptech.s4.response.Response;
import com.fptaptech.s4.service.interfaces.IShuttleBookingService;
import com.fptaptech.s4.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShuttleBookingService implements IShuttleBookingService {
    private final ShuttleBookingRepository shuttleBookingRepository;
    private final ShuttleRepository shuttleRepository;
    private final UserRepository userRepository;

    @Override
    public Response saveShuttleBooking(Long branchId, Long shuttleId, Long userId, ShuttleBookingDTO shuttleBookingRequest)
    {
        Response response = new Response();
        try {
            Shuttle shuttle = shuttleRepository.findByIdAndBranchId(shuttleId, branchId).orElseThrow(() -> new OurException("Shuttle Not Found"));
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));

            List<ShuttleBooking> existingBookings = shuttle.getShuttleBookings();

            if (!shuttleIsAvailable(shuttleBookingRequest, existingBookings)) {
                throw new OurException("Shuttle not available for selected date range");
            }

            // Calculate the total price
            long totalBookDays = ChronoUnit.DAYS.between(shuttleBookingRequest.getShuttleCheckInDate(), shuttleBookingRequest.getShuttleCheckOutDate());
            BigDecimal totalPrice = shuttle.getCarPrice().multiply(BigDecimal.valueOf(totalBookDays));

            ShuttleBooking shuttleBooking = new ShuttleBooking();
            shuttleBooking.setShuttle(shuttle);
            shuttleBooking.setUser(user);
            shuttleBooking.setShuttleCheckInDate(shuttleBookingRequest.getShuttleCheckInDate());
            shuttleBooking.setShuttleCheckOutDate(shuttleBookingRequest.getShuttleCheckOutDate());
            shuttleBooking.setTotalPrice(totalPrice);
            String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
            shuttleBooking.setBookingConfirmationCode(bookingConfirmationCode);
            shuttleBookingRepository.save(shuttleBooking);

            ShuttleBookingDTO shuttleBookingDTO = Utils.mapShuttleBookingEntityToShuttleBookingDTOPlusShuttle(shuttleBooking);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setBookingConfirmationCode(bookingConfirmationCode);
            response.setData(shuttleBookingDTO);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving shuttle booking: " + e.getMessage());
        }
        return response;
    }



    // Other service methods remain unchanged



    @Override
    public Response findBookingByConfirmationCode(Long branchId, String confirmationCode) {
        Response response = new Response();
        try {
            ShuttleBooking shuttleBooking = shuttleBookingRepository.findByBookingConfirmationCode(confirmationCode)
                    .orElseThrow(() -> new OurException("Booking Not Found"));
            if (!shuttleBooking.getShuttle().getBranch().getId().equals(branchId)) {
                throw new OurException("Booking not found in the specified branch");
            }
            ShuttleBookingDTO shuttleBookingDTO = Utils.mapShuttleBookingEntityToShuttleBookingDTOPlusShuttle(shuttleBooking);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setData(shuttleBookingDTO);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Fetching Shuttle Booking: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllShuttles(Long branchId) {
        Response response = new Response();
        try {
            List<ShuttleBooking> shuttleBookingList = shuttleBookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                    .filter(booking -> booking.getShuttle().getBranch().getId().equals(branchId))
                    .collect(Collectors.toList());
            List<ShuttleBookingDTO> shuttleBookingDTOList = Utils.mapShuttleBookingListEntityToShuttleBookingListDTO(shuttleBookingList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setData(shuttleBookingDTOList);
            List<String> userEmails = shuttleBookingList.stream() .map(booking -> booking.getUser().getEmail()) .distinct() .collect(Collectors.toList()); response.setEmail(String.join(", ", userEmails));
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching shuttle bookings: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response cancelShuttleBooking(Long branchId, Long shuttleBookingId) {
        Response response = new Response();
        try {
            ShuttleBooking shuttleBooking = shuttleBookingRepository.findById(shuttleBookingId).orElseThrow(() -> new OurException("Booking Not Found"));
            if (!shuttleBooking.getShuttle().getBranch().getId().equals(branchId)) {
                throw new OurException("Booking not found in the specified branch");
            }
            shuttleBookingRepository.deleteById(shuttleBookingId);
            response.setStatusCode(200);
            response.setMessage("successful");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error canceling shuttle booking: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllShuttleBookingsByUser(Long userId) {
        Response response = new Response();
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));
            List<ShuttleBooking> shuttleBookingList = shuttleBookingRepository.findByUser(user);

            if (shuttleBookingList.isEmpty()) {
                response.setStatusCode(404);
                response.setMessage("No shuttle bookings found for the specified user.");
                return response;
            }

            List<ShuttleBookingDTO> shuttleBookingDTOList = shuttleBookingList.stream()
                    .map(Utils::mapShuttleBookingEntityToShuttleBookingDTOPlusShuttle)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage("User shuttle bookings retrieved successfully.");
            response.setShuttleBookingList(shuttleBookingDTOList);
            response.setEmail(user.getEmail());
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching shuttle bookings: " + e.getMessage());
        }
        return response;
    }


    @Override
    public String getBookingEmail(Long branchId, Long shuttleBookingId) {
        ShuttleBooking booking = shuttleBookingRepository.findById(shuttleBookingId).orElse(null);
        return (booking != null && booking.getShuttle().getBranch().getId().equals(branchId)) ? booking.getUser().getEmail() : null;
    }

    private boolean shuttleIsAvailable(ShuttleBookingDTO shuttleBookingRequest, List<ShuttleBooking> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        shuttleBookingRequest.getShuttleCheckInDate().isEqual(existingBooking.getShuttleCheckInDate())
                                || shuttleBookingRequest.getShuttleCheckOutDate().isBefore(existingBooking.getShuttleCheckOutDate())
                                || (shuttleBookingRequest.getShuttleCheckInDate().isAfter(existingBooking.getShuttleCheckInDate())
                                && shuttleBookingRequest.getShuttleCheckInDate().isBefore(existingBooking.getShuttleCheckOutDate()))
                                || (shuttleBookingRequest.getShuttleCheckInDate().isBefore(existingBooking.getShuttleCheckInDate())
                                && shuttleBookingRequest.getShuttleCheckOutDate().isEqual(existingBooking.getShuttleCheckOutDate()))
                                || (shuttleBookingRequest.getShuttleCheckInDate().isBefore(existingBooking.getShuttleCheckInDate())
                                && shuttleBookingRequest.getShuttleCheckOutDate().isAfter(existingBooking.getShuttleCheckOutDate()))
                                || (shuttleBookingRequest.getShuttleCheckInDate().isEqual(existingBooking.getShuttleCheckOutDate())
                                && shuttleBookingRequest.getShuttleCheckOutDate().isEqual(existingBooking.getShuttleCheckInDate()))
                                || (shuttleBookingRequest.getShuttleCheckInDate().isEqual(existingBooking.getShuttleCheckOutDate())
                                && shuttleBookingRequest.getShuttleCheckOutDate().isEqual(shuttleBookingRequest.getShuttleCheckInDate()))
                );
    }

}
