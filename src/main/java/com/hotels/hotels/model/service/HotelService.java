package com.hotels.hotels.model.service;

import java.util.List;
import java.util.Optional;

import com.hotels.hotels.model.entity.Hotel;

public interface HotelService {
    List<Hotel> getAll();

    Optional<Hotel> getById(Long id);

    List<Hotel> getHotelsByParameters(Optional<String> name, Optional<String> brand, Optional<String> city,
            Optional<String> country, Optional<String[]> amenities);

    Hotel createHotel(Hotel hotel);

    boolean addAmenitiesToHotel(Long id, String[] amenities);

    List<Histogram> makeHistogramByParameter(Histogram.Type parameter);
}
