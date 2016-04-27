package com.snapdeal.healthcheck.app.enums;

import com.snapdeal.healthcheck.app.constants.StringUtils;

public enum DownTimeReasonCode {
	
	DEPLOYMENT("DEP"),
	NOTSET("NA");
	
	private String code;
	
	private DownTimeReasonCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
	
	public static DownTimeReasonCode getValueOf(String downTimeReasonCode) {
		if(StringUtils.isEmpty(downTimeReasonCode)) {
            return null;
        }
		if(DownTimeReasonCode.DEPLOYMENT.getCode().equalsIgnoreCase(downTimeReasonCode)) {
			return DownTimeReasonCode.DEPLOYMENT;
		} else {
			return DownTimeReasonCode.NOTSET;
		}
	}
}
