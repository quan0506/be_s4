package com.fptaptech.s4.service.interfaces;

import com.fptaptech.s4.entity.Hotel;

public interface IHotelService {
    Hotel addHotel(Hotel hotel);
    Hotel updateHotel(Long id, Hotel hotel);
    /*void deleteHotel(Long id);
    List<Hotel> getAllHotels();*/
    /*Hotel getHotelById(Long id);*/

}
