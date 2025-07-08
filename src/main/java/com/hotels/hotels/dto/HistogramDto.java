package com.hotels.hotels.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.hotels.hotels.model.service.Histogram;

public class HistogramDto {
    private Map<String, Long> map = new HashMap<>();

    public static HistogramDto fromHistogram(List<Histogram> histogram) {
        HistogramDto dto = new HistogramDto();
        dto.map = histogram
                .stream()
                .collect(Collectors.toMap(Histogram::value, Histogram::count));
        return dto;
    }

    public Map<String, Long> getMap() {
        return map;
    }

    public void setMap(Map<String, Long> map) {
        this.map = map;
    }
}
