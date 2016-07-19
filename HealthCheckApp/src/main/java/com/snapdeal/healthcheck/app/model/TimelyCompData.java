package com.snapdeal.healthcheck.app.model;

public class TimelyCompData implements Comparable<TimelyCompData>{

	private String componentName;
	private int totalDownTimes;
	private int totalTimeDownInMins;
	private String totalTimeDownStr;
	
	public TimelyCompData(String componentName) {
		this.componentName = componentName;
		this.totalDownTimes = 0;
		this.totalTimeDownInMins = 0;
	}

	public String getComponentName() {
		return componentName;
	}

	public String getTotalTimeDownStr() {
		return totalTimeDownStr;
	}

	public void setTotalTimeDownStr(String totalTimeDownStr) {
		this.totalTimeDownStr = totalTimeDownStr;
	}

	public int getTotalDownTimes() {
		return totalDownTimes;
	}

	public void setTotalDownTimes(int totalDownTimes) {
		this.totalDownTimes = totalDownTimes;
	}

	public int getTotalTimeDownInMins() {
		return totalTimeDownInMins;
	}

	public void setTotalTimeDownInMins(int totalTimeDownInMins) {
		this.totalTimeDownInMins = totalTimeDownInMins;
	}

	@Override
	public int hashCode() {
		return this.componentName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof TimelyCompData)
			return this.componentName.equals(((TimelyCompData) obj).getComponentName());
		return false;
	}
	
	@Override
	public int compareTo(TimelyCompData obj) {
		return this.componentName.compareTo(obj.getComponentName());
	}
	
	@Override
	public String toString() {
		return "TimelyCompData [componentName=" + componentName + ", totalDownTimes=" + totalDownTimes
				+ ", totalTimeDownInMins=" + totalTimeDownInMins + "]";
	}

}
