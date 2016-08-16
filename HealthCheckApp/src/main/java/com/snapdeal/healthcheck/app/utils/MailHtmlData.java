package com.snapdeal.healthcheck.app.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snapdeal.healthcheck.app.model.TimelyCompData;

public class MailHtmlData {
	
	private static final Logger log = LoggerFactory.getLogger(MailHtmlData.class);
	
	public static String createHtmlTableForTimelyReportData(Set<TimelyCompData> data) {
		StringBuilder result = new StringBuilder("<table class=\"table\" style=\"width:100%; border: 2px solid black; border-collapse: collapse;\"><tr>"
				+ "<th style=\"border: 1px solid black; padding: 5px; background-color: #A8D5EC; text-align: center;\">Component Name</th>"
				+ "<th style=\"border: 1px solid black; padding: 5px; background-color: #A8D5EC; text-align: center;\">Number of down times</th>"
				+ "<th style=\"border: 1px solid black; padding: 5px; background-color: #A8D5EC; text-align: center;\">Total Downtime</th></tr>");
		for(TimelyCompData dataEntry : data) {
			updateTotalTimeDownStrForMail(dataEntry);
			result.append("<tr style=\"text-align: center\">");
			result.append("<td style=\"border: 1px solid black; padding: 5px;\">"+dataEntry.getComponentName()+"</td>");
			result.append("<td style=\"border: 1px solid black; padding: 5px;\">"+dataEntry.getTotalDownTimes()+"</td>");
			result.append("<td style=\"border: 1px solid black; padding: 5px;\">"+dataEntry.getTotalTimeDownStr()+"</td>");
			result.append("</tr>");
		}
		result.append("</table>");
		return result.toString();
	}
	
	public static void sendHtmlMail(String toAddress, String ccAddress, String mailSubject, String mailBody, boolean sendMail) {
		log.debug("Sending HTML Mail to : " + toAddress);
		log.debug("Sending HTML Mail to cc : " + ccAddress);
		log.debug("Sending HTML Mail with mailsubject : " + mailSubject);
		String[] toAdd = toAddress.split(",");
		List<String> emailAddressTo = new ArrayList<>();
		for(int i=0;i<toAdd.length;i++) {
//			if(toAdd[i].contains(SNAPDEAL_ID))
				emailAddressTo.add(toAdd[i]);
		}
		
		String[] ccAdd = ccAddress.split(",");
		List<String> emailAddressCc = new ArrayList<>();
		for(int i=0;i<ccAdd.length;i++) {
//			if(ccAdd[i].contains(SNAPDEAL_ID))
				emailAddressCc.add(ccAdd[i]);
		}
		
		if(emailAddressTo.isEmpty()) {
			log.warn("Mail with subject line: "+ mailSubject +" was not sent as the TO Address list was empty!!");
		} else {
			if(sendMail) {
				try {
					EmailUtil mail = new EmailUtil(emailAddressTo, emailAddressCc, null, mailSubject, mailBody);
					boolean mailSent = true;
					do {
						mailSent = mail.sendHTMLEmail();
					} while (!mailSent);
					log.info("Mail with subject line: "+ mailSubject +" sent successfully to: " + emailAddressTo + ", cc: " + emailAddressCc);
				} catch (Exception e) {
					log.error("Mail sending failed with details: subject line: "+ mailSubject +" sent successfully to: " + emailAddressTo + ", cc: " + emailAddressCc + "\n Exception: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

	private static void updateTotalTimeDownStrForMail(TimelyCompData dataObj) {
		int totatDownTime = dataObj.getTotalTimeDownInMins();
		int hrs = totatDownTime/60;
		int mins = totatDownTime%60;
		if(mins == 0 && hrs == 0) {
			dataObj.setTotalTimeDownStr("0 min");
			return;
		}
		String hrsStr = "";
		String minsStr = "";
		if(hrs > 0) {
			if(hrs == 1)
				hrsStr = "1 hr ";
			else
				hrsStr = hrs + " hr ";
		}
		if(mins > 0) {
			if(mins == 1)
				minsStr = "1 min";
			else
				minsStr = mins + " mins";
		}
		dataObj.setTotalTimeDownStr(hrsStr+minsStr);
	}
}
