package com.snapdeal.healthcheck.app.constants;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.snapdeal.healthcheck.app.model.DownTimeUIData;

public class AppConstant {
	
	public static Date currentExecDate;
	public static final String SNAPDEAL_ID = "@snapdeal.com";
	public static Map<String, Boolean> healthResult;
	public static Set<String> componentNames;
	public static Map<String, List<DownTimeUIData>> uiData = null;
	public static final String MAIL_SIGN = "Thanks & Regards<br>Health Check App<br><br>NOTE: This is a system generated mail. Please DO NOT reply back to this mail."; 
}
