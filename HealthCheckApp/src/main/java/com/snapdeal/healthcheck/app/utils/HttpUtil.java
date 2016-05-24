package com.snapdeal.healthcheck.app.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapdeal.healthcheck.app.model.HttpCallResponse;


public class HttpUtil {

	private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);


	public static HttpCallResponse fetchResponse(String url, String callType, String headers, String payload) {
		HttpCallResponse httpResponse = new HttpCallResponse();
		HttpURLConnection conn = null;
		OutputStream os = null;

		try {
			URL urlToHit = new URL(url);
			conn = (HttpURLConnection) urlToHit.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod(callType);

			if (headers != null) {
				JSONObject headersJson = new JSONObject(headers);
				Iterator<String> iter = headersJson.keys();
				while (iter.hasNext()) {
					String key = iter.next();	
					String value = headersJson.getString(key);
					conn.setRequestProperty(key, value);
				}
			}
			if ("POST".equals(callType)) {
				os = conn.getOutputStream();
				os.write(payload.getBytes());
				os.flush();
			}

			int responseCode = conn.getResponseCode();
			httpResponse.setStatusCode(responseCode+" "+conn.getResponseMessage());
			httpResponse.setResponseHeaders(conn.getHeaderFields());

			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));
				String output;
				StringBuilder result = new StringBuilder();
				while ((output=br.readLine()) != null) {
					result.append(output);
				}
				br.close();
				httpResponse.setResponseBody(result.toString());
			}
		} catch(Exception e) {
			log.error("Exception occured while doing "+callType+": " + e.getMessage(), e);
			httpResponse.setHttpCallException(e.getMessage());
		} finally {
			if(os != null)
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if(conn != null)
				conn.disconnect();
		}
		return httpResponse;
	}
}
