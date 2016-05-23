package com.snapdeal.healthcheck.app.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snapdeal.healthcheck.app.model.HttpCallResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;


public class JerseyUtil {

	public static HttpCallResponse fetchResponse(String url, String callType, String contentType, String paramsJson) {
		HttpCallResponse httpResponse = new HttpCallResponse();
		try {
			String mediaType = fetchMediaType(contentType);
			ClientResponse response = null;

			if ("GET".equalsIgnoreCase(callType))
				response = callGet(url, mediaType);
			else if ("POST".equalsIgnoreCase(callType))
				response = callPost(url, mediaType, paramsJson);

			String statusCode = response.getClientResponseStatus().getReasonPhrase();
			httpResponse.setStatusCode(statusCode);
			httpResponse.setResponseHeaders((Map<String, List<String>>)response.getHeaders());
			httpResponse.setResponseBody(response.getEntity(String.class));
		}
		catch (Exception e) {
			httpResponse.setHttpCallException(e.getMessage());
		}
		return httpResponse;
	}


	public static ClientResponse callGet(String url, String contentType) throws Exception {
		Client client = Client.create();  
		WebResource webResource = client.resource(url);
		return webResource.type(contentType).get(ClientResponse.class);
	}


	public static ClientResponse callPost(String url, String contentType, String paramsJson) throws Exception {
		Client client = Client.create();  
		WebResource webResource = client.resource(url);

		if (paramsJson != null) {
			MultivaluedMap<String, String> params = fetchReqParamsMultivaluedMap(paramsJson);
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


	@SuppressWarnings("unchecked")
	public static MultivaluedMap<String, String> fetchReqParamsMultivaluedMap(String paramsJson) {
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();

		try {
			Map<String, String> reqParams = new ObjectMapper().readValue(paramsJson, HashMap.class);
			Iterator<String> iter = reqParams.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next().toString();	
				String value = reqParams.get(key);
				params.putSingle(key, value);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return params;
	}
}
