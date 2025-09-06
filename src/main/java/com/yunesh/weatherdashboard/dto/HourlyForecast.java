package com.yunesh.weatherdashboard.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HourlyForecast {
    private String time;
    private Double temperature;
    private Double humidity;
    private Double windSpeed;
    private String description;
}
