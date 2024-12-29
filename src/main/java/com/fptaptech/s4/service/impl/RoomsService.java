package com.fptaptech.s4.service.impl;

import com.fptaptech.s4.dto.RoomsDTO;
import com.fptaptech.s4.entity.Rooms;
import com.fptaptech.s4.repository.RoomsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomsService {

    private final RoomsRepository roomsRepository;

    // Create Room
    public Rooms createRoom(Rooms room) {
        room.setUpdatedAt(LocalDateTime.now());
        return roomsRepository.save(room);
    }

    // Get All Rooms
    public List<RoomsDTO> getAllRooms() {
        List<Rooms> rooms = roomsRepository.findAll();
        return rooms.stream()
                .map(room -> new RoomsDTO(room.getId(), room.getName(), room.getDescription(), room.getRoomStatus(), room.getUpdatedAt()))
                .collect(Collectors.toList());
    }

    // Get Room By ID
    public Rooms getRoomById(Long id) {
        return roomsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    // Update Room and Toggle Room Status
    public Rooms updateRoom(Long id, Rooms roomDetails) {
        Rooms existingRoom = roomsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        existingRoom.setName(roomDetails.getName());
        existingRoom.setDescription(roomDetails.getDescription());

        // Toggle room status
        String currentStatus = existingRoom.getRoomStatus();
        existingRoom.setRoomStatus("Opened".equals(currentStatus) ? "Closed" : "Opened");

        // Set updatedAt to current time
        existingRoom.setUpdatedAt(LocalDateTime.now());

        return roomsRepository.save(existingRoom);
    }

    // Delete Room
    public void deleteRoom(Long id) {
        Rooms room = roomsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        roomsRepository.delete(room);
    }
}



