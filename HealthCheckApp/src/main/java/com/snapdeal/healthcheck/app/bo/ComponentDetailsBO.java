package com.snapdeal.healthcheck.app.bo;

import java.util.List;

import com.snapdeal.healthcheck.app.model.ComponentDetails;

public interface ComponentDetailsBO {
	
	public List<ComponentDetails> getAllEnabledComponentDetails();
	
	public List<ComponentDetails> getAllComponentDetails();
	
	public ComponentDetails getComponentDetails(String compName);
	
	public void updateComponentDetails(ComponentDetails compDetail);
	
	public void saveComponentDetails(ComponentDetails compDetail);
	
	public void deleteComponent(ComponentDetails compDetail);
	
}
