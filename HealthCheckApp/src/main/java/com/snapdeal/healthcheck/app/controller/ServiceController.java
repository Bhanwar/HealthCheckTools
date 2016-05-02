package com.snapdeal.healthcheck.app.controller;

import static com.snapdeal.healthcheck.app.constants.AppConstant.healthResult;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snapdeal.healthcheck.app.constants.AppConstant;
import com.snapdeal.healthcheck.app.constants.Formatter;
import com.snapdeal.healthcheck.app.enums.Component;
import com.snapdeal.healthcheck.app.model.DownTimeUIData;
import com.snapdeal.healthcheck.app.model.StartUpResult;
import com.snapdeal.healthcheck.app.mongo.repositories.StartUpResultsRepository;
import com.snapdeal.healthcheck.app.services.GatherData;

@Controller
public class ServiceController {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private StartUpResultsRepository startUpDataRepo;

	@Autowired
	private GatherData dataObj;
	
	@PostConstruct
	public void init() {
		healthResult = new HashMap<>();
		log.debug("Fetching data from Mongo");
		List<StartUpResult> listComp = startUpDataRepo.findAll();
		if(listComp.isEmpty()) {
			log.debug("List is empty, initializing..");
			createInitialData();
		} else {
			log.debug("Got data from mongo, initializing. Total count: " + listComp.size());
			for(StartUpResult res : listComp) {
				healthResult.put(res.getComponentName(), res.isServerUp());
			}
		}
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
	public String contactUs() {
		return "I am up and running..!! " + AppConstant.currentExecDate;
	}
	
	@RequestMapping(value = "/", method=RequestMethod.GET)
	public String homePage(ModelMap model) {
		Date currExecDate = AppConstant.currentExecDate;
		Map<String, List<DownTimeUIData>> data = dataObj.getDataForHomePage(currExecDate);
		int timePercentage = dataObj.getTimePercentage(currExecDate);
		model.addAttribute("total", data.size());
		model.addAttribute("dateTime", currExecDate);
		model.addAttribute("data", data);
		model.addAttribute("dateStr", Formatter.dateFormatter.format(currExecDate));
		model.addAttribute("timePercentage", timePercentage);
		return "home";
	}
	
	private void createInitialData() {
		healthResult.put(Component.CAMS.code(), true);
		healthResult.put(Component.COCOFS.code(), true);
		healthResult.put(Component.IPMS.code(), true);
		healthResult.put(Component.OMS.code(), true);
		healthResult.put(Component.OPMS.code(), true);
		healthResult.put(Component.OPS.code(), true);
		healthResult.put(Component.PROMO.code(), true);
		healthResult.put(Component.SCORE.code(), true);
		healthResult.put(Component.ERAS.code(), true);
		healthResult.put(Component.MOBAPI.code(), true);
		healthResult.put(Component.RNR.code(), true);
		healthResult.put(Component.SEARCH.code(), true);
		healthResult.put(Component.CART.code(), true);
		healthResult.put(Component.SPMSPMNT.code(), true);
		healthResult.put(Component.SCOREADMIN.code(), true);
		healthResult.put(Component.FILMS.code(), true);
	}
}
