package com.snapdeal.healthcheck.app.services;

import java.util.Map;

public interface AdminTask {

	public String changePassword(String data);
	
	public Map<Boolean, String> checkComponent(String data);
	
	public String addUpdateComponent(String data);
	
	public String deleteComponent(String data);
}
