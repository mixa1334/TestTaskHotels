package com.hotels.hotels.dto;

import com.hotels.hotels.model.entity.Address;
import com.hotels.hotels.model.entity.Hotel;

public record HotelShortInfo(Long id, String name, String description, String address, String phone) {
    private static final String ADDRESS_PATTERN = "%s %s, %s, %s, %s";

    public static HotelShortInfo fromHotel(Hotel hotel) {
        Long id = hotel.getId();
        String address = formatAddress(hotel.getAddress());
        String name = hotel.getName();
        String description = hotel.getDescription();
        String phone = hotel.getContacts().getPhone();
        return new HotelShortInfo(id, name, description, address, phone);
    }

    private static String formatAddress(Address address) {
        return String.format(ADDRESS_PATTERN, address.getHouseNumber(), address.getStreet(),
                address.getCity(), address.getPostCode(), address.getCountry());
    }
}
