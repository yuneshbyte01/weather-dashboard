package com.yunesh.weatherdashboard.controller;

import com.yunesh.weatherdashboard.model.WeatherData;
import com.yunesh.weatherdashboard.service.WeatherApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherApiController {

    private final WeatherApiService weatherApiService;

    @GetMapping("/{city}")
    public WeatherData getWeather(@PathVariable String city) {
        return weatherApiService.fetchAndSaveWeather(city);
    }
}
