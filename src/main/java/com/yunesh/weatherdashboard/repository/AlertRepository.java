package com.yunesh.weatherdashboard.repository;

import com.yunesh.weatherdashboard.model.Alert;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface AlertRepository extends MongoRepository<Alert, String> {
    List<Alert> findByLocationAndIssuedAtBetween(String location, Instant start, Instant end);
}
