package com.snapdeal.healthcheck.app.model;

public class UIComponent {
	
	private String compName;
	private ComponentDetails compDetails;
	private TokenApiDetails tokenDetails;
	
	public String getCompName() {
		return compName;
	}
	public void setCompName(String compName) {
		this.compName = compName;
	}
	public ComponentDetails getCompDetails() {
		return compDetails;
	}
	public void setCompDetails(ComponentDetails compDetails) {
		this.compDetails = compDetails;
	}
	public TokenApiDetails getTokenDetails() {
		return tokenDetails;
	}
	public void setTokenDetails(TokenApiDetails tokenDetails) {
		this.tokenDetails = tokenDetails;
	}

}
