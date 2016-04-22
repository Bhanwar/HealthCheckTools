package com.snapdeal.healthcheck.app.services.impl;

import java.util.concurrent.Callable;
import static com.snapdeal.healthcheck.app.constants.ComponentNameConstants.COMPONENT_OPMS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapdeal.healthcheck.app.model.HealthCheckResult;
import com.snapdeal.healthcheck.components.OPMSHealthCheck;

public class OPMSHealthCheckImpl implements Callable<HealthCheckResult>{

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private String endPoint;
	
	public OPMSHealthCheckImpl(String endPoint) {
		this.endPoint = endPoint;
	}
	@Override
	public HealthCheckResult call() throws Exception {
		OPMSHealthCheck comp = new OPMSHealthCheck(endPoint);
		HealthCheckResult result = new HealthCheckResult(COMPONENT_OPMS);
		log.debug("Checking if OPMS server is up on endpoint: " + endPoint);
		result.setServerUp(comp.isServerUp());
		return result;
	}

}
