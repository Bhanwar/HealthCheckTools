package com.snapdeal.healthcheck.app.enums;

public enum ServerStatus {
	
	UP("SERVER_UP"),
	DOWN("SERVER_DOWN"),
	CONN_TIMED_OUT("CONN_TIMED_OUT"),
	NTWRK_ISSUE("NTWRK_ISSUE");
	
	private String code;
	
	private ServerStatus(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}
