package com.fptaptech.s4.service.impl;

import com.fptaptech.s4.entity.Booking;
import com.fptaptech.s4.entity.Branch;
import com.fptaptech.s4.entity.Room;
import com.fptaptech.s4.exception.ResourceNotFoundException;
import com.fptaptech.s4.repository.BranchRepository;
import com.fptaptech.s4.repository.RoomRepository;
import com.fptaptech.s4.service.interfaces.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {
    private final RoomRepository roomRepository;
    private final BranchRepository branchRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public Room addNewRoom(List<MultipartFile> photos, String roomType, BigDecimal roomPrice, Long branchId, String description) throws IOException {
        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);
        room.setDescription(description);

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));
        room.setBranch(branch);

        if (photos != null && !photos.isEmpty()) {
            List<String> photoUrls = uploadPhotos(photos);
            room.setPhotos(photoUrls);
        }

        return roomRepository.save(room);
    }

    @Override
    public Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, List<MultipartFile> photos, String description) throws IOException {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        if (roomType != null) room.setRoomType(roomType);
        if (roomPrice != null) room.setRoomPrice(roomPrice);
        if (description != null) room.setDescription(description);

        if (photos != null && !photos.isEmpty()) {
            List<String> photoUrls = uploadPhotos(photos);
            room.setPhotos(photoUrls);
        }

        return roomRepository.save(room);
    }

    private List<String> uploadPhotos(List<MultipartFile> photos) throws IOException {
        List<String> photoUrls = new ArrayList<>();
        for (MultipartFile photo : photos) {
            if (photo != null && !photo.isEmpty()) {
                Map uploadResult = cloudinaryService.upload(photo);
                String photoUrl = (String) uploadResult.get("url");
                photoUrls.add(photoUrl);
            }
        }
        return photoUrls;
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
    public List<Room> getRoomsByBranch(Long branchId) {
        return roomRepository.findByBranch_Id(branchId);
    }

    @Override
    public List<Room> getRoomsByTypeAndBranch(String roomType, Long branchId) {
        return roomRepository.findByRoomTypeAndBranch_Id(roomType, branchId);
    }

    @Override
    public List<Room> getRoomsByPriceAndBranch(BigDecimal roomPrice, Long branchId) {
        return roomRepository.findByRoomPriceAndBranch_Id(roomPrice, branchId);
    }

    @Override
    public byte[] getRoomPhotoByRoomId(Long roomId) {
        return roomRepository.findById(roomId).map(Room::getPhotos).orElse(Collections.emptyList()).stream()
                .findFirst().map(this::convertUrlToByteArray).orElse(null);
    }

    private byte[] convertUrlToByteArray(String url) {
        // Add logic to convert URL to byte array if needed
        return new byte[0]; // Placeholder
    }

    @Override
    public void deleteRoom(Long roomId) {
        Optional<Room> theRoom = roomRepository.findById(roomId);
        theRoom.ifPresent(room -> roomRepository.deleteById(roomId));
    }

    @Override
    public Optional<Room> getRoomById(Long roomId) {
        return roomRepository.findById(roomId);
    }

    @Override
    public boolean isRoomAvailable(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Booking> bookings =  roomRepository.findBookingsByRoomIdAndDateRange(roomId, checkInDate, checkOutDate);
        return bookings.isEmpty();
    }
}
