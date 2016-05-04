package com.snapdeal.healthcheck.app.services.impl;

import static com.snapdeal.healthcheck.app.utils.HttpCall.callPost;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.JsonPath;
import com.snapdeal.healthcheck.app.configurables.GetApiConfigValues;
import com.snapdeal.healthcheck.app.enums.Component;
import com.snapdeal.healthcheck.app.model.HealthCheckResult;
import com.snapdeal.healthcheck.app.utils.HttpCallResponse;

public class POMSHealthCheckImpl implements Callable<HealthCheckResult> {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private String endPoint;
	private String suborder;

	public POMSHealthCheckImpl(String endPoint, GetApiConfigValues objGetConfigValues) {
		this.endPoint = endPoint;
		this.suborder = objGetConfigValues.getPomsSuborderCode();
	}

	@Override
	public HealthCheckResult call() throws Exception {
		boolean isServerUp = false;
		String url = endPoint + "/service/poms/getRefundModes";
		HealthCheckResult result = new HealthCheckResult(Component.POMS.code());
		log.debug("Checking if POMS server is up on endpoint: " + endPoint);
		HttpCallResponse resp = callPost(url,
				"{\"responseProtocol\":\"PROTOCOL_JSON\", \"requestProtocol\":\"PROTOCOL_JSON\", \"suborderCodeList\":["
						+ suborder + "],\"delivered\" : \"false\"}");
		if (resp.getStatusCode() != null && resp.getStatusCode().equals("200 OK") && resp.getResponseBody() != null) {
			isServerUp = JsonPath.read(resp.getResponseBody(), "$.successful");
		}
		log.debug("Status code: " + resp.getStatusCode());
		log.debug("Response Body: " + resp.getResponseBody());
		result.setServerUp(isServerUp);
		return result;
	}

}