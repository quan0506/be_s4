package com.fptaptech.s4.response;

import com.fptaptech.s4.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponse {
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private boolean isBooked;
    private List<String> photos;
    private String description;
    private String status;

    private Long branchId;
    private List<BookingResponse> bookings;

    // Constructor bao gồm ảnh dưới dạng chuỗi base64
    public RoomResponse(Long id, String roomType, BigDecimal roomPrice, boolean isBooked,
                        List<String> photos, String description, Long branchId , List<BookingResponse> bookings) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.isBooked = isBooked;
        this.photos = photos;
        this.description= description;
        this.branchId = branchId;
        this.bookings = bookings;
    }

        // Constructor that does not include the branch reference
    public RoomResponse(Room room) {
        this.id = room.getId();
        this.roomType = room.getRoomType();
        this.roomPrice = room.getRoomPrice();
        this.photos = room.getPhotos();
        this.description = room.getDescription();
        this.status = room.getStatus();
        this.isBooked = room.isBooked();
        this.branchId = room.getBranch()!=null? room.getBranch().getId() : null;
        this.bookings = room.getBookings().stream()
            .map(BookingResponse::new)
            .collect(Collectors.toList());
        }

    public RoomResponse(Long id, String roomType, BigDecimal roomPrice, Long branchId ,List<String> photos, String description) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.isBooked = false;
        this.photos = photos;
        this.description = description;
        this.branchId = branchId;
    }

    public RoomResponse(Long id, String roomType, BigDecimal roomPrice, List<String> photos, String description) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.isBooked = false;
        this.photos = photos;
        this.description = description;
    }
}
