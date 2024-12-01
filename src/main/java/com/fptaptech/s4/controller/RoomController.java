package com.fptaptech.s4.controller;

import com.fptaptech.s4.exception.ResourceNotFoundException;
import com.fptaptech.s4.entity.BookedRoom;
import com.fptaptech.s4.entity.Room;
import com.fptaptech.s4.request.RoomRequest;
import com.fptaptech.s4.response.BookingResponse;
import com.fptaptech.s4.response.RoomResponse;
import com.fptaptech.s4.service.impl.BookingService;
import com.fptaptech.s4.service.interfaces.IRoomService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Id;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {
    private final IRoomService roomService;
    private final BookingService bookingService;

    @PostMapping("/add/new-room")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> addNewRoom(@RequestBody RoomRequest roomRequest) throws IOException {
        String base64Photo = roomRequest.getPhoto() != null ? Base64.encodeBase64String(roomRequest.getPhoto().getBytes()) : null;
        Room savedRoom = roomService.addNewRoom(base64Photo, roomRequest.getRoomType(), roomRequest.getRoomPrice(), roomRequest.getBranchId(), roomRequest.getDescription());
        RoomResponse response = new RoomResponse(savedRoom.getId(), savedRoom.getRoomType(),
                savedRoom.getRoomPrice(), savedRoom.getBranch().getId(), savedRoom.getPhoto(), savedRoom.getDescription()); // Sửa lại để lấy ID của branch
        return ResponseEntity.ok(response);
    }

    @GetMapping("/room/types")
    public List<String> getRoomTypes() {
        return roomService.getAllRoomTypes();
    }

    @GetMapping("/all-rooms")
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        List<RoomResponse> roomResponses = new ArrayList<>();
        for (Room room : rooms) {
            String base64Photo = room.getPhoto();
            RoomResponse roomResponse = getRoomResponse(room);
            roomResponse.setPhoto(base64Photo);
            roomResponses.add(roomResponse);
        }
        return ResponseEntity.ok(roomResponses);
    }

    @GetMapping("/rooms/by-branch/{branchId}")
    public ResponseEntity<List<RoomResponse>> getRoomsByBranch(@PathVariable Long branchId) {
        List<Room> rooms = roomService.getRoomsByBranch(branchId);
        List<RoomResponse> roomResponses = new ArrayList<>();
        for (Room room : rooms) {
            RoomResponse roomResponse = getRoomResponse(room);
            roomResponses.add(roomResponse);
        }
        return ResponseEntity.ok(roomResponses);
    }

    @GetMapping("/rooms/by-type")
    public ResponseEntity<List<RoomResponse>> getRoomsByTypeAndBranch(@RequestParam String roomType, @RequestParam Long branchId) {
        List<Room> rooms = roomService.getRoomsByTypeAndBranch(roomType, branchId);
        List<RoomResponse> roomResponses = new ArrayList<>();
        for (Room room : rooms) {
            RoomResponse roomResponse = getRoomResponse(room);
            roomResponses.add(roomResponse);
        }
        return ResponseEntity.ok(roomResponses);
    }

    @GetMapping("/rooms/by-price")
    public ResponseEntity<List<RoomResponse>> getRoomsByPriceAndBranch(@RequestParam BigDecimal roomPrice, @RequestParam Long branchId) {
        List<Room> rooms = roomService.getRoomsByPriceAndBranch(roomPrice, branchId);
        List<RoomResponse> roomResponses = new ArrayList<>();
        for (Room room : rooms) {
            RoomResponse roomResponse = getRoomResponse(room);
            roomResponses.add(roomResponse);
        }
        return ResponseEntity.ok(roomResponses);
    }

    @DeleteMapping("/delete/room/{roomId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{roomId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId,
                                                   @RequestParam(required = false) String roomType,
                                                   @RequestParam(required = false) BigDecimal roomPrice,
                                                   @RequestParam(required = false) String photo,
                                                   @RequestParam(required = false) String description) throws IOException {
        var base64Photo = photo != null && !photo.isEmpty() ?
                Base64.encodeBase64String(photo.getBytes()) :
                roomService.getRoomPhotoByRoomId(roomId);
        Room theRoom = roomService.updateRoom(roomId, roomType, roomPrice, base64Photo, description);
        theRoom.setPhoto(base64Photo);
        RoomResponse roomResponse = getRoomResponse(theRoom);
        return ResponseEntity.ok(roomResponse);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<Optional<RoomResponse>> getRoomById(@PathVariable Long roomId) {
        Optional<Room> theRoom = roomService.getRoomById(roomId);
        return theRoom.map(room -> {
            RoomResponse roomResponse = getRoomResponse(room);
            return ResponseEntity.ok(Optional.of(roomResponse));
        }).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
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

        Optional<Room> roomOpt = roomService.getRoomById(roomId);
        if (roomOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Room room = roomOpt.get();
        RoomResponse roomResponse = getRoomResponse(room);
        roomResponse.setPhoto(room.getPhoto());

        return ResponseEntity.ok(Collections.singletonList(roomResponse));
    }

    private RoomResponse getRoomResponse(Room room) {
        List<BookedRoom> bookings = getAllBookingsByRoomId(room.getId());
        List<BookingResponse> bookingInfo = bookings
                .stream()
                .map(booking -> new BookingResponse(booking.getBookingId(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate(), booking.getBookingConfirmationCode())).toList();
        return new RoomResponse(room.getId(),
                room.getRoomType(), room.getRoomPrice(),
                room.isBooked(), room.getPhoto(), room.getDescription(), room.getBranchId(), bookingInfo);
    }

    private List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingService.getAllBookingsByRoomId(roomId);
    }
}