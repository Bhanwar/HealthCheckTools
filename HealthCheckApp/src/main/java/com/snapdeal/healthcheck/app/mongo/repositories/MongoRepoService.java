package com.snapdeal.healthcheck.app.mongo.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.snapdeal.healthcheck.app.bo.TokenApiDetailsBO;
import com.snapdeal.healthcheck.app.model.ConnTimedOutComp;
import com.snapdeal.healthcheck.app.model.DownTimeData;
import com.snapdeal.healthcheck.app.model.StartUpResult;

public class MongoRepoService {

	@Autowired
	private DownTimeDataRepository downTimeRepo;
	
	@Autowired
	private StartUpResultsRepository startUpDataRepo;
	
	@Autowired
	private ConnTimedOutRepository connTimedOutRepo;
	
	@Autowired
	private TokenApiDetailsBO tokenDetails;
	
	public void save(DownTimeData data) {
		downTimeRepo.save(data);
	}
	
	public DownTimeData findUpTimeUpdate(String compName) {
		return downTimeRepo.findUpTimeUpdate(compName, "NO");
	}
	
	public List<DownTimeData> downTimeFindAllForDate(String date) {
		return downTimeRepo.findAllDownForDate(date);
	}
	
	public List<DownTimeData> findAllDownTimeData() {
		return downTimeRepo.findAllDownTimeData("NO");
	}
	
	public List<StartUpResult> getStartUpData() {
		 return startUpDataRepo.findAll();
	}
	
	public ConnTimedOutComp findIfConnTimedOut(String compName) {
		return connTimedOutRepo.findIfConnTimedOut(compName);
	}
	
	public void delete(ConnTimedOutComp data) {
		connTimedOutRepo.delete(data);
	}
	
	public void save(ConnTimedOutComp data) {
		connTimedOutRepo.save(data);
	}

	public TokenApiDetailsBO getTokenDetails() {
		return tokenDetails;
	}

	public void setTokenDetails(TokenApiDetailsBO tokenDetails) {
		this.tokenDetails = tokenDetails;
	}
}
