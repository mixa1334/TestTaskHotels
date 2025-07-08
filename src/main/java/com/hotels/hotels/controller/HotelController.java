package com.hotels.hotels.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotels.hotels.dto.HistogramDto;
import com.hotels.hotels.dto.HotelShortInfo;
import com.hotels.hotels.dto.NewHotelDto;
import com.hotels.hotels.model.entity.Hotel;
import com.hotels.hotels.model.service.Histogram;
import com.hotels.hotels.model.service.HotelService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/property-view")
@Validated
public class HotelController {
    @Autowired
    private HotelService service;

    @GetMapping("/hotels")
    public List<HotelShortInfo> getAllHotels() {
        return service
                .getAll()
                .stream()
                .map(HotelShortInfo::fromHotel)
                .toList();
    }

    @GetMapping("/hotels/{id}")
    public ResponseEntity<Hotel> getHotelById(
            @PathVariable @Positive(message = "invalid id: must be positive number") Long id) {
        return ResponseEntity.of(service.getById(id));
    }

    @GetMapping("/search")
    public List<HotelShortInfo> getHotelsByParameters(@RequestParam Optional<String> name,
            @RequestParam Optional<String> brand, @RequestParam Optional<String> city,
            @RequestParam Optional<String> country, @RequestParam Optional<String[]> amenities) {
        return service
                .getHotelsByParameters(name, brand, city, country, amenities)
                .stream()
                .map(HotelShortInfo::fromHotel)
                .toList();
    }

    @GetMapping("/histogram/{param}")
    public Map<String, Long> makeHistogramByParameter(@PathVariable Histogram.Type param) {
        return HistogramDto
                .fromHistogram(service.makeHistogramByParameter(param))
                .getMap();
    }

    @PostMapping("/hotels")
    public ResponseEntity<HotelShortInfo> createHotel(HttpServletRequest request,
            @RequestBody @Valid NewHotelDto newHotelDto) {
        HotelShortInfo hotel = HotelShortInfo.fromHotel(service.createHotel(newHotelDto.toHotel()));
        String responseUri = request.getRequestURI() + "/" + hotel.id();
        return ResponseEntity
                .created(URI.create(responseUri))
                .body(hotel);
    }

    @PostMapping("/hotels/{id}/amenities")
    public ResponseEntity<?> addAmenitiesToHotel(
            @PathVariable @Positive(message = "invalid id: must be positive number") Long id,
            @RequestBody @NotEmpty(message = "invalid amenities: cant be empty") String[] amenities) {
        return service.addAmenitiesToHotel(id, amenities)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}
