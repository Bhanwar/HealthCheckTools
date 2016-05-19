package com.snapdeal.healthcheck.app.dao;

import com.snapdeal.healthcheck.app.model.TokenApiDetails;

public interface TokenApiDetailsDAO {

	public TokenApiDetails getTokenApiDetails(String compName);

	public void saveTokenApiDetails(TokenApiDetails tokenApiDetail);

	public void updateTokenApiDetails(TokenApiDetails tokenApiDetail);
}
