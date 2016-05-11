package com.snapdeal.healthcheck.app.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.snapdeal.healthcheck.app.bo.ComponentDetailsBO;
import com.snapdeal.healthcheck.app.constants.AppConstant;
import com.snapdeal.healthcheck.app.model.ComponentDetails;
import com.snapdeal.healthcheck.app.services.SharePassword;
import com.snapdeal.healthcheck.app.utils.EmailUtil;

@Component
public class SharePasswordImpl implements SharePassword {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private ComponentDetailsBO compData;
	
	@Value("${admin_mail_id}")
	private String admin;

	@Override
	public void sharePasswordToQms() {

		List<ComponentDetails> compList = compData.getAllComponentDetails();
		for (ComponentDetails componentDetails : compList) {
			List<String> emailTo = new ArrayList<String>();
			emailTo.add(componentDetails.getQmSpoc());

			if (componentDetails.getAuthKeyShared().equals("NO")) {
				String msgSubject = "Your AuthKey for HealthCheckApp Staging for "
						+ componentDetails.getComponentName();
				String msgBody = "<html>Your AuthKey for HealthCheckApp Staging for <b>"
						+ componentDetails.getComponentName() + "</b> is : <br><b>" + componentDetails.getAuthKey()
						+ "</b><br>Please change the auth key using the below link<br><br>http://tm.snapdeal.com:9090/healthCheck/admin/updateAuthKey"
						+ "<br><br><b>NOTE: This auth key should be used to update reason when the server is down for the component: <i>" + componentDetails.getComponentName() + "</i></b>"
						+ "<br>For any queries please contact: " + admin
						+ "<br>" + AppConstant.MAIL_SIGN + "</html>";
				EmailUtil objEmail = new EmailUtil(emailTo, msgSubject, msgBody);
				objEmail.sendHTMLEmail();
				componentDetails.setAuthKeyShared("YES");
				compData.updateComponentDetails(componentDetails);
				log.debug("Update Auth Key: Email sent for component: " + componentDetails.getComponentName() + " to: "
						+ emailTo);
			}
		}
	}
}
