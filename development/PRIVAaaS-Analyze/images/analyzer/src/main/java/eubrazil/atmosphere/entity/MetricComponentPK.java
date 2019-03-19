package eubrazil.atmosphere.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the MetricComponent database table.
 * 
 */
@Embeddable
public class MetricComponentPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="descriptionId", insertable=false, updatable=false)
	private int descriptionId;

	@Column(name="metricId", insertable=false, updatable=false)
	private int metricId;

	@Column(name="qualityModelId", insertable=false, updatable=false)
	private int qualityModelId;

	public MetricComponentPK() {
	}
	public int getDescriptionId() {
		return this.descriptionId;
	}
	public void setDescriptionId(int descriptionId) {
		this.descriptionId = descriptionId;
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

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MetricComponentPK)) {
			return false;
		}
		MetricComponentPK castOther = (MetricComponentPK)other;
		return 
			(this.descriptionId == castOther.descriptionId)
			&& (this.metricId == castOther.metricId)
			&& (this.qualityModelId == castOther.qualityModelId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.descriptionId;
		hash = hash * prime + this.metricId;
		hash = hash * prime + this.qualityModelId;
		
		return hash;
	}
}