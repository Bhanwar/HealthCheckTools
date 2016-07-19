package com.snapdeal.healthcheck.app.model;

import java.util.Date;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "connIssueData")
public class ConnIssueComp {

	@Id
	private String id;

	private String componentName;
	private Date downTime;
	private Date upTime;
	private String status;
	private String totalDownTimeInMins;
	private String startDate;
	private String endDate;
	private Set<String> execDate;
	private String issueType;
	private String httpCallException;
	
	public ConnIssueComp(String componentName) {
		this.componentName = componentName;
	}
	
	public String getId() {
		return id;
	}
	public String getComponentName() {
		return componentName;
	}
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
	public String getIssueType() {
		return issueType;
	}
	public String getHttpCallException() {
		return httpCallException;
	}

	public void setHttpCallException(String httpCallException) {
		this.httpCallException = httpCallException;
	}
	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}
	public Date getDownTime() {
		return downTime;
	}
	public void setDownTime(Date downTime) {
		this.downTime = downTime;
	}
	public Date getUpTime() {
		return upTime;
	}
	public void setUpTime(Date upTime) {
		this.upTime = upTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public Set<String> getExecDate() {
		return execDate;
	}
	public void setExecDate(Set<String> execDate) {
		this.execDate = execDate;
	}
	
}
