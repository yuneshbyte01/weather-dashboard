package com.yunesh.weatherdashboard.controller;

import com.yunesh.weatherdashboard.service.WeatherApiService;
import com.yunesh.weatherdashboard.repository.AlertRepository;
import com.yunesh.weatherdashboard.dto.DailyForecast;
import com.yunesh.weatherdashboard.model.Alert;
import com.yunesh.weatherdashboard.model.WeatherData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UIController {

    private final WeatherApiService weatherApiService;
    private final AlertRepository alertRepository;

    // ✅ Home page: search + current weather + forecast + alerts
    @GetMapping("/")
    public String home(@RequestParam(required = false) String city, Model model) {
        if (city != null && !city.isEmpty()) {
            WeatherData data = weatherApiService.fetchAndSaveWeather(city);
            model.addAttribute("weather", data);
            model.addAttribute("city", city);

            // Add a 5-day forecast
            List<DailyForecast> forecasts = weatherApiService.fetchForecast(city, 5);
            model.addAttribute("forecasts", forecasts);

            // Add alerts (last 24h)
            Instant end = Instant.now();
            Instant start = end.minusSeconds(24 * 3600);
            List<Alert> alerts = alertRepository.findByLocationAndIssuedAtBetween(city, start, end);
            model.addAttribute("alerts", alerts);
        }
        return "index";
    }

    // (Optional) keep the forecast page if you still want standalone access
    @GetMapping("/forecast/{city}")
    public String forecast(@PathVariable String city, Model model) {
        List<DailyForecast> forecasts = weatherApiService.fetchForecast(city, 5);
        model.addAttribute("city", city);
        model.addAttribute("forecasts", forecasts);
        return "forecast";
    }

    // (Optional) keep an alert page if you still want standalone access
    @GetMapping("/alerts/{city}")
    public String alerts(@PathVariable String city, Model model) {
        Instant end = Instant.now();
        Instant start = end.minusSeconds(24 * 3600);
        List<Alert> alerts = alertRepository.findByLocationAndIssuedAtBetween(city, start, end);
        model.addAttribute("city", city);
        model.addAttribute("alerts", alerts);
        return "alerts";
    }
}
