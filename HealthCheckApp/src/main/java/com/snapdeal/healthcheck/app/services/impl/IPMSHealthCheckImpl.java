package com.snapdeal.healthcheck.app.services.impl;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapdeal.healthcheck.app.enums.Component;
import com.snapdeal.healthcheck.app.model.HealthCheckResult;
import com.snapdeal.healthcheck.components.IPMSHealthCheck;

public class IPMSHealthCheckImpl implements Callable<HealthCheckResult>{

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private String endPoint;
	
	public IPMSHealthCheckImpl(String endPoint) {
		this.endPoint = endPoint;
	}
	@Override
	public HealthCheckResult call() throws Exception {
		IPMSHealthCheck comp = new IPMSHealthCheck(endPoint);
		HealthCheckResult result = new HealthCheckResult(Component.IPMS.code());
		log.debug("Checking if IPMS server is up on endpoint: " + endPoint);
		result.setServerUp(comp.isServerUp());
		return result;
	}

}
