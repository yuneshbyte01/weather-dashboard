package com.yunesh.weatherdashboard.repository;

import com.yunesh.weatherdashboard.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {}
