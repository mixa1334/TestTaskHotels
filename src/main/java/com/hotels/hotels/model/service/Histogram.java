package com.hotels.hotels.model.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.hotels.hotels.model.repository.HotelRepository;

public record Histogram(String value, Long count) {
    public enum Type {
        BRAND(HotelRepository::histogramByBrand),
        CITY(HotelRepository::histogramByCity),
        COUNTRY(HotelRepository::histogramByCountry),
        AMENITIES(HotelRepository::histogramByAmenities);

        private final Function<HotelRepository, List<Histogram>> method;

        Type(Function<HotelRepository, List<Histogram>> method) {
            this.method = method;
        }

        public List<Histogram> apply(HotelRepository repository) {
            return method.apply(repository);
        }

        public static Optional<Type> fromString(String type) {
            if (type == null || type.isEmpty()) {
                return Optional.empty();
            }
            return Arrays
                    .stream(Type.values())
                    .filter(t -> t.name().equalsIgnoreCase(type.trim()))
                    .findFirst();
        }
    }
}
