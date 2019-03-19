package eubrazil.atmosphere.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ActionPlan database table.
 * 
 */
@Entity(name="actionplan")
@NamedQuery(name="actionplan.findAll", query="SELECT a FROM actionplan a")
public class ActionPlan implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int actionPlanId;

	private int actionId;

	private int executionOrder;

	private int planId;

	private int status;

	public ActionPlan() {
	}

	public int getActionPlanId() {
		return this.actionPlanId;
	}

	public void setActionPlanId(int actionPlanId) {
		this.actionPlanId = actionPlanId;
	}

	public int getActionId() {
		return this.actionId;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public int getExecutionOrder() {
		return this.executionOrder;
	}

	public void setExecutionOrder(int executionOrder) {
		this.executionOrder = executionOrder;
	}

	public int getPlanId() {
		return this.planId;
	}

	public void setPlanId(int planId) {
		this.planId = planId;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}