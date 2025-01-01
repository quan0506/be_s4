package com.fptaptech.s4.service.interfaces;

import java.util.List;

import com.fptaptech.s4.dto.RoomRequestDTO;

public interface IRoomCleaningService {
    void requestRoomCleaning(Long roomId);
    void acknowledgeCleaningRequest(Long requestId);
    List<RoomRequestDTO> getRecentCleaningRequests(Long roomId);
}
