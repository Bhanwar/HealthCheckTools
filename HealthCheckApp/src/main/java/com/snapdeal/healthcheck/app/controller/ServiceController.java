package com.snapdeal.healthcheck.app.controller;

import static com.snapdeal.healthcheck.app.constants.AppConstant.componentNames;
import static com.snapdeal.healthcheck.app.constants.AppConstant.currentExecDate;
import static com.snapdeal.healthcheck.app.constants.AppConstant.disabledComponentNames;
import static com.snapdeal.healthcheck.app.constants.Formatter.dateFormatter;
import static com.snapdeal.healthcheck.app.constants.Formatter.timeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snapdeal.healthcheck.app.bo.ComponentDetailsBO;
import com.snapdeal.healthcheck.app.constants.AppConstant;
import com.snapdeal.healthcheck.app.constants.Formatter;
import com.snapdeal.healthcheck.app.enums.ComponentType;
import com.snapdeal.healthcheck.app.enums.DownTimeReasonCode;
import com.snapdeal.healthcheck.app.enums.TokenComponent;
import com.snapdeal.healthcheck.app.model.ComponentDetails;
import com.snapdeal.healthcheck.app.model.DownTimeData;
import com.snapdeal.healthcheck.app.model.DownTimeUIData;
import com.snapdeal.healthcheck.app.model.TimelyCompData;
import com.snapdeal.healthcheck.app.model.UIComponent;
import com.snapdeal.healthcheck.app.mongo.repositories.DownTimeDataRepository;
import com.snapdeal.healthcheck.app.services.AdminTask;
import com.snapdeal.healthcheck.app.services.GatherData;
import com.snapdeal.healthcheck.app.services.SaveReason;

@Controller
public class ServiceController {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private DownTimeDataRepository downTimeRepo;
	
	@Autowired
	private GatherData dataObj;
	
	@Autowired
	private AdminTask admin;
	
	@Autowired
	private SaveReason updateReason;
	
	@Autowired
	private ComponentDetailsBO compDetails;
	
	@PostConstruct
	public void init() {
		log.debug("Initializing data!!");
		currentExecDate = new Date();
		dataObj.initializeHealthCheckResults(compDetails.getAllComponentDetails());
	}
	
	@RequestMapping(value = "/isServerUp", method=RequestMethod.GET)
	@ResponseBody
	public String isServerUp() {
		return "I am up and running..!! " + AppConstant.currentExecDate;
	}
	
	@RequestMapping(value = "/latest", method=RequestMethod.GET)
	@ResponseBody
	public String latest() {
		return "Result: " + AppConstant.healthResult;
	}
	
	@RequestMapping(value = "/getUpdateList", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, NavigableMap<String, String>> getUpdateList() {
		Map<String, TreeMap<String, String>> resultMap = new TreeMap<>();
		Map<String, NavigableMap<String, String>> resultMapDesc = new TreeMap<>();
		List<DownTimeData> dataList = downTimeRepo.findAllDownTimeDataToUpdateReason();
		for(DownTimeData data : dataList) {
			TreeMap<String, String> resultData = null;
			String compName = data.getComponentName();
			Date startDate = data.getDownTime();
			if(resultMap.containsKey(compName)) {
				resultData = resultMap.get(compName);
			} else {
				resultData = new TreeMap<String, String>();
			}
			resultData.put(dateFormatter.format(startDate) + " " + timeFormatter.format(startDate), data.getId());
			resultMap.put(compName, resultData);
		}
		for(Entry<String, TreeMap<String, String>> entry : resultMap.entrySet()) {
			TreeMap<String, String> resultDataDesc = entry.getValue();
			NavigableMap<String, String> resDesc = resultDataDesc.descendingMap();
			resultMapDesc.put(entry.getKey(), resDesc);
		}
		resultMap = null;
		return resultMapDesc;
	}
	
	@RequestMapping(value = "/getRunningJobs", method=RequestMethod.GET)
	@ResponseBody
	public String getRunningJobs() {
		log.debug("Getting running jobs..");
		Scheduler sch;
		
		StringBuilder resultData = new StringBuilder("Data: ");
		try {
			sch = StdSchedulerFactory.getDefaultScheduler();
			List<JobExecutionContext> result = sch.getCurrentlyExecutingJobs();
			for(JobExecutionContext jobContext : result) {
				resultData.append(jobContext.toString());
			}
		} catch (SchedulerException e) {
			log.error("Exception occured while fetching currently executing jobs: " + e.getMessage(), e);
		}
		return resultData.toString();
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
	public Set<String> getComponentList() {
		return componentNames;	
	}
	
	@RequestMapping(value = "/getAllComponentList", method=RequestMethod.GET)
	@ResponseBody
	public Set<String> getAllComponentList() {
		Set<String> allComponentsList = new HashSet<String>();
		allComponentsList.addAll(componentNames);
		allComponentsList.addAll(disabledComponentNames);
		return allComponentsList;
	}
	
	@RequestMapping(value = "/getComponentQmMap", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, String> getComponentQmMap() {
		Map<String, String> result = new TreeMap<>();
		List<ComponentDetails> compList = compDetails.getAllEnabledComponentDetails();
		for(ComponentDetails comp : compList) {
			result.put(comp.getComponentName(), comp.getQmSpoc());
		}
		return result;	
	}
	
	@RequestMapping(value = "/getCompEnums", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, List<String>> getCompEnums() {
		Map<String, List<String>> data = new HashMap<String, List<String>>();
		List<String> tokenComps = new ArrayList<>();
		TokenComponent[] comps = TokenComponent.values();
		for(int i=0;i<comps.length;i++) {
			String comp = comps[i].getCode();
			if(!comp.equals("Invalid")) {
				if(compDetails.getComponentDetails(comp) == null)
					tokenComps.add(comp);
			}
		}
		List<String> compTypes = new ArrayList<>();
		ComponentType[] types = ComponentType.values();
		for(ComponentType comp : types) {
			compTypes.add(comp.getCode());
		}
		data.put("compTypesEnum", compTypes);
		data.put("tokenApiEnum", tokenComps);
		return data;
	}
	
	@RequestMapping(value = "/admin/updateReasonPage", method=RequestMethod.GET)
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
	
	@RequestMapping(value = "/admin/deleteComponent", method=RequestMethod.GET)
	public String deleteComponent() {
		return "deleteComponent";
	}
	
	@RequestMapping(value = "/admin", method=RequestMethod.GET)
	public String adminPage() {
		return "adminPage";
	}
	
	@RequestMapping(value = "/admin/addUpdateComp", method=RequestMethod.GET)
	public String addUpdateCompPage() {
		return "addUpdateComp";
	}
	
	@RequestMapping(value = "/admin/resetAuthKey", method=RequestMethod.GET)
	public String resetAuthKeyPage() {
		return "resetAuthKey";
	}
	
	@RequestMapping(value = "/admin/getReport", method=RequestMethod.GET)
	public String getReportDateRange() {
		return "reportDateRange";
	}
	
	@RequestMapping(value = "/admin/endpointUpdate", method=RequestMethod.GET)
	public String endpointUpdatePage() {
		return "updateEndpoint";
	}
	
	@RequestMapping(value = "/reportGen", method=RequestMethod.POST)
	@ResponseBody
	public Set<TimelyCompData> getReportDateRangeData(@RequestBody String data) {
		return admin.getReportDateRangeData(data);
	}
	
	@RequestMapping(value = "/admin/getCompsForUpdate", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, UIComponent> getCompsForUpdate() {
		return admin.getCompsForUpdate();
	}
	
	@RequestMapping(value = "/admin/getCompsForEndpointUpdate", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, String> getCompsForEndpointUpdate() {
		return admin.getCompsForEndpointUpdate();
	}
	
	@RequestMapping(value = "/updateAuthKey", method=RequestMethod.POST)
	@ResponseBody
	public String updateAuthKey(@RequestBody String data) {
		return admin.changePassword(data);
	}
	
	@RequestMapping(value = "/checkComp", method=RequestMethod.POST)
	@ResponseBody
	public Map<Boolean, String> checkComp(@RequestBody String data) {
		return admin.checkComponent(data);
	}
	
	@RequestMapping(value = "/checkCompForEndpointUpdate", method=RequestMethod.POST)
	@ResponseBody
	public Map<Boolean, String> checkCompForEndpointUpdate(@RequestBody String data) {
		return admin.checkComponentForEndpointUpdate(data);
	}
	
	@RequestMapping(value = "/addUpdateComp", method=RequestMethod.POST)
	@ResponseBody
	public String addUpdateComp(@RequestBody String data) {
		return admin.addUpdateComponent(data);
	}
	
	@RequestMapping(value = "/deleteComponent", method=RequestMethod.POST)
	@ResponseBody
	public String deleteComponent(@RequestBody String data) {
		return admin.deleteComponent(data);
	}
	
	@RequestMapping(value = "/resetAuthKey", method=RequestMethod.POST)
	@ResponseBody
	public String resetAuthKey(@RequestBody String data) {
		return admin.resetAuthKey(data);
	}
	
	@RequestMapping(value = "/updateEndpoint", method=RequestMethod.POST)
	@ResponseBody
	public String updateEndpoint(@RequestBody String data) {
		return admin.updateEndpoint(data);
	}
	
	
	
	@RequestMapping(value = "/", method=RequestMethod.GET)
	public String homePage(ModelMap model) {
		Date currExecDate = AppConstant.currentExecDate;
		Map<String, List<DownTimeUIData>> data = dataObj.getDataForHomePage(currExecDate);
		double timePercentage = dataObj.getTimePercentage(currExecDate);
		model.addAttribute("total", data.size() + disabledComponentNames.size());
		model.addAttribute("dateTime", currExecDate);
		model.addAttribute("data", data);
		model.addAttribute("disabled", disabledComponentNames);
		model.addAttribute("dateStr", Formatter.dateFormatter.format(currExecDate));
		model.addAttribute("timePercentage", timePercentage);
		return "home";
	}
}
