package eubrazil.atmosphere.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the MetricComposition database table.
 * 
 */
@Embeddable
public class MetricCompositionPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="parentMetric", insertable=false, updatable=false)
	private int parentMetric;

	@Column(name="childMetric", insertable=false, updatable=false)
	private int childMetric;

	public MetricCompositionPK() {
	}
	public int getParentMetric() {
		return this.parentMetric;
	}
	public void setParentMetric(int parentMetric) {
		this.parentMetric = parentMetric;
	}
	public int getChildMetric() {
		return this.childMetric;
	}
	public void setChildMetric(int childMetric) {
		this.childMetric = childMetric;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MetricCompositionPK)) {
			return false;
		}
		MetricCompositionPK castOther = (MetricCompositionPK)other;
		return 
			(this.parentMetric == castOther.parentMetric)
			&& (this.childMetric == castOther.childMetric);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.parentMetric;
		hash = hash * prime + this.childMetric;
		
		return hash;
	}
}