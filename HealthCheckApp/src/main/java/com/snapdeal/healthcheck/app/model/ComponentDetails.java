package com.snapdeal.healthcheck.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "components")
public class ComponentDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "component_name")
	private String componentName;
	
	@Column(name = "qm_spoc")
	private String qmSpoc;
	
	@Column(name = "qa_spoc")
	private String qaSpoc;
	
	@Column(name = "endpoint")
	private String endpoint;
	
	@Column(name = "authkey")
	private String authKey;
	
	@Column(name = "health_check_api")
	private String healthCheckApi;
	@Column(name = "health_check_api_call_type")
	private String healthCheckApiCallType;
	@Column(name = "health_check_api_resp")
	private String healthCheckApiResponse;
	
	@Column(name = "first_get_api")
	private String firstGetApi;
	@Column(name = "first_get_api_call_type")
	private String firstGetApiCallType;
	@Column(name = "first_get_api_req_json")
	private String firstGetApiReqJson;
	@Column(name = "first_get_api_resp")
	private String firstGetApiResponce;
	
	@Column(name = "second_get_api")
	private String secondGetApi;
	@Column(name = "second_get_api_call_type")
	private String secondGetApiCallType;
	@Column(name = "second_get_api_req_json")
	private String secondGetApiReqJson;
	@Column(name = "second_get_api_resp")
	private String secondGetApiResponce;
	
	@Column(name = "created")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Column(name = "updated")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getQmSpoc() {
		return qmSpoc;
	}

	public void setQmSpoc(String qmSpoc) {
		this.qmSpoc = qmSpoc;
	}

	public String getQaSpoc() {
		return qaSpoc;
	}

	public void setQaSpoc(String qaSpoc) {
		this.qaSpoc = qaSpoc;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public Date getCreated() {
		return created;
	}

	public String getHealthCheckApi() {
		return healthCheckApi;
	}

	public void setHealthCheckApi(String healthCheckApi) {
		this.healthCheckApi = healthCheckApi;
	}

	public String getHealthCheckApiCallType() {
		return healthCheckApiCallType;
	}

	public void setHealthCheckApiCallType(String healthCheckApiCallType) {
		this.healthCheckApiCallType = healthCheckApiCallType;
	}

	public String getHealthCheckApiResponse() {
		return healthCheckApiResponse;
	}

	public void setHealthCheckApiResponse(String healthCheckApiResponse) {
		this.healthCheckApiResponse = healthCheckApiResponse;
	}

	public String getFirstGetApi() {
		return firstGetApi;
	}

	public void setFirstGetApi(String firstGetApi) {
		this.firstGetApi = firstGetApi;
	}

	public String getFirstGetApiCallType() {
		return firstGetApiCallType;
	}

	public void setFirstGetApiCallType(String firstGetApiCallType) {
		this.firstGetApiCallType = firstGetApiCallType;
	}

	public String getFirstGetApiReqJson() {
		return firstGetApiReqJson;
	}

	public void setFirstGetApiReqJson(String firstGetApiReqJson) {
		this.firstGetApiReqJson = firstGetApiReqJson;
	}

	public String getFirstGetApiResponce() {
		return firstGetApiResponce;
	}

	public void setFirstGetApiResponce(String firstGetApiResponce) {
		this.firstGetApiResponce = firstGetApiResponce;
	}

	public String getSecondGetApi() {
		return secondGetApi;
	}

	public void setSecondGetApi(String secondGetApi) {
		this.secondGetApi = secondGetApi;
	}

	public String getSecondGetApiCallType() {
		return secondGetApiCallType;
	}

	public void setSecondGetApiCallType(String secondGetApiCallType) {
		this.secondGetApiCallType = secondGetApiCallType;
	}

	public String getSecondGetApiReqJson() {
		return secondGetApiReqJson;
	}

	public void setSecondGetApiReqJson(String secondGetApiReqJson) {
		this.secondGetApiReqJson = secondGetApiReqJson;
	}

	public String getSecondGetApiResponce() {
		return secondGetApiResponce;
	}

	public void setSecondGetApiResponce(String secondGetApiResponce) {
		this.secondGetApiResponce = secondGetApiResponce;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}
}
