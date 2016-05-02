package com.snapdeal.healthcheck.app.dao;

import java.util.List;

import com.snapdeal.healthcheck.app.model.ComponentDetais;

public interface ComponentDetailsDAO {

	public List<ComponentDetais> getAllEndpointDetails();

	public ComponentDetais getEndpointDetails(String keyName);
}
