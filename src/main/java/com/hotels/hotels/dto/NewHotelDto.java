package com.hotels.hotels.dto;

import com.hotels.hotels.model.entity.Address;
import com.hotels.hotels.model.entity.ArrivalTime;
import com.hotels.hotels.model.entity.Contacts;
import com.hotels.hotels.model.entity.Hotel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record NewHotelDto(
        @NotBlank String name,
        String description,
        @NotBlank String brand,
        @Valid Address address,
        @Valid Contacts contacts,
        @Valid ArrivalTime arrivalTime) {

    public Hotel toHotel() {
        Hotel hotel = new Hotel();
        hotel.setName(name);
        hotel.setDescription(description);
        hotel.setBrand(brand);
        hotel.setAddress(address);
        hotel.setContacts(contacts);
        hotel.setArrivalTime(arrivalTime);
        return hotel;
    }
}
