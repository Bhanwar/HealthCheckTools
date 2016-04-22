package com.snapdeal.healthcheck.app.services.impl;

import java.util.concurrent.Callable;
import static com.snapdeal.healthcheck.app.constants.ComponentNameConstants.COMPONENT_OMS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapdeal.healthcheck.app.model.HealthCheckResult;
import com.snapdeal.healthcheck.components.OMSHealthCheck;

public class OMSHealthCheckImpl implements Callable<HealthCheckResult>{

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private String endPoint;
	
	public OMSHealthCheckImpl(String endPoint) {
		this.endPoint = endPoint;
	}
	
	@Override
	public HealthCheckResult call() throws Exception {
		OMSHealthCheck comp = new OMSHealthCheck(endPoint);
		HealthCheckResult result = new HealthCheckResult(COMPONENT_OMS);
		log.debug("Checking if OMS server is up on endpoint: " + endPoint);
		result.setServerUp(comp.isServerUp());
		return result;
	}

}
