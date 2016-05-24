package com.snapdeal.healthcheck.app.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapdeal.healthcheck.app.model.HttpCallResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;


public class JerseyUtil {

	private static final Logger log = LoggerFactory.getLogger(JerseyUtil.class);


	public static HttpCallResponse fetchResponse(String url, String callType, String headers, String payload) {
		HttpCallResponse httpResponse = new HttpCallResponse();
		try {
			ClientResponse response = null;
			JSONObject headersJson = new JSONObject(headers);
			String contentType = fetchMediaType(headersJson.getString("Content-Type"));

			if ("GET".equalsIgnoreCase(callType))
				response = callGet(url, contentType);
			else if ("POST".equalsIgnoreCase(callType))
				response = callPost(url, contentType, payload);

			String statusCode = response.getClientResponseStatus().getReasonPhrase();
			httpResponse.setStatusCode(statusCode);
			httpResponse.setResponseHeaders((Map<String, List<String>>)response.getHeaders());
			httpResponse.setResponseBody(response.getEntity(String.class));
		}
		catch (Exception e) {
			log.error("Exception occured while doing "+callType+": " + e.getMessage(), e);
			httpResponse.setHttpCallException(e.getMessage());
		}
		return httpResponse;
	}


	public static ClientResponse callGet(String url, String contentType) throws Exception {
		Client client = Client.create();  
		WebResource webResource = client.resource(url);
		return webResource.type(contentType).get(ClientResponse.class);
	}


	public static ClientResponse callPost(String url, String contentType, String payload) throws Exception {
		Client client = Client.create();  
		WebResource webResource = client.resource(url);

		if (payload != null) {
			MultivaluedMap<String, String> params = fetchReqParamsMultivaluedMap(payload);
			return webResource.type(contentType).post(ClientResponse.class, params);
		}
		else {
			return webResource.type(contentType).post(ClientResponse.class);
		}
	}


	public static String fetchMediaType(String type) {
		switch (type.toLowerCase()) {
		case "application/json":
			return MediaType.APPLICATION_JSON;
		case "application/x-www-form-urlencoded":
			return MediaType.APPLICATION_FORM_URLENCODED;
		case "appliation/xml":
			return MediaType.APPLICATION_XML;
		case "text/xml":
			return MediaType.TEXT_XML;
		default:
			return MediaType.WILDCARD;	
		}
	}


	public static MultivaluedMap<String, String> fetchReqParamsMultivaluedMap(String payload) {
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		JSONObject paramsJson = new JSONObject(params);	
		Iterator<String> iter = paramsJson.keys();

		while (iter.hasNext()) {
			String key = iter.next();	
			String value = paramsJson.getString(key);
			params.putSingle(key, value);
		}
		return params;
	}
}
