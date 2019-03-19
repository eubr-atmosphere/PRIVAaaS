package eubrazil.atmosphere.service;

/**
 * Used to inject dependency into a service in a class of type @Entity 
 * @author JorgeLuiz
 */
public interface SpringContextBridgedServices {

	TrustworthinessService getTrustworthinessService();
	
}
