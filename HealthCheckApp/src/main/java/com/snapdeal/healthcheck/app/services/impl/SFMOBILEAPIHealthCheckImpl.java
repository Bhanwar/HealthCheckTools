package com.snapdeal.healthcheck.app.services.impl;

import static com.snapdeal.healthcheck.app.utils.HttpCall.callGet;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapdeal.healthcheck.app.enums.Component;
import com.snapdeal.healthcheck.app.model.HealthCheckResult;
import com.snapdeal.healthcheck.app.utils.HttpCallResponse;

public class SFMOBILEAPIHealthCheckImpl implements Callable<HealthCheckResult>{

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private String endPoint;
	
	public SFMOBILEAPIHealthCheckImpl(String endPoint) {
		this.endPoint = endPoint;
	}
	@Override
	public HealthCheckResult call() throws Exception {
		boolean isServerUp = false;
		String url = endPoint + "/checkServerHealth";
		HealthCheckResult result = new HealthCheckResult(Component.FILMS.code());
		log.debug("Checking if SFMOBILEAPI server is up on endpoint: " + endPoint);
		HttpCallResponse resp = callGet(url);
		if (resp.getStatusCode() != null && resp.getStatusCode().equals("200 OK") && resp.getResponseBody() != null
				&& resp.getResponseBody().contains("PERFECT"))
			isServerUp = true;
		log.debug("Status code: " + resp.getStatusCode());
		log.debug("Response Body: " + resp.getResponseBody());
		result.setServerUp(isServerUp);
		return result;
	}
}
