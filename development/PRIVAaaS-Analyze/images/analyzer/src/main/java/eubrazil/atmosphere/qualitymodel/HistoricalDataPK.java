package eubrazil.atmosphere.qualitymodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class HistoricalDataPK implements Serializable {

	private static final long serialVersionUID = -5121472907336677308L;

	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="historicalDataId")
	private int historicalDataId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="instant")
	private java.util.Date instant;
	
	public HistoricalDataPK() {
	}

	public int getHistoricalDataId() {
		return historicalDataId;
	}

	public void setHistoricalDataId(int historicalDataId) {
		this.historicalDataId = historicalDataId;
	}

	public java.util.Date getInstant() {
		return instant;
	}

	public void setInstant(java.util.Date instant) {
		this.instant = instant;
	}
	
	@Override
	public String toString() {
		return "HistoricalDataPK [instant=" + instant + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + historicalDataId;
		result = prime * result + ((instant == null) ? 0 : instant.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HistoricalDataPK other = (HistoricalDataPK) obj;
		if (historicalDataId != other.historicalDataId)
			return false;
		if (instant == null) {
			if (other.instant != null)
				return false;
		} else if (!instant.equals(other.instant))
			return false;
		return true;
	}
	
}
