package com.snapdeal.healthcheck.app.scheduler;

import static com.snapdeal.healthcheck.app.constants.AppConstant.currentExecDate;
import static com.snapdeal.healthcheck.app.constants.AppConstant.healthResult;
import static com.snapdeal.healthcheck.app.constants.Formatter.dateFormatter;
import static com.snapdeal.healthcheck.app.constants.Formatter.timeFormatter;

import java.util.Date;
import java.util.List;
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

import com.snapdeal.healthcheck.app.enums.Component;
import com.snapdeal.healthcheck.app.enums.DownTimeReasonCode;
import com.snapdeal.healthcheck.app.model.DownTimeData;
import com.snapdeal.healthcheck.app.model.HealthCheckData;
import com.snapdeal.healthcheck.app.model.HealthCheckResult;
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

public class HealthCheckScheduler extends QuartzJobBean {


	private final Logger log = LoggerFactory.getLogger(getClass());
	private HealthCheckData data;

	private MongoRepoService repoService;

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

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		currentExecDate = new Date();
		String date = dateFormatter.format(currentExecDate);
		String time = timeFormatter.format(currentExecDate);
		int compCount = Component.values().length;
		log.debug("Running scheduled task: " + currentExecDate);
		ExecutorService exec = null;
		try {
			data.get();
			exec = Executors.newCachedThreadPool();
			CompletionService<HealthCheckResult> compSer = new ExecutorCompletionService<HealthCheckResult>(exec);
			compSer.submit(new CAMSHealthCheckImpl(data.getCamsEndPoint()));
			compSer.submit(new COCOFSHealthCheckImpl(data.getCocofsEndPoint()));
			compSer.submit(new IPMSHealthCheckImpl(data.getIpmsEndPoint()));
			compSer.submit(new OMSHealthCheckImpl(data.getOmsEndPoint()));
			compSer.submit(new OPMSHealthCheckImpl(data.getOpmsEndPoint()));
			compSer.submit(new OPSHealthCheckImpl(data.getOpsEndPoint()));
			compSer.submit(new SCOREHealthCheckImpl(data.getScoreEndPoint()));
			compSer.submit(new PromoHealthCheckImpl(data.getPromoEndPoint()));
			compSer.submit(new ERASHealthCheckImpl(data.getErasEndPoint()));
			compSer.submit(new MobAPIHealthCheckImpl(data.getMobApiEndPoint()));
			compSer.submit(new RNRHealthCheckImpl(data.getRNREndPoint()));
			compSer.submit(new SEARCHHealthCheckImpl(data.getSearchEndPoint()));
			compSer.submit(new UMSHealthCheckImpl(data.getUmsEndPoint()));
			compSer.submit(new CARTHealthCheckImpl(data.getCartEndPoint()));
			compSer.submit(new SPMSPMNTHealthCheckImpl(data.getSpmsEndPoint()));
			compSer.submit(new SCOREADMINHealthCheckImpl(data.getScoreAdminEndPoint()));
			compSer.submit(new FILMSUIHealthCheckImpl(data.getFilmsUIEndPoint()));
			compSer.submit(new SellerToolsHealthCheckImpl(data.getSellerToolsEndPoint()));
			compSer.submit(new SNSHealthCheckImpl(data.getSNSEndPoint()));
			compSer.submit(new UCMSTEHealthCheckImpl(data.getUCMSTemplateEndPoint()));
			compSer.submit(new UCMSPHealthCheckImpl(data.getUcmsProcessorEndPoint()));
			compSer.submit(new SHIPFARHealthCheckImpl(data.getShipFarEndPoint()));
			compSer.submit(new OMSADMINHealthCheckImpl(data.getOMSAdminEndPoint()));
			compSer.submit(new POMSHealthCheckImpl(data.getPomsEndPoint()));
			compSer.submit(new QNAHealthCheckImpl(data.getQnaEndPoint()));
			for (int i = 0; i < compCount; i++) {
				try {
					HealthCheckResult result = compSer.take().get();
					result.setExecDate(date);
					result.setExecTime(time);
					result.setExecDateTime(currentExecDate);
					log.debug(result.toString());
					if (healthResult.get(result.getComponentName()) != result.isServerUp()) {
						final HealthCheckResult updateRes = result;
						final Date updateDate = currentExecDate;
						exec.submit(new Runnable() {
							@Override
							public void run() {
								updateAndSendMail(updateRes.getComponentName(), updateRes.isServerUp(), updateDate);
							}
						});
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
			data.setReasonCode(DownTimeReasonCode.NOTSET);
			log.debug("Updating down time data in Mongo");
			repoService.save(data);
		} else {
			log.debug(compName + " Server is DOWN!!");
			data = new DownTimeData();
			data.setComponentName(compName);
			data.setStartDate(dateFormatter.format(execDate));
			data.setExecDate(dateFormatter.format(execDate));
			data.setDownTime(execDate);
			data.setServerUp("NO");
			log.debug("Saving down time data in Mongo");
			repoService.save(data);
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
}