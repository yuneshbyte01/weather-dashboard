package com.yunesh.weatherdashboard.dto;

import lombok.Data;

import java.util.List;

@Data
public class WeatherApiResponse {
    private Main main;
    private Wind wind;
    private List<Weather> weather;
    private String name; // city

    @Data
    public static class Main {
        private Double temp;
        private Double humidity;
    }

    @Data
    public static class Wind {
        private Double speed;
    }

    @Data
    public static class Weather {
        private String description;
    }
}
