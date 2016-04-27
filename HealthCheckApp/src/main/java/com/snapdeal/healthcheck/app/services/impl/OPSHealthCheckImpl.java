package com.snapdeal.healthcheck.app.services.impl;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapdeal.healthcheck.app.enums.Component;
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
		HealthCheckResult result = new HealthCheckResult(Component.OPS.code());
		log.debug("Checking is OPS server is up on endpoint: " + endPoint);
		result.setServerUp(comp.isServerUp());
		return result;
	}

}
