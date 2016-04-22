package com.snapdeal.healthcheck.app.mongo.repositories;

import org.springframework.beans.factory.annotation.Autowired;

import com.snapdeal.healthcheck.app.model.DownTimeData;
import com.snapdeal.healthcheck.app.model.HealthCheckResult;

public class MongoRepoService {

	@Autowired
	private HealthCheckResultsRepository healthCheckRepo;
	
	@Autowired
	private DownTimeDataRepository downTimeRepo;
	
	public void save(HealthCheckResult result) {
		healthCheckRepo.save(result);
	}
	
	public void save(DownTimeData data) {
		downTimeRepo.save(data);
	}
	
	public DownTimeData findUpTimeUpdate(String compName) {
		return downTimeRepo.findUpTimeUpdate(compName, "");
	}
	
}
