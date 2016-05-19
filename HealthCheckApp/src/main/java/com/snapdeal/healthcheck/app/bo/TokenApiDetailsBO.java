package com.snapdeal.healthcheck.app.bo;

import com.snapdeal.healthcheck.app.model.TokenApiDetails;

public interface TokenApiDetailsBO {

	public TokenApiDetails getTokenApiDetails(String compName);
	
	public void updateTokenApiDetails(TokenApiDetails tokenApiDetail);
	
	public void saveTokenApiDetails(TokenApiDetails tokenApiDetail);
}
