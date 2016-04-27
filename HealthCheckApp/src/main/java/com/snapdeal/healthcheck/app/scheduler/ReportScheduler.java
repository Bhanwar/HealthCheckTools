package com.snapdeal.healthcheck.app.scheduler;

import static com.snapdeal.healthcheck.app.constants.Formatter.dateFormatter;
import static com.snapdeal.healthcheck.app.constants.Formatter.timeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.snapdeal.healthcheck.app.enums.DownTimeReasonCode;
import com.snapdeal.healthcheck.app.model.DownTimeData;
import com.snapdeal.healthcheck.app.mongo.repositories.MongoRepoService;
import com.snapdeal.healthcheck.app.utils.EmailUtil;

public class ReportScheduler extends QuartzJobBean {

	private MongoRepoService repoService;
	private String toAddress;
	private String ccAddress;
	private String envName;
	private final Logger log = LoggerFactory.getLogger(getClass());
	private static final String SNAPDEAL_ID = "@snapdeal.com";

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		Date currentDate = new Date();
		String date = dateFormatter.format(currentDate);
		String time = timeFormatter.format(currentDate);
		List<DownTimeData> list = repoService.downTimeFindAllForDate(date);
		if (list != null && !list.isEmpty()) {
			log.debug("List is not empty! Size: " + list.size());
			String header = "<html><h2>Daily report for date: "+date+", as on: "+time+"</h2>";
			StringBuilder html = new StringBuilder(header);
			String content = "<table style=\"width:100%; border: 2px solid black; border-collapse: collapse;\"><tr>"
					+ "<th style=\"border: 1px solid black; padding: 5px;\">Component</th>"
					+ "<th style=\"border: 1px solid black; padding: 5px;\">Down Time</th>"
					+ "<th style=\"border: 1px solid black; padding: 5px;\">Up Time</th>"
					+ "<th style=\"border: 1px solid black; padding: 5px;\">Total Server Down Time (mins)</th>"
					+ "<th style=\"border: 1px solid black; padding: 5px;\">Reason Code</th>"
					+ "<th style=\"border: 1px solid black; padding: 5px;\">Description</th></tr>";
			html.append(content);
			for (DownTimeData data : list) {
				html.append("<tr style=\"text-align: center\">");
				html.append("<td style=\"border: 1px solid black; padding: 5px;\">"+getStringForHtml(data.getComponentName())+"</td>");
				html.append("<td style=\"border: 1px solid black; padding: 5px;\">"+getStringForHtml(data.getDownTime())+"</td>");
				html.append("<td style=\"border: 1px solid black; padding: 5px;\">"+getStringForHtml(data.getUpTime())+"</td>");
				html.append("<td style=\"border: 1px solid black; padding: 5px;\">"+getStringForHtml(data.getTotalDownTimeInMins())+"</td>");
				html.append("<td style=\"border: 1px solid black; padding: 5px;\">"+getStringForHtml(data.getReasonCode())+"</td>");
				html.append("<td style=\"border: 1px solid black; padding: 5px;\">"+getStringForHtml(data.getDescription())+"</td>");
				html.append("</tr>");
			}
			html.append("</table></html>");
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
			EmailUtil mail = new EmailUtil(emailAddressTo, emailAddressCc, null, envName + " health daily report", html.toString());
			mail.sendHTMLEmail();
		} else {
			log.debug("List is empty! Looks like no server were down today!!");
		}
	}

	
	private String getStringForHtml(String data) {
		if(data == null)
			return "";
		else
			return data;
	}
	private String getStringForHtml(Date data) {
		if(data == null)
			return "";
		else
			return data.toString();
	}
	private String getStringForHtml(DownTimeReasonCode data) {
		if(data == null)
			return "";
		else if(data.equals(DownTimeReasonCode.DEPLOYMENT))
			return "Deployment";
		else if(data.equals(DownTimeReasonCode.NOTSET))
			return "Not Set";
		else
			return "";
	}
	
	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getCcAddress() {
		return ccAddress;
	}

	public void setCcAddress(String ccAddress) {
		this.ccAddress = ccAddress;
	}

	public String getEnvName() {
		return envName;
	}

	public void setEnvName(String envName) {
		this.envName = envName;
	}

	public MongoRepoService getRepoService() {
		return repoService;
	}

	public void setRepoService(MongoRepoService repoService) {
		this.repoService = repoService;
	}
}
