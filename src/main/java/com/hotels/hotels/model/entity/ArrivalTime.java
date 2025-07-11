package com.hotels.hotels.model.entity;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class ArrivalTime {
    @Column(name = "check_in", nullable = false)
    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private LocalTime checkIn;

    @Column(name = "check_out")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime checkOut;

    public ArrivalTime() {
    }

    public ArrivalTime(@NotNull LocalTime checkIn, LocalTime checkOut) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public LocalTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalTime checkOut) {
        this.checkOut = checkOut;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((checkIn == null) ? 0 : checkIn.hashCode());
        result = prime * result + ((checkOut == null) ? 0 : checkOut.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ArrivalTime))
            return false;
        ArrivalTime other = (ArrivalTime) obj;
        if (checkIn == null) {
            if (other.checkIn != null)
                return false;
        } else if (!checkIn.equals(other.checkIn))
            return false;
        if (checkOut == null) {
            if (other.checkOut != null)
                return false;
        } else if (!checkOut.equals(other.checkOut))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ArrivalTime [checkIn=" + checkIn + ", checkOut=" + checkOut + "]";
    }
}
