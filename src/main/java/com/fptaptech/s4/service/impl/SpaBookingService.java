package com.fptaptech.s4.service.impl;



//import com.fptaptech.s4.dto.Response;
import com.fptaptech.s4.response.Response;
import com.fptaptech.s4.dto.SpaBookingDTO;
import com.fptaptech.s4.entity.Branch;
import com.fptaptech.s4.entity.Spa;
import com.fptaptech.s4.entity.SpaBooking;
import com.fptaptech.s4.entity.User;
import com.fptaptech.s4.exception.OurException;
import com.fptaptech.s4.repository.BranchRepository;
import com.fptaptech.s4.repository.SpaBookingRepository;
import com.fptaptech.s4.repository.SpaRepository;
import com.fptaptech.s4.repository.UserRepository;

import com.fptaptech.s4.service.interfaces.ISpaBookingService;
import com.fptaptech.s4.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SpaBookingService implements ISpaBookingService {
    private final SpaBookingRepository spaBookingRepository;
    private final SpaRepository spaRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;

    @Override
    public Response saveSpaBooking(Long branchId, Long spaId, Long userId, SpaBookingDTO spaBookingRequest) {
        Response response = new Response();
        try {
            Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new OurException("Branch Not Found"));
            Spa spa = spaRepository.findById(spaId).orElseThrow(() -> new OurException("Spa Not Found"));
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));

            SpaBooking spaBooking = new SpaBooking();
            spaBooking.setSpa(spa);
            spaBooking.setUser(user);
            spaBooking.setAppointmentTime(spaBookingRequest.getAppointmentTime());
            spaBooking.setSpaServiceTime(spaBookingRequest.getSpaServiceTime());
            spaBooking.setPhone(spaBookingRequest.getPhone());
            spaBooking.setNumberOfPeople(spaBookingRequest.getNumberOfPeople());
            spaBooking.setFullName(spaBookingRequest.getFullName());
            spaBooking.setDescription(spaBookingRequest.getDescription());

            spaBookingRepository.save(spaBooking);
            response.setStatusCode(200);
            response.setMessage("successful");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving spa booking: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllSpaBookingsByUser(Long userId) {
        Response response = new Response();
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));
            List<SpaBooking> spaBookingList = spaBookingRepository.findByUserIdOrderByAppointmentTimeAsc(userId);

            if (spaBookingList.isEmpty()) {
                response.setStatusCode(404);
                response.setMessage("No spa bookings found for the specified user.");
                return response;
            }

            List<SpaBookingDTO> spaBookingDTOList = spaBookingList.stream()
                    .map(Utils::mapSpaBookingEntityToSpaBookingDTO)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage("User spa bookings retrieved successfully.");
            response.setSpaBookingList(spaBookingDTOList);
            response.setEmail(user.getEmail());
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching spa bookings: " + e.getMessage());
        }
        return response;
    }



    @Override
    public Response getAllSpaBookings(Long branchId) {
        Response response = new Response();
        try {
            List<SpaBooking> spaBookingList = spaBookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                    .filter(booking -> booking.getSpa().getBranch().getId().equals(branchId))
                    .collect(Collectors.toList());
            List<SpaBookingDTO> spaBookingDTOList = Utils.mapSpaBookingListEntityToSpaBookingListDTO(spaBookingList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setData(spaBookingDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching spa bookings: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response findSpaBookingById(Long branchId, Long spaBookingId) {
        Response response = new Response();
        try {
            SpaBooking spaBooking = spaBookingRepository.findById(spaBookingId)
                    .orElseThrow(() -> new OurException("Booking Not Found"));
            SpaBookingDTO spaBookingDTO = Utils.mapSpaBookingEntityToSpaBookingDTO(spaBooking);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setData(spaBookingDTO);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching spa booking: " + e.getMessage());
        }
        return response;
    }

    @Override
    public String getBookingEmail(Long branchId, Long spaBookingId) {
        SpaBooking booking = spaBookingRepository.findById(spaBookingId).orElse(null);
        return booking != null ? booking.getUser().getEmail() : null;
    }

    @Override
    public Response cancelSpaBooking(Long branchId, Long spaBookingId) {
        Response response = new Response();
        try {
            SpaBooking spaBooking = spaBookingRepository.findById(spaBookingId)
                    .orElseThrow(() -> new OurException("Booking Not Found"));
            spaBookingRepository.deleteById(spaBookingId);
            response.setStatusCode(200);
            response.setMessage("Booking canceled successfully");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error canceling spa booking: " + e.getMessage());
        }
        return response;
    }


    @Override
    public Response getAllSpasGroupedByBranch() {
        Response response = new Response();
        try {
            List<SpaBooking> spaBookingList = spaBookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

            // Group by branch
            Map<Long, List<SpaBookingDTO>> groupedByBranch = spaBookingList.stream()
                    .collect(Collectors.groupingBy(
                            booking -> booking.getSpa().getBranch().getId(),
                            Collectors.mapping(booking -> Utils.mapSpaBookingEntityToSpaBookingDTO(booking), Collectors.toList())
                    ));

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setData(groupedByBranch);

            List<String> userEmails = spaBookingList.stream()
                    .map(booking -> booking.getUser().getEmail())
                    .distinct()
                    .collect(Collectors.toList());
            response.setEmail(String.join(", ", userEmails));
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching spa bookings: " + e.getMessage());
        }
        return response;
    }

}



