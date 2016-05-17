package com.snapdeal.healthcheck.app.services.impl;

import static com.snapdeal.healthcheck.app.constants.AppConstant.CONNECTION_TIMED_OUT;
import static com.snapdeal.healthcheck.app.constants.AppConstant.currentExecDate;
import static com.snapdeal.healthcheck.app.constants.Formatter.dateFormatter;
import static com.snapdeal.healthcheck.app.constants.Formatter.timeFormatter;

import java.util.Date;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapdeal.healthcheck.app.model.ComponentDetails;
import com.snapdeal.healthcheck.app.model.ConnTimedOutComp;
import com.snapdeal.healthcheck.app.model.HealthCheckResult;
import com.snapdeal.healthcheck.app.model.HttpCallResponse;
import com.snapdeal.healthcheck.app.mongo.repositories.MongoRepoService;
import com.snapdeal.healthcheck.app.utils.RestUtil;


public class EnvHealthCheckImpl implements Callable<HealthCheckResult> {

	private static final Logger log = LoggerFactory.getLogger(EnvHealthCheckImpl.class);

	private ComponentDetails compDetails;
	private MongoRepoService mongoService;

	public EnvHealthCheckImpl(ComponentDetails compDetails, MongoRepoService mongoService) {
		this.compDetails = compDetails;
		this.mongoService = mongoService;
	}

	@Override
	public HealthCheckResult call() throws Exception {
		return checkServerHealth(compDetails, mongoService);
	}

	public static HealthCheckResult checkServerHealth(ComponentDetails component, MongoRepoService mongoRepoService) {
		boolean isServerUp = false;
		boolean apiExist = false;
		int waitTimeInMillis = 5000;

		String compName = component.getComponentName();
		String endpoint = component.getEndpoint();
		String url = null;
		String callType = null;
		String headersJson = null;
		String reqJson = null;
		String expectedResp = null;
		String expStatusCode = "200 OK";
		String actualResp = null;
		String actualStatusCode = null;
		String htmlCallException = null;

		HealthCheckResult result = new HealthCheckResult(compName);
		HttpCallResponse response = null;
		Date resultDate = currentExecDate;
		String logSuffix = compName + ": ";

		log.debug("Checking health for component: " + compName);
		log.debug(logSuffix + "Comp details - " + component);

		// Health Check API
		if (component.getHealthCheckApiUrl() != null && component.getHealthCheckApiCallType() != null
				&& component.getHealthCheckApiResp() != null) {
			apiExist = true;
			response = null;

			url = endpoint + component.getHealthCheckApiUrl();
			callType = component.getHealthCheckApiCallType();
			headersJson = component.getHealthCheckApiHeadersJson();
			reqJson = component.getHealthCheckApiReqJson();
			log.debug(logSuffix + "Health Check API URL - " + url);

			response = RestUtil.fetchResponse(url, callType, headersJson, reqJson);
			if(response.getStatusCode() == null || !response.getStatusCode().equals(expStatusCode)) {
				log.debug(logSuffix + "Retrying Http Call GET..! Status Code: " + response.getStatusCode() + " Call Exception: " + response.getHttpCallException());
				try {
					Thread.sleep(waitTimeInMillis);
				} catch (InterruptedException e) {}
				response = RestUtil.fetchResponse(url, callType, headersJson, reqJson);
			}
			expectedResp = component.getHealthCheckApiResp();
			actualResp = response.getResponseBody();
			actualStatusCode = response.getStatusCode();
			htmlCallException = response.getHttpCallException();
			if (response.getStatusCode() != null && response.getStatusCode().equals(expStatusCode)
					&& response.getResponseBody() != null && response.getResponseBody().contains(expectedResp))
				isServerUp = true;
			else
				isServerUp = false;
			log.debug(logSuffix + "Health Check API Status code - " + response.getStatusCode());
		} else {
			log.warn(logSuffix + "Health Check API details not present!");
		}

		// First Getter API
		if (component.getFirstGetterApiUrl() != null && component.getFirstGetterApiCallType() != null
				&& component.getFirstGetterApiResp() != null) {
			apiExist = true;
			response = null;

			url = endpoint + component.getFirstGetterApiUrl();
			callType = component.getFirstGetterApiCallType();
			headersJson = component.getFirstGetterApiHeadersJson();
			reqJson = component.getFirstGetterApiReqJson();
			log.debug(logSuffix + "First Getter API URL - " + url);

			response = RestUtil.fetchResponse(url, callType, headersJson, reqJson);
			if(response.getStatusCode() == null || !response.getStatusCode().equals(expStatusCode)) {
				log.debug(logSuffix + "Retrying Http Call GET..! Status Code: " + response.getStatusCode() + " Call Exception: " + response.getHttpCallException());
				try {
					Thread.sleep(waitTimeInMillis);
				} catch (InterruptedException e) {}
				response = RestUtil.fetchResponse(url, callType, headersJson, reqJson);
			}
			expectedResp = component.getFirstGetterApiResp();
			actualResp = response.getResponseBody();
			actualStatusCode = response.getStatusCode();
			htmlCallException = response.getHttpCallException();
			if (response.getStatusCode() != null && response.getStatusCode().equals(expStatusCode)
					&& response.getResponseBody() != null && response.getResponseBody().contains(expectedResp))
				isServerUp = true;
			else
				isServerUp = false;
			log.debug(logSuffix + "First Getter API Status code - " + response.getStatusCode());
		} else {
			if (isServerUp)
				log.warn(logSuffix + "First Getter API details not present!");
		}

		// Second Getter API
		if (component.getSecondGetterApiUrl() != null && component.getSecondGetterApiCallType() != null
				&& component.getSecondGetterApiResp() != null) {
			apiExist = true;
			response = null;

			url = endpoint + component.getSecondGetterApiUrl();
			callType = component.getSecondGetterApiCallType();
			reqJson = component.getSecondGetterApiReqJson();
			log.debug(logSuffix + "Second Getter API URL - " + url);

			response = RestUtil.fetchResponse(url, callType, headersJson, reqJson);
			if(response.getStatusCode() == null || !response.getStatusCode().equals(expStatusCode)) {
				log.debug(logSuffix + "Retrying Http Call GET..! Status Code: " + response.getStatusCode() + " Call Exception: " + response.getHttpCallException());
				try {
					Thread.sleep(waitTimeInMillis);
				} catch (InterruptedException e) {}
				response = RestUtil.fetchResponse(url, callType, headersJson, reqJson);
			}
			expectedResp = component.getSecondGetterApiResp();
			actualResp = response.getResponseBody();
			actualStatusCode = response.getStatusCode();
			htmlCallException = response.getHttpCallException();
			if (response.getStatusCode() != null && response.getStatusCode().equals(expStatusCode)
					&& response.getResponseBody() != null && response.getResponseBody().contains(expectedResp))
				isServerUp = true;
			else
				isServerUp = false;
			log.debug(logSuffix + "Second Getter API Status code - " + response.getStatusCode());
		} else {
			if (isServerUp)
				log.warn(logSuffix + "Second Getter API details not present!");
		}

		result.setServerUp(isServerUp);

		if (mongoRepoService != null) {
			ConnTimedOutComp timedOut = null;
			if (!isServerUp) {
				log.debug(logSuffix + "server is not up");
				if (htmlCallException != null && htmlCallException.equals(CONNECTION_TIMED_OUT)) {
					log.debug(logSuffix + "server connection timed out");
					timedOut = mongoRepoService.findIfConnTimedOut(compName);
					if (timedOut == null) {
						log.debug(logSuffix + "creating connection timed out entry!");
						timedOut = new ConnTimedOutComp();
						timedOut.setComponentName(compName);
						timedOut.setExecDate(resultDate);
						mongoRepoService.save(timedOut);
						result.setServerUp(true);
						return result;
					}
					log.debug(
							logSuffix + "connection timed out entry already exist! Deleting and returning the result");
					mongoRepoService.delete(timedOut);
					resultDate = timedOut.getExecDate();
				}
			} else {
				timedOut = mongoRepoService.findIfConnTimedOut(compName);
				if (timedOut != null) {
					log.debug(logSuffix + "deleting previous connection timed out entry!");
					mongoRepoService.delete(timedOut);
				}
			}
		}
		if (!isServerUp) {
			result.setFailedURL(url);
			result.setFailedReqJson(reqJson);
			result.setFailedHttpCallException(htmlCallException);
			result.setFailedActualResp(actualResp);
			result.setFailedStatusCode(actualStatusCode);
			result.setFailedExpResp(expectedResp);
			log.debug(logSuffix + "Server Down!!");
			log.debug(logSuffix + "URL: " + url);
			log.debug(logSuffix + "Request JSON: " + reqJson);
			log.debug(logSuffix + "Status Code: " + actualStatusCode);
			log.debug(logSuffix + "Response: " + actualResp);
			log.debug(logSuffix + "HTTP Call Exception: " + htmlCallException);
		}
		String date = dateFormatter.format(resultDate);
		String time = timeFormatter.format(resultDate);
		result.setExecDate(date);
		result.setExecTime(time);
		result.setExecDateTime(resultDate);
		if(!apiExist)
			result.setServerUp(false);

		return result;
	}
}
