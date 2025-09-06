package com.yunesh.weatherdashboard.controller;

import com.yunesh.weatherdashboard.dto.DailyForecast;
import com.yunesh.weatherdashboard.model.WeatherData;
import com.yunesh.weatherdashboard.service.WeatherApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherApiController {

    private final WeatherApiService weatherApiService;

    @GetMapping("/current")
    public WeatherData getCurrentWeather(@RequestParam String city) {
        return weatherApiService.fetchAndSaveWeather(city);
    }

    @GetMapping("/forecast")
    public List<DailyForecast> getForecast(
            @RequestParam String city,
            @RequestParam(defaultValue = "3") int days) {
        return weatherApiService.fetchForecast(city, days);
    }
}
