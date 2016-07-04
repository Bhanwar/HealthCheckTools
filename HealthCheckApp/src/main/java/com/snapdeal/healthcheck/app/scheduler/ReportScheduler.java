package com.snapdeal.healthcheck.app.scheduler;

import static com.snapdeal.healthcheck.app.constants.AppConstant.currentExecDate;
import static com.snapdeal.healthcheck.app.constants.Formatter.dateFormatter;
import static com.snapdeal.healthcheck.app.constants.Formatter.timeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.snapdeal.healthcheck.app.constants.AppConstant;
import com.snapdeal.healthcheck.app.enums.DownTimeReasonCode;
import com.snapdeal.healthcheck.app.model.DownTimeData;
import com.snapdeal.healthcheck.app.mongo.repositories.MongoRepoService;
import com.snapdeal.healthcheck.app.utils.MailHtmlData;

public class ReportScheduler extends QuartzJobBean {

	private MongoRepoService repoService;
	private String toAddress;
	private String ccAddress;
	private String envName;
	private boolean sendMail;
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		Date currentDate = currentExecDate;
		String date = dateFormatter.format(currentDate);
		String time = timeFormatter.format(AppConstant.currentExecDate);
		String header = "<html><h2>Health Check Daily report for date: "+date+", as on: "+time+"</h2><h2>Environment: " + envName + "</h2>";
		StringBuilder html = new StringBuilder(header);
		List<DownTimeData> list = repoService.findAllCurrentExecDownTimeDataForDate(date);
		Map<String, List<DownTimeData>> dwnTymDataMap = new HashMap<String, List<DownTimeData>>();
		if (list != null && !list.isEmpty()) {
			List<DownTimeData> dwnTymList = null;
			for(DownTimeData dwnTymData: list) {
				String dwnTymCompName = dwnTymData.getComponentName();
				if(dwnTymDataMap.containsKey(dwnTymCompName))
					dwnTymList = dwnTymDataMap.get(dwnTymCompName);
				else
					dwnTymList = new ArrayList<DownTimeData>();
				
				dwnTymList.add(dwnTymData);
				dwnTymDataMap.put(dwnTymData.getComponentName(), dwnTymList);
			}
		}
		
		if (!dwnTymDataMap.isEmpty()) {
			log.debug("List is not empty! Size: " + dwnTymDataMap.size());
			
			String content = "<table style=\"width:100%; border: 2px solid black; border-collapse: collapse;\"><tr>"
					+ "<th style=\"border: 1px solid black; padding: 5px;\">Component</th>"
					+ "<th style=\"border: 1px solid black; padding: 5px;\">Down Time</th>"
					+ "<th style=\"border: 1px solid black; padding: 5px;\">Up Time</th>"
					+ "<th style=\"border: 1px solid black; padding: 5px;\">Total Server Down Time (mins)</th>"
					+ "<th style=\"border: 1px solid black; padding: 5px;\">Reason Code</th>"
					+ "<th style=\"border: 1px solid black; padding: 5px;\">Description</th></tr>";
			html.append(content);
			for (Entry<String, List<DownTimeData>> dwnTymEntrySet : dwnTymDataMap.entrySet()) {
				boolean addComp = true;
				int rowCount = dwnTymEntrySet.getValue().size();
				for(DownTimeData data : dwnTymEntrySet.getValue()) {
					html.append("<tr style=\"text-align: center\">");
					if(addComp) {
						html.append("<td rowspan=\""+rowCount+"\" style=\"border: 1px solid black; padding: 5px;\">"+getStringForHtml(dwnTymEntrySet.getKey())+"</td>");
						addComp = false;
					}
					html.append("<td style=\"border: 1px solid black; padding: 5px;\">"+getStringForHtml(data.getDownTime())+"</td>");
					html.append("<td style=\"border: 1px solid black; padding: 5px;\">"+getStringForHtml(data.getUpTime())+"</td>");
					html.append("<td style=\"border: 1px solid black; padding: 5px;\">"+getStringForHtml(data.getTotalDownTimeInMins())+"</td>");
					html.append("<td style=\"border: 1px solid black; padding: 5px;\">"+getStringForHtml(data.getReasonCode())+"</td>");
					html.append("<td style=\"border: 1px solid black; padding: 5px;\">"+getStringForHtml(data.getDescription())+"</td>");
					html.append("</tr>");
				}
			}
			html.append("</table>");
			html.append("</html>");
		} else {
			log.debug("List is empty! Looks like no server were down today!!");
			html.append("<h3>No servers were down today!</h3></html>");
		}
		
		
		MailHtmlData.sendHtmlMail(toAddress, ccAddress, envName + " health check daily report - " + date, html.toString(), sendMail);
//		String[] toAdd = toAddress.split(",");
//		List<String> emailAddressTo = new ArrayList<>();
//		for(int i=0;i<toAdd.length;i++) {
//			if(toAdd[i].contains(SNAPDEAL_ID))
//				emailAddressTo.add(toAdd[i]);
//		}
//		
//		String[] ccAdd = ccAddress.split(",");
//		List<String> emailAddressCc = new ArrayList<>();
//		for(int i=0;i<ccAdd.length;i++) {
//			if(ccAdd[i].contains(SNAPDEAL_ID))
//				emailAddressCc.add(ccAdd[i]);
//		}
//		
//		if(emailAddressTo.isEmpty()) {
//			log.warn("Daily Report was not sent as the TO Address list was empty!!");
//		} else {
//			if(sendMail) {
//				EmailUtil mail = new EmailUtil(emailAddressTo, emailAddressCc, null, envName + " health daily report - " + date, html.toString());
//				mail.sendHTMLEmail();
//			}
//		}
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

	public boolean isSendMail() {
		return sendMail;
	}

	public void setSendMail(boolean sendMail) {
		this.sendMail = sendMail;
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
