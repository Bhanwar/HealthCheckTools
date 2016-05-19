package com.snapdeal.healthcheck.app.comp.token;

import org.json.JSONObject;

import com.snapdeal.healthcheck.app.model.HttpCallResponse;
import com.snapdeal.healthcheck.app.model.TokenApiDetails;
import com.snapdeal.healthcheck.app.utils.RestUtil;


public class SellerServicesToken {

	public static String fetchTokenFromBody(TokenApiDetails tokenApi, String endpoint) {
		String url = endpoint + tokenApi.getLoginApi();
		String callType = tokenApi.getLoginApiCallType();
		String headersJson = "{\"Content-Type\":\"application/json\"}";
		String reqJson = tokenApi.getLoginApiReqJson();
		String invalidCredMsg = tokenApi.getLoginInvalidCredMsg();

		HttpCallResponse httpResponse = RestUtil.fetchResponse(url, callType, headersJson, reqJson);

		if (!"200 OK".equals(httpResponse.getStatusCode()))
			return "#LOGIN_FAILED";
		else if (httpResponse.getResponseBody().contains(invalidCredMsg))
			return "#INVALID_CREDENTIALS";
		else
			return new JSONObject(httpResponse.getResponseBody()).getString("token");
	}
}
