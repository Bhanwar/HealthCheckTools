package com.snapdeal.healthcheck.app.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="healtCheckResults")
public class HealthCheckResult {

	@Id
	private String id;
	
	private String componentName;
	private boolean serverUp;
	private String execDate;
	private String execTime;
	private Date execDateTime;
	
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
	public boolean isServerUp() {
		return serverUp;
	}
	public void setServerUp(boolean serverUp) {
		this.serverUp = serverUp;
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
		return "HealthCheckResult [componentName=" + componentName + ", serverUp=" + serverUp + ", execDate=" + execDate
				+ ", execTime=" + execTime + "]";
	}

}
