package eubrazil.atmosphere.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the MetricData database table.
 * 
 */
@Entity(name="metricdata")
@NamedQuery(name="metricdata.findAll", query="SELECT m FROM metricdata m")
public class MetricData implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MetricDataPK id;

	private int resourceId;

	private double value;

	public MetricData() {
	}

	public MetricDataPK getId() {
		return this.id;
	}

	public void setId(MetricDataPK id) {
		this.id = id;
	}

	public int getResourceId() {
		return this.resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public double getValue() {
		return this.value;
	}

	public void setValue(double value) {
		this.value = value;
	}

}