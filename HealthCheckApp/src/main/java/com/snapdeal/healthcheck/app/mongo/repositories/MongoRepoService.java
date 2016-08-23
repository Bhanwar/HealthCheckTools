package com.snapdeal.healthcheck.app.mongo.repositories;

import static com.snapdeal.healthcheck.app.constants.Formatter.dateFormatter;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.snapdeal.healthcheck.app.bo.TokenApiDetailsBO;
import com.snapdeal.healthcheck.app.model.ConnIssueComp;
import com.snapdeal.healthcheck.app.model.DownTimeData;
import com.snapdeal.healthcheck.app.model.TimelyCompData;

public class MongoRepoService {
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private DownTimeDataRepository downTimeRepo;
	
	@Autowired
	private ConnIssueRepository connIssueRepo;
	
	@Autowired
	private TokenApiDetailsBO tokenDetails;
	
	public void save(DownTimeData data) {
		downTimeRepo.save(data);
	}
	
	public void save(List<DownTimeData> dataList) {
		downTimeRepo.save(dataList);
	}
	
	public List<DownTimeData> getAllDataForComp(String compName) {
		return downTimeRepo.findAllDataForComp(compName);
	}
	
	public DownTimeData findUpTimeUpdate(String compName) {
		return downTimeRepo.findUpTimeUpdate(compName, "NO");
	}
	
	public List<ConnIssueComp> findAllOpenIssues() {
		return connIssueRepo.findAllOpenIssues();
	}
	
	public List<DownTimeData> downTimeFindAllForDate(String date) {
		return downTimeRepo.findAllDownForDate(date);
	}
	
	public List<DownTimeData> findAllDownTimeData() {
		return downTimeRepo.findAllDownTimeData("NO");
	}
	
	public ConnIssueComp getNetworkIssueEntry(String compName) {
		return connIssueRepo.getNetworkIssueEntry(compName);
	}
	
	public ConnIssueComp getConnTimedOutEntry(String compName) {
		return connIssueRepo.getConnectionTimedOutEntry(compName);
	}
	
	public List<DownTimeData> findAllCurrentExecDownTimeDataForDate(String execDate) {
		return downTimeRepo.findAllExecForDate(execDate);
	}
	
	public void save(ConnIssueComp data) {
		connIssueRepo.save(data);
	}

	public TokenApiDetailsBO getTokenDetails() {
		return tokenDetails;
	}

	public void setTokenDetails(TokenApiDetailsBO tokenDetails) {
		this.tokenDetails = tokenDetails;
	}
	
	public Set<TimelyCompData> getTimelyData(Date startDate, Date endDate, Comparator<TimelyCompData> comparator) {
		
		log.debug("Fetching timely data from Mongo!");
		if(startDate.compareTo(endDate) > 0) {
			log.debug("Start date is greater than End date!!");
			return null;
		}
		int maxTimeOutInMins = 0;
		Date currentDate = new Date();
		Set<TimelyCompData> resultSet = null;
		if(comparator != null)
			resultSet = new TreeSet<>(comparator);
		else
			resultSet = new TreeSet<>();
		
		Map<String, TimelyCompData> result = new TreeMap<String, TimelyCompData>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		Set<String> compId = new HashSet<>();
		Date runningDate = cal.getTime();
		while(runningDate.compareTo(endDate) <= 0) {
			String execDate = dateFormatter.format(runningDate);
			List<DownTimeData> downTimeList = findAllCurrentExecDownTimeDataForDate(execDate);
			if(!downTimeList.isEmpty()) {
				for(DownTimeData downTime : downTimeList) {
					if (compId.add(downTime.getId())) {
						String compName = downTime.getComponentName();
						int totalDownTime = 0;
						if (downTime.getServerUp().equals("YES"))
							totalDownTime = Integer.valueOf(downTime.getTotalDownTimeInMins());
						else {
							totalDownTime = (int) ((currentDate.getTime() - downTime.getDownTime().getTime()) / 60000);
						}
						if (!result.containsKey(compName))
							result.put(compName, new TimelyCompData(compName));

						TimelyCompData compData = result.get(compName);
						int totalTimeDownInMins = compData.getTotalTimeDownInMins() + totalDownTime;
						if (totalTimeDownInMins > maxTimeOutInMins)
							maxTimeOutInMins = totalDownTime;

						compData.setTotalDownTimes(compData.getTotalDownTimes() + 1);
						compData.setTotalTimeDownInMins(totalTimeDownInMins);
					}
				}
			}
			cal.add(Calendar.DATE, 1);
			runningDate = cal.getTime();
		}
		
		if(!result.isEmpty()) {
			int maxHrs = maxTimeOutInMins/60;
			String strDigits = new Integer(maxHrs).toString();
			int digits = strDigits.length();
			log.debug("Max digits: " + digits);
			for(Entry<String, TimelyCompData> data : result.entrySet()) {
				TimelyCompData tmp = data.getValue();
				tmp.setTotalTimeDownStr(getTimeInStr(tmp.getTotalTimeDownInMins(), digits));
				resultSet.add(tmp);
			}
		}
		
		log.debug("Timely data result size: " + resultSet.size());
		log.debug("Max time out in mins: " + maxTimeOutInMins);
		return resultSet;
	}
	
	private String getTimeInStr(int totalTime, int digits) {
		
		int hrsTime = totalTime/60;
		int minsTime = totalTime%60;
		int hrsDigits = new Integer(hrsTime).toString().length();
		String hours = "";
		String mins = String.valueOf(minsTime);
		if(mins.length() == 1)
			mins = "0" + mins;
		if(digits < hrsDigits)
			return "Admin Please Check!!";
		else {
			while(hrsDigits < digits) {
				hours  = hours + "0";
				hrsDigits++;
			}
		}
		hours = hours + hrsTime;
		return hours + " hrs " + mins + " mins";
	}
	
	public void delete(String id) {
		downTimeRepo.delete(downTimeRepo.findById(id));
	}
}