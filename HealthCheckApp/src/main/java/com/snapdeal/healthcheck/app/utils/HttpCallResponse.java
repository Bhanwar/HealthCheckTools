package com.snapdeal.healthcheck.app.utils;

public class HttpCallResponse {

	String statusCode;
	String responseBody;
	String httpCallException;
	
	public String getHttpCallException() {
		return httpCallException;
	}
	public void setHttpCallException(String httpCallException) {
		this.httpCallException = httpCallException;
	}
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
