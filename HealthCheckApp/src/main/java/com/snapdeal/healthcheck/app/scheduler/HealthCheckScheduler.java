package com.snapdeal.healthcheck.app.scheduler;

import static com.snapdeal.healthcheck.app.constants.AppConstant.MAIL_SIGN;
import static com.snapdeal.healthcheck.app.constants.AppConstant.SNAPDEAL_ID;
import static com.snapdeal.healthcheck.app.constants.AppConstant.currentExecDate;
import static com.snapdeal.healthcheck.app.constants.AppConstant.healthResult;
import static com.snapdeal.healthcheck.app.constants.Formatter.dateFormatter;
import static com.snapdeal.healthcheck.app.constants.Formatter.timeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import com.snapdeal.healthcheck.app.configurables.GetApiConfigValues;
import com.snapdeal.healthcheck.app.enums.DownTimeReasonCode;
import com.snapdeal.healthcheck.app.model.ComponentDetails;
import com.snapdeal.healthcheck.app.model.DownTimeData;
import com.snapdeal.healthcheck.app.model.HealthCheckData;
import com.snapdeal.healthcheck.app.model.HealthCheckResult;
import com.snapdeal.healthcheck.app.model.QuartzJobDataHolder;
import com.snapdeal.healthcheck.app.mongo.repositories.MongoRepoService;
import com.snapdeal.healthcheck.app.services.impl.CAMSHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.CARTHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.COCOFSHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.ERASHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.FILMSUIHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.IPMSHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.MobAPIHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.OMSADMINHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.OMSHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.OPMSHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.OPSHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.POMSHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.PromoHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.QNAHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.RNRHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.SCOREADMINHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.SCOREHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.SEARCHHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.SHIPFARHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.SNSHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.SPMSPMNTHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.SellerToolsHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.UCMSPHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.UCMSTEHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.UMSHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.RMSHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.SELLERSTHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.WEBHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.MATRIXHealthCheckImpl;
import com.snapdeal.healthcheck.app.utils.EmailUtil;

public class HealthCheckScheduler extends QuartzJobBean {

	private QuartzJobDataHolder dataObjects;
	private ComponentDetailsBO compDetails;
	private GetApiConfigValues objGetConfigValues;
	private final Logger log = LoggerFactory.getLogger(getClass());
	private HealthCheckData data;
	private String toAddress;
	private String ccAddress;
	private String envName;
	private MongoRepoService repoService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		data.get();
		compDetails = dataObjects.getCompDetails();
		objGetConfigValues = dataObjects.getObjGetConfig();
		currentExecDate = new Date();
		String date = dateFormatter.format(currentExecDate);
		String time = timeFormatter.format(currentExecDate);
		log.debug("Running scheduled task: " + currentExecDate);
		List<Callable<HealthCheckResult>> compCallList = new ArrayList<>();
		//Add new components here in this list to check is the server is up
		compCallList.add(new CAMSHealthCheckImpl(data.getCamsEndPoint()));
		compCallList.add(new COCOFSHealthCheckImpl(data.getCocofsEndPoint()));
		compCallList.add(new IPMSHealthCheckImpl(data.getIpmsEndPoint()));
		compCallList.add(new OMSHealthCheckImpl(data.getOmsEndPoint()));
		compCallList.add(new OPMSHealthCheckImpl(data.getOpmsEndPoint()));
		compCallList.add(new OPSHealthCheckImpl(data.getOpsEndPoint()));
		compCallList.add(new SCOREHealthCheckImpl(data.getScoreEndPoint()));
		compCallList.add(new PromoHealthCheckImpl(data.getPromoEndPoint()));
		compCallList.add(new ERASHealthCheckImpl(data.getErasEndPoint()));
		compCallList.add(new MobAPIHealthCheckImpl(data.getMobApiEndPoint()));
		compCallList.add(new RNRHealthCheckImpl(data.getRNREndPoint()));
		compCallList.add(new SEARCHHealthCheckImpl(data.getSearchEndPoint()));
		compCallList.add(new UMSHealthCheckImpl(data.getUmsEndPoint()));
		compCallList.add(new CARTHealthCheckImpl(data.getCartEndPoint()));
		compCallList.add(new SPMSPMNTHealthCheckImpl(data.getSpmsEndPoint()));
		compCallList.add(new SCOREADMINHealthCheckImpl(data.getScoreAdminEndPoint()));
		compCallList.add(new FILMSUIHealthCheckImpl(data.getFilmsUIEndPoint()));
		compCallList.add(new SellerToolsHealthCheckImpl(data.getSellerToolsEndPoint()));
		compCallList.add(new SNSHealthCheckImpl(data.getSNSEndPoint()));
		compCallList.add(new UCMSTEHealthCheckImpl(data.getUCMSTemplateEndPoint()));
		compCallList.add(new UCMSPHealthCheckImpl(data.getUcmsProcessorEndPoint()));
		compCallList.add(new SHIPFARHealthCheckImpl(data.getShipFarEndPoint()));
		compCallList.add(new OMSADMINHealthCheckImpl(data.getOMSAdminEndPoint(), objGetConfigValues));
		compCallList.add(new POMSHealthCheckImpl(data.getPomsEndPoint(), objGetConfigValues));
		compCallList.add(new QNAHealthCheckImpl(data.getQnaEndPoint()));
		compCallList.add(new RMSHealthCheckImpl(data.getRMSEndPoint()));
		compCallList.add(new SELLERSTHealthCheckImpl(data.getSellerSelfTrainingEndPoint(), objGetConfigValues));
		compCallList.add(new WEBHealthCheckImpl(data.getWebEndPoint()));
		compCallList.add(new MATRIXHealthCheckImpl(data.getMatrixEndPoint()));
		//End of components list
		
		
		ExecutorService exec = null;
		try {
			int compCount = compCallList.size();
			exec = Executors.newFixedThreadPool(compCount);
			CompletionService<HealthCheckResult> compSer = new ExecutorCompletionService<HealthCheckResult>(exec);
			for(Callable<HealthCheckResult> compToCall : compCallList) {
				compSer.submit(compToCall);
			}
			
			for (int i = 0; i < compCount; i++) {
				try {
					HealthCheckResult result = compSer.take().get();
					result.setExecDate(date);
					result.setExecTime(time);
					result.setExecDateTime(currentExecDate);
					log.debug(result.toString());
					if (healthResult.get(result.getComponentName()) != result.isServerUp()) {
						log.debug("Status has changed for comp: " + result.getComponentName() + ". Will update mongo and send mail");
						ExecutorService execMail = Executors.newFixedThreadPool(1);
						final HealthCheckResult updateRes = result;
						final Date updateDate = currentExecDate;
						execMail.submit(new Runnable() {
							@Override
							public void run() {
								updateAndSendMail(updateRes.getComponentName(), updateRes.isServerUp(), updateDate);
							}
						});
						execMail.shutdown();
					}
					healthResult.put(result.getComponentName(), result.isServerUp());
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

	private void updateAndSendMail(String compName, boolean isServerUp, Date execDate) {
		DownTimeData data = null;
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
			data.setExecDate(dateFormatter.format(execDate));
			data.setDownTime(execDate);
			data.setServerUp("NO");
			data.setReasonCode(DownTimeReasonCode.NOTSET);
			log.debug("Saving down time data in Mongo");
			repoService.save(data);
			sendServerDownMail(compName, execDate);
		}
	}
	
	private void updateDownTimeDataWithCurrentExecDate(Date execDate) {
		String currentExecDate = dateFormatter.format(execDate);
		List<DownTimeData> allDownServers = repoService.findAllDownTimeData();
		if(!allDownServers.isEmpty()) {
			for(DownTimeData downData : allDownServers) {
				if(!downData.getExecDate().equals(currentExecDate)) {
					log.debug("Updating current exec date for down time comp: " + downData.getComponentName());
					downData.setExecDate(currentExecDate);
					repoService.save(downData);
				}
			}
		}
	}
	
	private void sendServerUpMail(String compName, Date execDate) {
		String msgSubject = compName + " server is up & running on " + envName;
		String msgBody = "<html><h3>Your component: <i>"+ compName +"</i> is back up & running. Thanks for looking into it</h3>"+MAIL_SIGN+"</html>";
		sendMail(compName, msgSubject, msgBody);
	}
	
	private void sendServerDownMail(String compName, Date execDate) {
		String msgSubject = compName + " server is down on " + envName;
		String msgBody = "<html><h3>Your component: <i>"+ compName +"</i> seems to be down. Please have a look at it.</h3>"+MAIL_SIGN+"</html>";
		sendMail(compName, msgSubject, msgBody);
	}
	
	private void sendMail(String compName, String msgSubject, String msgBody) {
		ComponentDetails comp = compDetails.getComponentDetails(compName);
		if(comp == null) {
			log.error("No component found with the name: " + compName);
		} else {
			List<String> emailAddressTo = new ArrayList<>();
			List<String> emailAddressCc = new ArrayList<>();
			String[] list = comp.getQaSpoc().split(",");
			for(int i=0;i<list.length;i++){
				if(list[i].contains(SNAPDEAL_ID))
					emailAddressTo.add(list[i]);
			}
			if(comp.getQmSpoc() != null && comp.getQmSpoc().contains(SNAPDEAL_ID))
				emailAddressCc.add(comp.getQmSpoc());
			String[] ccAdd = ccAddress.split(",");
			for(int i=0;i<ccAdd.length;i++) {
				if(ccAdd[i].contains(SNAPDEAL_ID))
					emailAddressCc.add(ccAdd[i]);
			}
			log.debug("Sending mail to " + emailAddressTo);
			if(emailAddressTo.isEmpty()) {
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
	
	public HealthCheckData getData() {
		return data;
	}

	public MongoRepoService getRepoService() {
		return repoService;
	}

	public void setRepoService(MongoRepoService repoService) {
		this.repoService = repoService;
	}

	public void setData(HealthCheckData data) {
		this.data = data;
	}
	public QuartzJobDataHolder getDataObjects() {
		return dataObjects;
	}

	public void setDataObjects(QuartzJobDataHolder dataObjects) {
		this.dataObjects = dataObjects;
	}
}