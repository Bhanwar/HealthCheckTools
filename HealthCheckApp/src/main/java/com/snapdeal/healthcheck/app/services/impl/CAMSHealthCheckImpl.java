package com.snapdeal.healthcheck.app.services.impl;

import java.util.concurrent.Callable;
import static com.snapdeal.healthcheck.app.constants.ComponentNameConstants.COMPONENT_CAMS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapdeal.healthcheck.app.model.HealthCheckResult;
import com.snapdeal.healthcheck.components.CAMSHealthCheck;

public class CAMSHealthCheckImpl implements Callable<HealthCheckResult>{

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private String endPoint;
	
	public CAMSHealthCheckImpl(String endPoint) {
		this.endPoint = endPoint;
	}
	@Override
	public HealthCheckResult call() throws Exception {
		CAMSHealthCheck comp = new CAMSHealthCheck(endPoint);
		HealthCheckResult result = new HealthCheckResult(COMPONENT_CAMS);
		log.debug("Checking if CAMS server is up on endpoint: " + endPoint);
		result.setServerUp(comp.isServerUp());
		return result;
	}
}
