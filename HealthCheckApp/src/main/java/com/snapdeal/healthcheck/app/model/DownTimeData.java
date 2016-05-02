package com.snapdeal.healthcheck.app.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.snapdeal.healthcheck.app.enums.DownTimeReasonCode;

@Document(collection = "downTimeData")
public class DownTimeData {

	@Id
	private String id;

	private String componentName;
	private Date downTime;
	private Date upTime;
	private String serverUp;
	private String totalDownTimeInMins;
	private DownTimeReasonCode reasonCode;
	private String description;
	private String startDate;
	private String endDate;
	private String execDate;
	
	public DownTimeReasonCode getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(DownTimeReasonCode reasonCode) {
		this.reasonCode = reasonCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getComponentName() {
		return componentName;
	}
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
	public Date getDownTime() {
		return downTime;
	}
	public void setDownTime(Date downTime) {
		this.downTime = downTime;
	}
	public String getServerUp() {
		return serverUp;
	}
	public void setServerUp(String serverUp) {
		this.serverUp = serverUp;
	}
	public Date getUpTime() {
		return upTime;
	}
	public void setUpTime(Date upTime) {
		this.upTime = upTime;
	}
	public String getTotalDownTimeInMins() {
		return totalDownTimeInMins;
	}
	public void setTotalDownTimeInMins(String totalDownTimeInMins) {
		this.totalDownTimeInMins = totalDownTimeInMins;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getExecDate() {
		return execDate;
	}
	public void setExecDate(String execDate) {
		this.execDate = execDate;
	}
	
	@Override
	public String toString() {
		return "DownTimeData [componentName=" + componentName + ", downTime=" + downTime + ", upTime=" + upTime
				+ ", serverUp=" + serverUp + ", totalDownTimeInMins=" + totalDownTimeInMins + ", reasonCode="
				+ reasonCode + ", description=" + description + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", execDate=" + execDate + "]";
	}
	
	
}
