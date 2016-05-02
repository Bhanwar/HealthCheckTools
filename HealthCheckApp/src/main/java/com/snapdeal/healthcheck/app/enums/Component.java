package com.snapdeal.healthcheck.app.enums;

public enum Component {

	CAMS("CAMS"),
	COCOFS("COCOFS"),
	IPMS("IPMS"),
	OMS("OMS"),
	OPMS("OPMS"),
	OPS("OPS"),
	PROMO("Promo"),
	SCORE("SCORE"),
	ERAS("ERAS"),
	MOBAPI("MobAPI"),
	RNR("RNR"),
	SEARCHEXCL("SEARCHEXCL"),
	SEARCH("SEARCH"),
	SHIPPINGSELLER("SHIPPINGSELLER"),
	RCMND("RECOMMENDATION"),
	UMS("UMS"),
	CART("CART"),
	SPMSPMNT("SPMSPMNT"),
	SCOREADMIN("SCOREADMIN"),
	FILMS("FILMS");
	
	private String code;
	
	private Component(String code) {
		this.code = code;
	}
	
	public String code() {
		return this.code;
	}
}
