package com.snapdeal.healthcheck.app.services.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.snapdeal.healthcheck.app.bo.AdminBO;
import com.snapdeal.healthcheck.app.bo.ComponentDetailsBO;
import com.snapdeal.healthcheck.app.model.Administrator;
import com.snapdeal.healthcheck.app.model.ComponentDetails;
import com.snapdeal.healthcheck.app.model.HealthCheckResult;
import com.snapdeal.healthcheck.app.services.AdminTask;


@org.springframework.stereotype.Component
public class AdminTaskImpl implements AdminTask{

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private ComponentDetailsBO compDetails;

	@Autowired
	private AdminBO adminSer;

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
					log.debug("Updating password for component: " + compName);
					comp.setAuthKey(newAuthKey);
					compDetails.updateComponentDetails(comp);
					return "Auth Key Updated!!";
				} else
					return "Incorrect Auth Key!!";
			} else 
				return "Auth keys do not match!!";
		} catch(Exception e) {

		}
		return result;
	}

	@Override
	public Map<Boolean, String> checkComponent(String data) {
		Map<Boolean, String> result = new HashMap<>();
		String resultData = "";
		try{
			JSONObject jsonData = new JSONObject(data);

			ComponentDetails component = new ComponentDetails();
			component.setComponentName(getJsonString(jsonData, "compName"));
			component.setQmSpoc(getJsonString(jsonData, "qmSpoc"));
			component.setQaSpoc(getJsonString(jsonData, "qaSpoc"));
			component.setEndpoint(getJsonString(jsonData, "endpoint"));
			component.setHealthCheckApiUrl(getJsonString(jsonData, "hcApiUrl"));
			component.setHealthCheckApiCallType(getJsonString(jsonData, "hcApiCallType"));
			component.setHealthCheckApiHeadersJson(getJsonString(jsonData, "hcApiHeadersJson"));
			component.setHealthCheckApiReqJson(getJsonString(jsonData, "hcApiReqJson"));
			component.setHealthCheckApiResp(getJsonString(jsonData, "hcApiResp"));
			component.setFirstGetterApiUrl(getJsonString(jsonData, "fgApiUrl"));
			component.setFirstGetterApiCallType(getJsonString(jsonData, "fgApiCallType"));
			component.setFirstGetterApiHeadersJson(getJsonString(jsonData, "fgApiHeadersJson"));
			component.setFirstGetterApiReqJson(getJsonString(jsonData, "fgApiReqJson"));
			component.setFirstGetterApiResp(getJsonString(jsonData, "fgApiResp"));
			component.setSecondGetterApiUrl(getJsonString(jsonData, "sgApiUrl"));
			component.setSecondGetterApiCallType(getJsonString(jsonData, "sgApiCallType"));
			component.setSecondGetterApiHeadersJson(getJsonString(jsonData, "sgApiHeadersJson"));
			component.setSecondGetterApiReqJson(getJsonString(jsonData, "sgApiReqJson"));
			component.setSecondGetterApiResp(getJsonString(jsonData, "sgApiResp"));

			HealthCheckResult resultObj = EnvHealthCheckImpl.checkServerHealth(component, null);

			StringBuilder dataResult = new StringBuilder();
			if(resultObj.isServerUp())
				dataResult.append("<h4>Success!!</h4>");
			else
				dataResult.append("<h4>Failed!!</h4>");
			dataResult.append("<br>Response:<br>");
			dataResult.append(resultObj);

			resultData = dataResult.toString();
			result.put(resultObj.isServerUp(), resultData);
		}catch(Exception e) {
			log.error("Exception occured while checking component status: " + e.getMessage(), e);
			resultData = "Exception occured while checking component status: " + e.getMessage();
			result.put(false, resultData);
		}

		return result;
	}

	private String getJsonString(JSONObject jsonData, String key) {
		String result = null;
		String data = jsonData.getString(key).trim();

		if(data!=null && !"".equals(data) && !"#EMPTY_STRING".equals(data))
			result = data;
		return result;
	}

	@Override
	public String addUpdateComponent(String data) {
		String resultData = "";
		try{
			JSONObject jsonData = new JSONObject(data);

			String authKey = getJsonString(jsonData, "authKey");
			Administrator admin = adminSer.getAdmin();
			if(admin == null)
				return "Something went wrong. Please contact admin";

			if(authKey != null && authKey.equals(admin.getAuthKey()))
				log.debug("Valid admin");
			else
				return "<h4>Not Authorized!!</h4>";

			ComponentDetails component = new ComponentDetails();
			component.setComponentName(getJsonString(jsonData, "compName"));
			component.setQmSpoc(getJsonString(jsonData, "qmSpoc"));
			component.setQaSpoc(getJsonString(jsonData, "qaSpoc"));
			component.setEndpoint(getJsonString(jsonData, "endpoint"));
			component.setHealthCheckApiUrl(getJsonString(jsonData, "hcApiUrl"));
			component.setHealthCheckApiCallType(getJsonString(jsonData, "hcApiCallType"));
			component.setHealthCheckApiHeadersJson(getJsonString(jsonData, "hcApiHeadersJson"));
			component.setHealthCheckApiReqJson(getJsonString(jsonData, "hcApiReqJson"));
			component.setHealthCheckApiResp(getJsonString(jsonData, "hcApiResp"));
			component.setFirstGetterApiUrl(getJsonString(jsonData, "fgApiUrl"));
			component.setFirstGetterApiCallType(getJsonString(jsonData, "fgApiCallType"));
			component.setFirstGetterApiHeadersJson(getJsonString(jsonData, "fgApiHeadersJson"));
			component.setFirstGetterApiReqJson(getJsonString(jsonData, "fgApiReqJson"));
			component.setFirstGetterApiResp(getJsonString(jsonData, "fgApiResp"));
			component.setSecondGetterApiUrl(getJsonString(jsonData, "sgApiUrl"));
			component.setSecondGetterApiCallType(getJsonString(jsonData, "sgApiCallType"));
			component.setSecondGetterApiHeadersJson(getJsonString(jsonData, "sgApiHeadersJson"));
			component.setSecondGetterApiReqJson(getJsonString(jsonData, "sgApiReqJson"));
			component.setSecondGetterApiResp(getJsonString(jsonData, "sgApiResp"));

			String validate = validateCompData(component);
			if(validate != null)
				return validate;

			HealthCheckResult resultObj = EnvHealthCheckImpl.checkServerHealth(component, null);
			if(resultObj.isServerUp()) {
				UUID uuid = UUID.randomUUID();
				component.setAuthKey(uuid.toString());
				component.setAuthKeyShared("NO");
				compDetails.saveComponentDetails(component);
				resultData = "<h4>Successfully added entry!!</h4>";
			} else {
				StringBuilder dataResult = new StringBuilder();
				dataResult.append("<h4>Failed!!</h4>");
				dataResult.append("<br>Test before you add!!<br>");
				dataResult.append("<br>Response:<br>");
				dataResult.append(resultObj);
				resultData = dataResult.toString();
			}
		}catch(Exception e) {
			log.error("Exception occured while checking component status: " + e.getMessage(), e);
			resultData = "Exception occured while checking component status: " + e.getMessage();
		}

		return resultData;
	}

	@Override
	public String deleteComponent(String data) {
		// TODO Auto-generated method stub
		return null;
	}

	private String validateCompData(ComponentDetails component) {
		String result = null;
		String compName = component.getComponentName();
		if(compName == null)
			return "Component name cannot be empty!!";
		if(compDetails.getComponentDetails(compName) != null)
			return "Component: " + compName + ", Already Exist!!";
		if(component.getQaSpoc() == null)
			return "QA SPOC details is required";
		if(component.getQmSpoc() == null)
			return "QM SPOC details is required";

		return result;
	}
}
