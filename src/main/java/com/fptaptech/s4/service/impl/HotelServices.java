package com.fptaptech.s4.service.impl;

import com.fptaptech.s4.entity.Hotel;
import com.fptaptech.s4.exception.ResourceNotFoundException;
import com.fptaptech.s4.repository.HotelRepository;
import com.fptaptech.s4.service.IHotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelServices implements IHotelService {

    private final HotelRepository hotelRepository;

    @Override
    public Hotel addHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @Override
    public Hotel updateHotel(Long id, Hotel hotel) {
        Hotel existingHotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));
        existingHotel.setHotelName(hotel.getHotelName());
        existingHotel.setStarRating(hotel.getStarRating());
        return hotelRepository.save(existingHotel);
    }

    /*@Override
    public void deleteHotel(Long id) {
        hotelRepository.deleteById(id);
    }

    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }*/

    /*@Override
    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));
    }*/
}
