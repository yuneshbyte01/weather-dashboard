package com.yunesh.weatherdashboard.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "forecasts")
public class Forecast {

    @Id
    private String id;

    @NotBlank
    private String location;

    @NotNull
    private LocalDate date;

    private Double minTemp;
    private Double maxTemp;
    private String description;

    private Instant createdAt;
}

