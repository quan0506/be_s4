package com.fptaptech.s4.service.impl;


import com.fptaptech.s4.response.Response;
import com.fptaptech.s4.dto.ShuttleDTO;
import com.fptaptech.s4.entity.Branch;
import com.fptaptech.s4.entity.Shuttle;
import com.fptaptech.s4.exception.OurException;
import com.fptaptech.s4.repository.BranchRepository;
import com.fptaptech.s4.repository.ShuttleRepository;

import com.fptaptech.s4.service.interfaces.IShuttleService;
import com.fptaptech.s4.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ShuttleService implements IShuttleService {
    private final ShuttleRepository shuttleRepository;
    private final BranchRepository branchRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public Response addNewCar(Long branchId, MultipartFile photo, String carType, BigDecimal carPrice, String description) {
        Response response = new Response();
        try {
            Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new OurException("Branch Not Found"));
            Map uploadResult = cloudinaryService.upload(photo);
            String imageUrl = (String) uploadResult.get("url");

            Shuttle car = new Shuttle();
            car.setBranch(branch); // Set the branch
            car.setCarPhotoUrl(imageUrl);
            car.setCarType(carType);
            car.setCarPrice(carPrice);
            car.setCarDescription(description);
            Shuttle savedCar = shuttleRepository.save(car);

            ShuttleDTO carDTO = Utils.mapShuttleEntityToShuttleDTO(savedCar);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setData(carDTO);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving car: " + e.getMessage());
        }
        return response;
    }

    @Override
    public List<String> getAllCarTypes(Long branchId) {
        return shuttleRepository.findDistinctCarTypes(branchId);
    }

    @Override
    public Response getAllCars(Long branchId) {
        Response response = new Response();
        try {
            List<Shuttle> carList = shuttleRepository.findByBranchId(branchId, Sort.by(Sort.Direction.DESC, "id"));
            List<ShuttleDTO> carDTOList = Utils.mapShuttleListEntityToShuttleListDTO(carList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setShuttleList(carDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching cars: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteCar(Long branchId, Long carId) {
        Response response = new Response();
        try {
            Shuttle car = shuttleRepository.findByIdAndBranchId(carId, branchId).orElseThrow(() -> new OurException("Car Not Found"));
            shuttleRepository.deleteById(carId);
            response.setStatusCode(200);
            response.setMessage("successful");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting car: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateCar(Long branchId, Long carId, String description, String carType, BigDecimal carPrice, MultipartFile photo) {
        Response response = new Response();
        try {
            String imageUrl = null;
            if (photo != null && !photo.isEmpty()) {
                Map uploadResult = cloudinaryService.upload(photo);
                imageUrl = (String) uploadResult.get("url");
            }

            Shuttle car = shuttleRepository.findByIdAndBranchId(carId, branchId).orElseThrow(() -> new OurException("Car Not Found"));
            if (carType != null) car.setCarType(carType);
            if (carPrice != null) car.setCarPrice(carPrice);
            if (description != null) car.setCarDescription(description);
            if (imageUrl != null) car.setCarPhotoUrl(imageUrl);

            Shuttle updatedCar = shuttleRepository.save(car);
            ShuttleDTO carDTO = Utils.mapShuttleEntityToShuttleDTO(updatedCar);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setShuttle(carDTO);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating car: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getCarById(Long branchId, Long carId) {
        Response response = new Response();
        try {
            Shuttle car = shuttleRepository.findByIdAndBranchId(carId, branchId).orElseThrow(() -> new OurException("Car Not Found"));
            ShuttleDTO carDTO = Utils.mapShuttleEntityToShuttleDTOPlusBookings(car);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setShuttle(carDTO);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching car: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAvailableCarsByDateAndType(Long branchId, LocalDate shuttleCheckInDate, LocalDate shuttleCheckOutDate, String carType) {
        Response response = new Response();
        try {
            List<Shuttle> availableCars = shuttleRepository.findAvailableCarsByDatesAndTypes(branchId, shuttleCheckInDate, shuttleCheckOutDate, carType);
            List<ShuttleDTO> carDTOList = Utils.mapShuttleListEntityToShuttleListDTO(availableCars);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setShuttleList(carDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching available cars: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllAvailableCars(Long branchId) {
        Response response = new Response();
        try {
            List<Shuttle> carList = shuttleRepository.getAllAvailableCars(branchId);
            List<ShuttleDTO> carDTOList = Utils.mapShuttleListEntityToShuttleListDTO(carList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setShuttleList(carDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching available cars: " + e.getMessage());
        }
        return response;
    }

    public Response getAllShuttles() {
        Response response = new Response();
        try {
            List<Shuttle> shuttles = shuttleRepository.findAll();
            List<ShuttleDTO> shuttleDTOList = Utils.mapShuttleListEntityToShuttleListDTO(shuttles);
            response.setStatusCode(200);
            response.setMessage("Shuttles retrieved successfully.");
            response.setData(shuttleDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching shuttles: " + e.getMessage());
        }
        return response;
    }
}

