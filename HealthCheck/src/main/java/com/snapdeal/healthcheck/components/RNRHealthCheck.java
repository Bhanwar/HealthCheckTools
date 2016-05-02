package com.snapdeal.healthcheck.components;

import static com.snapdeal.healthcheck.httputil.HttpCall.callGet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.JsonPath;
import com.snapdeal.healthcheck.httputil.HttpCallResponse;

public class RNRHealthCheck {

	private String endPoint;

	public String getEndPoint() {
		return endPoint;
	}

	public RNRHealthCheck(String endPoint) {
		this.endPoint = endPoint;
	}

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public boolean isServerUp() {
		boolean isServerUp = false;
		String url = endPoint + "/reviews-api/api/service/reviews/health";
		HttpCallResponse resp = callGet(url);
		if (resp.getStatusCode() != null && resp.getStatusCode().equals("200 OK") && resp.getResponseBody() != null) {
			isServerUp = JsonPath.read(resp.getResponseBody(), "$.status").toString().equalsIgnoreCase("Up and Running");			
		}
		logger.debug("Status code: " + resp.getStatusCode());
		logger.debug("Response Body: " + resp.getResponseBody());
		return isServerUp;
	}

}
