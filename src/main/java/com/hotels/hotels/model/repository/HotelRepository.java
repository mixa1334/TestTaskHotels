package com.hotels.hotels.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotels.hotels.model.entity.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

}
