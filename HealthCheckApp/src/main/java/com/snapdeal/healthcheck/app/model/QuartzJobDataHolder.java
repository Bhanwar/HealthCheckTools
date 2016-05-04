package com.snapdeal.healthcheck.app.model;

import org.springframework.beans.factory.annotation.Autowired;

import com.snapdeal.healthcheck.app.bo.ComponentDetailsBO;
import com.snapdeal.healthcheck.app.configurables.GetApiConfigValues;

public class QuartzJobDataHolder {
	
	@Autowired
	private ComponentDetailsBO compDetails;
	
	@Autowired
	private GetApiConfigValues objGetConfig;

	public ComponentDetailsBO getCompDetails() {
		return compDetails;
	}

	public GetApiConfigValues getObjGetConfig() {
		return objGetConfig;
	}

}
