package com.snapdeal.healthcheck.app.services.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.snapdeal.healthcheck.app.bo.AdminBO;
import com.snapdeal.healthcheck.app.bo.ComponentDetailsBO;
import com.snapdeal.healthcheck.app.bo.TokenApiDetailsBO;
import com.snapdeal.healthcheck.app.constants.AppConstant;
import com.snapdeal.healthcheck.app.enums.ComponentType;
import com.snapdeal.healthcheck.app.enums.TokenComponent;
import com.snapdeal.healthcheck.app.model.Administrator;
import com.snapdeal.healthcheck.app.model.ComponentDetails;
import com.snapdeal.healthcheck.app.model.HealthCheckResult;
import com.snapdeal.healthcheck.app.model.TimelyCompData;
import com.snapdeal.healthcheck.app.model.TokenApiDetails;
import com.snapdeal.healthcheck.app.model.UIComponent;
import com.snapdeal.healthcheck.app.mongo.repositories.MongoRepoService;
import com.snapdeal.healthcheck.app.services.AdminTask;
import com.snapdeal.healthcheck.app.utils.MailHtmlData;


@org.springframework.stereotype.Component
public class AdminTaskImpl implements AdminTask{

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private AdminBO adminSer;

	@Autowired
	private TokenApiDetailsBO tokenDetails;

	@Autowired
	private ComponentDetailsBO compDetails;
	
	@Autowired
	private MongoRepoService mongoService;

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
			String compName = null;
			if(jsonData.getBoolean("tokenRequired"))
				compName = getJsonString(jsonData, "tokenCompName");
			else
				compName = getJsonString(jsonData, "compName");
			
			TokenApiDetails tokenApi = getTokenApiDetailsFromJSON(jsonData, compName);
			ComponentDetails component = getComponentDetailsFromJSON(jsonData, null, compName);
			HealthCheckResult resultObj = EnvHealthCheckImpl.checkServerHealth(component, null, tokenApi);

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

	
	@Override
	public String addUpdateComponent(String data) {
		String resultData = "";
		try{
			JSONObject jsonData = new JSONObject(data);
			String updateCompName = null;
			String authKey = getJsonString(jsonData, "authKey");
			HealthCheckResult resultObj = null;
			TokenApiDetails tokenApi = null;
			ComponentDetails component = null;
			Administrator admin = adminSer.getAdmin();
			String compName = null;
			boolean tokenApiExist = jsonData.getBoolean("tokenRequired");
			boolean updateComp = jsonData.getBoolean("updateComp");
			if(admin == null)
				return "Something went wrong. Please contact admin";
			if(authKey != null && authKey.equals(admin.getAuthKey()))
				log.debug("Valid admin");
			else
				return "<h4>Not Authorized!!</h4>";

			if(tokenApiExist) {
				compName = getJsonString(jsonData, "tokenCompName");
				tokenApi = getTokenApiDetailsFromJSON(jsonData, compName);
			} else
				compName = getJsonString(jsonData, "compName");
			
			if(updateComp) {
				updateCompName = getJsonString(jsonData, "updateCompName");
				component = compDetails.getComponentDetails(updateCompName);	
			}
			
			component = getComponentDetailsFromJSON(jsonData, component, compName);
			
			String validate = validateCompData(component, updateCompName);
			if(validate != null)
				return validate;
			if(component.isEnabled())
				resultObj = EnvHealthCheckImpl.checkServerHealth(component, null, tokenApi);
			
			if(!component.isEnabled() || resultObj.isServerUp()) {
				if(updateComp) {
					compDetails.updateComponentDetails(component);
					resultData = "<h4>Successfully updated entry!!</h4>Old Component Name: " + updateCompName + "<br>New Component Name: " + component.getComponentName();
				} else {
					UUID uuid = UUID.randomUUID();
					component.setAuthKey(uuid.toString());
					component.setAuthKeyShared("NO");
					compDetails.saveComponentDetails(component);
					if(tokenApiExist)
						tokenDetails.saveTokenApiDetails(tokenApi);
					resultData = "<h4>Successfully added entry!! - " + component.getComponentName() + "</h4>";
				}
			} else {
				StringBuilder dataResult = new StringBuilder();
				dataResult.append("<h4>Failed!!</h4>");
				dataResult.append("<br>Test before you add!!<br>");
				dataResult.append("<br>Response:<br>");
				dataResult.append(resultObj);
				resultData = dataResult.toString();
			}
		}catch(Exception e) {
			log.error("Exception occured while adding component details: " + e.getMessage(), e);
			resultData = "Exception occured while adding component details: " + e.getMessage();
		}
		return resultData;
	}

	@Override
	public String deleteComponent(String data) {
		// TODO Auto-generated method stub
		return null;
	}

	private TokenApiDetails getTokenApiDetailsFromJSON(JSONObject jsonData, String compName) {
		TokenApiDetails tokenApi = null;
		TokenComponent tokenComp = TokenComponent.getValueOf(compName);
		if(!tokenComp.equals(TokenComponent.INV)) {
			tokenApi = new TokenApiDetails();
			tokenApi.setComponentName(compName);
			tokenApi.setLoginApi(getJsonString(jsonData, "loginApiUrl"));
			tokenApi.setLoginApiCallType(getJsonString(jsonData, "loginApiCallType"));
			tokenApi.setLoginApiReqJson(getJsonString(jsonData, "loginApiReqJson"));
			tokenApi.setLoginInvalidCredMsg(getJsonString(jsonData, "loginInvalidCredMsg"));
		}
		return tokenApi;
	}
	
	private String getJsonString(JSONObject jsonData, String key) {
		String result = null;
		String data = jsonData.getString(key).trim();
		if(data!= null && !data.equals("")) {
			if(data.equals(AppConstant.ADMIN_UI_EMPTY_STRING))
				result = "";
			else
				result = data;
		}
		return result;
	}

	private ComponentDetails getComponentDetailsFromJSON(JSONObject jsonData, ComponentDetails component, String compName) {
		if(component == null)
			component = new ComponentDetails();
		
		component.setComponentName(compName);
		component.setComponentType(ComponentType.getValueOf(getJsonString(jsonData, "compType")));
		component.setEnabled(jsonData.getBoolean("enabled"));
		component.setQmSpoc(getJsonString(jsonData, "qmSpoc"));
		component.setQaSpoc(getJsonString(jsonData, "qaSpoc"));
		component.setEndpoint(getJsonString(jsonData, "endpoint"));
		component.setHealthCheckApi(getJsonString(jsonData, "hcApiUrl"));
		component.setHealthCheckApiCallType(getJsonString(jsonData, "hcApiCallType"));
		component.setHealthCheckHeaders(getJsonString(jsonData, "hcApiHeadersJson"));
		component.setHealthCheckApiReqJson(getJsonString(jsonData, "hcApiReqJson"));
		component.setHealthCheckApiResp(getJsonString(jsonData, "hcApiResp"));
		component.setFirstGetApi(getJsonString(jsonData, "fgApiUrl"));
		component.setFirstGetApiCallType(getJsonString(jsonData, "fgApiCallType"));
		component.setFirstGetHeaders(getJsonString(jsonData, "fgApiHeadersJson"));
		component.setFirstGetApiReqJson(getJsonString(jsonData, "fgApiReqJson"));
		component.setFirstGetApiResp(getJsonString(jsonData, "fgApiResp"));
		component.setSecondGetApi(getJsonString(jsonData, "sgApiUrl"));
		component.setSecondGetApiCallType(getJsonString(jsonData, "sgApiCallType"));
		component.setSecondGetHeaders(getJsonString(jsonData, "sgApiHeadersJson"));
		component.setSecondGetApiReqJson(getJsonString(jsonData, "sgApiReqJson"));
		component.setSecondGetApiResp(getJsonString(jsonData, "sgApiResp"));
		return component;
	}
	
	private String validateCompData(ComponentDetails component, String updateCompName) {
		String result = null;
		String compName = component.getComponentName();
		if(compName == null)
			return "Component name cannot be empty!!";
		
		if(updateCompName == null || (updateCompName != null && !updateCompName.equals(compName))) {
			if(compDetails.getComponentDetails(compName) != null)
				return "Component: " + compName + ", Already Exist!!";
			if (tokenDetails.getTokenApiDetails(compName) != null)
				return "Component Token already exist for: " + compName;
		}
		
		if(component.isEnabled()) {
			if(component.getQaSpoc() == null)
				return "QA SPOC details is required";
			if(component.getQmSpoc() == null)
				return "QM SPOC details is required";
		}
		return result;
	}

	@Override
	public Map<String, UIComponent> getCompsForUpdate() {
		Map<String, UIComponent> result = new TreeMap<>();
		List<ComponentDetails> componentList = compDetails.getAllComponentDetails();
		List<TokenApiDetails> tokenApiList = tokenDetails.getAllTokenApiDetails();
		for(ComponentDetails comp : componentList) {
			UIComponent component = new UIComponent();
			component.setCompDetails(comp);
			component.setCompName(comp.getComponentName());
			result.put(comp.getComponentName(), component);
		}
		for(TokenApiDetails tokenApi : tokenApiList) {
			UIComponent component = result.get(tokenApi.getComponentName());
			if(component != null)
				component.setTokenDetails(tokenApi);
		}
		return result;
	}

	@Override
	public String getReportDateRangeData(String data) {
		Date startDate;
		Date endDate;
		try {
			JSONObject jsonData = new JSONObject(data);
			String startDateStr = getJsonString(jsonData, "startDate");
			String endDateStr = getJsonString(jsonData, "endDate");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			try {
				startDate = formatter.parse(startDateStr);
				endDate = formatter.parse(endDateStr);
			}catch(Exception e) {
				return "Please pass valid dates in the format: dd-mm-yyyy";
			}
			log.info("Calling get timely data: " + startDate + " " + endDate);
			Map<String, TimelyCompData> result = mongoService.getTimelyData(startDate, endDate);
			if(result == null)
				return "<h3>End Date cannot be less than Start Date</h3>";
			if(result.isEmpty())
				return "<h3>No servers were down between this date range!</h3>";
			else
				return MailHtmlData.createHtmlTableForTimelyReportData(result);
		}catch(Exception e) {
			log.error("Exception: " + e.getMessage(), e);
			return "Exception occured: " + e.getMessage() + "<br>Please contact admin!";
		}
	}

	@Override
	public Map<String, String> getCompsForEndpointUpdate() {
		Map<String, String> result = new TreeMap<>();
		List<ComponentDetails> componentList = compDetails.getAllEnabledComponentDetails();
		for(ComponentDetails comp : componentList) {
			result.put(comp.getComponentName(), comp.getEndpoint());
		}
		return result;
	}

	@Override
	public Map<Boolean, String> checkComponentForEndpointUpdate(String data) {
		Map<Boolean, String> result = new HashMap<>();
		String resultData = "";
		try{
			JSONObject jsonData = new JSONObject(data);
			String compName = getJsonString(jsonData, "updateCompName");
			String endpoint = getJsonString(jsonData, "endpoint");
			
			ComponentDetails component = compDetails.getComponentDetails(compName);
			component.setEndpoint(endpoint);
			HealthCheckResult resultObj = EnvHealthCheckImpl.checkServerHealth(component, null, null);

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

	@Override
	public String updateEndpoint(String data) {
		String resultData = "";
		try{
			JSONObject jsonData = new JSONObject(data);
			String compName = getJsonString(jsonData, "updateCompName");
			String endpoint = getJsonString(jsonData, "endpoint");
			String authKey = getJsonString(jsonData, "authKey");
			if(authKey == null)
				return "<h4>Auth key required!!</h4>";
			ComponentDetails component = compDetails.getComponentDetails(compName);
			if(!authKey.equals(component.getAuthKey()))
				return "<h4>Not Authorized!!</h4>";
			String oldEP = component.getEndpoint();
			component.setEndpoint(endpoint);
			HealthCheckResult resultObj = EnvHealthCheckImpl.checkServerHealth(component, null, null);

			StringBuilder dataResult = new StringBuilder();
			if(resultObj.isServerUp()) {
				compDetails.updateComponentDetails(component);
				dataResult.append("<h4>Successfully updated endpoint from: "+ oldEP +" to: "+ endpoint +"</h4>");
			} else
				dataResult.append("<h4>Please Test the connection before submitting</h4>");
			
			resultData = dataResult.toString();
		}catch(Exception e) {
			log.error("Exception occured while checking component status: " + e.getMessage(), e);
			resultData = "Exception occured while checking component status: " + e.getMessage();
		}
		return resultData;
	}
}
