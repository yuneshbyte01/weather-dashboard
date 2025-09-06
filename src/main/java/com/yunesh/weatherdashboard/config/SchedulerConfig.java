package com.yunesh.weatherdashboard.config;

import com.yunesh.weatherdashboard.service.WeatherApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {

    private final WeatherApiService weatherApiService;

    // ✅ Run every hour (3,600,000 ms)
    @Scheduled(fixedRate = 3600000)
    public void logWeatherForCities() {
        List<String> cities = List.of("Kathmandu", "Pokhara", "Biratnagar"); // extend as needed

        cities.forEach(city -> {
            try {
                weatherApiService.fetchAndSaveWeather(city);
                System.out.println("🌍 Logged weather for " + city);
            } catch (Exception e) {
                System.err.println("⚠️ Failed to log weather for " + city + ": " + e.getMessage());
            }
        });
    }
}
