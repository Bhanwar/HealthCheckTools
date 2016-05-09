package com.snapdeal.healthcheck.app.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.snapdeal.healthcheck.app.model.PasswordShared;
@Repository
public interface PasswordSharedRepository extends MongoRepository<PasswordShared, String>{

	@Query("{ 'componentName' : ?0}")
	PasswordShared findSharedComponents(String compName);
	
}
