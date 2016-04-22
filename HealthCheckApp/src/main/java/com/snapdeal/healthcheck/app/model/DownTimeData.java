package com.snapdeal.healthcheck.app.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "downTimeData")
public class DownTimeData {

	@Id
	private String id;

	private String componentName;
	private String downTime;
	private String upTime;
	private String totalDownTime;
	private String date;
	private Date execDate;
	
	public Date getExecDate() {
		return execDate;
	}
	public void setExecDate(Date execDate) {
		this.execDate = execDate;
	}
	public String getComponentName() {
		return componentName;
	}
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
	public String getDownTime() {
		return downTime;
	}
	public void setDownTime(String downTime) {
		this.downTime = downTime;
	}
	public String getUpTime() {
		return upTime;
	}
	public void setUpTime(String upTime) {
		this.upTime = upTime;
	}
	public String getTotalDownTime() {
		return totalDownTime;
	}
	public void setTotalDownTime(String totalDownTime) {
		this.totalDownTime = totalDownTime;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "DownTimeData [componentName=" + componentName + ", downTime=" + downTime + ", upTime=" + upTime
				+ ", totalDownTime=" + totalDownTime + ", date=" + date + "]";
	}
	
	

}
