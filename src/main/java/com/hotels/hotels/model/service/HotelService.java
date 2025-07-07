package com.hotels.hotels.model.service;

import java.util.List;
import java.util.Optional;

import com.hotels.hotels.model.dto.Histogram;
import com.hotels.hotels.model.dto.HotelShortInfo;
import com.hotels.hotels.model.entity.Hotel;

public interface HotelService {
    List<HotelShortInfo> getShortInfoAboutHotels();

    Optional<Hotel> getHotel(Long id);

    List<HotelShortInfo> getHotelsByParameters(Optional<String> name, Optional<String> brand, Optional<String> city,
            Optional<String> country, Optional<String> amenities);

    HotelShortInfo createHotel(Hotel hotel);

    boolean addAmenitiesToHotel(Long hotelId, String[] amenities);

    List<Histogram> makeHistogramByParameter(String parameter);
}
