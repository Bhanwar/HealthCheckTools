package com.snapdeal.healthcheck.app.enums;

public enum Component {

	CAMS("CAMS","CAMS"),
	COCOFS("COCOFS","COCOFS"),
	IPMS("IPMS","IPMS"),
	OMS("OMS","OMS"),
	OPMS("OPMS","OPMS"),
	OPS("OPS","OPS"),
	PROMO("PROMO","Promo"),
	SCORE("SCORE","SCORE"),
	ERAS("ERAS","ERAS"),
	MOBAPI("MobAPI","Mobile API"),
	RNR("RNR","Review & Rating"),
	SEARCHEXCL("SEARCHEXCL",""),
	SEARCH("SEARCH",""),
	SHIPPINGSELLER("SHIPPINGSELLER",""),
	RCMND("RECOMMENDATION",""),
	UMS("UMS",""),
	CART("CART",""),
	SPMSPMNT("SPMSPMNT",""),
	SCOREADMIN("SCOREADMIN",""),
	FILMS("FILMS","");
	
	private String code;
	private String name;
	
	private Component(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public String code() {
		return this.code;
	}
	
	public String getName() {
		return this.name;
	}
}
