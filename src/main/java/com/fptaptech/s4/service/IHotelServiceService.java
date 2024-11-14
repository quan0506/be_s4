package com.fptaptech.s4.service;

import com.fptaptech.s4.entity.HotelServices;
import java.util.Optional;

public interface IHotelServiceService {
    HotelServices addService(Long hotelId, HotelServices hotelServices);

    HotelServices updateService(Long id, HotelServices hotelServices);

    void deleteService(Long id);

    Optional<HotelServices> getAllServicesByHotel(Long hotelId);

    HotelServices getServiceById(Long id);
}
