package com.snapdeal.healthcheck.app.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.snapdeal.healthcheck.app.model.ConnTimedOutComp;
@Repository
public interface ConnTimedOutRepository extends MongoRepository<ConnTimedOutComp, String>{

	@Query("{ 'componentName' : ?0}")
	public ConnTimedOutComp findIfConnTimedOut(String compName);
}
