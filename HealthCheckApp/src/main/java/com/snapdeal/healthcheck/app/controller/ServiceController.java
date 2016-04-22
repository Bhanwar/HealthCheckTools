package com.snapdeal.healthcheck.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import static com.snapdeal.healthcheck.app.constants.ComponentNameConstants.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snapdeal.healthcheck.app.model.StartUpResult;
import com.snapdeal.healthcheck.app.mongo.repositories.StartUpResultsRepository;

@Controller
public class ServiceController {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public static Map<String, Boolean> healthResult;
	
	@Autowired
	private StartUpResultsRepository startUpDataRepo;

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
	public String contactUs () {
		return "I am up and running..!!";
	}
	
	private void createInitialData() {
		healthResult.put(COMPONENT_CAMS, true);
		healthResult.put(COMPONENT_COCOFS, true);
		healthResult.put(COMPONENT_IPMS, true);
		healthResult.put(COMPONENT_OMS, true);
		healthResult.put(COMPONENT_OPMS, true);
		healthResult.put(COMPONENT_OPS, true);
		healthResult.put(COMPONENT_PROMO, true);
		healthResult.put(COMPONENT_SCORE, true);
	}
}
