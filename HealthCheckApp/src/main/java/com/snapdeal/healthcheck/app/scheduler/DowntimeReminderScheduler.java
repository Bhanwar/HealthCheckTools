package com.snapdeal.healthcheck.app.scheduler;

import static com.snapdeal.healthcheck.app.constants.AppConstant.MAIL_SIGN;
import static com.snapdeal.healthcheck.app.constants.AppConstant.SNAPDEAL_ID;
import static com.snapdeal.healthcheck.app.constants.AppConstant.componentNames;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.snapdeal.healthcheck.app.bo.ComponentDetailsBO;
import com.snapdeal.healthcheck.app.model.ComponentDetails;
import com.snapdeal.healthcheck.app.model.DownTimeData;
import com.snapdeal.healthcheck.app.model.QuartzJobDataHolder;
import com.snapdeal.healthcheck.app.mongo.repositories.MongoRepoService;
import com.snapdeal.healthcheck.app.utils.MailHtmlData;

public class DowntimeReminderScheduler extends QuartzJobBean {

	private QuartzJobDataHolder dataObjects;
	private ComponentDetailsBO compDetails;
	private final Logger log = LoggerFactory.getLogger(getClass());
	private String ccAddress;
	private String envName;
	private boolean sendMail;
	private MongoRepoService repoService;
	Date currentDate;

	/**
	 * This job will run every 30 minutes and send reminder Email for components down for more than 30 minutes 
	 */
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			currentDate = new Date();
			compDetails = dataObjects.getCompDetails();
			List<ComponentDetails> components = compDetails.getAllComponentDetails();
			log.debug("Running scheduled task: " + currentDate);
						
			Set<String> newComponentNames = new TreeSet<>();			
			for (ComponentDetails comp : components) {
				String compName = comp.getComponentName();
				if (comp.isEnabled()) {
					newComponentNames.add(compName);										
				} 
			}

			componentNames = newComponentNames;			
			// My code starts here
			if (!componentNames.isEmpty()) {
				for (String compName : componentNames) {
					sendDownTimeReminderMail(compName, currentDate);
				}
			}
			// My code ends here
		} catch (Exception e) {
			log.error("Exception occured while running scheduler!! ", e);
		}
	}
	
	private void sendDownTimeReminderMail(String compName, Date execDate) {
		DownTimeData data = null;
		log.debug(compName + " Server is DOWN!!");
		data = repoService.findUpTimeUpdate(compName);
		if (data != null) {

			long totalTimeMins = (execDate.getTime() - data.getDownTime().getTime()) / 60000;
			log.debug("Total down time: " + totalTimeMins);
			if (sendMail) {
				sendServerDownMail(compName, data, execDate, totalTimeMins);
			} else {
				log.warn(compName + ": Down time data already exist in Mongo!! This should not happen, please check!");
			}
		}
	}


	private void sendServerDownMail(String compName, DownTimeData result, Date execDate, long downTime) {
		long timeDividedBy60 = downTime/60;
		//Send Mail only if component is down for whole number of hours , 1, 2 ,3 etc hours		
		if (timeDividedBy60 % 1==0 && timeDividedBy60!=0) {
			String msgSubject = compName + " server is down on " + envName + " - " + execDate;
			String msgBody = "<html><h3>Your component: <i>" + compName + "</i> has been down for more than "
					+ timeDividedBy60  + " Hour(s) .Please have a look at it.</h3>";
			StringBuilder msg = new StringBuilder(msgBody);
			msg.append("<br><b>URL: </b>" + getStringForHtml(result.getFailedUrl()));
			if (result.getFailedReqJson() != null)
				msg.append("<br><b>Request JSON: </b>" + getStringForHtml(result.getFailedReqJson()));
			msg.append("<br><br><b>Expected token in response: </b>" + getStringForHtml(result.getFailedExpResp()));
			msg.append("<br>");
			if (result.getFailedStatusCode() != null)
				msg.append("<br><b>Status code: </b>" + getStringForHtml(result.getFailedStatusCode()));
			if (result.getFailedResp() != null)
				msg.append("<br><b>Response: </b>" + getStringForHtml(result.getFailedResp()));
			if (result.getFailedHttpException() != null)
				msg.append("<br><b>Http Call Exception: </b>" + getStringForHtml(result.getFailedHttpException()));
			msg.append("<br><br><br>" + MAIL_SIGN + "</html>");
			sendMail(compName, msgSubject, msg.toString());
		}
	}

	private void sendMail(String compName, String msgSubject, String msgBody) {
		ComponentDetails comp = compDetails.getComponentDetails(compName);
		if (comp == null) {
			log.error("No component found with the name: " + compName);
		} else {
			String toAddress = comp.getQaSpoc();
			if (comp.getQmSpoc() != null && comp.getQmSpoc().contains(SNAPDEAL_ID)) {
				toAddress = toAddress + "," + comp.getQmSpoc();
				msgBody = msgBody.replace("${QMSPOC}", comp.getQmSpoc());
			}
			MailHtmlData.sendHtmlMail(toAddress, ccAddress, msgSubject, msgBody, sendMail);
		}
	}

	private String getStringForHtml(String data) {
		if (data == null)
			return "";
		else
			return data;
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

	public boolean isSendMail() {
		return sendMail;
	}

	public void setSendMail(boolean sendMail) {
		this.sendMail = sendMail;
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

	public QuartzJobDataHolder getDataObjects() {
		return dataObjects;
	}

	public void setDataObjects(QuartzJobDataHolder dataObjects) {
		this.dataObjects = dataObjects;
	}
}