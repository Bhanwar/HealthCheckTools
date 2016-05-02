package com.snapdeal.healthcheck.app.services.impl;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapdeal.healthcheck.app.enums.Component;
import com.snapdeal.healthcheck.app.model.HealthCheckResult;
import com.snapdeal.healthcheck.components.MobAPIHealthCheck;

public class MobAPIHealthCheckImpl implements Callable<HealthCheckResult>{

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private String endPoint;
	
	public MobAPIHealthCheckImpl(String endPoint) {
		this.endPoint = endPoint;
	}
	@Override
	public HealthCheckResult call() throws Exception {
		MobAPIHealthCheck comp = new MobAPIHealthCheck(endPoint);
		HealthCheckResult result = new HealthCheckResult(Component.MOBAPI.code());
		log.debug("Checking if Mobile API server is up on endpoint: " + endPoint);
		result.setServerUp(comp.isServerUp());
		return result;
	}
}
