package eubrazil.atmosphere.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;


/**
 * The persistent class for the Plan database table.
 * 
 */
@Entity(name="plan")
@NamedQuery(name="plan.findAll", query="SELECT p FROM plan p")
public class Plan implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int planId;

	private int metricId;

	private int qualityModelId;

	private int status;

	private Timestamp valueTime;

	public Plan() {
	}

	public int getPlanId() {
		return this.planId;
	}

	public void setPlanId(int planId) {
		this.planId = planId;
	}

	public int getMetricId() {
		return this.metricId;
	}

	public void setMetricId(int metricId) {
		this.metricId = metricId;
	}

	public int getQualityModelId() {
		return this.qualityModelId;
	}

	public void setQualityModelId(int qualityModelId) {
		this.qualityModelId = qualityModelId;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Timestamp getValueTime() {
		return this.valueTime;
	}

	public void setValueTime(Timestamp valueTime) {
		this.valueTime = valueTime;
	}

}