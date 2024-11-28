package com.fptaptech.s4.response;

import com.fptaptech.s4.entity.Branch;
import com.fptaptech.s4.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RoomResponse {
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private boolean isBooked;
    private String photo;
    private String description;
    private Long branchId;
    private List<BookingResponse> bookings;

    // Constructor bao gồm ảnh dưới dạng chuỗi base64
    public RoomResponse(Long id, String roomType, BigDecimal roomPrice, boolean isBooked,
                        String photo, String description, List<BookingResponse> bookings) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.isBooked = isBooked;
        this.photo = photo;
        this.description= description;
        this.bookings = bookings;
    }

    public RoomResponse(Room room) {
        this.id = room.getId();
        this.roomType = room.getRoomType();
        this.roomPrice = room.getRoomPrice();
        this.photo = room.getPhoto();
        this.description = room.getDescription();
        this.isBooked = room.isBooked();
        this.bookings = room.getBookings().stream()
                .map(BookingResponse::new)
                .collect(Collectors.toList());
    }

    public RoomResponse(Long id, String roomType, BigDecimal roomPrice, Long branchId ,String photo, String description) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.isBooked = false;
        this.photo = photo;
        this.description = description;
        this.branchId = branchId;
    }

    public RoomResponse(Long id, String roomType, BigDecimal roomPrice, String photo, String description) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.isBooked = false;
        this.photo = photo;
        this.description = description;
    }
}
