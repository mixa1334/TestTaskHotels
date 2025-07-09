package com.hotels.hotels.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotels.hotels.dto.NewHotelDto;
import com.hotels.hotels.model.entity.Address;
import com.hotels.hotels.model.entity.ArrivalTime;
import com.hotels.hotels.model.entity.Contacts;
import com.hotels.hotels.model.entity.Hotel;
import com.hotels.hotels.model.repository.HotelRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class HotelControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    HotelRepository hotelRepository;

    @Test
    void testAddAmenitiesToHotel() throws Throwable {
        Set<String> amenities = Set.of("spa", "free wifi", "test");
        String input = objectMapper.writeValueAsString(amenities);

        Hotel hotel = hotelRepository.findById(1L).get();
        assertNotEquals(amenities, hotel.getAmenities());

        mockMvc.perform(
                post("/property-view/hotels/1/amenities").contentType(MediaType.APPLICATION_JSON).content(input))
                .andExpect(status().isOk());

        hotel = hotelRepository.findById(1L).get();
        assertEquals(amenities, hotel.getAmenities());
    }

    @Test
    void testAddAmenitiesToHotel_invalid_input() throws Throwable {
        mockMvc.perform(
                post("/property-view/hotels/1/amenities").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of())))
                .andExpect(status().isBadRequest());
        mockMvc.perform(
                post("/property-view/hotels/1/amenities").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mockMvc.perform(
                post("/property-view/hotels/-1/amenities").contentType(MediaType.APPLICATION_JSON).content("input"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(
                post("/property-view/hotels/id/amenities").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of("spa"))))
                .andExpect(status().isBadRequest());
        mockMvc.perform(
                post("/property-view/hotels/9/amenities").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of("spa"))))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateHotel() throws Throwable {
        NewHotelDto newHotelDto = new NewHotelDto("Test hotel", null, "some brand",
                new Address(89, "Some street", "city", "country", "235443"),
                new Contacts("+375 17 333-33-33", "hello@gmail.com"),
                new ArrivalTime(LocalTime.of(13, 34), null));
        String input = objectMapper.writeValueAsString(newHotelDto);

        MvcResult result = mockMvc
                .perform(post("/property-view/hotels").contentType(MediaType.APPLICATION_JSON).content(input))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(r -> assertTrue(
                        r.getResponse().getHeader(HttpHeaders.LOCATION).contains("/property-view/hotels/")))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.length()").value(5))
                .andReturn();

        String location = result.getResponse().getHeader(HttpHeaders.LOCATION);
        Hotel fromDb = hotelRepository.findById(Long.parseLong(location.substring(location.lastIndexOf('/') + 1)))
                .get();
        assertEquals(newHotelDto.toHotel(), fromDb);
    }

    @Test
    void testCreateHotel_invalid_input() throws Throwable {
        NewHotelDto newHotelDto = new NewHotelDto("Test hotel", null, "some brand",
                new Address(89, "Some street", "city", "country", "235443"),
                new Contacts("+375 17 333-33-33", "hello@gmail.com"),
                new ArrivalTime(LocalTime.of(13, 34), null));
        NewHotelDto newHotelDto_invalid = new NewHotelDto(null, null, "some brand",
                new Address(-89, "Some street", "city", "country", "235443"),
                new Contacts("+375 17 333-33-33", "hello@gmail.com"),
                new ArrivalTime(LocalTime.of(13, 34), null));
        String input = objectMapper.writeValueAsString(newHotelDto);
        String invalidInput = objectMapper.writeValueAsString(newHotelDto_invalid);

        mockMvc.perform(post("/property-view/hotels").contentType(MediaType.APPLICATION_JSON).content(invalidInput))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/property-view/hotels").contentType(MediaType.APPLICATION_JSON).content(input))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/property-view/hotels").contentType(MediaType.APPLICATION_JSON).content(input))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllHotels() throws Throwable {
        mockMvc.perform(get("/property-view/hotels").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("DoubleTree by Hilton Minsk"))
                .andExpect(jsonPath("$[0].address").value("9 Pobediteley Avenue, Minsk, 220004, Belarus"))
                .andExpect(jsonPath("$[0].phone").value("+375 17 309-80-00"))
                .andExpect(jsonPath("$[1].id").isNumber())
                .andExpect(jsonPath("$[2].name").value("Beijing Hotel Minsk"))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void testGetHotelById() throws Throwable {
        mockMvc.perform(get("/property-view/hotels/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("DoubleTree by Hilton Minsk"))
                .andExpect(jsonPath("$.brand").value("Hilton"))
                .andExpect(jsonPath("$.contacts.phone").value("+375 17 309-80-00"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.arrivalTime.checkOut").value("12:00"))
                .andExpect(jsonPath("$.length()").value(8))
                .andExpect(jsonPath("$.amenities.length()").value(10));
    }

    @Test
    void testGetHotelById_invdali_input() throws Throwable {
        mockMvc.perform(get("/property-view/hotels/-3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/property-view/hotels/hello").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/property-view/hotels/7").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetHotelsByParameters_single() throws Throwable {
        mockMvc.perform(get("/property-view/search?test=hello").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));

        mockMvc.perform(get("/property-view/search?city=minsK&test=hello").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].length()").value(5))
                .andExpect(jsonPath("$[0].name").value("DoubleTree by Hilton Minsk"));

        mockMvc.perform(get("/property-view/search?brand=hilton").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].length()").value(5))
                .andExpect(jsonPath("$[0].name").value("DoubleTree by Hilton Minsk"));

        mockMvc.perform(get("/property-view/search?city=brest").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].length()").value(5))
                .andExpect(jsonPath("$[0].name").value("Beijing Hotel Minsk"));

        mockMvc.perform(get("/property-view/search?country=belarus").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].length()").value(5));

        mockMvc.perform(get("/property-view/search?name=hotel").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].length()").value(5))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[1].id").value(3));

        mockMvc.perform(get("/property-view/search?amenities=free parking&amenities=pet")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].length()").value(5))
                .andExpect(jsonPath("$[0].name").value("DoubleTree by Hilton Minsk"));
    }

    @Test
    void testGetHotelsByParameters_mixed() throws Throwable {
        mockMvc.perform(
                get("/property-view/search?brand=hilton&country=russia").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
        mockMvc.perform(
                get("/property-view/search?country=belarus&amenities=free").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void testMakeHistogramByParameter() throws Throwable {
        mockMvc.perform(get("/property-view/histogram/city").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.Minsk").value(2))
                .andExpect(jsonPath("$.Brest").value(1));

        mockMvc.perform(get("/property-view/histogram/amenities").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(16));

        mockMvc.perform(get("/property-view/histogram/brand").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));

        mockMvc.perform(get("/property-view/histogram/country").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.Belarus").isNumber())
                .andExpect(jsonPath("$.Belarus").value(3));
    }

    @Test
    void testMakeHistogramByParameter_invalid() throws Throwable {
        mockMvc.perform(get("/property-view/histogram/testparam").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
