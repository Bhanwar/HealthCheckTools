package com.snapdeal.healthcheck.app.services.impl;

import static com.snapdeal.healthcheck.app.constants.Formatter.dateFormatter;
import static com.snapdeal.healthcheck.app.constants.Formatter.timeFormatter;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.snapdeal.healthcheck.app.enums.Component;
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
		Component[] comps = Component.values();
		Map<String, List<DownTimeUIData>> data = new HashMap<String, List<DownTimeUIData>>();
		List<DownTimeData> list = downTimeRepo.findAllExecForDate(date);
		
		for (int i = 0; i < comps.length; i++) {
			String componentName = comps[i].getName();
			List<DownTimeUIData> dataList = new ArrayList<>();
			data.put(componentName, dataList);
		}

		if (!list.isEmpty()) {
			for (DownTimeData downTime : list) {
				Date compExecTime = currExecDate;
				String upTime = "Server still down";
				String totalTime = "NA";
				DownTimeUIData uiData = new DownTimeUIData();
				Date downTimeDate = downTime.getDownTime();
				int leftMargin = 0;
				if (dateFormatter.format(downTimeDate).equals(date))
					leftMargin = getPercentageForTime(downTimeDate);
				String downTimeStr = timeFormatter.format(downTime.getDownTime()) + " "
						+ dateFormatter.format(downTime.getDownTime());
				if (downTime.getUpTime() != null) {
					compExecTime = downTime.getUpTime();
					upTime = timeFormatter.format(compExecTime) + " " + dateFormatter.format(compExecTime);
					totalTime = downTime.getTotalDownTimeInMins();
				}
				int rightMargin = getPercentageForTime(compExecTime);
				uiData.setId(downTime.getId());
				uiData.setLeftMargin(leftMargin);
				uiData.setWidth(rightMargin - leftMargin);
				uiData.setUpTime(upTime);
				uiData.setTotalTime(totalTime);
				uiData.setDownTime(downTimeStr);
				String mapKey = Component.getValueOf(downTime.getComponentName()).getName();
				List<DownTimeUIData> dataList = data.get(mapKey);
				dataList.add(uiData);
				data.put(mapKey, dataList);
			}
		}
		return data;
	}

	@Override
	public int getTimePercentage(Date execDate) {
		return getPercentageForTime(execDate);
	}

	private int getPercentageForTime(Date currExecDate) {
		double data = 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(currExecDate);
		data = ((double) cal.get(Calendar.MINUTE) / 60 + (double) cal.get(Calendar.HOUR_OF_DAY)) / 24;
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.HALF_UP);
		data = Double.valueOf(df.format(data)) * 100;
		return (int) data;
	}
}
