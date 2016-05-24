package com.snapdeal.healthcheck.app.comp.token;

import org.json.JSONObject;

import com.snapdeal.healthcheck.app.model.HttpCallResponse;
import com.snapdeal.healthcheck.app.model.TokenApiDetails;
import com.snapdeal.healthcheck.app.utils.RestUtil;


public class SellerServicesToken {

	public static HttpCallResponse fetchTokenFromBody(TokenApiDetails tokenApi, String endpoint) {
		String url = endpoint + tokenApi.getLoginApi();
		String callType = tokenApi.getLoginApiCallType();
		String headers = "{\"Content-Type\":\"application/json\"}";
		String payload = tokenApi.getLoginApiReqJson();

		HttpCallResponse httpResponse = RestUtil.fetchResponse(url, callType, headers, payload);
		if (httpResponse.getResponseBody() != null)
			httpResponse.setToken(new JSONObject(httpResponse.getResponseBody()).getString("token"));

		return httpResponse;
	}
}
