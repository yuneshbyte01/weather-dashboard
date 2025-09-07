package com.yunesh.weatherdashboard.service;

import com.yunesh.weatherdashboard.dto.DailyForecast;
import com.yunesh.weatherdashboard.dto.ForecastApiResponse;
import com.yunesh.weatherdashboard.dto.WeatherApiResponse;
import com.yunesh.weatherdashboard.model.Alert;
import com.yunesh.weatherdashboard.model.History;
import com.yunesh.weatherdashboard.model.WeatherData;
import com.yunesh.weatherdashboard.repository.AlertRepository;
import com.yunesh.weatherdashboard.repository.HistoryRepository;
import com.yunesh.weatherdashboard.repository.WeatherDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeatherApiService {

    private final WeatherDataRepository weatherDataRepository;
    private final HistoryRepository historyRepository;
    private final AlertRepository alertRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.forecast.url}")
    private String forecastUrl;

    // ✅ Current weather (cache 5 minutes and store history + alerts)
    @Cacheable(value = "currentWeather", key = "#city", unless = "#result == null")
    public WeatherData fetchAndSaveWeather(String city) {
        String url = String.format("%s?q=%s&appid=%s&units=metric", apiUrl, city, apiKey);

        try {
            WeatherApiResponse response = restTemplate.getForObject(url, WeatherApiResponse.class);

            if (response == null || response.getMain() == null) {
                throw new RuntimeException("No data received from Weather API");
            }

            WeatherData weatherData = WeatherData.builder()
                    .location(response.getName())
                    .temperature(response.getMain().getTemp())
                    .humidity(response.getMain().getHumidity())
                    .windSpeed(response.getWind().getSpeed())
                    .description(
                            (response.getWeather() != null && !response.getWeather().isEmpty())
                                    ? response.getWeather().get(0).getDescription() // ✅ fixed
                                    : "N/A"
                    )
                    .timestamp(Instant.now())
                    .build();

            // Save current snapshot
            weatherDataRepository.save(weatherData);

            // Save to history
            History history = History.builder()
                    .location(weatherData.getLocation())
                    .temperature(weatherData.getTemperature())
                    .humidity(weatherData.getHumidity())
                    .windSpeed(weatherData.getWindSpeed())
                    .description(weatherData.getDescription())
                    .recordedAt(Instant.now())
                    .build();

            historyRepository.save(history);

            // ✅ Check alerts
            checkAndGenerateAlerts(weatherData);

            return weatherData;

        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to fetch current weather for " + city + ": " + e.getMessage(), e);
        }
    }

    // ✅ Forecast (cache 30 minutes, aggregated daily)
    @Cacheable(value = "forecastWeather", key = "#city + '-' + #days", unless = "#result == null")
    public List<DailyForecast> fetchForecast(String city, int days) {
        String url = String.format("%s?q=%s&appid=%s&units=metric", forecastUrl, city, apiKey);

        try {
            ForecastApiResponse response = restTemplate.getForObject(url, ForecastApiResponse.class);

            if (response == null || response.getList() == null) {
                throw new RuntimeException("No forecast data from Weather API");
            }

            // Group by date (yyyy-MM-dd)
            Map<String, List<ForecastApiResponse.ForecastItem>> groupedByDate = response.getList().stream()
                    .collect(Collectors.groupingBy(item -> item.getDt_txt().split(" ")[0],
                            LinkedHashMap::new, Collectors.toList())); // preserve order

            return groupedByDate.entrySet().stream()
                    .limit(days)
                    .map(entry -> {
                        String date = entry.getKey();
                        List<ForecastApiResponse.ForecastItem> items = entry.getValue();

                        double minTemp = items.stream()
                                .mapToDouble(i -> i.getMain().getTemp_min())
                                .min().orElse(Double.NaN);

                        double maxTemp = items.stream()
                                .mapToDouble(i -> i.getMain().getTemp_max())
                                .max().orElse(Double.NaN);

                        double avgHumidity = items.stream()
                                .mapToDouble(i -> i.getMain().getHumidity())
                                .average().orElse(Double.NaN);

                        double avgWindSpeed = items.stream()
                                .mapToDouble(i -> i.getWind().getSpeed())
                                .average().orElse(Double.NaN);

                        // Pick most frequent description
                        String description = items.stream()
                                .filter(i -> i.getWeather() != null && !i.getWeather().isEmpty())
                                .collect(Collectors.groupingBy(i -> i.getWeather().get(0).getDescription(),
                                        Collectors.counting()))
                                .entrySet().stream()
                                .max(Map.Entry.comparingByValue())
                                .map(Map.Entry::getKey)
                                .orElse("N/A");

                        LocalDate parsedDate = LocalDate.parse(entry.getKey()); // key is "2025-09-07"
                        return DailyForecast.builder()
                                .date(parsedDate)
                                .minTemp(minTemp)
                                .maxTemp(maxTemp)
                                .humidity(avgHumidity)
                                .windSpeed(avgWindSpeed)
                                .description(description)
                                .build();
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to fetch forecast for " + city + ": " + e.getMessage(), e);
        }
    }

    // ✅ Alert Rules
    private void checkAndGenerateAlerts(WeatherData currentData) {
        String desc = currentData.getDescription().toLowerCase();

        // Rule 1: Severe conditions
        if (desc.contains("storm") || desc.contains("heavy rain") ||
                desc.contains("flood") || desc.contains("heatwave") ||
                desc.contains("snow") || currentData.getWindSpeed() > 20.0) {

            Alert alert = Alert.builder()
                    .location(currentData.getLocation())
                    .type("Severe Weather")
                    .message("Severe condition detected: " + desc)
                    .severity("High")
                    .issuedAt(Instant.now())
                    .build();

            alertRepository.save(alert);
        }

        // Rule 2: Sudden temperature spike in last 3 hours
        historyRepository.findByLocationAndRecordedAtBetween(
                currentData.getLocation(),
                Instant.now().minusSeconds(3 * 3600),
                Instant.now()
        ).stream().findFirst().ifPresent(last -> {
            double diff = Math.abs(currentData.getTemperature() - last.getTemperature());
            if (diff > 5.0) {
                Alert alert = Alert.builder()
                        .location(currentData.getLocation())
                        .type("Temperature Spike")
                        .message("Temperature changed by " + diff + "°C in last 3 hours")
                        .severity("Medium")
                        .issuedAt(Instant.now())
                        .build();

                alertRepository.save(alert);
            }
        });
    }
}
