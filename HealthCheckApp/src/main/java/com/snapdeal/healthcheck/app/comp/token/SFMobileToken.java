package com.snapdeal.healthcheck.app.comp.token;

import java.util.List;

import com.snapdeal.healthcheck.app.model.HttpCallResponse;
import com.snapdeal.healthcheck.app.model.TokenApiDetails;
import com.snapdeal.healthcheck.app.utils.JerseyUtil;


public class SFMobileToken {

	public static HttpCallResponse fetchTokenFromHeader(TokenApiDetails tokenApi, String endpoint) {
		String url = endpoint + tokenApi.getLoginApi();
		String callType = tokenApi.getLoginApiCallType();
		String headers = "{\"Content-Type\":\"application/x-www-form-urlencoded\"}";
		String payload = tokenApi.getLoginApiReqJson();

		HttpCallResponse httpResponse = JerseyUtil.fetchResponse(url, callType, headers, payload);
		if (httpResponse.getResponseHeaders() != null) {
			List<String> cookies = httpResponse.getResponseHeaders().get("Set-Cookie");
			for (String cookie : cookies)
				if (cookie.startsWith("JSESSIONID="))
					httpResponse.setToken(cookie.substring(0, cookie.indexOf(";")));
		}
		return httpResponse;
	}
}
