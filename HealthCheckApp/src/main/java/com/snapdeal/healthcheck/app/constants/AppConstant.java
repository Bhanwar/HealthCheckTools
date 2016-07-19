package com.snapdeal.healthcheck.app.constants;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.snapdeal.healthcheck.app.model.DownTimeUIData;

public class AppConstant {
	
	public static Date currentExecDate;
	public static final String SNAPDEAL_ID = "@snapdeal.com";
	public static final String CONNECTION_TIMED_OUT = "Connection timed out";
	public static final String NO_ROUTE_TO_HOST = "No route to host";
	public static final String NETWORK_UNREACHABLE = "Network is unreachable";
	public static final String ADMIN_UI_EMPTY_STRING = "$#EMPTY_STRING#$";
	public static final String ADMIN_UI_REQ_TOKEN = "$#REQ_TOKEN#$";
	public static final String ADMIN_UI_HEADER_TOKEN = "$#HDR_TOKEN#$";
	public static String sshUser;
	public static String sshKeyLocation;
	public static String currentExecDateString;
	public static Map<String, Boolean> healthResult;
	public static Set<String> componentNames;
	public static Set<String> disabledComponentNames;
	public static Map<String, List<DownTimeUIData>> uiData = null;
	public static final String MAIL_SIGN = "<br>Thanks & Regards<br>Health Check App<br><br>NOTE: This is a system generated mail. Please DO NOT reply back to this mail."; 
}
