package com.snapdeal.healthcheck.app.scheduler;

import static com.snapdeal.healthcheck.app.constants.ComponentNameConstants.compCount;
import static com.snapdeal.healthcheck.app.controller.ServiceController.healthResult;

import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.snapdeal.healthcheck.app.model.DownTimeData;
import com.snapdeal.healthcheck.app.model.HealthCheckData;
import com.snapdeal.healthcheck.app.model.HealthCheckResult;
import com.snapdeal.healthcheck.app.mongo.repositories.MongoRepoService;
import com.snapdeal.healthcheck.app.services.impl.CAMSHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.COCOFSHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.IPMSHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.OMSHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.OPMSHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.OPSHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.PromoHealthCheckImpl;
import com.snapdeal.healthcheck.app.services.impl.SCOREHealthCheckImpl;

public class HealthCheckScheduler extends QuartzJobBean {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private HealthCheckData data;
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM, yyyy");
	private SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
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
		Date executingDateTime = new Date();
		String date = dateFormatter.format(executingDateTime);
		String time = timeFormatter.format(executingDateTime);
		log.debug("Running scheduled task: " + executingDateTime);
		ExecutorService exec = null;
		try {
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
			for (int i = 0; i < compCount; i++) {
				try {
					HealthCheckResult result = compSer.take().get();
					result.setExecDate(date);
					result.setExecTime(time);
					log.debug(result.toString());
					if(healthResult.get(result.getComponentName()) != result.isServerUp()) {
						updateAndSendMail(result.getComponentName(), result.isServerUp(), executingDateTime);
					}
					healthResult.put(result.getComponentName(), result.isServerUp());
					repoService.save(result);
				} catch (InterruptedException | ExecutionException e) {
					log.error("Exception occured while getting results: " + e.getMessage(), e);
				}
			}
			log.info("Health check result: " + healthResult);
		} catch (Exception ee) {
			log.error("Exception occured while executing scheduled task: " + ee.getMessage(), ee);
		} finally {
			if(exec != null)
				exec.shutdown();
		}
	}

	private void updateAndSendMail(String compName, boolean isServerUp, Date execDate) {
		DownTimeData data = null;
		if(isServerUp) {
			log.debug(compName + " Server is UP!!");
			data = repoService.findUpTimeUpdate(compName);
			log.debug("Finding data..");
			log.debug("Find response: " + data.toString());
			data.setUpTime(timeFormatter.format(execDate));
			log.debug("Mongo data: date - " + data.getExecDate());
			long totalTimeMins = (execDate.getTime() - data.getExecDate().getTime())/60000;
			log.debug("Total down time: ");
			data.setTotalDownTime(Long.toString(totalTimeMins));
			log.debug("Updating down time data in Mongo");
			repoService.save(data);
		} else {
			log.debug(compName + " Server is DOWN!!");
			data = new DownTimeData();
			data.setComponentName(compName);
			data.setDate(dateFormatter.format(execDate));
			data.setDownTime(timeFormatter.format(execDate));
			data.setExecDate(execDate);
			data.setUpTime("");
			log.debug("Saving down time data in Mongo");
			repoService.save(data);
		}
	}
}