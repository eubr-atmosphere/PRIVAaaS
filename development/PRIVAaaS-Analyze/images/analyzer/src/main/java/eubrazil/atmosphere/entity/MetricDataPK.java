package eubrazil.atmosphere.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The primary key class for the MetricData database table.
 * 
 */
@Embeddable
public class MetricDataPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="metricId", insertable=false, updatable=false)
	private int metricId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="valueTime")
	private java.util.Date valueTime;

	@Column(name="qualityModelId", insertable=false, updatable=false)
	private int qualityModelId;

	public MetricDataPK() {
	}
	public int getMetricId() {
		return this.metricId;
	}
	public void setMetricId(int metricId) {
		this.metricId = metricId;
	}
	public java.util.Date getValueTime() {
		return this.valueTime;
	}
	public void setValueTime(java.util.Date valueTime) {
		this.valueTime = valueTime;
	}
	public int getQualityModelId() {
		return this.qualityModelId;
	}
	public void setQualityModelId(int qualityModelId) {
		this.qualityModelId = qualityModelId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MetricDataPK)) {
			return false;
		}
		MetricDataPK castOther = (MetricDataPK)other;
		return 
			(this.metricId == castOther.metricId)
			&& this.valueTime.equals(castOther.valueTime)
			&& (this.qualityModelId == castOther.qualityModelId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.metricId;
		hash = hash * prime + this.valueTime.hashCode();
		hash = hash * prime + this.qualityModelId;
		
		return hash;
	}
}