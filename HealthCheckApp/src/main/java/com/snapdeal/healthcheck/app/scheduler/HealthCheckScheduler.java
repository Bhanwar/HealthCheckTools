package com.snapdeal.healthcheck.app.scheduler;

import static com.snapdeal.healthcheck.app.constants.AppConstant.MAIL_SIGN;
import static com.snapdeal.healthcheck.app.constants.AppConstant.SNAPDEAL_ID;
import static com.snapdeal.healthcheck.app.constants.AppConstant.componentNames;
import static com.snapdeal.healthcheck.app.constants.AppConstant.currentExecDate;
import static com.snapdeal.healthcheck.app.constants.AppConstant.disabledComponentNames;
import static com.snapdeal.healthcheck.app.constants.AppConstant.healthResult;
import static com.snapdeal.healthcheck.app.constants.AppConstant.currentExecDateString;
import static com.snapdeal.healthcheck.app.constants.Formatter.dateFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.snapdeal.healthcheck.app.bo.ComponentDetailsBO;
import com.snapdeal.healthcheck.app.enums.DownTimeReasonCode;
import com.snapdeal.healthcheck.app.enums.ServerStatus;
import com.snapdeal.healthcheck.app.model.ComponentDetails;
import com.snapdeal.healthcheck.app.model.ConnIssueComp;
import com.snapdeal.healthcheck.app.model.DownTimeData;
import com.snapdeal.healthcheck.app.model.HealthCheckResult;
import com.snapdeal.healthcheck.app.model.QuartzJobDataHolder;
import com.snapdeal.healthcheck.app.mongo.repositories.MongoRepoService;
import com.snapdeal.healthcheck.app.services.impl.EnvHealthCheckImpl;
import com.snapdeal.healthcheck.app.utils.EmailUtil;
import com.snapdeal.healthcheck.app.utils.MailHtmlData;

public class HealthCheckScheduler extends QuartzJobBean {

	private QuartzJobDataHolder dataObjects;
	private ComponentDetailsBO compDetails;
	private final Logger log = LoggerFactory.getLogger(getClass());
	private String ccAddress;
	private String ntwrkAddress;
	private String envName;
	private boolean sendMail;
	private MongoRepoService repoService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		
		try {
			currentExecDate = new Date();
			compDetails = dataObjects.getCompDetails();
			List<ComponentDetails> components = compDetails.getAllComponentDetails();
			log.debug("Running scheduled task: " + currentExecDate);
			String execDateString = dateFormatter.format(currentExecDate);
			List<Callable<HealthCheckResult>> compCallList = new ArrayList<>();
			int ntwrkIssueCount = 0;
			Set<String> newComponentNames = new TreeSet<>();
			Set<String> newDisabledComponentNames = new TreeSet<>();
			for (ComponentDetails comp : components) {
				String compName = comp.getComponentName();
				if (comp.isEnabled()) {
					newComponentNames.add(compName);
					compCallList.add(new EnvHealthCheckImpl(comp, repoService));
					// Required for newly added components on the run
					if (healthResult.get(compName) == null)
						healthResult.put(compName, true);
				} else {
					newDisabledComponentNames.add(compName);
				}
			}

			componentNames = newComponentNames;
			disabledComponentNames = newDisabledComponentNames;

			ExecutorService exec = null;
			if (!compCallList.isEmpty()) {
				try {
					int compCount = compCallList.size();
					exec = Executors.newFixedThreadPool(compCount);
					CompletionService<HealthCheckResult> compSer = new ExecutorCompletionService<HealthCheckResult>(
							exec);
					for (Callable<HealthCheckResult> compToCall : compCallList) {
						compSer.submit(compToCall);
					}

					for (int i = 0; i < compCount; i++) {
						HealthCheckResult result = null;
						try {
							result = compSer.take().get();
							log.debug(result.toString());
							ExecutorService execMail = Executors.newFixedThreadPool(2);
							final HealthCheckResult updateRes = result;
							final Date updateDate = currentExecDate;
							if (healthResult.get(result.getComponentName()) != result.isServerUp()) {
								log.debug("Status has changed for comp: " + result.getComponentName());
								execMail.submit(new Runnable() {
									@Override
									public void run() {
										updateAndSendMail(updateRes, updateDate);
									}
								});
							}
							healthResult.put(result.getComponentName(), result.isServerUp());
							if (result.getServerStatus().equals(ServerStatus.NTWRK_ISSUE)
									|| result.getServerStatus().equals(ServerStatus.CONN_TIMED_OUT)) {
								execMail.submit(new Runnable() {
									@Override
									public void run() {
										createCompIssueEntryMongo(updateRes, updateDate);
										if (updateRes.getServerStatus().equals(ServerStatus.NTWRK_ISSUE))
											closeConnTimedOutEntryMongo(updateRes, updateDate,
													ServerStatus.CONN_TIMED_OUT);
										else if (updateRes.getServerStatus().equals(ServerStatus.CONN_TIMED_OUT))
											closeConnTimedOutEntryMongo(updateRes, updateDate,
													ServerStatus.NTWRK_ISSUE);
									}
								});
							} else {
								execMail.submit(new Runnable() {
									@Override
									public void run() {
										closeConnTimedOutEntryMongo(updateRes, updateDate, ServerStatus.NTWRK_ISSUE);
										closeConnTimedOutEntryMongo(updateRes, updateDate, ServerStatus.CONN_TIMED_OUT);
									}
								});
							}
							execMail.shutdown();
						} catch (InterruptedException | ExecutionException e) {
							log.error("Exception occured while getting results: " + e.getMessage(), e);
						}
					}
					if (currentExecDateString == null || !currentExecDateString.equals(execDateString)) {
						currentExecDateString = execDateString;
						updateMongoDataWithCurrentExecDate(currentExecDateString);
					}
					log.info("Health check result: " + healthResult);
				} catch (Exception ee) {
					log.error("Exception occured while executing scheduled task: " + ee.getMessage(), ee);
				} finally {
					if (exec != null)
						exec.shutdown();
				}
			}
			dataObjects.shareAuthKeysToQMs();
			if (ntwrkIssueCount > 0) {
				sendNetworkIssueMail(ntwrkIssueCount, currentExecDate);
			}
		} catch (Exception e) {
			log.error("Exception occured while running scheduler!! ", e);
		}
	}

	private void createCompIssueEntryMongo(HealthCheckResult result, Date execDate) {
		String compName = result.getComponentName();
		ConnIssueComp compIssueData = null;
		boolean ntwrkIssue = false;
		if (result.getServerStatus().equals(ServerStatus.CONN_TIMED_OUT))
			compIssueData = repoService.getConnTimedOutEntry(compName);
		else if (result.getServerStatus().equals(ServerStatus.NTWRK_ISSUE)) {
			compIssueData = repoService.getNetworkIssueEntry(compName);
			ntwrkIssue = true;
		} else
			return;

		if (compIssueData == null) {
			log.debug("Creating component issue entry for comp: " + compName + ", Type: "
					+ result.getServerStatus().getCode());
			String execDateStr = dateFormatter.format(execDate);
			compIssueData = new ConnIssueComp(compName);
			compIssueData.setStatus("OPEN");
			if (ntwrkIssue)
				compIssueData.setIssueType("NWI");
			else
				compIssueData.setIssueType("CTO");
			compIssueData.setDownTime(execDate);
			compIssueData.setStartDate(execDateStr);
			compIssueData.setHttpCallException(result.getFailedHttpCallException());
			Set<String> execDates = new HashSet<>();
			execDates.add(execDateStr);
			compIssueData.setExecDate(execDates);
			repoService.save(compIssueData);
			if (!ntwrkIssue)
				sendConnTimedOutMail(compName, result, execDate);
		} else
			log.debug("Component issue entry already exist for comp: " + compName + ", Type: "
					+ result.getServerStatus().getCode());
	}

	private void closeConnTimedOutEntryMongo(HealthCheckResult result, Date execDate, ServerStatus serverStatus) {
		String compName = result.getComponentName();
		ConnIssueComp compIssueData = null;
		if (serverStatus.equals(ServerStatus.CONN_TIMED_OUT))
			compIssueData = repoService.getConnTimedOutEntry(compName);
		else if (serverStatus.equals(ServerStatus.NTWRK_ISSUE))
			compIssueData = repoService.getNetworkIssueEntry(compName);
		else
			return;
		if (compIssueData != null) {
			log.debug("Closing comp entry for comp: " + compName + ", Type: " + serverStatus.getCode());
			String execDateStr = dateFormatter.format(execDate);
			compIssueData.setUpTime(execDate);
			compIssueData.setEndDate(execDateStr);
			compIssueData.setTotalDownTimeInMins(
					Long.toString((execDate.getTime() - compIssueData.getDownTime().getTime()) / 60000));
			compIssueData.setStatus("CLOSED");
			repoService.save(compIssueData);
		}
	}

	private void sendNetworkIssueMail(int count, Date execDate) {
		if (sendMail) {
			List<String> emailAddressTo = new ArrayList<>();
			List<String> emailAddressCc = new ArrayList<>();
			String msgSubject = "Network issue from Health Check App to " + count + " components! Please check<eom>";
			String msgBody = "<html>" + MAIL_SIGN + "</html>";
			String[] toAdd = ntwrkAddress.split(",");
			for (int i = 0; i < toAdd.length; i++) {
				if (toAdd[i].contains(SNAPDEAL_ID))
					emailAddressTo.add(toAdd[i]);
			}
			log.debug("Sending network issue mail to Admins! To: " + emailAddressTo + ", Cc: " + emailAddressCc);
			EmailUtil mail = new EmailUtil(emailAddressTo, emailAddressCc, null, msgSubject, msgBody);
			boolean mailSent = true;
			do {
				mailSent = mail.sendHTMLEmail();
			} while (!mailSent);
		}
	}

	private void updateAndSendMail(HealthCheckResult result, Date execDate) {
		DownTimeData data = null;
		boolean isServerUp = result.isServerUp();
		String compName = result.getComponentName();
		String execDateStr = dateFormatter.format(execDate);
		if (isServerUp) {
			log.debug(compName + " Server is UP!!");
			data = repoService.findUpTimeUpdate(compName);
			if (data != null) {
				data.setUpTime(execDate);
				long totalTimeMins = (execDate.getTime() - data.getDownTime().getTime()) / 60000;
				log.debug("Total down time: " + totalTimeMins);
				data.setTotalDownTimeInMins(Long.toString(totalTimeMins));
				data.setServerUp("YES");
				data.setEndDate(execDateStr);
				log.debug("Updating down time data in Mongo");
				repoService.save(data);
				if (sendMail)
					sendServerUpMail(compName, execDate);
			}
		} else {
			log.debug(compName + " Server is DOWN!!");
			data = repoService.findUpTimeUpdate(compName);
			if (data == null) {
				data = new DownTimeData();
				data.setComponentName(compName);
				data.setStartDate(execDateStr);
				Set<String> execDates = new HashSet<>();
				execDates.add(execDateStr);
				data.setExecDate(execDates);
				data.setDownTime(execDate);
				data.setServerUp("NO");
				data.setReasonCode(DownTimeReasonCode.NOTSET);
				data.setFailedUrl(result.getFailedURL());
				data.setFailedExpResp(result.getFailedExpResp());
				data.setFailedHttpException(result.getFailedHttpCallException());
				data.setFailedReqJson(result.getFailedReqJson());
				data.setFailedResp(result.getFailedActualResp());
				data.setFailedStatusCode(result.getFailedStatusCode());
				log.debug("Saving down time data in Mongo");
				repoService.save(data);
				if (sendMail)
					sendServerDownMail(compName, result, execDate);
			} else {
				log.warn(compName + ": Down time data already exist in Mongo!! This should not happen, please check!");
			}
		}
	}

	private void updateMongoDataWithCurrentExecDate(String currentExecDate) {
		List<DownTimeData> allDownServers = repoService.findAllDownTimeData();
		if (!allDownServers.isEmpty()) {
			for (DownTimeData downData : allDownServers) {
				if (!downData.getExecDate().contains(currentExecDate)) {
					log.debug("Updating current exec date for down time comp: " + downData.getComponentName());
					Set<String> execDates = downData.getExecDate();
					execDates.add(currentExecDate);
					downData.setExecDate(execDates);
					repoService.save(downData);
				}
			}
		}
		List<ConnIssueComp> openIssues = repoService.findAllOpenIssues();
		if (!openIssues.isEmpty()) {
			for (ConnIssueComp issue : openIssues) {
				if (!issue.getExecDate().contains(currentExecDate)) {
					log.debug("Updating current exec date for issue comp: " + issue.getComponentName());
					Set<String> execDates = issue.getExecDate();
					execDates.add(currentExecDate);
					issue.setExecDate(execDates);
					repoService.save(issue);
				}
			}
		}
	}

	private void sendServerUpMail(String compName, Date execDate) {
		String msgSubject = compName + " server is up & running on " + envName + " - " + execDate;
		String msgBody = "<html><h3>Your component: <i>" + compName
				+ "</i> is back up & running. Thanks for looking into it</h3>" + "<br><br>@${QMSPOC}<br>"
				+ "Use the below link to update the server downtime reason,<br>http://tm.snapdeal.io:9090/healthCheck/admin/updateReasonPage"
				+ "<br>" + MAIL_SIGN + "</html>";
		sendMail(compName, msgSubject, msgBody);
	}

	private void sendConnTimedOutMail(String compName, HealthCheckResult result, Date execDate) {
		if (sendMail) {
			String msgSubject = compName + " server is timing out on " + envName + " - " + execDate;
			String msgBody = "<html><h3>Your component: <i>" + compName
					+ "</i> is timing out for the below call. Please have a look at it.</h3>";

			StringBuilder msg = new StringBuilder(msgBody);
			msg.append("<br><b>URL: </b>" + getStringForHtml(result.getFailedURL()));
			if (result.getFailedReqJson() != null)
				msg.append("<br><b>Request JSON: </b>" + getStringForHtml(result.getFailedReqJson()));
			msg.append("<br><br><b>Http Call Exception: </b>" + getStringForHtml(result.getFailedHttpCallException()));
			msg.append("<br><br><br>" + MAIL_SIGN + "</html>");
			sendMail(compName, msgSubject, msg.toString());
		}
	}

	private void sendServerDownMail(String compName, HealthCheckResult result, Date execDate) {
		String msgSubject = compName + " server is down on " + envName + " - " + execDate;
		String msgBody = "<html><h3>Your component: <i>" + compName
				+ "</i> seems to be down. Please have a look at it.</h3>";

		StringBuilder msg = new StringBuilder(msgBody);
		msg.append("<br><b>URL: </b>" + getStringForHtml(result.getFailedURL()));
		if (result.getFailedReqJson() != null)
			msg.append("<br><b>Request JSON: </b>" + getStringForHtml(result.getFailedReqJson()));
		msg.append("<br><br><b>Expected token in response: </b>" + getStringForHtml(result.getFailedExpResp()));
		msg.append("<br>");
		if (result.getFailedStatusCode() != null)
			msg.append("<br><b>Status code: </b>" + getStringForHtml(result.getFailedStatusCode()));

		if (result.getFailedActualResp() != null)
			msg.append("<br><b>Response: </b>" + getStringForHtml(result.getFailedActualResp()));
		if (result.getFailedHttpCallException() != null)
			msg.append("<br><b>Http Call Exception: </b>" + getStringForHtml(result.getFailedHttpCallException()));
		msg.append("<br><br><br>" + MAIL_SIGN + "</html>");
		sendMail(compName, msgSubject, msg.toString());
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