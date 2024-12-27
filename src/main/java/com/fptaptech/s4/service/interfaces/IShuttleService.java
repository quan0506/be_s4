package com.fptaptech.s4.service.interfaces;

import com.fptaptech.s4.dto.Response;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IShuttleService {
    Response addNewCar(Long branchId, List<MultipartFile> photos, String carType, BigDecimal carPrice, String description);

    List<String> getAllCarTypes(Long branchId);

    Response getAllCars(Long branchId);

    Response deleteCar(Long branchId, Long carId);

    Response updateCar(Long branchId, Long carId, String description, String carType, BigDecimal carPrice, List<MultipartFile> photos);

    Response getCarById(Long branchId, Long carId);

    Response getAvailableCarsByDateAndType(Long branchId, LocalDate shuttleCheckInDate, LocalDate shuttleCheckOutDate, String carType);

    Response getAllAvailableCars(Long branchId);

    Response getAllShuttles();
}
