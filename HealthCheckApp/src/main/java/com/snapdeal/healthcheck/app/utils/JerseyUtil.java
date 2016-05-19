package com.snapdeal.healthcheck.app.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;


public class JerseyUtil {

	public static ClientResponse fetchResponse(String url, String callType, String contentType, String paramsJson) {
		ClientResponse response = null;
		try {
			String mediaType = fetchMediaType(contentType);
			if ("GET".equalsIgnoreCase(callType))
				response = callGet(url, mediaType);
			else if ("POST".equalsIgnoreCase(callType))
				response = callPost(url, mediaType, paramsJson);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return response;
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
			MultivaluedMap<String, String> reqParams = fetchRequestBody(paramsJson);
			return webResource.type(contentType).post(ClientResponse.class, reqParams);
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
	public static MultivaluedMap<String, String> fetchRequestBody(String paramsJson) {
		MultivaluedMap<String, String> reqParams = new MultivaluedMapImpl();
		try {
			Map<String, String> params = new ObjectMapper().readValue(paramsJson, HashMap.class);

			Iterator<String> iter = params.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next().toString();	
				String value = params.get(key);
				reqParams.putSingle(key, value);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return reqParams;
	}
}
