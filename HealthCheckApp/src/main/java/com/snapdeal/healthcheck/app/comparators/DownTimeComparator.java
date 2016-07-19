package com.snapdeal.healthcheck.app.comparators;

import java.util.Comparator;

import com.snapdeal.healthcheck.app.model.TimelyCompData;

public class DownTimeComparator implements Comparator<TimelyCompData> {

	@Override
	public int compare(TimelyCompData o1, TimelyCompData o2) {
		if(o1.getTotalDownTimes() > o2.getTotalDownTimes())
			return -1;
		else if(o1.getTotalDownTimes() < o2.getTotalDownTimes())
			return 1;
		else {
			if(o1.getComponentName().equals(o2.getComponentName()))
				return 0;
			else
				return o1.getComponentName().compareTo(o2.getComponentName());
		}
	}

}
