package com.snapdeal.healthcheck.app.enums;

public enum Component {

	CAMS("CAMS"),
	COCOFS("COCOFS"),
	IPMS("IPMS"),
	OMS("OMS"),
	OPMS("OPMS"),
	OPS("OPS"),
	PROMO("Promo"),
	SCORE("SCORE");
	
	private String code;
	
	private Component(String code) {
		this.code = code;
	}
	
	public String code() {
		return this.code;
	}
}
