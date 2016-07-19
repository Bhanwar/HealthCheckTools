package com.snapdeal.healthcheck.app.model;

import java.util.Date;

import com.snapdeal.healthcheck.app.enums.ServerStatus;

public class HealthCheckResult {

	private String componentName;
	private String execDate;
	private String execTime;
	private Date execDateTime;
	private String failedURL;
	private String failedReqJson;
	private String failedExpResp;
	private String failedStatusCode;
	private String failedActualResp;
	private String failedHttpCallException;
	private ServerStatus serverStatus;
	private boolean serverUp;
	
	public String getFailedURL() {
		return failedURL;
	}

	public ServerStatus getServerStatus() {
		return serverStatus;
	}

	public boolean isServerUp() {
		return serverUp;
	}

	public void setServerUp(boolean serverUp) {
		this.serverUp = serverUp;
	}

	public void setServerStatus(ServerStatus serverStatus) {
		this.serverStatus = serverStatus;
	}

	public void setFailedURL(String failedURL) {
		this.failedURL = failedURL;
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

	public String getFailedExpResp() {
		return failedExpResp;
	}

	public void setFailedExpResp(String failedExpResp) {
		this.failedExpResp = failedExpResp;
	}

	public String getFailedActualResp() {
		return failedActualResp;
	}

	public void setFailedActualResp(String failedActualResp) {
		this.failedActualResp = failedActualResp;
	}

	public String getFailedHttpCallException() {
		return failedHttpCallException;
	}

	public void setFailedHttpCallException(String failedHttpCallException) {
		this.failedHttpCallException = failedHttpCallException;
	}

	public HealthCheckResult(String componentName) {
		this.componentName = componentName;
	}
	
	public String getComponentName() {
		return componentName;
	}
	public Date getExecDateTime() {
		return execDateTime;
	}

	public void setExecDateTime(Date execDateTime) {
		this.execDateTime = execDateTime;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
	
	public String getExecDate() {
		return execDate;
	}
	public void setExecDate(String execDate) {
		this.execDate = execDate;
	}
	public String getExecTime() {
		return execTime;
	}
	public void setExecTime(String execTime) {
		this.execTime = execTime;
	}

	@Override
	public String toString() {
		return "HealthCheckResult [componentName=" + componentName + ", serverStatus=" + serverStatus.getCode() + ", execDate=" + execDate
				+ ", execTime=" + execTime + ", execDateTime=" + execDateTime + ", failedURL=" + failedURL
				+ ", failedReqJson=" + failedReqJson + ", failedExpResp=" + failedExpResp + ", failedStatusCode="
				+ failedStatusCode + ", failedActualResp=" + failedActualResp + ", failedHttpCallException="
				+ failedHttpCallException + "]";
	}

}
