package com.snapdeal.healthcheck.app.services.impl;
import static com.snapdeal.healthcheck.app.utils.HttpCall.callGet;
import static com.snapdeal.healthcheck.app.utils.HttpCall.callPost;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.JsonPath;
import com.snapdeal.healthcheck.app.enums.Component;
import com.snapdeal.healthcheck.app.model.HealthCheckResult;
import com.snapdeal.healthcheck.app.utils.HttpCallResponse;


public class WEBHealthCheckImpl implements Callable<HealthCheckResult> {

private final Logger log = LoggerFactory.getLogger(getClass());
	
	private String endPoint;
	public WEBHealthCheckImpl(String endPoint) {
		this.endPoint = endPoint;
	}
	
	
	@Override
	public HealthCheckResult call() throws Exception {
		boolean isServerUp = false;
		String url = endPoint + "/web/healthcheck";
		
		HealthCheckResult result = new HealthCheckResult(Component.WEB.code());
		log.debug("Checking if WEB server is up on endpoint: " + endPoint);
		HttpCallResponse resp = callPost(url, 
				"{\"responseProtocol\":\"PROTOCOL_JSON\", \"requestProtocol\":\"PROTOCOL_JSON\"}");
		if (resp.getStatusCode() != null && resp.getStatusCode().equals("200 OK") && resp.getResponseBody() != null && resp.getResponseBody().contains("healthy")) {
			isServerUp = true;
		}
		log.debug("Status code: " + resp.getStatusCode());
		log.debug("Response Body: " + resp.getResponseBody());
		result.setServerUp(isServerUp);
		return result;
	}
	
}
