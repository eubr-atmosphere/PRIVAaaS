package eubrazil.atmosphere.qualitymodel;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import eubrazil.atmosphere.service.SpringContextBridgedServices;
import eubrazil.atmosphere.service.TrustworthinessService;

/**
 * Autowire the TrustworthinessService
 * @author JorgeLuiz
 */
@Component
public class SpringContextBridge implements SpringContextBridgedServices, ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Autowired
	private TrustworthinessService trustworthinessService; // Autowire the TrustworthinessService

	@SuppressWarnings("static-access")
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public static SpringContextBridgedServices services() {
		return applicationContext.getBean(SpringContextBridgedServices.class);
	}

	@Override
	public TrustworthinessService getTrustworthinessService() {
		return trustworthinessService;
	}

}
