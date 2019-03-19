package eubrazil.atmosphere.qualitymodel;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

/**
 * The persistent class for the historicaldata database table.
 * @author JorgeLuiz
 */
@Entity(name="historicaldata")
@NamedQuery(name="historicaldata.findAll", query="SELECT h FROM historicaldata h")
public class HistoricalData implements Serializable {

	private static final long serialVersionUID = 467016496297144958L;
	
	@EmbeddedId
	private HistoricalDataPK id;

	private double value;

	//bi-directional many-to-one association to Attribute
	@ManyToOne
	@JoinColumn(name="attributeId")
	private Attribute attribute;

	public HistoricalData() {
	}

	public HistoricalData(Timestamp instant, double value) {
		super();
		this.setInstant(instant);
		this.value = value;
	}

	public HistoricalDataPK getId() {
		return id;
	}

	public void setId(HistoricalDataPK id) {
		this.id = id;
	}

	public double getValue() {
		return this.value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public Attribute getAttribute() {
		return this.attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	public void setInstant(java.util.Date instant) {
		if (this.id == null) {
			this.id = new HistoricalDataPK();
		}
		this.id.setInstant(instant);
	}
	
	public void setHistoricalDataId(int historicalDataId) {
		if (this.id == null) {
			this.id = new HistoricalDataPK();
		}
		this.id.setHistoricalDataId(historicalDataId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attribute == null) ? 0 : attribute.getAttributeId());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		HistoricalData other = (HistoricalData) obj;
		if (attribute == null) {
			if (other.attribute != null)
				return false;
		} else if (!attribute.equals(other.attribute))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "HistoricalData [value=" + value + "]";
	}
	
}