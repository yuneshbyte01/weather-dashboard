package com.yunesh.weatherdashboard.repository;

import com.yunesh.weatherdashboard.model.History;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HistoryRepository extends MongoRepository<History, String> {}
