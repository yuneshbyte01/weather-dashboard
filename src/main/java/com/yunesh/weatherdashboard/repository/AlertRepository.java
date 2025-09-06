package com.yunesh.weatherdashboard.repository;

import com.yunesh.weatherdashboard.model.Alert;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlertRepository extends MongoRepository<Alert, String> {}
