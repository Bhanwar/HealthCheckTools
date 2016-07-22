package com.snapdeal.healthcheck.app.mongo.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.snapdeal.healthcheck.app.model.DownTimeData;
@Repository
public interface DownTimeDataRepository extends MongoRepository<DownTimeData, String>{

	@Query("{ 'componentName' : ?0 , 'serverUp' : ?1}")
	DownTimeData findUpTimeUpdate(String compName, String isServerUp);
	
	@Query("{ 'execDate' : ?0}")
	List<DownTimeData> findAllExecForDate(String date);
	
	@Query("{ 'componentName' : ?0}")
	List<DownTimeData> findAllDataForComp(String compName);
	
	@Query("{ 'execDate' : ?0}")
	List<DownTimeData> findAllExecForDateSorted(String date, Sort sort);
	
	@Query("{ 'startDate' : ?0}")
	List<DownTimeData> findAllDownForDate(String date);
	
	@Query("{ 'serverUp' : ?0 }")
	List<DownTimeData> findAllDownTimeData(String isServerUp);
	
	@Query("{ 'serverUp' : ?0 }")
	List<DownTimeData> findAllDownTimeDataSorted(String isServerUp, Sort sort);
	
	@Query("{ 'reasonCode' : 'NOTSET' }")
	List<DownTimeData> findAllDownTimeDataToUpdateReason();
	
	@Query("{ '_id' : ?0}")
	DownTimeData findById(String compId);
	
	//Write to make it Up and update upTime
}
