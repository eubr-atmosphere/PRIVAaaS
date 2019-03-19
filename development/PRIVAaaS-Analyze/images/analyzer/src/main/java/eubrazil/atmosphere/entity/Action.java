package eubrazil.atmosphere.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;


/**
 * The persistent class for the Action database table.
 * 
 */
@Entity(name="action")
@NamedQuery(name="action.findAll", query="SELECT a FROM action a")
public class Action implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int actionId;

	private String actionName;

	private int actuatorId;

	private int resourceId;

	public Action() {
	}

	public int getActionId() {
		return this.actionId;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public String getActionName() {
		return this.actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public int getActuatorId() {
		return this.actuatorId;
	}

	public void setActuatorId(int actuatorId) {
		this.actuatorId = actuatorId;
	}

	public int getResourceId() {
		return this.resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

}