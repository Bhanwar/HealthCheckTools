package com.snapdeal.healthcheck.app.dao;

import java.util.List;

import com.snapdeal.healthcheck.app.model.EndpointDetails;

public interface EndpointDetailsDAO {

	public List<EndpointDetails> getAllEndpointDetails();

	public EndpointDetails getEndpointDetails(String keyName);
}
