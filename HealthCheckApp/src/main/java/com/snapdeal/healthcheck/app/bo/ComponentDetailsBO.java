package com.snapdeal.healthcheck.app.bo;

import java.util.List;

import com.snapdeal.healthcheck.app.model.ComponentDetails;

public interface ComponentDetailsBO {
	
	public List<ComponentDetails> getAllEndpointDetails();
	
	public ComponentDetails getEndpointDetails(String keyName);
	
}
