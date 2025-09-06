package com.yunesh.weatherdashboard.service;

import com.yunesh.weatherdashboard.dto.WeatherApiResponse;
import com.yunesh.weatherdashboard.model.WeatherData;
import com.yunesh.weatherdashboard.repository.WeatherDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class WeatherApiService {

    private final WeatherDataRepository weatherDataRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    public WeatherData fetchAndSaveWeather(String city) {
        String url = String.format("%s?q=%s&appid=%s&units=metric", apiUrl, city, apiKey);

        WeatherApiResponse response;
        try {
            response = restTemplate.getForObject(url, WeatherApiResponse.class);
            if (response == null) {
                throw new RuntimeException("No data from Weather API");
            }
            // map and save...
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch weather data: " + e.getMessage(), e);
        }

        WeatherData weatherData = WeatherData.builder()
                .location(response.getName())
                .temperature(response.getMain().getTemp())
                .humidity(response.getMain().getHumidity())
                .windSpeed(response.getWind().getSpeed())
                .description(response.getWeather().getFirst().getDescription())
                .timestamp(Instant.now())
                .build();

        return weatherDataRepository.save(weatherData);
    }
}
