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
	MOBAPI("MOBAPI","Mobile API"),
	RNR("RNR","Review & Rating"),
	//SEARCHEXCL("SEARCHEXCL","Search Exclusive"),
	SEARCH("SEARCH","Search"),
	//SHIPPINGSELLER("SHIPPINGSELLER","Shipping Seller"),
	//RCMND("RECOMMENDATION","Recommendation"),
	UMS("UMS","UMS"),
	CART("CART","CART"),
	SPMSPMNT("SPMSPMNT","SPMS Payment"),
	SCOREADMIN("SCOREADMIN","Score Admin"),
	FILMS("FILMS","Films UI"),
	SELLERTOOLS("SELLERTOOLS", "MRG Seller Tools"),
	SNS("SNS", "Snap & Search"),
	UCMSTE("UCMSTE", "UCMS Template Engine"),
	UCMSP("UCMSP", "UCMS Processor"),
	SHIPFAR("SHIPFAR", "Ship Far"),
	OMSADMIN("OMSADMIN", "OMS Admin"),
	POMS("POMS", "POMS"),
	QNA("QNA", "QnA"),
	RMS("RMS","RMS"),
	SELLERST("SELLERST","SELLERST"),
	WEB("WEB","WEB");
	
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
