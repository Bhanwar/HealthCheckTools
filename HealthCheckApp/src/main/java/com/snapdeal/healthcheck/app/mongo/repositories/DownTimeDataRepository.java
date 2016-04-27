package com.snapdeal.healthcheck.app.mongo.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.snapdeal.healthcheck.app.model.DownTimeData;
@Repository
public interface DownTimeDataRepository extends MongoRepository<DownTimeData, String>{

	@Query("{ 'componentName' : ?0 , 'serverUp' : ?1}")
	DownTimeData findUpTimeUpdate(String compName, String upTime);
	
	@Query("{ 'date' : ?0}")
	List<DownTimeData> findAllForDate(String date);
}
