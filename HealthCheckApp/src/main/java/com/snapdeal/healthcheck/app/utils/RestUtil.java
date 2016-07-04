package com.snapdeal.healthcheck.app.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Headers;
import com.jayway.restassured.response.Response;
import com.snapdeal.healthcheck.app.model.HttpCallResponse;


public class RestUtil {

	private static final Logger log = LoggerFactory.getLogger(RestUtil.class);


	public static HttpCallResponse fetchResponse(String url, String callType, String headers, String payload) {
		HttpCallResponse httpResponse = new HttpCallResponse();
		try {
			resetClient(headers);
			Response response = null;

			if ("GET".equalsIgnoreCase(callType))
				response = callGet(url, payload);
			else if ("POST".equalsIgnoreCase(callType))
				response = callPost(url, payload);

			String statusCode = response.getStatusLine().replace("HTTP/1.1 ", "");
			httpResponse.setStatusCode(statusCode);
			httpResponse.setResponseHeaders(convertHeadersToMap(response.getHeaders()));
			httpResponse.setResponseBody(response.body().asString());
		}
		catch (Exception e) {
			log.error("Exception occured while doing "+callType+": " + e.getMessage(), e);
			httpResponse.setHttpCallException(e.getMessage());
		}
		return httpResponse;
	}

	public static HttpCallResponse fetchResponseAuth(String url, String callType, String headers, String payload, String username, String pwd) {
		HttpCallResponse httpResponse = new HttpCallResponse();
		try {
			resetClient(headers);
			Response response = null;

			if ("GET".equalsIgnoreCase(callType))
				response = callGet(url, payload, username, pwd);
			else if ("POST".equalsIgnoreCase(callType))
				response = callPost(url, payload);

			String statusCode = response.getStatusLine().replace("HTTP/1.1 ", "");
			httpResponse.setStatusCode(statusCode);
			httpResponse.setResponseHeaders(convertHeadersToMap(response.getHeaders()));
			httpResponse.setResponseBody(response.body().asString());
		}
		catch (Exception e) {
			log.error("Exception occured while doing "+callType+": " + e.getMessage(), e);
			httpResponse.setHttpCallException(e.getMessage());
		}
		return httpResponse;
	}

	public static void resetClient(String headers) throws Exception {
		RestAssured.reset();
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.urlEncodingEnabled = true;
		if (headers != null) {
			Map<String, String> headersMap = fetchReqParamsMap(headers);
			RestAssured.requestSpecification = new RequestSpecBuilder()
					.addHeaders(headersMap)
					.build();
		}
	}


	public static Response callGet(String url, String payload) throws Exception {
		if (payload != null) {
			Map<String, String> params = fetchReqParamsMap(payload);
			return RestAssured.given().queryParams(params).when().get(url);
		}
		else {
			return RestAssured.given().when().get(url);
		}
	}
	
	public static Response callGet(String url, String payload, String username, String password) throws Exception {
		if (payload != null) {
			Map<String, String> params = fetchReqParamsMap(payload);
			return RestAssured.given().authentication().basic(username, password).queryParams(params).when().get(url);
		}
		else {
			return RestAssured.given().authentication().basic(username, password).when().get(url);
		}
	}

	public static Response callPost(String url, String payload) throws Exception {
		if (payload != null)
			return RestAssured.given().body(payload).when().post(url);
		else
			return RestAssured.given().when().post(url);
	}


	public static Map<String, List<String>> convertHeadersToMap(Headers headers) {
		Map<String, List<String>> headersMap = new HashMap<String, List<String>>();
		Iterator<Header> iter = headers.iterator();

		while (iter.hasNext()) {
			Header header = iter.next();
			String key = header.getName();
			if (headersMap.containsKey(key))
				headersMap.get(key).add(header.getValue());
			else {
				List<String> values = new ArrayList<String>();
				values.add(header.getValue());
				headersMap.put(key, values);
			}
		}
		return headersMap;
	}


	@SuppressWarnings("unchecked")
	public static Map<String, String> fetchReqParamsMap(String payload) {
		Map<String, String> params = null;
		try {
			params = new ObjectMapper().readValue(payload, HashMap.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return params;
	}


	public static Object fetchReqParamsObj(String payload) {
		Object payloadObj = null;
		try {
			payloadObj = new ObjectMapper().readValue(payload, Object.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return payloadObj;
	}


	public static JSONObject mergeJsonObjects(JSONObject parentJson, JSONObject childJson) {
		Iterator<?> keys = childJson.keys();
		while(keys.hasNext()) {
			String key = (String) keys.next();
			parentJson.put(key, childJson.get(key));
		}
		return parentJson;
	}
}
