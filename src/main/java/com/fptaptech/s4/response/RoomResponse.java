package com.fptaptech.s4.response;

import com.fptaptech.s4.entity.Room;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Builder
public class RoomResponse {
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private boolean isBooked;
    private byte[] photo;
    private String description;
    private Long branchId;
    private List<BookingResponse> bookings;

    // Constructor bao gồm ảnh dưới dạng chuỗi base64
    public RoomResponse(Long id, String roomType, BigDecimal roomPrice, boolean isBooked,
                        byte[] photo, String description, Long branchId , List<BookingResponse> bookings) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.isBooked = isBooked;
        this.photo = photo;
        this.description= description;
        this.branchId = branchId;
        this.bookings = bookings;
    }

        // Constructor that does not include the branch reference
    public RoomResponse(Room room) {
        this.id = room.getId();
        this.roomType = room.getRoomType();
        this.roomPrice = room.getRoomPrice();
        this.photo = room.getPhoto();
        this.description = room.getDescription();
        this.isBooked = room.isBooked();
        this.branchId = room.getBranch()!=null? room.getBranch().getId() : null;
        this.bookings = room.getBookings().stream()
            .map(BookingResponse::new)
            .collect(Collectors.toList());
        }

    public RoomResponse(Long id, String roomType, BigDecimal roomPrice, Long branchId ,byte[] photo, String description) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.isBooked = false;
        this.photo = photo;
        this.description = description;
        this.branchId = branchId;
    }

    public RoomResponse(Long id, String roomType, BigDecimal roomPrice, byte[] photo, String description) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.isBooked = false;
        this.photo = photo;
        this.description = description;
    }
}
