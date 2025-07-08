package com.hotels.hotels.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "hotels", uniqueConstraints = @UniqueConstraint(name = "unique_address", columnNames = { "house_number",
        "street", "city", "country", "postcode" }))
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    @NotBlank
    private String brand;

    @Embedded
    @Valid
    private Address address;

    @Embedded
    @Valid
    private Contacts contacts;

    @Embedded
    @Valid
    private ArrivalTime arrivalTime;

    @ElementCollection
    @CollectionTable(name = "amenities", joinColumns = @JoinColumn(name = "hotel_id"), uniqueConstraints = @UniqueConstraint(columnNames = {"hotel_id", "amenity" }))
    @Column(name = "amenity")
    private List<String> amenities = new ArrayList<>();

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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((brand == null) ? 0 : brand.hashCode());
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + ((contacts == null) ? 0 : contacts.hashCode());
        result = prime * result + ((arrivalTime == null) ? 0 : arrivalTime.hashCode());
        result = prime * result + ((amenities == null) ? 0 : amenities.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Hotel))
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
        if (brand == null) {
            if (other.brand != null)
                return false;
        } else if (!brand.equals(other.brand))
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
        if (amenities == null) {
            if (other.amenities != null)
                return false;
        } else if (!amenities.equals(other.amenities))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Hotel [id=" + id + ", name=" + name + ", description=" + description + ", brand=" + brand + ", address="
                + address + ", contacts=" + contacts + ", arrivalTime=" + arrivalTime + ", amenities=" + amenities
                + "]";
    }
}
