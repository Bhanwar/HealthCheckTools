package com.snapdeal.healthcheck.app.dao;

import java.util.List;

import com.snapdeal.healthcheck.app.model.ComponentDetails;

public interface ComponentDetailsDAO {

	public List<ComponentDetails> getAllComponentDetails();
	
	public List<ComponentDetails> getAllEnabledComponentDetails();

	public ComponentDetails getComponentDetails(String compName);
	
	public void saveComponentDetails(ComponentDetails compDetail);
	
	public void updateComponentDetails(ComponentDetails compDetail);
	
	public void deleteComponent(ComponentDetails compDetail);
}
