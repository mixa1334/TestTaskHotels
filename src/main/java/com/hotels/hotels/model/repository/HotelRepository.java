package com.hotels.hotels.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.hotels.hotels.model.entity.Hotel;
import com.hotels.hotels.model.service.Histogram;

public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {
    @Query("SELECT new com.hotels.hotels.model.service.Histogram(h.brand, COUNT(h)) FROM Hotel h GROUP BY h.brand")
    List<Histogram> histogramByBrand();

    @Query("SELECT new com.hotels.hotels.model.service.Histogram(h.address.city, COUNT(h)) FROM Hotel h GROUP BY h.address.city")
    List<Histogram> histogramByCity();

    @Query("SELECT new com.hotels.hotels.model.service.Histogram(h.address.country, COUNT(h)) FROM Hotel h GROUP BY h.address.country")
    List<Histogram> histogramByCountry();

    @Query("SELECT new com.hotels.hotels.model.service.Histogram(a, COUNT(h)) FROM Hotel h JOIN h.amenities a GROUP BY a")
    List<Histogram> histogramByAmenities();
}
