package com.fptaptech.s4.service.interfaces;

import com.fptaptech.s4.entity.Room;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IRoomService {
    Room addNewRoom(List<MultipartFile> photos, String roomType, BigDecimal roomPrice, Long branchId, String description) throws IOException;
    List<String> getAllRoomTypes();
    List<Room> getAllRooms();
    List<Room> getRoomsByBranch(Long branchId);
    List<Room> getRoomsByTypeAndBranch(String roomType, Long branchId);
    List<Room> getRoomsByPriceAndBranch(BigDecimal roomPrice, Long branchId);
    byte[] getRoomPhotoByRoomId(Long roomId);
    void deleteRoom(Long roomId);
    Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, List<MultipartFile> photos, String description) throws IOException;
    Optional<Room> getRoomById(Long roomId);
    boolean isRoomAvailable(Long roomId, LocalDate checkInDate, LocalDate checkOutDate);
}

