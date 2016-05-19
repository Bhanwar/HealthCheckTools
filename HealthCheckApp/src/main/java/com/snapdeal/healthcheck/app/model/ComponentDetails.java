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

	@Column(name = "health_check_api")
	private String healthCheckApi;
	@Column(name = "health_check_api_call_type")
	private String healthCheckApiCallType;
	@Column(name = "health_check_headers")
	private String healthCheckHeaders;
	@Column(name = "health_check_api_req_json")
	private String healthCheckApiReqJson;
	@Column(name = "health_check_api_resp")
	private String healthCheckApiResp;

	@Column(name = "first_get_api")
	private String firstGetApi;
	@Column(name = "first_get_api_call_type")
	private String firstGetApiCallType;
	@Column(name = "first_get_headers")
	private String firstGetHeaders;
	@Column(name = "first_get_api_req_json")
	private String firstGetApiReqJson;
	@Column(name = "first_get_api_resp")
	private String firstGetApiResp;

	@Column(name = "second_get_api")
	private String secondGetApi;
	@Column(name = "second_get_api_call_type")
	private String secondGetApiCallType;
	@Column(name = "second_get_headers")
	private String secondGetHeaders;
	@Column(name = "second_get_api_req_json")
	private String secondGetApiReqJson;
	@Column(name = "second_get_api_resp")
	private String secondGetApiResp;

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

	public String getHealthCheckHeaders() {
		return healthCheckHeaders;
	}

	public void setHealthCheckHeaders(String healthCheckHeaders) {
		this.healthCheckHeaders = healthCheckHeaders;
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

	public String getFirstGetHeaders() {
		return firstGetHeaders;
	}

	public void setFirstGetHeaders(String firstGetHeaders) {
		this.firstGetHeaders = firstGetHeaders;
	}

	public String getFirstGetApiReqJson() {
		return firstGetApiReqJson;
	}

	public void setFirstGetApiReqJson(String firstGetApiReqJson) {
		this.firstGetApiReqJson = firstGetApiReqJson;
	}

	public String getFirstGetApiResp() {
		return firstGetApiResp;
	}

	public void setFirstGetApiResp(String firstGetApiResp) {
		this.firstGetApiResp = firstGetApiResp;
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

	public String getSecondGetHeaders() {
		return secondGetHeaders;
	}

	public void setSecondGetHeaders(String secondGetHeaders) {
		this.secondGetHeaders = secondGetHeaders;
	}

	public String getSecondGetApiReqJson() {
		return secondGetApiReqJson;
	}

	public void setSecondGetApiReqJson(String secondGetApiReqJson) {
		this.secondGetApiReqJson = secondGetApiReqJson;
	}

	public String getSecondGetApiResp() {
		return secondGetApiResp;
	}

	public void setSecondGetApiResp(String secondGetApiResp) {
		this.secondGetApiResp = secondGetApiResp;
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
				+ ", healthCheckApi=" + healthCheckApi + ", healthCheckApiCallType=" + healthCheckApiCallType
				+ ", healthCheckHeaders=" + healthCheckHeaders + ", healthCheckApiReqJson=" + healthCheckApiReqJson
				+ ", healthCheckApiResp=" + healthCheckApiResp + ", firstGetApi=" + firstGetApi
				+ ", firstGetApiCallType=" + firstGetApiCallType + ", firstGetHeaders=" + firstGetHeaders
				+ ", firstGetApiReqJson=" + firstGetApiReqJson + ", firstGetApiResp=" + firstGetApiResp
				+ ", secondGetApi=" + secondGetApi + ", secondGetApiCallType=" + secondGetApiCallType
				+ ", secondGetHeaders=" + secondGetHeaders + ", secondGetApiReqJson=" + secondGetApiReqJson
				+ ", secondGetApiResp=" + secondGetApiResp + ", created=" + created + ", updated=" + updated + "]";
	}
}
