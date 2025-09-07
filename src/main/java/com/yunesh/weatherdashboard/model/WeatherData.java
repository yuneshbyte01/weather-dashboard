package com.yunesh.weatherdashboard.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "weather_data")
public class WeatherData {

    @Id
    private String id;

    @NotBlank
    private String location; // city, country

    @NotNull
    private Double temperature;

    @NotNull
    private Double humidity;

    @NotNull
    private Double windSpeed;

    private Double feelsLike;
    private Double pressure;

    private Instant sunrise;
    private Instant sunset;

    private String description; // e.g., sunny, cloudy

    private Instant timestamp;
}
