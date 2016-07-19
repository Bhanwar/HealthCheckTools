package com.snapdeal.healthcheck.app.model;

public class DownTimeUIData {
	
	String id;
	double leftMargin;
	double width;
	String downTime;
	String upTime;
	String totalTime;
	String statusCode;
	String httpExcp;
	String type;

	public double getLeftMargin() {
		return leftMargin;
	}

	public void setLeftMargin(double leftMargin) {
		this.leftMargin = leftMargin;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getHttpExcp() {
		return httpExcp;
	}

	public void setHttpExcp(String httpExcp) {
		this.httpExcp = httpExcp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	@Override
	public String toString() {
		return "DownTimeUIData [id=" + id + ", leftMargin=" + leftMargin + ", width=" + width + ", downTime=" + downTime
				+ ", upTime=" + upTime + ", totalTime=" + totalTime + "]";
	}
}
