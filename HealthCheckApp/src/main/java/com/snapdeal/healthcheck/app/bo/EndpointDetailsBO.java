package com.snapdeal.healthcheck.app.bo;

import java.util.List;

import com.snapdeal.healthcheck.app.model.EndpointDetails;

public interface EndpointDetailsBO {
	
	public List<EndpointDetails> getAllEndpointDetails();
	
	public EndpointDetails getEndpointDetails(String keyName);
	
}
