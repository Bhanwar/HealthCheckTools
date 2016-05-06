package com.snapdeal.healthcheck.app.model;

import org.springframework.beans.factory.annotation.Autowired;

import com.snapdeal.healthcheck.app.bo.ComponentDetailsBO;

public class QuartzJobDataHolder {
	
	@Autowired
	private ComponentDetailsBO compDetails;
	
	public ComponentDetailsBO getCompDetails() {
		return compDetails;
	}

}
