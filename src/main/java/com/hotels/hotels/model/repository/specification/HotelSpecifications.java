package com.hotels.hotels.model.repository.specification;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;

import com.hotels.hotels.model.entity.Address_;
import com.hotels.hotels.model.entity.Hotel;
import com.hotels.hotels.model.entity.Hotel_;

public class HotelSpecifications {
    private HotelSpecifications() {
    }

    public static Specification<Hotel> hasName(Optional<String> name) {
        return name.isEmpty() ? null
                : (root, query, builder) -> builder.like(builder.lower(root.get(Hotel_.NAME)), createPattern(name.get()));
    }

    public static Specification<Hotel> hasBrand(Optional<String> brand) {
        return brand.isEmpty() ? null
                : (root, query, builder) -> builder.like(builder.lower(root.get(Hotel_.BRAND)), createPattern(brand.get()));
    }

    public static Specification<Hotel> hasCity(Optional<String> city) {
        return city.isEmpty() ? null
                : (root, query, builder) -> builder.like(builder.lower(root.get(Hotel_.ADDRESS).get(Address_.CITY)), createPattern(city.get()));
    }

    public static Specification<Hotel> hasCountry(Optional<String> country) {
        return country.isEmpty() ? null
                : (root, query, builder) -> builder.like(builder.lower(root.get(Hotel_.ADDRESS).get(Address_.COUNTRY)), createPattern(country.get()));
    }

    public static Specification<Hotel> hasAmenities(Optional<String[]> amenities) {
        return amenities.isEmpty() ? null
                : (root, query, builder) -> Arrays
                        .stream(amenities.get())
                        .map(amenity -> builder.like(builder.lower(root.join(Hotel_.amenities)), createPattern(amenity)))
                        .reduce(builder.conjunction(), builder::and);
    }

    private static String createPattern(String value) {
        return "%" + value.toLowerCase() + "%";
    }
}
