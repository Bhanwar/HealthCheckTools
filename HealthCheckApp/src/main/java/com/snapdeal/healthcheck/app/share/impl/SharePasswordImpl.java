package com.snapdeal.healthcheck.app.share.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snapdeal.healthcheck.app.bo.ComponentDetailsBO;
import com.snapdeal.healthcheck.app.model.ComponentDetails;
import com.snapdeal.healthcheck.app.model.PasswordShared;
import com.snapdeal.healthcheck.app.mongo.repositories.PasswordSharedRepository;
import com.snapdeal.healthcheck.app.share.SharePassword;
import com.snapdeal.healthcheck.app.utils.EmailUtil;

@Component
public class SharePasswordImpl implements SharePassword {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ComponentDetailsBO compData;
	
	@Autowired
	private PasswordSharedRepository pwdShared;
	
	@Override
	public void sharePasswordToQms() {
		
		List<ComponentDetails> compList = compData.getAllComponentDetails();
		for (ComponentDetails componentDetails : compList) {
			List<String> emailTo = new ArrayList<String>();
			emailTo.add(componentDetails.getQmSpoc());
			
			if (pwdShared.findSharedComponents(componentDetails.getComponentName()) == null) {
				String msgSubject = "Your AuthKey for HealthCheckApp Staging for " + componentDetails.getComponentName();
				String msgBody = "<html>Your AuthKey for HealthCheckApp Staging for <b>" + componentDetails.getComponentName() +"</b> is : <br><b>" + componentDetails.getAuthKey() + "</b><br>Please change the auth key using the below link<br><br>http://tm.snapdeal.com:9090/healthCheck/admin/updateAuthKey</html>";
				EmailUtil objEmail = new EmailUtil(emailTo, msgSubject, msgBody);
				objEmail.sendHTMLEmail();
				PasswordShared pwdShare = new PasswordShared();
				pwdShare.setComponentName(componentDetails.getComponentName());
				pwdShared.save(pwdShare);
				log.debug("Email sent for component: " + componentDetails.getComponentName() + " to: " + emailTo);
			} else
				log.debug("Email already sent for component: " + componentDetails.getComponentName() + " to: " + emailTo);
		}
	}
}
