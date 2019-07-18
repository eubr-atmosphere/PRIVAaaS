package eubr.atmosphere.tma.actuator;

import org.springframework.stereotype.Component;

@Component("actuator")
public class ConcreteActuator implements Actuator {

	@Override
	public void act(ActuatorPayload actuatorPayload) {
		// This is the actuation itself.
		// TODO Decide what action to perform, and perform it
		String action = actuatorPayload.getAction();
		switch (action) {
		case "action1":
			System.out.println("Action 1" + action);
			break;
			
		case "action2":
			System.out.println("Action 2" + action);
			break;

		default:
			System.out.println("Not defined action: " + action);
			break;
		}

	}

}
