package com.snapdeal.healthcheck.app.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Headers;
import com.jayway.restassured.response.Response;
import com.snapdeal.healthcheck.app.model.HttpCallResponse;
import com.sun.jersey.core.util.MultivaluedMapImpl;


public class RestUtil {

	public static HttpCallResponse fetchResponse(String url, String callType, String headersJson, String paramsJson) {
		HttpCallResponse httpResponse = new HttpCallResponse();
		try {
			resetClient(headersJson);
			Response response = null;

			if ("GET".equalsIgnoreCase(callType))
				response = callGet(url, paramsJson);
			else if ("POST".equalsIgnoreCase(callType))
				response = callPost(url, paramsJson);

			String statusCode = response.getStatusLine().replace("HTTP/1.1 ", "");
			httpResponse.setStatusCode(statusCode);
			httpResponse.setResponseHeaders(convertHeadersToMap(response.getHeaders()));
			httpResponse.setResponseBody(response.body().asString());
		}
		catch (Exception e) {
			httpResponse.setHttpCallException(e.getMessage());
		}
		return httpResponse;
	}


	public static void resetClient(String headersJson) throws Exception {
		RestAssured.reset();
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.urlEncodingEnabled = true;
		if (headersJson != null) {
			Map<String, String> headers = convertJsonStrToMap(headersJson);
			RestAssured.requestSpecification = new RequestSpecBuilder()
					.addHeaders(headers)
					.build();
		}
	}


	public static Response callGet(String url, String paramsJson) throws Exception {
		if (paramsJson != null) {
			Map<String, String> params = convertJsonStrToMap(paramsJson);
			return RestAssured.given().queryParams(params).when().get(url);
		}
		else {
			return RestAssured.given().when().get(url);
		}
	}


	public static Response callPost(String url, String paramsJson) throws Exception {
		if (paramsJson != null) {
			Object payload = convertJsonStrToObj(paramsJson);
			return RestAssured.given().body(payload).when().post(url);
		}
		else {
			return RestAssured.given().when().post(url);
		}
	}


	public static Map<String, List<String>> convertHeadersToMap(Headers headers) {
		Map<String, List<String>> responseHeaders = new MultivaluedMapImpl();
		Iterator<Header> iter = headers.iterator();

		while (iter.hasNext()) {
			Header header = iter.next();
			String key = header.getName();
			if (responseHeaders.containsKey(key))
				responseHeaders.get(key).add(header.getValue());
			else {
				List<String> values = new ArrayList<String>();
				values.add(header.getValue());
				responseHeaders.put(key, values);
			}
		}
		return responseHeaders;
	}


	@SuppressWarnings("unchecked")
	public static Map<String, String> convertJsonStrToMap(String paramsJson) {
		Map<String, String> params = null;
		try {
			params = new ObjectMapper().readValue(paramsJson, HashMap.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return params;
	}


	public static Object convertJsonStrToObj(String paramsJson) {
		Object payload = null;
		try {
			payload = new ObjectMapper().readValue(paramsJson, Object.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return payload;
	}


	public static JSONObject mergeJsonObjects(JSONObject parentObj, JSONObject childObj) {
		Iterator<?> keys = childObj.keys();
		while(keys.hasNext()) {
			String key = (String) keys.next();
			parentObj.put(key, childObj.get(key));
		}
		return parentObj;
	}
}
