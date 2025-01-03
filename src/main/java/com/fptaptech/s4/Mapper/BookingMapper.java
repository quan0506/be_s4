package com.fptaptech.s4.Mapper;

import com.fptaptech.s4.dto.BookingDTO;
import com.fptaptech.s4.entity.Booking;

public class BookingMapper {

    public static BookingDTO toDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setBookingId(booking.getBookingId());
//        dto.setUserName(booking.getUser().getFirstName());
//        dto.setUserName(booking.getUser().getLastName());
        dto.setRoomType(booking.getRoom().getRoomType());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        dto.setAdults(booking.getAdults());
        dto.setChildren(booking.getChildren());
        dto.setTotalPrice(booking.getTotalPrice());
        dto.setConfirmBookingCode(booking.getConfirmBookingCode());
        dto.setStatus(booking.getStatus());
        return dto;
    }
}
