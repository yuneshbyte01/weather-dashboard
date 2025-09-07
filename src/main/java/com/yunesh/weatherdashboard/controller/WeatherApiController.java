package com.yunesh.weatherdashboard.controller;

import com.yunesh.weatherdashboard.dto.DailyForecast;
import com.yunesh.weatherdashboard.model.Alert;
import com.yunesh.weatherdashboard.model.History;
import com.yunesh.weatherdashboard.model.WeatherData;
import com.yunesh.weatherdashboard.repository.AlertRepository;
import com.yunesh.weatherdashboard.repository.HistoryRepository;
import com.yunesh.weatherdashboard.service.WeatherApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherApiController {

    private final WeatherApiService weatherApiService;
    private final HistoryRepository historyRepository;
    private final AlertRepository alertRepository;

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

    @GetMapping("/history")
    public List<History> getHistory(
            @RequestParam String city,
            @RequestParam(defaultValue = "7") int days) {

        Instant end = Instant.now();
        Instant start = end.minusSeconds((long) days * 24 * 3600);

        return historyRepository.findByLocationAndRecordedAtBetween(city, start, end);
    }

    @GetMapping("/alerts")
    public List<Alert> getAlerts(
            @RequestParam String city,
            @RequestParam(defaultValue = "24") int hours) {
        Instant end = Instant.now();
        Instant start = end.minusSeconds(hours * 3600);
        return alertRepository.findByLocationAndIssuedAtBetween(city, start, end);
    }
}
