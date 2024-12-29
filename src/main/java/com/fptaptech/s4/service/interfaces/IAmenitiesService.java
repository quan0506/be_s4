package com.fptaptech.s4.service.interfaces;

import com.fptaptech.s4.dto.Response;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;


public interface IAmenitiesService {
    Response createAmenity(Long roomId, List<MultipartFile> photos, String name, String description);
    Response getAllAmenities();
    Response getAllAmenitiesByRoomId(Long roomId);
    Response getAmenityById(Long id);
    Response updateAmenity(Long id, Long roomId, List<MultipartFile> photos, String name, String description);
    Response deleteAmenity(Long id);
}
