package com.snapdeal.healthcheck.app.services.impl;

import java.util.concurrent.Callable;
import static com.snapdeal.healthcheck.app.constants.ComponentNameConstants.COMPONENT_COCOFS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapdeal.healthcheck.app.model.HealthCheckResult;
import com.snapdeal.healthcheck.components.COCOFSHealthCheck;

public class COCOFSHealthCheckImpl implements Callable<HealthCheckResult>{

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private String endPoint;
	
	public COCOFSHealthCheckImpl(String endPoint) {
		this.endPoint = endPoint;
	}
	@Override
	public HealthCheckResult call() throws Exception {
		COCOFSHealthCheck comp = new COCOFSHealthCheck(endPoint);
		HealthCheckResult result = new HealthCheckResult(COMPONENT_COCOFS);
		log.debug("Checking if COCOFS server is up on endpoint: " + endPoint);
		result.setServerUp(comp.isServerUp());
		return result;
	}

}
