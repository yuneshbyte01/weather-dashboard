package com.yunesh.weatherdashboard.dto;

import lombok.Data;

import java.util.List;

@Data
public class ForecastApiResponse {
    private List<ForecastItem> list;

    @Data
    public static class ForecastItem {
        private Main main;
        private List<Weather> weather;
        private Wind wind;
        private String dt_txt; // timestamp string

        @Data
        public static class Main {
            private Double temp_min;
            private Double temp_max;
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
}
