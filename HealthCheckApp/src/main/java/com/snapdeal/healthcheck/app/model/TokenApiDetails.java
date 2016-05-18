package com.snapdeal.healthcheck.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "token_api_details")
public class TokenApiDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "component_name", nullable = false, unique = true)
	private String componentName;

	@Column(name = "login_api")
	private String loginApi;
	@Column(name = "login_api_call_type")
	private String loginApiCallType;
	@Column(name = "login_api_req_json")
	private String loginApiReqJson;
	
	public int getId() {
		return id;
	}
	public String getComponentName() {
		return componentName;
	}
	public String getLoginApi() {
		return loginApi;
	}
	public String getLoginApiCallType() {
		return loginApiCallType;
	}
	public String getLoginApiReqJson() {
		return loginApiReqJson;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
	public void setLoginApi(String loginApi) {
		this.loginApi = loginApi;
	}
	public void setLoginApiCallType(String loginApiCallType) {
		this.loginApiCallType = loginApiCallType;
	}
	public void setLoginApiReqJson(String loginApiReqJson) {
		this.loginApiReqJson = loginApiReqJson;
	}
	
	@Override
	public String toString() {
		return "TokenApiDetails [id=" + id + ", componentName=" + componentName + ", loginApi=" + loginApi
				+ ", loginApiCallType=" + loginApiCallType + ", loginApiReqJson=" + loginApiReqJson + "]";
	}
}
