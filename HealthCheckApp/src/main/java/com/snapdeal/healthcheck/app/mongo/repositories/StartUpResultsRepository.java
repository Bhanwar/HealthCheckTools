package com.snapdeal.healthcheck.app.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.snapdeal.healthcheck.app.model.StartUpResult;

@Repository
public interface StartUpResultsRepository extends MongoRepository<StartUpResult, String>{

}
