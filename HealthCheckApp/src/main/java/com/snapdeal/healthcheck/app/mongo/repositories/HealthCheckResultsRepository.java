package com.snapdeal.healthcheck.app.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.snapdeal.healthcheck.app.model.HealthCheckResult;

@Repository
public interface HealthCheckResultsRepository extends MongoRepository<HealthCheckResult, String>{

}