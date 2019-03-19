package eubrazil.atmosphere.entity.dashboard;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name="properties")
public class Properties implements Serializable {

	private static final long serialVersionUID = 6028870066004550610L;

	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	private float relevance;
	private float threshold;
	private float periodicity;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getRelevance() {
		return relevance;
	}
	public void setRelevance(float relevance) {
		this.relevance = relevance;
	}
	public float getThreshold() {
		return threshold;
	}
	public void setThreshold(float threshold) {
		this.threshold = threshold;
	}
	public float getPeriodicity() {
		return periodicity;
	}
	public void setPeriodicity(float periodicity) {
		this.periodicity = periodicity;
	}
	
}
