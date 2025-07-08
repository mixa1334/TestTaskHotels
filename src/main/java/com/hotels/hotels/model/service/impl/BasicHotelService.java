package com.hotels.hotels.model.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.hotels.hotels.logging.Loggable;
import com.hotels.hotels.model.entity.Address;
import com.hotels.hotels.model.entity.Contacts;
import com.hotels.hotels.model.entity.Hotel;
import com.hotels.hotels.model.repository.HotelRepository;
import com.hotels.hotels.model.service.Histogram;
import com.hotels.hotels.model.service.HotelService;

import static com.hotels.hotels.model.repository.specification.HotelSpecifications.*;

@Service
@Transactional(isolation = Isolation.SERIALIZABLE)
public class BasicHotelService implements HotelService {
    @Autowired
    private HotelRepository hotelRepository;

    @Override
    @Loggable
    public boolean addAmenitiesToHotel(Long id, String[] amenities) {
        Optional<Hotel> hotelById = hotelRepository.findById(id);
        if (hotelById.isEmpty()) {
            return false;
        }
        Hotel hotel = hotelById.get();
        hotel.getAmenities().clear();
        hotel.getAmenities().addAll(Arrays.asList(amenities));
        hotelRepository.save(hotel);
        return true;
    }

    @Override
    @Loggable
    public Hotel createHotel(Hotel hotel) {
        checkIfValuesUnique(hotel);
        return hotelRepository.save(hotel);
    }

    @Override
    @Loggable
    public List<Hotel> getAll() {
        return hotelRepository.findAll();
    }

    @Override
    @Loggable
    public Optional<Hotel> getById(Long id) {
        return hotelRepository.findById(id);
    }

    @Override
    @Loggable
    public List<Hotel> getHotelsByParameters(Optional<String> name, Optional<String> brand,
            Optional<String> city, Optional<String> country, Optional<String[]> amenities) {
        Specification<Hotel> specification = Specification.allOf(
                hasName(name),
                hasBrand(brand),
                hasCity(city),
                hasCountry(country),
                hasAmenities(amenities));
        return hotelRepository.findAll(specification);
    }

    @Override
    @Loggable
    public List<Histogram> makeHistogramByParameter(Histogram.Type parameter) {
        return parameter.apply(hotelRepository);
    }

    private void checkIfValuesUnique(Hotel hotel) {
        Address address = hotel.getAddress();
        Contacts contacts = hotel.getContacts();
        if (hotelRepository.existsByAddressStreetAndAddressHouseNumberAndAddressCityAndAddressCountryAndAddressPostCode(
                address.getStreet(), address.getHouseNumber(), address.getCity(), address.getCountry(),
                address.getPostCode())) {
            throw new IllegalArgumentException("hotel with same address exists");
        }
        if (hotelRepository.existsByContactsPhone(contacts.getPhone())) {
            throw new IllegalArgumentException("hotel with same phone number exists");
        }
        if (hotelRepository.existsByContactsEmail(contacts.getEmail())) {
            throw new IllegalArgumentException("hotel with same email exists");
        }
    }
}