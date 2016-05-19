package com.snapdeal.healthcheck.app.comp.token;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import com.snapdeal.healthcheck.app.model.TokenApiDetails;
import com.snapdeal.healthcheck.app.utils.JerseyUtil;
import com.sun.jersey.api.client.ClientResponse;


public class SFMobileToken {

	public static String fetchTokenFromHeader(TokenApiDetails tokenApi, String endpoint) {
		String url = endpoint + tokenApi.getLoginApi();
		String callType = tokenApi.getLoginApiCallType();
		String contentType = "application/x-www-form-urlencoded";
		String reqJson = tokenApi.getLoginApiReqJson();
		String invalidCredMsg = tokenApi.getLoginInvalidCredMsg();

		ClientResponse response = JerseyUtil.fetchResponse(url, callType, contentType, reqJson);

		if (response!=null && response.getStatus()!=200)
			return "#LOGIN_FAILED";
		else if (response.getEntity(String.class).contains(invalidCredMsg))
			return "#INVALID_CREDENTIALS";
		else {
			MultivaluedMap<String, String> headers = response.getHeaders();
			List<String> cookies = headers.get("Set-Cookie");
			String jSessionID = null;
			for (String cookie : cookies)
				if (cookie.startsWith("JSESSIONID="))
					jSessionID = cookie.substring(0, cookie.indexOf(";"));
			return jSessionID;
		}
	}
}
