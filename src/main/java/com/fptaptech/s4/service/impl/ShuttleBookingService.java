package com.fptaptech.s4.service.impl;

<<<<<<< HEAD
import com.fptaptech.s4.dto.Response;
=======
import com.fptaptech.s4.response.Response;
>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
import com.fptaptech.s4.dto.ShuttleBookingDTO;
import com.fptaptech.s4.entity.Shuttle;
import com.fptaptech.s4.entity.ShuttleBooking;
import com.fptaptech.s4.entity.User;
import com.fptaptech.s4.exception.OurException;
import com.fptaptech.s4.repository.BranchRepository;
import com.fptaptech.s4.repository.ShuttleBookingRepository;
import com.fptaptech.s4.repository.ShuttleRepository;
import com.fptaptech.s4.repository.UserRepository;
<<<<<<< HEAD
import com.fptaptech.s4.service.IShuttleBookingService;
=======
import com.fptaptech.s4.service.interfaces.IShuttleBookingService;
>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
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
    private final BranchRepository branchRepository; // Assuming you have a BranchRepository

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
            response.setShuttleBooking(shuttleBookingDTO);
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
            response.setShuttleBooking(shuttleBookingDTO);
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
<<<<<<< HEAD
    public Response getAllShuttles(Long branchId) {
=======
    public Response getAllShuttleBookings(Long branchId) {
>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
        Response response = new Response();
        try {
            List<ShuttleBooking> shuttleBookingList = shuttleBookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                    .filter(booking -> booking.getShuttle().getBranch().getId().equals(branchId))
                    .collect(Collectors.toList());
            List<ShuttleBookingDTO> shuttleBookingDTOList = Utils.mapShuttleBookingListEntityToShuttleBookingListDTO(shuttleBookingList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setShuttleBookingList(shuttleBookingDTOList);
<<<<<<< HEAD
            List<String> userEmails = shuttleBookingList.stream() .map(booking -> booking.getUser().getEmail()) .distinct() .collect(Collectors.toList()); response.setEmail(String.join(", ", userEmails));
=======
>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
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

<<<<<<< HEAD
    @Override public Response getAllShuttleBookingsByUser(Long userId) {
        Response response = new Response();
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));
            List<ShuttleBooking> shuttleBookingList = shuttleBookingRepository.findByUser(user);
            List<ShuttleBookingDTO> shuttleBookingDTOList = Utils.mapShuttleBookingListEntityToShuttleBookingListDTO(shuttleBookingList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setShuttleBookingList(shuttleBookingDTOList);
            response.setEmail(user.getEmail());}
        catch (OurException e) { response.setStatusCode(404);
            response.setMessage(e.getMessage()); } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching shuttle bookings: " + e.getMessage()); }
        return response;
    }

=======
>>>>>>> 6b3f6db58591a116e0c4b625467d8b7ff67d55f1
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






