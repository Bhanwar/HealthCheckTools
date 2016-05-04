package com.snapdeal.healthcheck.app.services.impl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.snapdeal.healthcheck.app.bo.ComponentDetailsBO;
import com.snapdeal.healthcheck.app.model.ComponentDetails;
import com.snapdeal.healthcheck.app.services.AdminTask;


@org.springframework.stereotype.Component
public class AdminTaskImpl implements AdminTask{
	
	@Autowired
	private ComponentDetailsBO compDetails;

	@Override
	public String changePassword(String data) {
		String result = "";
		try {
			JSONObject jsonData = new JSONObject(data);
			String compName = getJsonString(jsonData, "comp");
			String oldAuthKey = getJsonString(jsonData, "oldAuthKey");
			String newAuthKey = getJsonString(jsonData, "newAuthKey");
			if(oldAuthKey == null)
				return "Old Auth Key is required!!";
			if(newAuthKey == null)
				return "New Auth Key is required!!";
			if(newAuthKey.equals(getJsonString(jsonData, "newReAuthKey"))) {
				ComponentDetails comp = compDetails.getComponentDetails(compName);
				if(comp.getAuthKey().equals(oldAuthKey)) {
					comp.setAuthKey(newAuthKey);
					compDetails.saveComponentDetails(comp);
					return "Auth Key Updated!!";
				} else
					return "Incorrect Auth Key!!";
			} else 
				return "Auth keys do not match!!";
			
		} catch(Exception e) {
			
		}
		return result;
	}

	
	private String getJsonString(JSONObject jsonData, String key) {
		String result = null;
		String data = jsonData.getString(key).trim();
		if(data!= null && !data.equals("")) {
			result = data;
		}
		return result;
	}
}
