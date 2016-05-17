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

	@Column(name = "component_name", nullable = false, unique = true)
	private String componentName;

	@Column(name = "qm_spoc")
	private String qmSpoc;
	@Column(name = "qa_spoc")
	private String qaSpoc;

	@Column(name = "authkey")
	private String authKey;
	@Column(name = "authkey_shared")
	private String authKeyShared;

	@Column(name = "endpoint")
	private String endpoint;

	@Column(name = "health_check_api_url")
	private String healthCheckApiUrl;
	@Column(name = "health_check_api_call_type")
	private String healthCheckApiCallType;
	@Column(name = "health_check_api_headers_json")
	private String healthCheckApiHeadersJson;
	@Column(name = "health_check_api_req_json")
	private String healthCheckApiReqJson;
	@Column(name = "health_check_api_resp")
	private String healthCheckApiResp;

	@Column(name = "first_getter_api_url")
	private String firstGetterApiUrl;
	@Column(name = "first_getter_api_call_type")
	private String firstGetterApiCallType;
	@Column(name = "first_getter_api_headers_json")
	private String firstGetterApiHeadersJson;
	@Column(name = "first_getter_api_req_json")
	private String firstGetterApiReqJson;
	@Column(name = "first_getter_api_resp")
	private String firstGetterApiResp;

	@Column(name = "second_getter_api_url")
	private String secondGetterApiUrl;
	@Column(name = "second_getter_api_call_type")
	private String secondGetterApiCallType;
	@Column(name = "second_getter_api_headers_json")
	private String secondGetterApiHeadersJson;
	@Column(name = "second_getter_api_req_json")
	private String secondGetterApiReqJson;
	@Column(name = "second_getter_api_resp")
	private String secondGetterApiResp;

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

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public String getAuthKeyShared() {
		return authKeyShared;
	}

	public void setAuthKeyShared(String authKeyShared) {
		this.authKeyShared = authKeyShared;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getHealthCheckApiUrl() {
		return healthCheckApiUrl;
	}

	public void setHealthCheckApiUrl(String healthCheckApiUrl) {
		this.healthCheckApiUrl = healthCheckApiUrl;
	}

	public String getHealthCheckApiCallType() {
		return healthCheckApiCallType;
	}

	public void setHealthCheckApiCallType(String healthCheckApiCallType) {
		this.healthCheckApiCallType = healthCheckApiCallType;
	}

	public String getHealthCheckApiHeadersJson() {
		return healthCheckApiHeadersJson;
	}

	public void setHealthCheckApiHeadersJson(String healthCheckApiHeadersJson) {
		this.healthCheckApiHeadersJson = healthCheckApiHeadersJson;
	}

	public String getHealthCheckApiReqJson() {
		return healthCheckApiReqJson;
	}

	public void setHealthCheckApiReqJson(String healthCheckApiReqJson) {
		this.healthCheckApiReqJson = healthCheckApiReqJson;
	}

	public String getHealthCheckApiResp() {
		return healthCheckApiResp;
	}

	public void setHealthCheckApiResp(String healthCheckApiResp) {
		this.healthCheckApiResp = healthCheckApiResp;
	}

	public String getFirstGetterApiUrl() {
		return firstGetterApiUrl;
	}

	public void setFirstGetterApiUrl(String firstGetterApiUrl) {
		this.firstGetterApiUrl = firstGetterApiUrl;
	}

	public String getFirstGetterApiCallType() {
		return firstGetterApiCallType;
	}

	public void setFirstGetterApiCallType(String firstGetterApiCallType) {
		this.firstGetterApiCallType = firstGetterApiCallType;
	}

	public String getFirstGetterApiHeadersJson() {
		return firstGetterApiHeadersJson;
	}

	public void setFirstGetterApiHeadersJson(String firstGetterApiHeadersJson) {
		this.firstGetterApiHeadersJson = firstGetterApiHeadersJson;
	}

	public String getFirstGetterApiReqJson() {
		return firstGetterApiReqJson;
	}

	public void setFirstGetterApiReqJson(String firstGetterApiReqJson) {
		this.firstGetterApiReqJson = firstGetterApiReqJson;
	}

	public String getFirstGetterApiResp() {
		return firstGetterApiResp;
	}

	public void setFirstGetterApiResp(String firstGetterApiResp) {
		this.firstGetterApiResp = firstGetterApiResp;
	}

	public String getSecondGetterApiUrl() {
		return secondGetterApiUrl;
	}

	public void setSecondGetterApiUrl(String secondGetterApiUrl) {
		this.secondGetterApiUrl = secondGetterApiUrl;
	}

	public String getSecondGetterApiCallType() {
		return secondGetterApiCallType;
	}

	public void setSecondGetterApiCallType(String secondGetterApiCallType) {
		this.secondGetterApiCallType = secondGetterApiCallType;
	}

	public String getSecondGetterApiHeadersJson() {
		return secondGetterApiHeadersJson;
	}

	public void setSecondGetterApiHeadersJson(String secondGetterApiHeadersJson) {
		this.secondGetterApiHeadersJson = secondGetterApiHeadersJson;
	}

	public String getSecondGetterApiReqJson() {
		return secondGetterApiReqJson;
	}

	public void setSecondGetterApiReqJson(String secondGetterApiReqJson) {
		this.secondGetterApiReqJson = secondGetterApiReqJson;
	}

	public String getSecondGetterApiResp() {
		return secondGetterApiResp;
	}

	public void setSecondGetterApiResp(String secondGetterApiResp) {
		this.secondGetterApiResp = secondGetterApiResp;
	}

	public Date getCreated() {
		return created;
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

	@Override
	public String toString() {
		return "ComponentDetails [id=" + id + ", componentName=" + componentName + ", qmSpoc=" + qmSpoc + ", qaSpoc="
				+ qaSpoc + ", authKey=" + authKey + ", authKeyShared=" + authKeyShared + ", endpoint=" + endpoint
				+ ", healthCheckApiUrl=" + healthCheckApiUrl + ", healthCheckApiCallType=" + healthCheckApiCallType
				+ ", healthCheckApiHeadersJson=" + healthCheckApiHeadersJson + ", healthCheckApiReqJson="
				+ healthCheckApiReqJson + ", healthCheckApiResp=" + healthCheckApiResp + ", firstGetterApiUrl="
				+ firstGetterApiUrl + ", firstGetterApiCallType=" + firstGetterApiCallType
				+ ", firstGetterApiHeadersJson=" + firstGetterApiHeadersJson + ", firstGetterApiReqJson="
				+ firstGetterApiReqJson + ", firstGetterApiResp=" + firstGetterApiResp + ", secondGetterApiUrl="
				+ secondGetterApiUrl + ", secondGetterApiCallType=" + secondGetterApiCallType
				+ ", secondGetterApiHeadersJson=" + secondGetterApiHeadersJson + ", secondGetterApiReqJson="
				+ secondGetterApiReqJson + ", secondGetterApiResp=" + secondGetterApiResp + ", created=" + created
				+ ", updated=" + updated + "]";
	}
}
