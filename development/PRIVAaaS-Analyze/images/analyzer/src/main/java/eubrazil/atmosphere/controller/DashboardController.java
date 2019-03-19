package eubrazil.atmosphere.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import eubrazil.atmosphere.entity.dashboard.Properties;
import eubrazil.atmosphere.entity.dashboard.PropertiesJsonMap;
import eubrazil.atmosphere.service.impl.DashboardService;

/**
* Dashboard Controller
* @author JorgeLuiz
* @version 1.0
 */
@RestController
@RequestMapping("/dash")
@Controller
public class DashboardController {

public static final Logger LOGGER = LoggerFactory.getLogger(DashboardController.class); 
	
	@Autowired
	private DashboardService dashBoardService;
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public void save(@RequestBody PropertiesJsonMap prop) {
		Properties newProp = new Properties();
		newProp.setName(prop.getPropRelevance()[0].getName());
		newProp.setRelevance(prop.getPropRelevance()[0].getData());
		newProp.setThreshold(prop.getThresholds()[0].getData());
		newProp.setPeriodicity(prop.getPeriodicity()[0].getData());
		dashBoardService.save(newProp);
		newProp.toString();
	}
	
	// GET properties
}
