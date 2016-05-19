package com.snapdeal.healthcheck.app.enums;

import com.snapdeal.healthcheck.app.utils.StringUtils;

public enum DownTimeReasonCode {

	DEPLOYMENT("DEPLOYMENT"),
	LOW_DISK_SPACE("LOW_DISK_SPACE"),
	DB_ISSUE("DB_ISSUE"),
	OTHER("OTHER"),
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
		} else if(DownTimeReasonCode.LOW_DISK_SPACE.getCode().equalsIgnoreCase(downTimeReasonCode)) {
			return DownTimeReasonCode.LOW_DISK_SPACE;
		} else if(DownTimeReasonCode.DB_ISSUE.getCode().equalsIgnoreCase(downTimeReasonCode)) {
			return DownTimeReasonCode.DB_ISSUE;
		} else if(DownTimeReasonCode.OTHER.getCode().equalsIgnoreCase(downTimeReasonCode)) {
			return DownTimeReasonCode.OTHER;
		} else {
			return DownTimeReasonCode.NOTSET;
		}
	}
}
