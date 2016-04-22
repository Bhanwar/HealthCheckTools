package com.snapdeal.healthcheck.httputil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpCall {
	
	private static final Logger log = LoggerFactory.getLogger(HttpCall.class);
	
	public static HttpCallResponse callPost(String url, String json) {
		HttpURLConnection conn = null;
		HttpCallResponse response = null;
		OutputStream os = null;
		try {
			response = new HttpCallResponse();
			URL urlToHit = new URL(url);
			conn = (HttpURLConnection) urlToHit.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestMethod("POST");
			log.debug("URL: " + url);
			log.debug("Request JSON: " + json);
			os = conn.getOutputStream();
			os.write(json.getBytes());
			os.flush();
			
			response.setStatusCode(conn.getResponseCode() + " " + conn.getResponseMessage());
			
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));
				String output;
				StringBuilder result = new StringBuilder();
				while ((output = br.readLine()) != null) {
					result.append(output);
				}
				br.close();
				response.setResponseBody(result.toString());
			}
		}catch(Exception e) {
			log.error("Exception occured while doing post: " + e.getMessage(), e);
		}finally {
			if(os!=null)
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			
			if(conn!=null)
				conn.disconnect();
		}
		return response;
	}
	
	public static HttpCallResponse callGet(String url) {
		HttpURLConnection conn = null;
		HttpCallResponse response = null;
		try {
			response = new HttpCallResponse();
			URL urlToHit = new URL(url);
			conn = (HttpURLConnection) urlToHit.openConnection();
			conn.setRequestMethod("GET");
			response.setStatusCode(conn.getResponseCode() + " " + conn.getResponseMessage());
			
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));
				String output;
				StringBuilder result = new StringBuilder();
				while ((output = br.readLine()) != null) {
					result.append(output);
				}
				br.close();
				response.setResponseBody(result.toString());
			}
		}catch(Exception e) {
			log.error("Exception occured while doing get: " + e.getMessage(), e);
		}finally {
			if(conn!=null)
				conn.disconnect();
		}
		return response;
	}
	

}
