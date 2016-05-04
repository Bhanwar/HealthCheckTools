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
	SEARCHEXCL("SEARCHEXCL","Search Exclusive"),
	SEARCH("SEARCH","Search"),
	SHIPPINGSELLER("SHIPPINGSELLER","Shipping Seller"),
	RCMND("RECOMMENDATION","Recommendation"),
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
	
	public static Component getValueOf(String code) {
		if(Component.CAMS.code().equalsIgnoreCase(code))
			return Component.CAMS;
		else if(Component.CART.code().equalsIgnoreCase(code))
			return Component.CART;
		else if(Component.COCOFS.code().equalsIgnoreCase(code))
			return Component.COCOFS;
		else if(Component.ERAS.code().equalsIgnoreCase(code))
			return Component.ERAS;
		else if(Component.FILMS.code().equalsIgnoreCase(code))
			return Component.FILMS;
		else if(Component.IPMS.code().equalsIgnoreCase(code))
			return Component.IPMS;
		else if(Component.MOBAPI.code().equalsIgnoreCase(code))
			return Component.MOBAPI;
		else if(Component.OMS.code().equalsIgnoreCase(code))
			return Component.OMS;
		else if(Component.OMSADMIN.code().equalsIgnoreCase(code))
			return Component.OMSADMIN;
		else if(Component.OPMS.code().equalsIgnoreCase(code))
			return Component.OPMS;
		else if(Component.OPS.code().equalsIgnoreCase(code))
			return Component.OPS;
		else if(Component.POMS.code().equalsIgnoreCase(code))
			return Component.POMS;
		else if(Component.PROMO.code().equalsIgnoreCase(code))
			return Component.PROMO;
		else if(Component.QNA.code().equalsIgnoreCase(code))
			return Component.QNA;
		else if(Component.RNR.code().equalsIgnoreCase(code))
			return Component.RNR;
		else if(Component.SCORE.code().equalsIgnoreCase(code))
			return Component.SCORE;
		else if(Component.SCOREADMIN.code().equalsIgnoreCase(code))
			return Component.SCOREADMIN;
		else if(Component.SEARCH.code().equalsIgnoreCase(code))
			return Component.SEARCH;
		else if(Component.SELLERTOOLS.code().equalsIgnoreCase(code))
			return Component.SELLERTOOLS;
		else if(Component.SHIPFAR.code().equalsIgnoreCase(code))
			return Component.SHIPFAR;
		else if(Component.SNS.code().equalsIgnoreCase(code))
			return Component.SNS;
		else if(Component.SPMSPMNT.code().equalsIgnoreCase(code))
			return Component.SPMSPMNT;
		else if(Component.UCMSP.code().equalsIgnoreCase(code))
			return Component.UCMSP;
		else if(Component.UCMSTE.code().equalsIgnoreCase(code))
			return Component.UCMSTE;
		else if(Component.UMS.code().equalsIgnoreCase(code))
			return Component.UMS;
		else
			return null;
	}
}
