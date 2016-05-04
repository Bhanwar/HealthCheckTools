package com.snapdeal.healthcheck.app.services.impl;

import static com.snapdeal.healthcheck.app.utils.HttpCall.callGet;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapdeal.healthcheck.app.enums.Component;
import com.snapdeal.healthcheck.app.model.HealthCheckResult;
import com.snapdeal.healthcheck.app.utils.HttpCallResponse;


public class APIGatewayHealthCheckImpl implements Callable<HealthCheckResult>{

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private String endPoint;
	
	public APIGatewayHealthCheckImpl(String endPoint) {
		this.endPoint = endPoint;
	}
	
	@Override
	public HealthCheckResult call() throws Exception {
		boolean isServerUp = false;
		String url = endPoint + "/system/monitor/healthcheck";
		HealthCheckResult result = new HealthCheckResult(Component.APIGATEWAY.code());
		log.debug("Checking if API Gateway server is up on endpoint: " + endPoint);
		HttpCallResponse resp = callGet(url);
		if (resp.getStatusCode() != null && resp.getStatusCode().equals("200 OK") && resp.getResponseBody() != null
				&& resp.getResponseBody().contains("Up and Running"))
			isServerUp = true;
		log.debug("Status code: " + resp.getStatusCode());
		log.debug("Response Body: " + resp.getResponseBody());
		result.setServerUp(isServerUp);
		return result;
	}
}
