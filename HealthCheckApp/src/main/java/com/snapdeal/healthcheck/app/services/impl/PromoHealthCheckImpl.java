package com.snapdeal.healthcheck.app.services.impl;

import java.util.concurrent.Callable;
import static com.snapdeal.healthcheck.app.constants.ComponentNameConstants.COMPONENT_PROMO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapdeal.healthcheck.app.model.HealthCheckResult;
import com.snapdeal.healthcheck.components.PromoHealthCheck;

public class PromoHealthCheckImpl implements Callable<HealthCheckResult>{

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private String endPoint;
	
	public PromoHealthCheckImpl(String endPoint) {
		this.endPoint = endPoint;
	}
	@Override
	public HealthCheckResult call() throws Exception {
		PromoHealthCheck comp = new PromoHealthCheck(endPoint);
		HealthCheckResult result = new HealthCheckResult(COMPONENT_PROMO);
		log.debug("Checking if Promo server is up on endpoint: " + endPoint);
		result.setServerUp(comp.isServerUp());
		return result;
	}

}
