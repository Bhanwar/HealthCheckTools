package com.snapdeal.healthcheck.app.services.impl;

import static com.snapdeal.healthcheck.app.utils.HttpCall.callGet;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapdeal.healthcheck.app.enums.Component;
import com.snapdeal.healthcheck.app.model.HealthCheckResult;
import com.snapdeal.healthcheck.app.utils.HttpCallResponse;

public class CARTHealthCheckImpl implements Callable<HealthCheckResult>{

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private String endPoint;
	
	public CARTHealthCheckImpl(String endPoint) {
		this.endPoint = endPoint;
	}
	@Override
	public HealthCheckResult call() throws Exception {
		boolean isServerUp = false;
		String url = endPoint + "/service/cartAPIService/cartHealthCheck?pwd=Snapdeal";
		HealthCheckResult result = new HealthCheckResult(Component.CART.code());
		log.debug("Checking if CART server is up on endpoint: " + endPoint);
		HttpCallResponse resp = callGet(url);
		if (resp.getStatusCode() != null && resp.getStatusCode().equals("200 OK") && resp.getResponseBody() != null
				&& resp.getResponseBody().contains("Authentication SUCCESS !!! We are UP :)"))
			isServerUp = true;
		log.debug("Status code: " + resp.getStatusCode());
		log.debug("Response Body: " + resp.getResponseBody());
		result.setServerUp(isServerUp);
		return result;
	}
}
