package com.hotels.hotels;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.format.FormatterRegistry;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.hotels.hotels.model.service.Histogram;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
@EnableTransactionManagement
public class Config implements WebMvcConfigurer {
    @Autowired
    private Environment environment;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(
                String.class, Histogram.Type.class,
                str -> Histogram.Type.fromString(str)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid parameter type")));
    }

    @Bean
    @Profile("!test")
    OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl(environment.getProperty("api.server.url"));
        server.setDescription("Hotels");
        Contact contact = new Contact();
        contact.setName("Mikhail");
        contact.setEmail("m.kravczov@inbox.ru");
        Info info = new Info()
                .title("Hotels test api")
                .version("1.0")
                .description("test task stage 2")
                .contact(contact);
        return new OpenAPI().info(info).servers(List.of(server));
    }
}
