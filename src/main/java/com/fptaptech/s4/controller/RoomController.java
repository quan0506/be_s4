package com.fptaptech.s4.controller;

import com.fptaptech.s4.exception.ResourceNotFoundException;
import com.fptaptech.s4.entity.BookedRoom;
import com.fptaptech.s4.entity.Room;
import com.fptaptech.s4.response.BookingResponse;
import com.fptaptech.s4.response.RoomResponse;
import com.fptaptech.s4.service.impl.BookingService;
import com.fptaptech.s4.service.impl.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {
    private final RoomService roomService;
    private final BookingService bookingService;

    @PostMapping("/add/new-room")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> addNewRoom(@RequestParam String roomType,
                                                   @RequestParam BigDecimal roomPrice,
                                                   @RequestParam Long branchId,
                                                   @RequestParam String description,
                                                   @RequestParam List<MultipartFile> photos) throws IOException {
        Room savedRoom = roomService.addNewRoom(photos, roomType, roomPrice, branchId, description);
        RoomResponse response = new RoomResponse(savedRoom.getId(), savedRoom.getRoomType(),
                savedRoom.getRoomPrice(), savedRoom.getBranch().getId(), savedRoom.getPhotos(), savedRoom.getDescription());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all-rooms")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        List<RoomResponse> roomResponses = rooms.stream()
                .map(RoomResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(roomResponses);
    }

    @GetMapping("/rooms/by-branch/{branchId}")
    public ResponseEntity<List<RoomResponse>> getRoomsByBranch(@PathVariable Long branchId) {
        List<Room> rooms = roomService.getRoomsByBranch(branchId);
        List<RoomResponse> roomResponses = rooms.stream()
                .map(RoomResponse::new)
                .toList();
        return ResponseEntity.ok(roomResponses);
    }

    @GetMapping("/rooms/by-type")
    public ResponseEntity<List<RoomResponse>> getRoomsByTypeAndBranch(@RequestParam String roomType, @RequestParam Long branchId) {
        List<Room> rooms = roomService.getRoomsByTypeAndBranch(roomType, branchId);
        List<RoomResponse> roomResponses = rooms.stream()
                .map(RoomResponse::new)
                .toList();
        return ResponseEntity.ok(roomResponses);
    }

    @GetMapping("/rooms/by-price")
    public ResponseEntity<List<RoomResponse>> getRoomsByPriceAndBranch(@RequestParam BigDecimal roomPrice, @RequestParam Long branchId) {
        List<Room> rooms = roomService.getRoomsByPriceAndBranch(roomPrice, branchId);
        List<RoomResponse> roomResponses = rooms.stream()
                .map(RoomResponse::new)
                .toList();
        return ResponseEntity.ok(roomResponses);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long roomId) {
        return roomService.getRoomById(roomId)
                .map(RoomResponse::new)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
    }


    @GetMapping("/available-rooms")
    public ResponseEntity<List<RoomResponse>> isRoomAvailable(
            @RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam("roomId") Long roomId) {
        boolean isAvailable = roomService.isRoomAvailable(roomId, checkInDate, checkOutDate);

        if (!isAvailable) {
            return ResponseEntity.noContent().build();
        }

        return roomService.getRoomById(roomId)
                .map(this::getRoomResponse)
                .map(Collections::singletonList)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/room/{roomId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{roomId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId,
                                                   @RequestParam(required = false) String roomType,
                                                   @RequestParam(required = false) BigDecimal roomPrice,
                                                   @RequestParam(required = false) List<MultipartFile> photos,
                                                   @RequestParam(required = false) String description) throws IOException {
        Room updatedRoom = roomService.updateRoom(roomId, roomType, roomPrice, photos, description);
        RoomResponse roomResponse = getRoomResponse(updatedRoom);
        return ResponseEntity.ok(roomResponse);
    }

    private RoomResponse getRoomResponse(Room room) {
        List<BookedRoom> bookings = getAllBookingsByRoomId(room.getId());
        List<BookingResponse> bookingInfo = bookings.stream()
                .map(booking -> new BookingResponse(booking.getBookingId(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate(),
                        booking.getBookingConfirmationCode()))
                .toList();
        return new RoomResponse(room.getId(), room.getRoomType(), room.getRoomPrice(),
                room.isBooked(), room.getPhotos(), room.getDescription(), room.getBranch().getId(), bookingInfo);
    }

    private List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingService.getAllBookingsByRoomId(roomId);
    }
}

