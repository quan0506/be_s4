package com.fptaptech.s4.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class RoomResponse {
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private boolean isBooked;
    private String photo;
    private String description;
    private List<BookingResponse> bookings;

    // Constructor không bao gồm ảnh
    public RoomResponse(Long id, String roomType, BigDecimal roomPrice) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
    }

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
}
