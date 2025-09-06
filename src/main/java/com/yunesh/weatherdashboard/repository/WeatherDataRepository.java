package com.yunesh.weatherdashboard.repository;

import com.yunesh.weatherdashboard.model.WeatherData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WeatherDataRepository extends MongoRepository<WeatherData, String> {}
