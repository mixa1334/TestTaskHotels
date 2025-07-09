package com.hotels.hotels.model.entity;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Embeddable
public class Contacts {
    @Column(nullable = false, unique = true, length = 30)
    @NotBlank
    @Pattern(regexp = "^\\+375\\s(17|29|44|33|25)\\s\\d{3}-\\d{2}-\\d{2}$")
    private String phone;

    @Column(nullable = false, unique = true, length = 50)
    @NotBlank
    @Email
    @Length(max = 50)
    private String email;

    public Contacts(@NotBlank String phone, @NotBlank String email) {
        this.phone = phone;
        this.email = email;
    }

    public Contacts() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((phone == null) ? 0 : phone.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Contacts))
            return false;
        Contacts other = (Contacts) obj;
        if (phone == null) {
            if (other.phone != null)
                return false;
        } else if (!phone.equals(other.phone))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Contacts [phone=" + phone + ", email=" + email + "]";
    }
}
