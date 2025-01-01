package com.fptaptech.s4.service.impl;

import com.fptaptech.s4.entity.Booking;
import com.fptaptech.s4.entity.Room;
import com.fptaptech.s4.entity.User;
import com.fptaptech.s4.exception.ResourceNotFoundException;
import com.fptaptech.s4.entity.BookedRoom;
import com.fptaptech.s4.exception.RoomNotAvailableException;
import com.fptaptech.s4.repository.BookingRepository;
import com.fptaptech.s4.repository.UserRepository;
import com.fptaptech.s4.response.BookingResponseDTO;
import com.fptaptech.s4.service.interfaces.IBookingService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;




@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {

    private final BookingRepository bookingRepository;
    private final RoomService roomService;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Override
    public BookingResponseDTO createBooking(Booking booking) {
        LocalDate now = LocalDate.now();

        // Chekc thời gian
        if(booking.getCheckInDate().isBefore(now) || booking.getCheckOutDate().isBefore(now)){
            throw new RoomNotAvailableException("Phòng không tồn tại ở ngày checkin này");
        }

        // Tìm kiếm Room với id
        Room room = roomService.getRoomById(booking.getRoom().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Phòng không tồn tại"));

        // Kiểm tra tình trạng phòng trước khi tạo booking
        if (!roomService.isRoomAvailable(room.getId(), booking.getCheckInDate(), booking.getCheckOutDate())) {
            throw new RoomNotAvailableException("Phòng đã được đặt hoặc đang trong thời gian sử dụng");
        }

        booking.setRoom(room);
        booking.setConfirmBookingCode(generateConfirmCode());
        booking.updateStatus();

        // Tìm kiếm người dùng với id
        var userId = booking.getUser().getId();
        var newUser = userRepository.findById(userId);

        booking.getUser().setFirstName(newUser.get().getFirstName());
        booking.getUser().setLastName(newUser.get().getLastName());
        booking.getUser().setEmail(newUser.get().getEmail());
        booking.getUser().setPhone(newUser.get().getPhone());

        // Tính tổng giá tiền
        BigDecimal totalPrice = calculateTotalPrice(booking);
        booking.setTotalPrice(totalPrice);

        Booking savedBooking = bookingRepository.save(booking);

        // Cập nhật trạng thái phòng sau khi tạo booking
        room.addBooking(savedBooking);
        room.setBooked(true);
        room.updateStatus();
        roomService.saveRoom(room);

        // Gửi email xác nhận
        String subject = "Xác nhận đặt phòng";
        Context context = new Context();
        context.setVariable("name", newUser.get().getFirstName());
        context.setVariable("confirmationCode", savedBooking.getConfirmBookingCode());
        context.setVariable("checkInDate", booking.getCheckInDate());
        context.setVariable("checkOutDate", booking.getCheckOutDate());
        context.setVariable("totalPrice", booking.getTotalPrice());
        context.setVariable("roomType", booking.getRoom().getRoomType());
        emailService.sendHtmlMessage(newUser.get().getEmail(), subject, "emailTemplate", context);

        return new BookingResponseDTO(
                savedBooking.getBookingId(),
                savedBooking.getUser().getId(),
                savedBooking.getRoom().getId(),
                booking.getRoom().getBranchName(),
                savedBooking.getCheckInDate(),
                savedBooking.getCheckOutDate(),
                savedBooking.getAdults(),
                savedBooking.getChildren(),
                savedBooking.getTotalPrice(),
                savedBooking.getConfirmBookingCode(),
                savedBooking.getStatus()
        );
    }

    private BigDecimal calculateTotalPrice(Booking booking) {
        Room room = booking.getRoom();
        BigDecimal roomPrice = room.getRoomPrice();
        LocalDate checkInDate = booking.getCheckInDate();
        LocalDate checkOutDate = booking.getCheckOutDate();

        // Tính số ngày lưu trú
        long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate);

        // Tính tổng giá tiền
        BigDecimal totalPrice = roomPrice.multiply(BigDecimal.valueOf(days));

        return totalPrice;
    }

    private String generateConfirmCode() {
        // Generate a random 5-digit number
        Random random = new Random();
        int number = random.nextInt(90000) + 10000; // Generates a number between 10000 and 99999
        return String.valueOf(number);
    }


    @Override
    public void requestCancellation(Long bookingId, String userEmail) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        // Kiểm tra ngày hủy phòng
        LocalDate today = LocalDate.now();
        if (booking.getCheckInDate().isBefore(today.plusDays(1))) {
            throw new IllegalArgumentException("Booking cannot be cancelled within one day before check-in date");
        }

        // Hủy phòng
        booking.setStatus("Cancelled");
        bookingRepository.save(booking);

        // Gửi thông báo hủy phòng thành công
        Context context = new Context();
        context.setVariable("bookingId", booking.getBookingId());
        emailService.sendHtmlMessage(userEmail, "Hủy phòng thành công", "cancelationTemplate", context);
    }


    @Override
    public Booking updateBooking(Long bookingId, Booking booking) {
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Đặt phòng không tồn tại"));
        existingBooking.setCheckInDate(booking.getCheckInDate());
        existingBooking.setCheckOutDate(booking.getCheckOutDate());
        existingBooking.setAdults(booking.getAdults());
        existingBooking.setChildren(booking.getChildren());
        existingBooking.setStatus(booking.getStatus());
        return bookingRepository.save(existingBooking);
    }

/*    @Override
    public Booking updateBooking(Long bookingId, Booking booking, boolean updateDates) {
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Đặt phòng không tồn tại"));

        if (updateDates) {
            existingBooking.setCheckInDate(booking.getCheckInDate());
            existingBooking.setCheckOutDate(booking.getCheckOutDate());
        }

        existingBooking.setAdults(booking.getAdults());
        existingBooking.setChildren(booking.getChildren());
        existingBooking.setStatus(booking.getStatus());
        return bookingRepository.save(existingBooking);
    }*/

    @Override
    public void deleteBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Đặt phòng không tồn tại"));
        bookingRepository.delete(booking);
    }

    @Override
    public Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Đặt phòng không tồn tại"));
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public void processPayment(Long bookingId, String paymentMethod) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Đặt phòng không tồn tại"));
        // Xử lý thanh toán theo phương thức (Online/Offline)
        booking.setStatus("Đã xác thực");
        bookingRepository.save(booking);
    }

    @Override
    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return List.of();
    }




    @Override
    public List<BookingResponseDTO> getBookingsByUserId(Long userId, Authentication authentication) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));

        boolean isAdminOrEmployee = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN") || role.getAuthority().equals("ROLE_EMPLOYEE"));

        List<Booking> bookings;
        if (isAdminOrEmployee) {
            bookings = bookingRepository.findAll();
        } else {
            bookings = bookingRepository.findByUser_Id(userId);
        }

        return bookings.stream()
                .map(booking -> new BookingResponseDTO(
                        booking.getBookingId(),
                        booking.getUser().getId(),
                        booking.getRoom().getId(),
                        booking.getRoom().getBranchName(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate(),
                        booking.getAdults(),
                        booking.getChildren(),
                        booking.getTotalPrice(),
                        booking.getConfirmBookingCode(),
                        booking.getStatus(),
                        booking.getUser().getEmail(),
                        booking.getUser().getPhone(),
                        booking.getUser().getFirstName(),
                        booking.getUser().getLastName()

                ))
                .collect(Collectors.toList());
    }
}


