package com.fptaptech.s4.controller;

import com.fptaptech.s4.dto.RoomRequestDTO;

import com.fptaptech.s4.service.interfaces.IRoomCleaningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cleaning")
@RequiredArgsConstructor
public class RoomCleaningController {
    private final IRoomCleaningService roomCleaningService;

    @PostMapping("/request/{roomId}")
    public ResponseEntity<Void> requestRoomCleaning(@PathVariable Long roomId) {
        roomCleaningService.requestRoomCleaning(roomId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/acknowledge/{requestId}")
    public ResponseEntity<Void> acknowledgeCleaningRequest(@PathVariable Long requestId) {
        roomCleaningService.acknowledgeCleaningRequest(requestId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/recent-requests/{roomId}")
    public ResponseEntity<List<RoomRequestDTO>> getRecentCleaningRequests(@PathVariable Long roomId) {
        List<RoomRequestDTO> requests = roomCleaningService.getRecentCleaningRequests(roomId);
        return ResponseEntity.ok(requests);
    }
}
