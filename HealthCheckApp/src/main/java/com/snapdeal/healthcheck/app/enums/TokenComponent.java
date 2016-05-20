package com.snapdeal.healthcheck.app.enums;

import com.snapdeal.healthcheck.app.utils.StringUtils;

public enum TokenComponent {

	SF_MOBILE("SF Mobile"),
	SELLER_SERVICES("Seller Services"),
	INV("Invalid");
	
	private String code;
	
	private TokenComponent(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	public static TokenComponent getValueOf(String component) {
		if(StringUtils.isEmpty(component)) {
			return null;
		}
		if(TokenComponent.SF_MOBILE.getCode().equalsIgnoreCase(component)) {
			return TokenComponent.SF_MOBILE;
		} else if(TokenComponent.SELLER_SERVICES.getCode().equalsIgnoreCase(component)) {
			return TokenComponent.SELLER_SERVICES;
		} else {
			return TokenComponent.INV;
		}
	}
}
