package com.snapdeal.healthcheck.app.services.impl;

import java.util.concurrent.Callable;
import static com.snapdeal.healthcheck.app.constants.ComponentNameConstants.COMPONENT_SCORE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapdeal.healthcheck.app.model.HealthCheckResult;
import com.snapdeal.healthcheck.components.SCOREHealthCheck;

public class SCOREHealthCheckImpl implements Callable<HealthCheckResult>{

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private String endPoint;
	
	public SCOREHealthCheckImpl(String endPoint) {
		this.endPoint = endPoint;
	}
	@Override
	public HealthCheckResult call() throws Exception {
		SCOREHealthCheck comp = new SCOREHealthCheck(endPoint);
		HealthCheckResult result = new HealthCheckResult(COMPONENT_SCORE);
		log.debug("Checking if SCORE server is up on endpoint: " + endPoint);
		result.setServerUp(comp.isServerUp());
		return result;
	}

}
