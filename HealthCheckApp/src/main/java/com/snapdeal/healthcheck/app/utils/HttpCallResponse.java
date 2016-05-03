package com.snapdeal.healthcheck.app.utils;

public class HttpCallResponse {

	String statusCode;
	String responseBody;
	
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getResponseBody() {
		return responseBody;
	}
	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}
	
	@Override
	public String toString() {
		return "HttpCallResponse [statusCode=" + statusCode + ", responseBody=" + responseBody + "]";
	}
	
}
