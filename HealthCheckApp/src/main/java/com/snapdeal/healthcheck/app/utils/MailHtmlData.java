package com.snapdeal.healthcheck.app.utils;

import static com.snapdeal.healthcheck.app.constants.AppConstant.SNAPDEAL_ID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapdeal.healthcheck.app.model.TimelyCompData;

public class MailHtmlData {
	
	private static final Logger log = LoggerFactory.getLogger(MailHtmlData.class);
	
	public static String createHtmlTableForTimelyReportData(Map<String, TimelyCompData> data) {
		StringBuilder result = new StringBuilder("<table class=\"table\" style=\"width:100%; border: 2px solid black; border-collapse: collapse;\"><tr>"
				+ "<th style=\"border: 1px solid black; padding: 5px; background-color: #A8D5EC; text-align: center;\">Component Name</th>"
				+ "<th style=\"border: 1px solid black; padding: 5px; background-color: #A8D5EC; text-align: center;\">Number of down times</th>"
				+ "<th style=\"border: 1px solid black; padding: 5px; background-color: #A8D5EC; text-align: center;\">Total Downtime</th></tr>");
		for(Entry<String, TimelyCompData> entry : data.entrySet()) {
			String hrs = "";
			String mins = "";
			int totalTime = entry.getValue().gettotalTimeDownInMins();
			int hrsTime = totalTime/60;
			int minsTime = totalTime%60;
			if(hrsTime > 1) {
				if(hrsTime == 1)
					hrs = "1 hr";
				else
					hrs = hrsTime + " hrs";
			}
			if(minsTime > 0) {
				if(minsTime == 1)
					mins = "1 min";
				else
					mins = minsTime + " mins";
			}
			result.append("<tr style=\"text-align: center\">");
			result.append("<td style=\"border: 1px solid black; padding: 5px;\">"+entry.getKey()+"</td>");
			result.append("<td style=\"border: 1px solid black; padding: 5px;\">"+entry.getValue().getTotalDownTimes()+"</td>");
			result.append("<td style=\"border: 1px solid black; padding: 5px;\">"+hrs + " " +mins+"</td>");
			result.append("</tr>");
		}
		result.append("</table>");
		return result.toString();
	}
	
	public static void sendHtmlMail(String toAddress, String ccAddress, String mailSubject, String mailBody, boolean sendMail) {
		String[] toAdd = toAddress.split(",");
		List<String> emailAddressTo = new ArrayList<>();
		for(int i=0;i<toAdd.length;i++) {
			if(toAdd[i].contains(SNAPDEAL_ID))
				emailAddressTo.add(toAdd[i]);
		}
		
		String[] ccAdd = ccAddress.split(",");
		List<String> emailAddressCc = new ArrayList<>();
		for(int i=0;i<ccAdd.length;i++) {
			if(ccAdd[i].contains(SNAPDEAL_ID))
				emailAddressCc.add(ccAdd[i]);
		}
		
		if(emailAddressTo.isEmpty()) {
			log.warn("Mail with subject line: "+ mailSubject +" was not sent as the TO Address list was empty!!");
		} else {
			if(sendMail) {
				EmailUtil mail = new EmailUtil(emailAddressTo, emailAddressCc, null, mailSubject, mailBody);
				boolean mailSent = true;
				do {
					mailSent = mail.sendHTMLEmail();
				} while (!mailSent);
				log.debug("Mail with subject line: "+ mailSubject +" sent successfully to: " + emailAddressTo + ", cc: " + emailAddressCc);
			}
		}
	}

}
