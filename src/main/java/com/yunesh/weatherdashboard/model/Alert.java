package com.yunesh.weatherdashboard.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "alerts")
public class Alert {

    @Id
    private String id;

    @NotBlank
    private String location;

    @NotBlank
    private String type; // Storm, Flood, Heatwave, etc.

    @NotBlank
    private String message;

    private String severity; // Low, Medium, High

    private Instant issuedAt;
}

