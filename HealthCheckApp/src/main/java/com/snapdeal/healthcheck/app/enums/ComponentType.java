package com.snapdeal.healthcheck.app.enums;

import com.snapdeal.healthcheck.app.utils.StringUtils;

public enum ComponentType {
	
	TOMCAT("TOMCAT"),
	JAR("JAR");
	
	private String code;
	
	private ComponentType(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return this.code;
	}

	public static ComponentType getValueOf(String compType) {
		if(StringUtils.isEmpty(compType)) {
			return null;
		}
		if(ComponentType.TOMCAT.getCode().equalsIgnoreCase(compType)) {
			return ComponentType.TOMCAT;
		} else if(ComponentType.JAR.getCode().equalsIgnoreCase(compType)) {
			return ComponentType.JAR;
		} else {
			return null;
		}
	}
}
