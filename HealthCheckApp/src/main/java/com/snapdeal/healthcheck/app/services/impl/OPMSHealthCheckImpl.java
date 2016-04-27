package com.snapdeal.healthcheck.app.services.impl;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapdeal.healthcheck.app.enums.Component;
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
		HealthCheckResult result = new HealthCheckResult(Component.OPMS.code());
		log.debug("Checking if OPMS server is up on endpoint: " + endPoint);
		result.setServerUp(comp.isServerUp());
		return result;
	}

}
