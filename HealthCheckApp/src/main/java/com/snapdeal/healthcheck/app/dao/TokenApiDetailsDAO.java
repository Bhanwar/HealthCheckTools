package com.snapdeal.healthcheck.app.dao;

import com.snapdeal.healthcheck.app.model.TokenApiDetails;

public interface TokenApiDetailsDAO {

	public TokenApiDetails getTokenApiDetails(String compName);
	
}
