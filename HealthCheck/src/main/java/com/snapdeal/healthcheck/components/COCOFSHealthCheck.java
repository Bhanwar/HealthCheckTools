package com.snapdeal.healthcheck.components;

import static com.snapdeal.healthcheck.httputil.HttpCall.callPost;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapdeal.healthcheck.httputil.HttpCallResponse;

public class COCOFSHealthCheck {

	private String endPoint;

	public String getEndPoint() {
		return endPoint;
	}

	public COCOFSHealthCheck(String endPoint) {
		this.endPoint = endPoint;
	}

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public boolean isServerUp() {
		boolean isServerUp = false;
		String url = endPoint + "/checkSystemHealth";
		HttpCallResponse resp = callPost(url, "");
		if (resp.getStatusCode() != null && resp.getStatusCode().equals("200 OK") && resp.getResponseBody() != null
				&& resp.getResponseBody().contains("cocofs aerospike=true, cocofs mysql database=true"))
			isServerUp = true;
		logger.debug("Status code: " + resp.getStatusCode());
		logger.debug("Response Body: " + resp.getResponseBody());
		return isServerUp;
	}

}