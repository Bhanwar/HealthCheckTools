package com.snapdeal.healthcheck.app.bo;

import java.util.List;

import com.snapdeal.healthcheck.app.model.TokenApiDetails;

public interface TokenApiDetailsBO {

	public TokenApiDetails getTokenApiDetails(String compName);
	
	public List<TokenApiDetails> getAllTokenApiDetails();
	
	public void updateTokenApiDetails(TokenApiDetails tokenApiDetail);
	
	public void saveTokenApiDetails(TokenApiDetails tokenApiDetail);
}
