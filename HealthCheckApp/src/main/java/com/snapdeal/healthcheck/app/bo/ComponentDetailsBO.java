package com.snapdeal.healthcheck.app.bo;

import java.util.List;

import com.snapdeal.healthcheck.app.model.ComponentDetais;

public interface ComponentDetailsBO {
	
	public List<ComponentDetais> getAllEndpointDetails();
	
	public ComponentDetais getEndpointDetails(String keyName);
	
}
