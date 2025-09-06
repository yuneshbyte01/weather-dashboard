package com.yunesh.weatherdashboard.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DailyForecast {
    private String date;
    private Double minTemp;
    private Double maxTemp;
    private Double humidity;
    private Double windSpeed;
    private String description;
}
