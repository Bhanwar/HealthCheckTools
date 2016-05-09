package com.snapdeal.healthcheck.app.scheduler;

import static com.snapdeal.healthcheck.app.constants.AppConstant.MAIL_SIGN;
import static com.snapdeal.healthcheck.app.constants.AppConstant.SNAPDEAL_ID;
import static com.snapdeal.healthcheck.app.constants.AppConstant.componentNames;
import static com.snapdeal.healthcheck.app.constants.AppConstant.currentExecDate;
import static com.snapdeal.healthcheck.app.constants.AppConstant.healthResult;
import static com.snapdeal.healthcheck.app.constants.Formatter.dateFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import com.snapdeal.healthcheck.app.model.ComponentDetails;
import com.snapdeal.healthcheck.app.model.DownTimeData;
import com.snapdeal.healthcheck.app.model.HealthCheckResult;
import com.snapdeal.healthcheck.app.model.QuartzJobDataHolder;
import com.snapdeal.healthcheck.app.model.StartUpResult;
import com.snapdeal.healthcheck.app.mongo.repositories.MongoRepoService;
import com.snapdeal.healthcheck.app.services.impl.EnvHealthCheckImpl;
import com.snapdeal.healthcheck.app.utils.EmailUtil;

public class HealthCheckScheduler extends QuartzJobBean {

	private QuartzJobDataHolder dataObjects;
	private ComponentDetailsBO compDetails;
	private final Logger log = LoggerFactory.getLogger(getClass());
	private String toAddress;
	private String ccAddress;
	private String envName;
	private MongoRepoService repoService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		compDetails = dataObjects.getCompDetails();
		List<ComponentDetails> components = compDetails.getAllComponentDetails();
		currentExecDate = new Date();
		log.debug("Running scheduled task: " + currentExecDate);
		List<Callable<HealthCheckResult>> compCallList = new ArrayList<>();
		for (ComponentDetails comp : components) {
			componentNames.add(comp.getComponentName());
			compCallList.add(new EnvHealthCheckImpl(comp,repoService));
		}
	
		//Initialize result from mongo
		if(healthResult == null) {
			healthResult = new HashMap<>();
			for(ComponentDetails comp : components) {
				healthResult.put(comp.getComponentName(), true);
			}
			log.debug("Fetching data from Mongo");
			List<StartUpResult> listComp = repoService.getStartUpData();
			if(!listComp.isEmpty()) {
				log.debug("Got data from mongo, initializing. Total count: " + listComp.size());
				for(StartUpResult res : listComp) {
					healthResult.put(res.getComponentName(), res.isServerUp());
				}
			}
		}
		
		ExecutorService exec = null;
		if (!compCallList.isEmpty()) {
			try {
				int compCount = compCallList.size();
				exec = Executors.newFixedThreadPool(compCount);
				CompletionService<HealthCheckResult> compSer = new ExecutorCompletionService<HealthCheckResult>(exec);
				for (Callable<HealthCheckResult> compToCall : compCallList) {
					compSer.submit(compToCall);
				}

				for (int i = 0; i < compCount; i++) {
					try {
						HealthCheckResult result = compSer.take().get();
						log.debug(result.toString());
						ExecutorService execMail = Executors.newFixedThreadPool(1);
						if (healthResult.get(result.getComponentName()) != result.isServerUp()) {
							log.debug("Status has changed for comp: " + result.getComponentName());
							final HealthCheckResult updateRes = result;
							final Date updateDate = currentExecDate;
							execMail.submit(new Runnable() {
								@Override
								public void run() {
									updateAndSendMail(updateRes, updateDate);
								}
							});
						}
						healthResult.put(result.getComponentName(), result.isServerUp());
						execMail.shutdown();
					} catch (InterruptedException | ExecutionException e) {
						log.error("Exception occured while getting results: " + e.getMessage(), e);
					}
				}
				updateDownTimeDataWithCurrentExecDate(currentExecDate);
				log.info("Health check result: " + healthResult);
			} catch (Exception ee) {
				log.error("Exception occured while executing scheduled task: " + ee.getMessage(), ee);
			} finally {
				if (exec != null)
					exec.shutdown();
			}
		}
	}

	private void updateAndSendMail(HealthCheckResult result, Date execDate) {
		DownTimeData data = null;
		boolean isServerUp = result.isServerUp();
		String compName = result.getComponentName();
		if (isServerUp) {
			log.debug(compName + " Server is UP!!");
			data = repoService.findUpTimeUpdate(compName);
			data.setUpTime(execDate);
			long totalTimeMins = (execDate.getTime() - data.getDownTime().getTime()) / 60000;
			log.debug("Total down time: " + totalTimeMins);
			data.setTotalDownTimeInMins(Long.toString(totalTimeMins));
			data.setServerUp("YES");
			data.setEndDate(dateFormatter.format(execDate));
			log.debug("Updating down time data in Mongo");
			repoService.save(data);
			sendServerUpMail(compName, execDate);
		} else {
			log.debug(compName + " Server is DOWN!!");
			data = new DownTimeData();
			data.setComponentName(compName);
			data.setStartDate(dateFormatter.format(execDate));
			Set<String> execDates = new HashSet<>();
			execDates.add(dateFormatter.format(execDate));
			data.setExecDate(execDates);
			data.setDownTime(execDate);
			data.setServerUp("NO");
			data.setReasonCode(DownTimeReasonCode.NOTSET);
			data.setFailedUrl(result.getFailedURL());
			data.setFailedExpResp(result.getFailedExpResp());
			data.setFailedHttpException(result.getFailedHttpCallException());
			data.setFailedReqJson(result.getFailedReqJson());
			data.setFailedResp(result.getFailedActualResp());
			log.debug("Saving down time data in Mongo");
			repoService.save(data);
			sendServerDownMail(compName, result, execDate);
		}
	}

	private void updateDownTimeDataWithCurrentExecDate(Date execDate) {
		String currentExecDate = dateFormatter.format(execDate);
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
	}

	private void sendServerUpMail(String compName, Date execDate) {
		String msgSubject = compName + " server is up & running on " + envName;
		String msgBody = "<html><h3>Your component: <i>" + compName
				+ "</i> is back up & running. Thanks for looking into it</h3>" + MAIL_SIGN + "</html>";
		sendMail(compName, msgSubject, msgBody);
	}

	private void sendServerDownMail(String compName, HealthCheckResult result, Date execDate) {
		String msgSubject = compName + " server is down on " + envName;
		String msgBody = "<html><h3>Your component: <i>" + compName
				+ "</i> seems to be down. Please have a look at it.</h3>";
		
		StringBuilder msg = new StringBuilder(msgBody);
		msg.append("<br>URL: " + result.getFailedURL());
		msg.append("<br>Request JSON: " + result.getFailedReqJson());
		msg.append("<br>Response: " + result.getFailedActualResp());
		msg.append("<br>Expected token in response: " + result.getFailedExpResp());
		msg.append("<br>Http Call Exception: " + result.getFailedHttpCallException());
		msg.append("<br>" + MAIL_SIGN + "</html>");
		sendMail(compName, msgSubject, msg.toString());
	}

	private void sendMail(String compName, String msgSubject, String msgBody) {
		ComponentDetails comp = compDetails.getComponentDetails(compName);
		if (comp == null) {
			log.error("No component found with the name: " + compName);
		} else {
			List<String> emailAddressTo = new ArrayList<>();
			List<String> emailAddressCc = new ArrayList<>();
			String[] list = comp.getQaSpoc().split(",");
			for (int i = 0; i < list.length; i++) {
				if (list[i].contains(SNAPDEAL_ID))
					emailAddressTo.add(list[i]);
			}
			if (comp.getQmSpoc() != null && comp.getQmSpoc().contains(SNAPDEAL_ID))
				emailAddressCc.add(comp.getQmSpoc());
			String[] ccAdd = ccAddress.split(",");
			for (int i = 0; i < ccAdd.length; i++) {
				if (ccAdd[i].contains(SNAPDEAL_ID))
					emailAddressCc.add(ccAdd[i]);
			}
			log.debug("Sending mail to " + emailAddressTo);
			if (emailAddressTo.isEmpty()) {
				log.warn("Daily Report was not sent as the TO Address list was empty!!");
			} else {
				EmailUtil mail = new EmailUtil(emailAddressTo, emailAddressCc, null, msgSubject, msgBody);
				mail.sendHTMLEmail();
			}
		}
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

	public QuartzJobDataHolder getDataObjects() {
		return dataObjects;
	}

	public void setDataObjects(QuartzJobDataHolder dataObjects) {
		this.dataObjects = dataObjects;
	}
}