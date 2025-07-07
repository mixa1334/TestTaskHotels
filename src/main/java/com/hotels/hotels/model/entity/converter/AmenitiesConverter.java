package com.hotels.hotels.model.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class AmenitiesConverter implements AttributeConverter<String[], String> {
    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(String[] arg0) {
        return arg0 == null ? "" : String.join(DELIMITER, arg0);
    }

    @Override
    public String[] convertToEntityAttribute(String arg0) {
        return arg0.split(DELIMITER);
    }
}
