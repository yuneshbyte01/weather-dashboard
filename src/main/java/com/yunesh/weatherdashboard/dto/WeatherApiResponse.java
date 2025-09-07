package com.yunesh.weatherdashboard.dto;

import lombok.Data;

import java.util.List;

@Data
public class WeatherApiResponse {
    private Main main;
    private Wind wind;
    private List<Weather> weather;
    private Sys sys;
    private String name;

    @Data
    public static class Main {
        private Double temp;
        private Double feels_like;
        private Double pressure;
        private Double humidity;
        private Double temp_min;
        private Double temp_max;
    }

    @Data
    public static class Wind {
        private Double speed;
    }

    @Data
    public static class Weather {
        private String description;
        private String icon;
    }

    @Data
    public static class Sys {
        private Long sunrise;
        private Long sunset;
    }
}
