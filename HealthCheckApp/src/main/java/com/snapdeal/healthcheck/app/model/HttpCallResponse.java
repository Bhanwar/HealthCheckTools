package com.snapdeal.healthcheck.app.model;

import java.util.List;
import java.util.Map;

public class HttpCallResponse {

	String statusCode;
	Map<String, List<String>> responseHeaders;
	String responseBody;
	String token;
	String httpCallException;

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public Map<String, List<String>> getResponseHeaders() {
		return responseHeaders;
	}

	public void setResponseHeaders(Map<String, List<String>> responseHeaders) {
		this.responseHeaders = responseHeaders;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getHttpCallException() {
		return httpCallException;
	}

	public void setHttpCallException(String httpCallException) {
		this.httpCallException = httpCallException;
	}

	@Override
	public String toString() {
		return "HttpCallResponse [statusCode=" + statusCode + ", responseHeaders=" + responseHeaders + ", responseBody="
				+ responseBody + ", token=" + token + ", httpCallException=" + httpCallException + "]";
	}
}
