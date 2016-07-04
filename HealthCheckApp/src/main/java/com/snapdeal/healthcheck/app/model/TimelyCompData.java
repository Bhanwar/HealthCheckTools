package com.snapdeal.healthcheck.app.model;

public class TimelyCompData {

	private int totalDownTimes;
	private int totalTimeDownInMins;
	
	public TimelyCompData() {
		this.totalDownTimes = 0;
		this.totalTimeDownInMins = 0;
	}
	
	public int getTotalDownTimes() {
		return totalDownTimes;
	}
	public void setTotalDownTimes(int totalDownTimes) {
		this.totalDownTimes = totalDownTimes;
	}
	public int gettotalTimeDownInMins() {
		return totalTimeDownInMins;
	}
	public void settotalTimeDownInMins(int totalTimeDownInMins) {
		this.totalTimeDownInMins = totalTimeDownInMins;
	}
}
