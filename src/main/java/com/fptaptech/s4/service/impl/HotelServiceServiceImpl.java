package com.fptaptech.s4.service.impl;

import com.fptaptech.s4.entity.Hotel;
import com.fptaptech.s4.exception.ResourceNotFoundException;
import com.fptaptech.s4.entity.HotelServices;
import com.fptaptech.s4.repository.HotelRepository;
import com.fptaptech.s4.repository.HotelServiceRepository;
import com.fptaptech.s4.service.IHotelServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelServiceServiceImpl implements IHotelServiceService {

    private final HotelRepository hotelRepository;
    private final HotelServiceRepository hotelServiceRepository;

    @Override
    public HotelServices addService(Long id, HotelServices hotelService) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));
        hotelService.setHotel(hotel);
        return hotelServiceRepository.save(hotelService);
    }

    @Override
    public HotelServices updateService(Long id, HotelServices hotelService) {
        HotelServices existingService = hotelServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
        existingService.setName(hotelService.getName());
        existingService.setDescription(hotelService.getDescription());
        existingService.setPrice(hotelService.getPrice());
        existingService.setHotel(hotelService.getHotel());
        return hotelServiceRepository.save(existingService);
    }

    @Override
    public void deleteService(Long id) {
        HotelServices existingService = hotelServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
        hotelServiceRepository.delete(existingService);
    }

    @Override
    public Optional<HotelServices> getAllServicesByHotel(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));
        return hotelServiceRepository.findById(hotel.getId());
    }

    @Override
    public HotelServices getServiceById(Long id) {
        return hotelServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
    }
}