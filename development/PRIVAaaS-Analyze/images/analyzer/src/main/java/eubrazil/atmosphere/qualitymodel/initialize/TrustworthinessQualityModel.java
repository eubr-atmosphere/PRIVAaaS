package eubrazil.atmosphere.qualitymodel.initialize;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eubrazil.atmosphere.commons.utils.ListUtils;
import eubrazil.atmosphere.qualitymodel.ConfigurationProfile;
import eubrazil.atmosphere.service.TrustworthinessService;

/**
 * Initializing Trustworthiness Quality Model on application startup
 * @author JorgeLuiz
 */
@Component
public class TrustworthinessQualityModel {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	private static TrustworthinessQualityModel instance = new TrustworthinessQualityModel();

	private ConfigurationProfile configurationActor = null;
	
	@Autowired
	private TrustworthinessService trustworthinessService;
	
	private TrustworthinessQualityModel() {
	}

	@PostConstruct
	public void init() {
		LOGGER.info("-------------------------------------------------------");
		LOGGER.info("---------Initializing trustworthiness instance---------");
		LOGGER.info("-------------------------------------------------------");
		
		//TODO Load Config profile active = true;
		List<ConfigurationProfile> configProfileList = trustworthinessService.findPrivacyInstance();
		
		if (ListUtils.isEmpty(configProfileList)) {
			LOGGER.error("Quality Model for privacy not defined in the database.");
			return;
		}
		
		ConfigurationProfile configurationActorBD =  ListUtils.getFirstElement(configProfileList);
		LOGGER.info("TrustworthinessQualityModel (init method) - ConfigurationProfile: " + configurationActorBD);
		
		
		instance.setConfigurationActor(configurationActorBD);

		LOGGER.info("-------------------------------------------------------");
		LOGGER.info("---------Trustworthiness instance inicialized----------");
		LOGGER.info("-------------------------------------------------------");
	}

	public static TrustworthinessQualityModel getPrivacyInstance() {
		return instance;
	}

	public ConfigurationProfile getConfigurationActor() {
		return configurationActor;
	}

	public void setConfigurationActor(ConfigurationProfile configurationActor) {
		this.configurationActor = configurationActor;
	}

}
