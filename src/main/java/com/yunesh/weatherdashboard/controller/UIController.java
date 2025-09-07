package com.yunesh.weatherdashboard.controller;

import com.yunesh.weatherdashboard.dto.DailyForecast;
import com.yunesh.weatherdashboard.dto.HourlyForecast;
import com.yunesh.weatherdashboard.model.Alert;
import com.yunesh.weatherdashboard.model.WeatherData;
import com.yunesh.weatherdashboard.repository.AlertRepository;
import com.yunesh.weatherdashboard.service.WeatherApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UIController {

    private final WeatherApiService weatherApiService;
    private final AlertRepository alertRepository;

    // 🏠 Home page: current weather + forecast + alerts + hourly forecast
    @GetMapping("/")
    public String home(@RequestParam(required = false) String city, Model model) {
        if (city != null && !city.isEmpty()) {
            // Current weather
            WeatherData data = weatherApiService.fetchAndSaveWeather(city);
            model.addAttribute("weather", data);
            model.addAttribute("city", city);

            // 5-day daily forecast
            List<DailyForecast> forecasts = weatherApiService.fetchForecast(city, 5);
            model.addAttribute("forecasts", forecasts);

            // Next 12-hour forecast
            List<HourlyForecast> hourly = weatherApiService.fetchHourlyForecast(city, 12);
            model.addAttribute("hourly", hourly); // ✅ renamed for consistency

            // Alerts (filtered by city)
            List<Alert> alerts = alertRepository.findAll().stream()
                    .filter(a -> a.getLocation().equalsIgnoreCase(city))
                    .toList();
            model.addAttribute("alerts", alerts);
        }
        return "index";
    }
}
