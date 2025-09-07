package com.yunesh.weatherdashboard.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DailyForecast {
    private LocalDate date;
    private Double minTemp;
    private Double maxTemp;
    private Double humidity;
    private Double windSpeed;
    private String description;
}
