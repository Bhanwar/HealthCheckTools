package com.snapdeal.healthcheck.app.services.impl;

import static com.snapdeal.healthcheck.app.constants.AppConstant.componentNames;
import static com.snapdeal.healthcheck.app.constants.AppConstant.disabledComponentNames;
import static com.snapdeal.healthcheck.app.constants.Formatter.dateFormatter;
import static com.snapdeal.healthcheck.app.constants.Formatter.timeFormatter;
import static com.snapdeal.healthcheck.app.constants.AppConstant.healthResult;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import com.snapdeal.healthcheck.app.model.ComponentDetails;
import com.snapdeal.healthcheck.app.model.DownTimeData;
import com.snapdeal.healthcheck.app.model.DownTimeUIData;
import com.snapdeal.healthcheck.app.mongo.repositories.DownTimeDataRepository;
import com.snapdeal.healthcheck.app.services.GatherData;

@org.springframework.stereotype.Component
public class GatherDataImpl implements GatherData {

	@Autowired
	private DownTimeDataRepository downTimeRepo;
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public Map<String, List<DownTimeUIData>> getDataForHomePage(Date currExecDate) {
		log.debug("Gathering data for UI");
		String date = dateFormatter.format(currExecDate);
		
		//Map<String, List<DownTimeUIData>> data = new HashMap<String, List<DownTimeUIData>>();
		Map<String, List<DownTimeUIData>> data = new LinkedHashMap<String, List<DownTimeUIData>>();
		List<DownTimeData> list = downTimeRepo.findAllExecForDateSorted(date, new Sort(Sort.Direction.DESC, "id"));
		List<DownTimeData> listStillUp = downTimeRepo.findAllDownTimeDataSorted("NO", new Sort(Sort.Direction.ASC, "componentName"));
		
		if(!listStillUp.isEmpty()) {
			for(DownTimeData downTime : listStillUp) {
				if(data.get(downTime.getComponentName()) == null)
					if(componentNames.contains(downTime.getComponentName()))
							data.put(downTime.getComponentName(), new ArrayList<DownTimeUIData>());
			}
		}
		
		if (!list.isEmpty()) {
			for (DownTimeData downTime : list) {
				Date compExecTime = currExecDate;
				String upTime = "Server still down";
				String totalTime = "NA";
				DownTimeUIData uiData = new DownTimeUIData();
				Date downTimeDate = downTime.getDownTime();
				String statusCode = "";
				String httpCallException = "";
				double leftMargin = 0;
				if (dateFormatter.format(downTimeDate).equals(date))
					leftMargin = getPercentageForTime(downTimeDate);
				String downTimeStr = timeFormatter.format(downTime.getDownTime()) + " "
						+ dateFormatter.format(downTime.getDownTime());
				if (downTime.getUpTime() != null) {
					compExecTime = downTime.getUpTime();
					upTime = timeFormatter.format(compExecTime) + " " + dateFormatter.format(compExecTime);
					totalTime = downTime.getTotalDownTimeInMins();
				}
				if(downTime.getFailedStatusCode() != null)
					statusCode = downTime.getFailedStatusCode();
				if(downTime.getFailedHttpException() != null)
					httpCallException = downTime.getFailedHttpException();
				
				double rightMargin = getPercentageForTime(compExecTime);
				uiData.setId(downTime.getId());
				uiData.setLeftMargin(leftMargin);
				uiData.setWidth(rightMargin - leftMargin);
				uiData.setUpTime(upTime);
				uiData.setTotalTime(totalTime);
				uiData.setDownTime(downTimeStr);
				uiData.setStatusCode(statusCode);
				uiData.setHttpExcp(httpCallException);
				String mapKey = downTime.getComponentName();
				List<DownTimeUIData> dataList = null;
				if(componentNames.contains(mapKey)) {
					if(data.get(mapKey) == null)
						dataList = new ArrayList<>();
					else
						dataList = data.get(mapKey);
					dataList.add(uiData);
					data.put(mapKey, dataList);
				}
			}
		}
		
		for (String compName : componentNames) {
			List<DownTimeUIData> dataList = new ArrayList<>();
			if(data.get(compName) == null)
				data.put(compName, dataList);
		}
		
		return data;
	}

	@Override
	public double getTimePercentage(Date execDate) {
		return getPercentageForTime(execDate);
	}

	private double getPercentageForTime(Date currExecDate) {
		double data = 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(currExecDate);
		data = (((double) cal.get(Calendar.MINUTE) / 60 + (double) cal.get(Calendar.HOUR_OF_DAY)) / 24) * 100;
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.HALF_UP);
		data = Double.valueOf(df.format(data));
		return data;
	}

	@Override
	public void initializeHealthCheckResults(List<ComponentDetails> components) {
		if(healthResult == null) {
			if(componentNames == null)
				componentNames = new TreeSet<>();
			
			if(disabledComponentNames == null)
				disabledComponentNames = new TreeSet<>();
			
			log.debug("Initializing health result data..");
			healthResult = new HashMap<>();
			for (ComponentDetails comp : components) {
				String compName = comp.getComponentName();
				if(comp.isEnabled()) {
					log.debug(compName + " - Enabled: " + comp.isEnabled());
					componentNames.add(compName);
					DownTimeData upTime = downTimeRepo.findUpTimeUpdate(compName,"NO");
					if(upTime == null) {
						log.debug(compName + " : true");
						healthResult.put(compName, true);
					} else {
						log.debug(compName + " : false");
						healthResult.put(compName, false);
					}
				} else {
					disabledComponentNames.add(compName);
				}
			}
		}
	}
}
