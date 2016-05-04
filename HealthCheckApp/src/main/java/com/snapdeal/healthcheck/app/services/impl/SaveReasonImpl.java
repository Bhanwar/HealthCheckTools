package com.snapdeal.healthcheck.app.services.impl;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.snapdeal.healthcheck.app.bo.ComponentDetailsBO;
import com.snapdeal.healthcheck.app.enums.DownTimeReasonCode;
import com.snapdeal.healthcheck.app.model.ComponentDetails;
import com.snapdeal.healthcheck.app.model.DownTimeData;
import com.snapdeal.healthcheck.app.mongo.repositories.DownTimeDataRepository;
import com.snapdeal.healthcheck.app.services.SaveReason;

@org.springframework.stereotype.Component
public class SaveReasonImpl implements SaveReason{
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private DownTimeDataRepository downTimeRepo; 
	
	@Autowired
	private ComponentDetailsBO compDetails;
	
	@Override
	public String saveUpdateReason(String data) {
		String result = "Update FAILED. Contact admin!";
		try {
			JSONObject jsonData = new JSONObject(data);
			String id = getJsonString(jsonData, "comp");
			String reason = getJsonString(jsonData, "reason");
			String desc = getJsonString(jsonData, "description");
			String authKey = getJsonString(jsonData, "authKey");
			log.debug("Data received: " + id + " " + reason + " " + desc);
			DownTimeData dataObj = downTimeRepo.findById(id);
			if(dataObj == null) {
				log.warn("Could not find mongo entry with id: " + id);
				result = "No entry found to update!!";
			} else {
				String compName = dataObj.getComponentName();
				ComponentDetails comp = compDetails.getComponentDetails(compName);
				if(comp != null && comp.getAuthKey().equals(authKey)) {
					DownTimeReasonCode reasonCode = DownTimeReasonCode.getValueOf(reason);
					dataObj.setReasonCode(reasonCode);
					dataObj.setDescription(desc);
					log.debug("Saving data in Mongo");
					downTimeRepo.save(dataObj);
					log.debug("Data saved");
					return "Reason saved for component - " + compName;
				} else {
					return "Not Authorized!!";
				}
			}
		}catch(Exception e){
			log.error("Exception occured: ",e);
		}
		return result;
	}
	
	private String getJsonString(JSONObject jsonData, String key) {
		String data = null;
		data = jsonData.getString(key).trim();
		if(data!= null && !data.equals("")) {
			return data;
		}
		return data;
	}

}
