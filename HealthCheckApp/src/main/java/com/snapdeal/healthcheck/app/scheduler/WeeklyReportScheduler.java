package com.snapdeal.healthcheck.app.scheduler;

import static com.snapdeal.healthcheck.app.constants.AppConstant.currentExecDate;
import static com.snapdeal.healthcheck.app.constants.Formatter.dateFormatter;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.snapdeal.healthcheck.app.model.TimelyCompData;
import com.snapdeal.healthcheck.app.mongo.repositories.MongoRepoService;
import com.snapdeal.healthcheck.app.utils.MailHtmlData;

public class WeeklyReportScheduler extends QuartzJobBean {

	private MongoRepoService repoService;
	private String toAddress;
	private String ccAddress;
	private String envName;
	private boolean sendMail;
	private final Logger log = LoggerFactory.getLogger(getClass());
	Calendar calender = Calendar.getInstance();

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("Generating weekly report!");
		Date currentDate = currentExecDate;
		calender.setTime(currentDate);
		calender.add(Calendar.DATE, -6);
		Date startDate = calender.getTime();
		Map<String, TimelyCompData> result = repoService.getTimelyData(startDate, currentDate);
		String startDateStr = dateFormatter.format(startDate);
		String endDateStr = dateFormatter.format(currentDate);
		StringBuilder htmlData = new StringBuilder(
				"<html><h2>Health Check Weekly report from: " + startDateStr + ", to: "
						+ endDateStr + "</h2><h2>Environment: " + envName + "</h2>");
		if (!result.isEmpty()) {
			htmlData.append(MailHtmlData.createHtmlTableForTimelyReportData(result));
		}
		htmlData.append("</html>");
		log.info("Sending weekly report..");
		MailHtmlData.sendHtmlMail(toAddress, ccAddress,
				envName + " health check weekly report " + startDateStr + " - " + endDateStr, htmlData.toString(),
				sendMail);
	}

	public MongoRepoService getRepoService() {
		return repoService;
	}

	public void setRepoService(MongoRepoService repoService) {
		this.repoService = repoService;
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

	public boolean isSendMail() {
		return sendMail;
	}

	public void setSendMail(boolean sendMail) {
		this.sendMail = sendMail;
	}
}
