package com.hotels.hotels.model.entity;

import java.util.Arrays;

import com.hotels.hotels.model.entity.converter.AmenitiesConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "hotels", uniqueConstraints = @UniqueConstraint(name = "unique_address", columnNames = { "house_number",
        "street", "city", "country", "postcode" }))
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Embedded
    private Address address;
    @Embedded
    private Contacts contacts;
    @Embedded
    private ArrivalTime arrivalTime;
    @Column(name = "amenities")
    @Convert(converter = AmenitiesConverter.class)
    private String[] amenities;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Contacts getContacts() {
        return contacts;
    }

    public void setContacts(Contacts contacts) {
        this.contacts = contacts;
    }

    public ArrivalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(ArrivalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String[] getAmenities() {
        return amenities;
    }

    public void setAmenities(String[] amenities) {
        this.amenities = amenities;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + ((contacts == null) ? 0 : contacts.hashCode());
        result = prime * result + ((arrivalTime == null) ? 0 : arrivalTime.hashCode());
        result = prime * result + Arrays.hashCode(amenities);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Hotel other = (Hotel) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        if (contacts == null) {
            if (other.contacts != null)
                return false;
        } else if (!contacts.equals(other.contacts))
            return false;
        if (arrivalTime == null) {
            if (other.arrivalTime != null)
                return false;
        } else if (!arrivalTime.equals(other.arrivalTime))
            return false;
        return Arrays.equals(amenities, other.amenities);
    }

    @Override
    public String toString() {
        return "Hotel [id=" + id + ", name=" + name + ", description=" + description + ", address=" + address
                + ", contacts=" + contacts + ", arrivalTime=" + arrivalTime + ", amenities="
                + Arrays.toString(amenities) + "]";
    }
}
