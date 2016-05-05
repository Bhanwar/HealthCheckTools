package com.snapdeal.healthcheck.app.controller;

import static com.snapdeal.healthcheck.app.constants.AppConstant.healthResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snapdeal.healthcheck.app.constants.AppConstant;
import com.snapdeal.healthcheck.app.constants.Formatter;
import com.snapdeal.healthcheck.app.enums.Component;
import com.snapdeal.healthcheck.app.enums.DownTimeReasonCode;
import com.snapdeal.healthcheck.app.model.DownTimeData;
import com.snapdeal.healthcheck.app.model.DownTimeUIData;
import com.snapdeal.healthcheck.app.model.StartUpResult;
import com.snapdeal.healthcheck.app.mongo.repositories.DownTimeDataRepository;
import com.snapdeal.healthcheck.app.mongo.repositories.StartUpResultsRepository;
import com.snapdeal.healthcheck.app.services.AdminTask;
import com.snapdeal.healthcheck.app.services.GatherData;
import com.snapdeal.healthcheck.app.services.SaveReason;

@Controller
public class ServiceController {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private StartUpResultsRepository startUpDataRepo;

	@Autowired
	private DownTimeDataRepository downTimeRepo;
	
	@Autowired
	private GatherData dataObj;
	
	@Autowired
	private AdminTask admin;
	
	@Autowired
	private SaveReason updateReason;
	
	@PostConstruct
	public void init() {
		healthResult = new HashMap<>();
		log.debug("Fetching data from Mongo");
		List<StartUpResult> listComp = startUpDataRepo.findAll();
		createInitialData();
		if(!listComp.isEmpty()) {
			log.debug("Got data from mongo, initializing. Total count: " + listComp.size());
			for(StartUpResult res : listComp) {
				healthResult.put(res.getComponentName(), res.isServerUp());
			}
		}
		// objSharePassword.sharePasswordToQms();
	}
	
	@PreDestroy
	public void destroy() {
		startUpDataRepo.deleteAll();
		log.debug("Deleting and re entering start up data!");
		for(Entry<String, Boolean> entry : healthResult.entrySet()) {
			StartUpResult result = new StartUpResult();
			result.setComponentName(entry.getKey());
			result.setServerUp(entry.getValue());
			startUpDataRepo.save(result);
		}
	}
	
	@RequestMapping(value = "/isServerUp", method=RequestMethod.GET)
	@ResponseBody
	public String isServerUp() {
		return "I am up and running..!! " + AppConstant.currentExecDate;
	}
	
	@RequestMapping(value = "/getUpdateList", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, String> getUpdateList() {
		Map<String, String> result = new HashMap<>();
		List<DownTimeData> dataList = downTimeRepo.findAllDownTimeDataToUpdateReason();
		for(DownTimeData data : dataList)
			result.put(data.getId(),data.getComponentName() + " " + data.getStartDate());
		
		return result;
	}
	
	@RequestMapping(value = "/getReasonCodes", method=RequestMethod.GET)
	@ResponseBody
	public List<String> getReasonCodes() {
		List<String> reasonCodes = new ArrayList<>();
		DownTimeReasonCode[] reasons = DownTimeReasonCode.values();
		for(int i=0;i<reasons.length;i++) {
			if(!reasons[i].getCode().equals("NA"))
				reasonCodes.add(reasons[i].getCode());
		}
		return reasonCodes;
	}
	
	@RequestMapping(value = "/getComponentList", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, String> getComponentList() {
		Map<String, String> result = new HashMap<>();
		Component[] comps = Component.values();
		for(int i=0;i<comps.length;i++) {
			result.put(comps[i].code(), comps[i].getName());
		}
		return result;
	}
	
	@RequestMapping(value = "/updateReasonPage", method=RequestMethod.GET)
	public String getUpdateReasonPage(ModelMap model) {
		return "updateReason";
	}
	
	@RequestMapping(value = "/updateReason", method=RequestMethod.POST)
	@ResponseBody
	public String updateReason(@RequestBody String data) {
		return updateReason.saveUpdateReason(data);
	}
	
	@RequestMapping(value = "/admin/updateAuthKey", method=RequestMethod.GET)
	public String updateAuthKeyPage() {
		return "authKeyUpdate";
	}
	
	@RequestMapping(value = "/updateAuthKey", method=RequestMethod.POST)
	@ResponseBody
	public String updateAuthKey(@RequestBody String data) {
		return admin.changePassword(data);
	}
	
	@RequestMapping(value = "/", method=RequestMethod.GET)
	public String homePage(ModelMap model) {
		Date currExecDate = AppConstant.currentExecDate;
		Map<String, List<DownTimeUIData>> data = dataObj.getDataForHomePage(currExecDate);
		double timePercentage = dataObj.getTimePercentage(currExecDate);
		model.addAttribute("total", data.size());
		model.addAttribute("dateTime", currExecDate);
		model.addAttribute("data", data);
		model.addAttribute("dateStr", Formatter.dateFormatter.format(currExecDate));
		model.addAttribute("timePercentage", timePercentage);
		return "home";
	}
	
	private void createInitialData() {
		Component[] comps = Component.values();
		for(int i=0;i<comps.length;i++) {
			healthResult.put(comps[i].code(), true);
		}	
	}
}
