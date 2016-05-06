package com.snapdeal.healthcheck.app.services.impl;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapdeal.healthcheck.app.model.ComponentDetails;
import com.snapdeal.healthcheck.app.model.HealthCheckResult;
import static com.snapdeal.healthcheck.app.utils.HttpCall.callGet;
import static com.snapdeal.healthcheck.app.utils.HttpCall.callGetApplicatioJSON;
import static com.snapdeal.healthcheck.app.utils.HttpCall.callPost;
import com.snapdeal.healthcheck.app.utils.HttpCallResponse;

public class EnvHealthCheckImpl implements Callable<HealthCheckResult> {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private ComponentDetails compDetails;

	public EnvHealthCheckImpl(ComponentDetails compDetails) {
		this.compDetails = compDetails;
	}

	@Override
	public HealthCheckResult call() throws Exception {
		return checkServerHealth(compDetails);
	}

	public HealthCheckResult checkServerHealth(ComponentDetails component) {
		boolean isServerUp = false;
		String compName = component.getComponentName();
		String logSuffix = compName + ": ";
		HealthCheckResult result = new HealthCheckResult(compName);
		HttpCallResponse response = null;
		String url = null;
		String reqJson = null;
		String actualResp = null;
		String expectedResp = null;
		String htmlCallException = null;
		String statusCode = "200 OK";
		log.debug("Checking health for component: " + compName);
		log.debug(logSuffix + "URL - " + url);
		// Health check
		if (component.getHealthCheckApi() != null && component.getHealthCheckApiCallType() != null
				&& component.getHealthCheckApiResponse() != null) {
			response = null;
			url = component.getEndpoint() + component.getHealthCheckApi();
			if (component.getHealthCheckApiCallType().equals("GET")) {
				response = callGet(url);
			} else if (component.getHealthCheckApiCallType().equals("GETJSON")) {
				response = callGetApplicatioJSON(url);
			} else if (component.getHealthCheckApiCallType().equals("POST")) {
				response = callPost(url, "");
			}
			expectedResp = component.getHealthCheckApiResponse();
			htmlCallException = response.getHttpCallException();
			actualResp = response.getResponseBody();
			if (response.getStatusCode() != null && response.getStatusCode().equals(statusCode)
					&& response.getResponseBody() != null
					&& response.getResponseBody().contains(expectedResp))
				isServerUp = true;
			log.debug(logSuffix + "Health API Status code - " + response.getStatusCode());
			log.debug(logSuffix + "Health API Response Body - " + response.getResponseBody());
		} else {
			log.warn(logSuffix + "Health check API details not present!");
		}

		// First Get API
		if (isServerUp && component.getFirstGetApi() != null && component.getFirstGetApiCallType() != null
				&& component.getFirstGetApiReqJson() != null && component.getFirstGetApiResponce() != null) {

			response = null;
			reqJson = component.getFirstGetApiReqJson();
			url = component.getEndpoint() + component.getFirstGetApi();
			if (component.getFirstGetApiCallType().equals("GET")) {
				response = callGet(url);
			} else if (component.getFirstGetApiCallType().equals("GETJSON")) {
				response = callGetApplicatioJSON(url);
			} else if (component.getFirstGetApiCallType().equals("POST")) {
				response = callPost(url, reqJson);
			}
			expectedResp = component.getFirstGetApiResponce();
			htmlCallException = response.getHttpCallException();
			actualResp = response.getResponseBody();
			if (response.getStatusCode() != null && response.getStatusCode().equals(statusCode)
					&& response.getResponseBody() != null
					&& response.getResponseBody().contains(expectedResp))
				isServerUp = true;
			log.debug(logSuffix + "First Get API Status code - " + response.getStatusCode());
			log.debug(logSuffix + "First Get API Response Body - " + response.getResponseBody());
		} else {
			log.warn(logSuffix + "First Get API details not present!");
		}

		// Second Get API
		if (isServerUp && component.getSecondGetApi() != null && component.getSecondGetApiCallType() != null
				&& component.getSecondGetApiReqJson() != null && component.getSecondGetApiResponce() != null) {

			response = null;
			reqJson = component.getSecondGetApiReqJson();
			url = component.getEndpoint() + component.getSecondGetApi();
			if (component.getSecondGetApiCallType().equals("GET")) {
				response = callGet(url);
			} else if (component.getSecondGetApiCallType().equals("GETJSON")) {
				response = callGetApplicatioJSON(url);
			} else if (component.getSecondGetApiCallType().equals("POST")) {
				response = callPost(url, reqJson);
			}
			expectedResp = component.getSecondGetApiResponce();
			htmlCallException = response.getHttpCallException();
			actualResp = response.getResponseBody();
			if (response.getStatusCode() != null && response.getStatusCode().equals(statusCode)
					&& response.getResponseBody() != null
					&& response.getResponseBody().contains(expectedResp))
				isServerUp = true;
			log.debug(logSuffix + "Second Get API Status code - " + response.getStatusCode());
			log.debug(logSuffix + "Second Get API Response Body - " + response.getResponseBody());
		} else {
			log.warn(logSuffix + "Second Get API details not present!");
		}

		result.setServerUp(isServerUp);
		if (!isServerUp) {
			log.debug(logSuffix+"Adding server failure details to result");
			result.setFailedURL(url);
			result.setFailedReqJson(reqJson);
			result.setFailedHttpCallException(htmlCallException);
			result.setFailedActualResp(actualResp);
			result.setFailedExpResp(expectedResp);
		}

		return result;
	}

}
