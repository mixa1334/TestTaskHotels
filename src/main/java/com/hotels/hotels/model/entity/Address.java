package com.hotels.hotels.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    @Column(name = "house_number", nullable = false)
    private int houseNumber;
    @Column(nullable = false)
    private String street;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private String postcode;

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + houseNumber;
        result = prime * result + ((street == null) ? 0 : street.hashCode());
        result = prime * result + ((city == null) ? 0 : city.hashCode());
        result = prime * result + ((country == null) ? 0 : country.hashCode());
        result = prime * result + ((postcode == null) ? 0 : postcode.hashCode());
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
        Address other = (Address) obj;
        if (houseNumber != other.houseNumber)
            return false;
        if (street == null) {
            if (other.street != null)
                return false;
        } else if (!street.equals(other.street))
            return false;
        if (city == null) {
            if (other.city != null)
                return false;
        } else if (!city.equals(other.city))
            return false;
        if (country == null) {
            if (other.country != null)
                return false;
        } else if (!country.equals(other.country))
            return false;
        if (postcode == null) {
            if (other.postcode != null)
                return false;
        } else if (!postcode.equals(other.postcode))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Address [houseNumber=" + houseNumber + ", street=" + street + ", city=" + city + ", country=" + country
                + ", postcode=" + postcode + "]";
    }
}
