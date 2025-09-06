package com.yunesh.weatherdashboard.repository;

import com.yunesh.weatherdashboard.model.Forecast;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ForecastRepository extends MongoRepository<Forecast, String> {}
