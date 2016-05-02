package com.snapdeal.healthcheck.app.constants;

import java.util.Date;
import java.util.Map;

public class AppConstant {
	
	public static Date currentExecDate = new Date();
	
	public static Map<String, Boolean> healthResult;

	public static final String TOMCAT_ENDPOINT = "_Tomcat_Endpoint";
	
}
