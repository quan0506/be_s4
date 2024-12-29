package com.fptaptech.s4.controller;

import com.fptaptech.s4.dto.RoomsDTO;
import com.fptaptech.s4.entity.Rooms;
import com.fptaptech.s4.service.impl.RoomsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomsController {

    private final RoomsService roomsService;

    // Create Room
    @PostMapping("/createRooms")
    public ResponseEntity<Rooms> createRoom(@RequestBody Rooms room) {
        Rooms createdRoom = roomsService.createRoom(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
    }

    // Get All Rooms
    @GetMapping("/getAllRooms")
    public ResponseEntity<List<RoomsDTO>> getAllRooms() {
        List<RoomsDTO> rooms = roomsService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    // Get Room By ID
    @GetMapping("/getRoomsById/{id}")
    public ResponseEntity<Rooms> getRoomById(@PathVariable Long id) {
        Rooms room = roomsService.getRoomById(id);
        return ResponseEntity.ok(room);
    }

    // Update Room and Toggle Room Status
    @PutMapping("/updateRooms/{id}")
    public ResponseEntity<Rooms> updateRoom(@PathVariable Long id, @RequestBody Rooms roomDetails) {
        Rooms updatedRoom = roomsService.updateRoom(id, roomDetails);
        return ResponseEntity.ok(updatedRoom);
    }

    // Delete Room
    @DeleteMapping("/deleteRooms/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomsService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}



