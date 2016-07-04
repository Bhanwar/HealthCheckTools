package com.snapdeal.healthcheck.app.services;

import java.util.Map;

import com.snapdeal.healthcheck.app.model.UIComponent;

public interface AdminTask {

	public String changePassword(String data);
	
	public Map<Boolean, String> checkComponent(String data);
	
	public Map<Boolean, String> checkComponentForEndpointUpdate(String data);
	
	public String addUpdateComponent(String data);
	
	public String updateEndpoint(String data);
	
	public String deleteComponent(String data);
	
	public Map<String, UIComponent> getCompsForUpdate();
	
	public Map<String, String> getCompsForEndpointUpdate();
	
	public String getReportDateRangeData(String data);
}
