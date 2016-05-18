package com.snapdeal.healthcheck.app.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.response.Response;

public class RestUtil {

	public static HttpCallResponse fetchResponse(String url, String callType, String headerStr, String payloadStr) {
		resetClient(headerStr);
		Response response = null;
		HttpCallResponse httpResponse = new HttpCallResponse();

		try {
			if ("GET".equalsIgnoreCase(callType))
				response = callGet(url, payloadStr);
			else if ("POST".equalsIgnoreCase(callType))
				response = callPost(url, payloadStr);

			httpResponse.setStatusCode(response.getStatusLine().replace("HTTP/1.1 ", ""));
			httpResponse.setResponseBody(response.body().asString());
		} catch (Exception e) {
			httpResponse.setHttpCallException(e.getMessage());
		}

		return httpResponse;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> convertJsonStrToMap(String str) {
		Map<String, String> headersMap = new HashMap<String, String>();
		try {
			headersMap = new ObjectMapper().readValue(str, HashMap.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return headersMap;
	}

	public static void resetClient(String headerStr) {
		RestAssured.reset();
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.urlEncodingEnabled = true;

		if (headerStr != null) {
			Map<String, String> headersMap = convertJsonStrToMap(headerStr);
			RestAssured.requestSpecification = new RequestSpecBuilder().addHeaders(headersMap).build();
		}
	}

	public static Response callGet(String url, String payloadStr) throws Exception {
		if (payloadStr != null) {
			Map<String, String> payload = convertJsonStrToMap(payloadStr);
			return RestAssured.given().queryParams(payload).when().get(url);
		} else {
			return RestAssured.given().when().get(url);
		}
	}

	public static Response callPost(String url, String payloadStr) throws Exception {
		if (payloadStr != null) {
			Map<String, String> payload = convertJsonStrToMap(payloadStr);
			return RestAssured.given().body(payload).when().post(url);
		} else {
			return RestAssured.given().when().post(url);
		}
	}

}