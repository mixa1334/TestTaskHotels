package com.hotels.hotels;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.hotels.hotels.model.service.Histogram;

@Configuration
@EnableTransactionManagement
public class Config implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(
                String.class, Histogram.Type.class,
                str -> Histogram.Type.fromString(str).orElseThrow(() -> new IllegalArgumentException("Invalid parameter type")));
    }
}
