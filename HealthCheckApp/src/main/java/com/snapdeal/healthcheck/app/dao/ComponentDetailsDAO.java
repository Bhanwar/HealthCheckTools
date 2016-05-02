package com.snapdeal.healthcheck.app.dao;

import java.util.List;

import com.snapdeal.healthcheck.app.model.ComponentDetails;

public interface ComponentDetailsDAO {

	public List<ComponentDetails> getAllEndpointDetails();

	public ComponentDetails getEndpointDetails(String keyName);
}
