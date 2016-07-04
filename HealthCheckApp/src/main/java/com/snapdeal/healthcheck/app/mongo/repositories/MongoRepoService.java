package com.snapdeal.healthcheck.app.mongo.repositories;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import static com.snapdeal.healthcheck.app.constants.Formatter.dateFormatter;
import static com.snapdeal.healthcheck.app.constants.AppConstant.currentExecDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.snapdeal.healthcheck.app.bo.TokenApiDetailsBO;
import com.snapdeal.healthcheck.app.model.ConnTimedOutComp;
import com.snapdeal.healthcheck.app.model.DownTimeData;
import com.snapdeal.healthcheck.app.model.TimelyCompData;

public class MongoRepoService {
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private DownTimeDataRepository downTimeRepo;
	
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
	
	public List<DownTimeData> findAllCurrentExecDownTimeDataForDate(String execDate) {
		return downTimeRepo.findAllExecForDate(execDate);
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
	
	public Map<String, TimelyCompData> getTimelyData(Date startDate, Date endDate) {
		
		log.debug("Fetching timely data from Mongo!");
		if(startDate.compareTo(endDate) > 0) {
			log.debug("Start date is greater than End date!!");
			return null;
		}
		
		Map<String, TimelyCompData> result = new TreeMap<String, TimelyCompData>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		
		Date runningDate = cal.getTime();
		while(runningDate.compareTo(endDate) <= 0) {
			String execDate = dateFormatter.format(runningDate);
			List<DownTimeData> downTimeList = downTimeFindAllForDate(execDate);
			if(!downTimeList.isEmpty()) {
				for(DownTimeData downTime : downTimeList) {
					String compName = downTime.getComponentName();
					int totalDownTime=0;
					if(downTime.getServerUp().equals("YES"))
						totalDownTime = Integer.valueOf(downTime.getTotalDownTimeInMins());
					else {
						totalDownTime = (int) ((currentExecDate.getTime() - downTime.getDownTime().getTime()) / 60000);
					}
					if(!result.containsKey(compName))
						result.put(compName, new TimelyCompData());
					
					TimelyCompData compData = result.get(compName);
					
					compData.setTotalDownTimes(compData.getTotalDownTimes() + 1);
					compData.settotalTimeDownInMins(compData.gettotalTimeDownInMins() + totalDownTime);
				}
			}
			cal.add(Calendar.DATE, 1);
			runningDate = cal.getTime();
		}
		log.debug("Result Map size: " + result.size());
		return result;
	}
}