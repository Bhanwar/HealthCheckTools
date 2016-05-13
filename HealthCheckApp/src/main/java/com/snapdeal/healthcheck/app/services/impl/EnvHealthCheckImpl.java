package com.snapdeal.healthcheck.app.services.impl;

import static com.snapdeal.healthcheck.app.constants.AppConstant.CONNECTION_TIMED_OUT;
import static com.snapdeal.healthcheck.app.constants.AppConstant.currentExecDate;
import static com.snapdeal.healthcheck.app.constants.Formatter.dateFormatter;
import static com.snapdeal.healthcheck.app.constants.Formatter.timeFormatter;
import static com.snapdeal.healthcheck.app.utils.HttpCall.callGet;
import static com.snapdeal.healthcheck.app.utils.HttpCall.callGetApplicatioJSON;
import static com.snapdeal.healthcheck.app.utils.HttpCall.callPost;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapdeal.healthcheck.app.model.ComponentDetails;
import com.snapdeal.healthcheck.app.model.ConnTimedOutComp;
import com.snapdeal.healthcheck.app.model.HealthCheckResult;
import com.snapdeal.healthcheck.app.mongo.repositories.MongoRepoService;
import com.snapdeal.healthcheck.app.utils.HttpCallResponse;

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
		boolean isServerUp = true;
		boolean apiExist = false;
		String compName = component.getComponentName();
		String logSuffix = compName + ": ";
		HealthCheckResult result = new HealthCheckResult(compName);
		HttpCallResponse response = null;
		Date resultDate = currentExecDate;
		String url = null;
		String reqJson = null;
		String actualStatusCode = null;
		String actualResp = null;
		String expectedResp = null;
		String htmlCallException = null;
		String statusCode = "200 OK";
		log.debug("Checking health for component: " + compName);
		log.debug(logSuffix + "Comp details - " + component);
		
		// Health check
		if (component.getHealthCheckApi() != null && component.getHealthCheckApiCallType() != null
				&& component.getHealthCheckApiResponse() != null) {
			response = null;
			apiExist = true;
			url = component.getEndpoint() + component.getHealthCheckApi();
			log.debug(logSuffix + "Health Check URL - " + url);
			if (component.getHealthCheckApiCallType().equals("GET")) {
				response = callGet(url);
				if(response.getStatusCode() == null || !response.getStatusCode().equals(statusCode)) {
					log.debug(logSuffix + "Retrying Http Call GET..! Status Code: " + response.getStatusCode() + " Call Exception: " + response.getHttpCallException());
					try {
						pingIp(component.getEndpoint(), logSuffix);
						Thread.sleep(3000);
					} catch (InterruptedException e) {}
					response = callGet(url);
				}
			} else if (component.getHealthCheckApiCallType().equals("GETJSON")) {
				response = callGetApplicatioJSON(url);
				if(response.getStatusCode() == null || !response.getStatusCode().equals(statusCode)) {
					log.debug(logSuffix + "Retrying Http Call GET with Json..! Status Code: " + response.getStatusCode() + " Call Exception: " + response.getHttpCallException());
					try {
						pingIp(component.getEndpoint(), logSuffix);
						Thread.sleep(3000);
					} catch (InterruptedException e) {}
					response = callGetApplicatioJSON(url);
				}
			} else if (component.getHealthCheckApiCallType().equals("POST")) {
				response = callPost(url, "");
				if(response.getStatusCode() == null || !response.getStatusCode().equals(statusCode)) {
					log.debug(logSuffix + "Retrying Http Call POST..! Status Code: " + response.getStatusCode() + " Call Exception: " + response.getHttpCallException());
					try {
						pingIp(component.getEndpoint(), logSuffix);
						Thread.sleep(3000);
					} catch (InterruptedException e) {}
					response = callPost(url, "");
				}
			}
			expectedResp = component.getHealthCheckApiResponse();
			htmlCallException = response.getHttpCallException();
			actualResp = response.getResponseBody();
			actualStatusCode = response.getStatusCode();
			if (response.getStatusCode() != null && response.getStatusCode().equals(statusCode)
					&& response.getResponseBody() != null && response.getResponseBody().contains(expectedResp))
				isServerUp = true;
			else
				isServerUp = false;
			log.debug(logSuffix + "Health API Status code - " + response.getStatusCode());
			//log.debug(logSuffix + "Health API Response Body - " + response.getResponseBody());
		} else {
			log.warn(logSuffix + "Health check API details not present!");
		}

		// First Get API
		if (isServerUp && component.getFirstGetApi() != null && component.getFirstGetApiCallType() != null
				&& component.getFirstGetApiResponce() != null) {

			response = null;
			apiExist = true;
			reqJson = component.getFirstGetApiReqJson();
			if(reqJson == null)
				reqJson = "";
			
			url = component.getEndpoint() + component.getFirstGetApi();
			log.debug(logSuffix + "First Get API URL - " + url);
			if (component.getFirstGetApiCallType().equals("GET")) {
				response = callGet(url);
				if(response.getStatusCode() == null || !response.getStatusCode().equals(statusCode)) {
					log.debug(logSuffix + "Retrying Http Call GET..! Status Code: " + response.getStatusCode() + " Call Exception: " + response.getHttpCallException());
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {}
					response = callGet(url);
				}
			} else if (component.getFirstGetApiCallType().equals("GETJSON")) {
				response = callGetApplicatioJSON(url);
				if(response.getStatusCode() == null || !response.getStatusCode().equals(statusCode)) {
					log.debug(logSuffix + "Retrying Http Call GET with Json..! Status Code: " + response.getStatusCode() + " Call Exception: " + response.getHttpCallException());
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {}
					response =  callGetApplicatioJSON(url);
				}
			} else if (component.getFirstGetApiCallType().equals("POST")) {
				response = callPost(url, reqJson);
				if(response.getStatusCode() == null || !response.getStatusCode().equals(statusCode)) {
					log.debug(logSuffix + "Retrying Http Call POST..! Status Code: " + response.getStatusCode() + " Call Exception: " + response.getHttpCallException());
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {}
					response = callPost(url, reqJson);
				}
			}
			expectedResp = component.getFirstGetApiResponce();
			htmlCallException = response.getHttpCallException();
			actualResp = response.getResponseBody();
			actualStatusCode = response.getStatusCode();
			if (response.getStatusCode() != null && response.getStatusCode().equals(statusCode)
					&& response.getResponseBody() != null && response.getResponseBody().contains(expectedResp))
				isServerUp = true;
			else
				isServerUp = false;
			log.debug(logSuffix + "First Get API Status code - " + response.getStatusCode());
			//log.debug(logSuffix + "First Get API Response Body - " + response.getResponseBody());
		} else {
			if (isServerUp)
				log.warn(logSuffix + "First Get API details not present!");
		}

		// Second Get API
		if (isServerUp && component.getSecondGetApi() != null && component.getSecondGetApiCallType() != null
				&& component.getSecondGetApiResponce() != null) {

			response = null;
			apiExist = true;
			reqJson = component.getSecondGetApiReqJson();
			if(reqJson == null)
				reqJson = "";
			
			url = component.getEndpoint() + component.getSecondGetApi();
			log.debug(logSuffix + "Second Get API URL - " + url);
			if (component.getSecondGetApiCallType().equals("GET")) {
				response = callGet(url);
				if(response.getStatusCode() == null || !response.getStatusCode().equals(statusCode)) {
					log.debug(logSuffix + "Retrying Http Call GET..! Status Code: " + response.getStatusCode() + " Call Exception: " + response.getHttpCallException());
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {}
					response = callGet(url);
				}
			} else if (component.getSecondGetApiCallType().equals("GETJSON")) {
				response = callGetApplicatioJSON(url);
				if(response.getStatusCode() == null || !response.getStatusCode().equals(statusCode)) {
					log.debug(logSuffix + "Retrying Http Call GET with Json..! Status Code: " + response.getStatusCode() + " Call Exception: " + response.getHttpCallException());
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {}
					response = callGetApplicatioJSON(url);
				}
			} else if (component.getSecondGetApiCallType().equals("POST")) {
				response = callPost(url, reqJson);
				if(response.getStatusCode() == null || !response.getStatusCode().equals(statusCode)) {
					log.debug(logSuffix + "Retrying Http Call POST..! Status Code: " + response.getStatusCode() + " Call Exception: " + response.getHttpCallException());
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {}
					response = callPost(url, reqJson);
				}
			}
			expectedResp = component.getSecondGetApiResponce();
			htmlCallException = response.getHttpCallException();
			actualResp = response.getResponseBody();
			actualStatusCode = response.getStatusCode();
			if (response.getStatusCode() != null && response.getStatusCode().equals(statusCode)
					&& response.getResponseBody() != null && response.getResponseBody().contains(expectedResp))
				isServerUp = true;
			else
				isServerUp = false;
			log.debug(logSuffix + "Second Get API Status code - " + response.getStatusCode());
			//log.debug(logSuffix + "Second Get API Response Body - " + response.getResponseBody());
		} else {
			if (isServerUp)
				log.warn(logSuffix + "Second Get API details not present!");
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
			//Check for netwrok issues
			if(htmlCallException != null && htmlCallException.equals("No route to host")) {
				result.setNtwrkIssue(true);
				result.setServerUp(true);
			}
			
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

	
	private static void pingIp(String endpoint, String logSuffix) {
		try {
			URL url = new URL(endpoint);
			String host = url.getHost();
			log.debug(logSuffix + "Trying to ping " + host);
			String s = "";
			
			ProcessBuilder pb = new ProcessBuilder("ping", "-c", "4", host);
		    Process process = pb.start();

		    BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
		    BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

		    // read the output from the command
		    while ((s = stdInput.readLine()) != null)
		    {
		    	log.debug(logSuffix + s);
		    }

		    // read any errors from the attempted command
		    while ((s = stdError.readLine()) != null)
		    {
		      log.error(logSuffix + s);
		    }
			
		} catch (Exception e) {
			log.error(logSuffix + "Exception occured while performing ping! " + e.getMessage(), e);
		}
	}
}
