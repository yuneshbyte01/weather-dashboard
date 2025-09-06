package com.yunesh.weatherdashboard.repository;

import com.yunesh.weatherdashboard.model.History;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface HistoryRepository extends MongoRepository<History, String> {
    List<History> findByLocationAndRecordedAtBetween(String location, Instant start, Instant end);
}
