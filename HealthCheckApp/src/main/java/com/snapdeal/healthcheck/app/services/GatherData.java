package com.snapdeal.healthcheck.app.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.snapdeal.healthcheck.app.model.DownTimeUIData;

public interface GatherData {

	public Map<String, List<DownTimeUIData>> getDataForHomePage(Date execDate);
	
	public double getTimePercentage(Date execDate);
}
