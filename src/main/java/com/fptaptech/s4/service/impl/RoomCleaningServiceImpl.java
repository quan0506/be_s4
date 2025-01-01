package com.fptaptech.s4.service.impl;


import com.fptaptech.s4.dto.RoomRequestDTO;
import com.fptaptech.s4.entity.RequestType;
import com.fptaptech.s4.entity.Room;

import com.fptaptech.s4.exception.ResourceNotFoundException;
import com.fptaptech.s4.repository.RoomRepository;
import com.fptaptech.s4.repository.RoomRequestRepository;
import com.fptaptech.s4.request.RoomRequest;
import com.fptaptech.s4.service.interfaces.IRoomCleaningService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomCleaningServiceImpl implements IRoomCleaningService {
    private final RoomRepository roomRepository;
    private final RoomRequestRepository roomRequestRepository;
    private final EmailService emailService;

    @Override
    public void requestRoomCleaning(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        room.setNeedsCleaning(true);
        roomRepository.save(room);

        RoomRequest roomRequest = new RoomRequest();
        roomRequest.setRoom(room);
        roomRequest.setRequestType(RequestType.CLEANING); // Đảm bảo requestType không null
        roomRequest.setDescription("Room cleaning requested");
        roomRequest.setCreatedAt(LocalDateTime.now());
        roomRequestRepository.save(roomRequest);
    }

    @Override
    public void acknowledgeCleaningRequest(Long requestId) {
        RoomRequest roomRequest = roomRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));
        roomRequest.setDescription("Cleaning request acknowledged by receptionist");
        roomRequestRepository.save(roomRequest);

        Room room = roomRequest.getRoom();
        room.setNeedsCleaning(false);
        roomRepository.save(room);

        String branchEmail = room.getBranch().getEmail();
        if (branchEmail == null) {
            throw new IllegalArgumentException("Branch email must not be null");
        }

        String subject = "Yêu cầu dọn phòng đã được xác nhận";
        Context context = new Context();
        context.setVariable("name", roomRequest.getRoom().getBranch().getBranchName());
        emailService.sendHtmlMessage(branchEmail, subject, "cleaningRequestAcknowledged", context);
    }

    @Override
    public List<RoomRequestDTO> getRecentCleaningRequests(Long roomId) {
        List<RoomRequest> roomRequests = roomRequestRepository.findByRoomIdOrderByCreatedAtDesc(roomId);
        return roomRequests.stream().map(this::toDto).collect(Collectors.toList());
    }

    private RoomRequestDTO toDto(RoomRequest roomRequest) {
        if (roomRequest.getRequestType() == null) {
            throw new IllegalArgumentException("RequestType must not be null");
        }
        return new RoomRequestDTO(
                roomRequest.getId(),
                roomRequest.getRoom().getId(),
                roomRequest.getRequestType().name(),
                roomRequest.getDescription(),
                roomRequest.getCreatedAt()
        );
    }
}
