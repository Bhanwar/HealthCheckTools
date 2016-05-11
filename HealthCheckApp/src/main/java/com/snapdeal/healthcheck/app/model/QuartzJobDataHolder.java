package com.snapdeal.healthcheck.app.model;

import org.springframework.beans.factory.annotation.Autowired;

import com.snapdeal.healthcheck.app.bo.ComponentDetailsBO;
import com.snapdeal.healthcheck.app.services.SharePassword;

public class QuartzJobDataHolder {
	
	@Autowired
	private ComponentDetailsBO compDetails;
	
	@Autowired
	private SharePassword sharePwd;
	
	public ComponentDetailsBO getCompDetails() {
		return compDetails;
	}
	
	public void shareAuthKeysToQMs() {
		sharePwd.sharePasswordToQms();
	}

}
