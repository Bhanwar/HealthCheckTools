package com.snapdeal.healthcheck.app.model;

import java.util.Date;
import java.util.Set;

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
	private Set<String> execDate;
	private String failedUrl;
	private String failedReqJson;
	private String failedResp;
	private String failedStatusCode;
	private String failedExpResp;
	private String failedHttpException;
	
	public DownTimeReasonCode getReasonCode() {
		return reasonCode;
	}
	public String getId() {
		return id;
	}
	public String getFailedUrl() {
		return failedUrl;
	}
	public void setFailedUrl(String failedUrl) {
		this.failedUrl = failedUrl;
	}
	public String getFailedReqJson() {
		return failedReqJson;
	}
	public String getFailedStatusCode() {
		return failedStatusCode;
	}
	public void setFailedStatusCode(String failedStatusCode) {
		this.failedStatusCode = failedStatusCode;
	}
	public void setFailedReqJson(String failedReqJson) {
		this.failedReqJson = failedReqJson;
	}
	public String getFailedResp() {
		return failedResp;
	}
	public void setFailedResp(String failedResp) {
		this.failedResp = failedResp;
	}
	public String getFailedExpResp() {
		return failedExpResp;
	}
	public void setFailedExpResp(String failedExpResp) {
		this.failedExpResp = failedExpResp;
	}
	public String getFailedHttpException() {
		return failedHttpException;
	}
	public void setFailedHttpException(String failedHttpException) {
		this.failedHttpException = failedHttpException;
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
	
	public Set<String> getExecDate() {
		return execDate;
	}
	public void setExecDate(Set<String> execDate) {
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
