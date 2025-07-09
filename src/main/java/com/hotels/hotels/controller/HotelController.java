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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.hotels.hotels.dto.HistogramDto;
import com.hotels.hotels.dto.HotelShortInfo;
import com.hotels.hotels.dto.NewHotelDto;
import com.hotels.hotels.logging.Loggable;
import com.hotels.hotels.model.entity.Hotel;
import com.hotels.hotels.model.service.Histogram;
import com.hotels.hotels.model.service.HotelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/property-view", produces = "application/json")
@Validated
public class HotelController {
    @Autowired
    private HotelService service;

    @GetMapping("/hotels")
    @Tag(name = "get", description = "get methods")
    @Operation(summary = "Short info about hotels", description = "Response - json with all hotels (short info)")
    @ApiResponse(responseCode = "200", description = "return current hotels from database")
    @Loggable
    public List<HotelShortInfo> getAllHotels() {
        return service
                .getAll()
                .stream()
                .map(HotelShortInfo::fromHotel)
                .toList();
    }

    @GetMapping("/hotels/{id}")
    @Tag(name = "get", description = "get methods")
    @Operation(summary = "Full information about hotel", description = "Response - json with info about hotel by its id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "return hotel by id (existed)"),
        @ApiResponse(responseCode = "400", description = "invalid input id"),
        @ApiResponse(responseCode = "404", description = "hotel does not exist")
    })
    @Loggable
    public ResponseEntity<Hotel> getHotelById(
            @PathVariable @Positive(message = "invalid id: must be positive number") Long id) {
        return ResponseEntity.of(service.getById(id));
    }

    @GetMapping("/search")
    @Tag(name = "get", description = "get methods")
    @Operation(summary = "Search hotels by parameters", description = "You can build search request with different parameters (name, brand, city, country, amenities)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "return hotels by parameters"),
        @ApiResponse(responseCode = "400", description = "invalid input parameters")
    })
    @Loggable
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
    @Tag(name = "get", description = "get methods")
    @Operation(summary = "Makes histogram based on input parameter", description = "Response - json with histogram by parameter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "return histogram by parameter"),
        @ApiResponse(responseCode = "400", description = "invalid histogram parameter")
    })
    @Loggable
    public Map<String, Long> makeHistogramByParameter(@PathVariable Histogram.Type param) {
        return HistogramDto
                .fromHistogram(service.makeHistogramByParameter(param))
                .getMap();
    }

    @PostMapping("/hotels")
    @Tag(name = "post", description = "post methods")
    @Operation(summary = "Create hotel", description = "Provide json with new hotel info")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "hotel created and return short info (include generated id)"),
        @ApiResponse(responseCode = "400", description = "invalid hotel parameters or hotel with these parameters exists")
    })
    @Loggable
    public ResponseEntity<HotelShortInfo> createHotel(@RequestBody @Valid NewHotelDto newHotelDto) {
        HotelShortInfo hotel = HotelShortInfo.fromHotel(service.createHotel(newHotelDto.toHotel()));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(hotel.id())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(hotel);
    }

    @PostMapping("/hotels/{id}/amenities")
    @Tag(name = "post", description = "post methods")
    @Operation(summary = "Add amenities to existed hotel", description = "Replace amenities for existed hotel")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "amenities were added to the hotel"),
        @ApiResponse(responseCode = "400", description = "invalid hotel id or amenities"),
        @ApiResponse(responseCode = "404", description = "hotel does not exists")
    })
    @Loggable
    public ResponseEntity<?> addAmenitiesToHotel(
            @PathVariable @Positive(message = "invalid id: must be positive number") Long id,
            @RequestBody @NotEmpty(message = "invalid amenities: cant be empty") String[] amenities) {
        return service.addAmenitiesToHotel(id, amenities)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}
