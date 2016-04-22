package com.snapdeal.healthcheck.app.services.impl;

import java.util.concurrent.Callable;
import static com.snapdeal.healthcheck.app.constants.ComponentNameConstants.COMPONENT_OPS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapdeal.healthcheck.app.model.HealthCheckResult;
import com.snapdeal.healthcheck.components.OPSHealthCheck;

public class OPSHealthCheckImpl implements Callable<HealthCheckResult>{

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private String endPoint;
	
	public OPSHealthCheckImpl(String endPoint) {
		this.endPoint = endPoint;
	}
	
	@Override
	public HealthCheckResult call() throws Exception {
		OPSHealthCheck comp = new OPSHealthCheck(endPoint);
		HealthCheckResult result = new HealthCheckResult(COMPONENT_OPS);
		log.debug("Checking is OPS server is up on endpoint: " + endPoint);
		result.setServerUp(comp.isServerUp());
		return result;
	}

}
