package com.fptaptech.s4.service.impl;

import com.fptaptech.s4.entity.Branch;
import com.fptaptech.s4.entity.Room;
import com.fptaptech.s4.exception.ResourceNotFoundException;
import com.fptaptech.s4.repository.BranchRepository;
import com.fptaptech.s4.repository.RoomRepository;
import com.fptaptech.s4.service.interfaces.IRoomService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {
    private final RoomRepository roomRepository;
    private final BranchRepository branchRepository;

    @Override
    public Room addNewRoom(String file, String roomType, BigDecimal roomPrice, Long branchId, String description) throws IOException {
        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);
        room.setDescription(description);

        if (!file.isEmpty()) {
            byte[] photoBytes = file.getBytes();
            String base64Photo = Base64.encodeBase64String(photoBytes);
            room.setPhoto(base64Photo);
        }

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));
        room.setBranch(branch);

        return roomRepository.save(room);
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public String getRoomPhotoByRoomId(Long roomId) {
        return roomRepository.findById(roomId).map(Room::getPhoto).orElse(null);
    }

    @Override
    public void deleteRoom(Long roomId) {
        Optional<Room> theRoom = roomRepository.findById(roomId);
        if (theRoom.isPresent()) {
            roomRepository.deleteById(roomId);
        }
    }

    @Override
    public Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, String base64Photo, String description){
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        if (roomType != null) room.setRoomType(roomType);
        if (roomPrice != null) room.setRoomPrice(roomPrice);
        if (base64Photo != null && !base64Photo.isEmpty()) {
            room.setPhoto(base64Photo);
        }
        if (description != null) room.setDescription(description);
        return roomRepository.save(room);
    }

    @Override
    public Optional<Room> getRoomById(Long roomId) {
        return roomRepository.findById(roomId);
    }

    @Override
    public List<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        return roomRepository.findAvailableRoomsByDatesAndType(checkInDate, checkOutDate, roomType);
    }
}
