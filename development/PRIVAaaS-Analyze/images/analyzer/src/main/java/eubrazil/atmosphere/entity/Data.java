package eubrazil.atmosphere.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;


/**
 * The persistent class for the Data database table.
 */
@Entity(name="data")
@NamedQuery(name="data.findAll", query="SELECT d FROM data d")
public class Data implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DataPK id;

	private double value;

	//bi-directional many-to-one association to Metric
//	@ManyToOne
//	@JoinColumn(name="metricId", insertable = false, updatable = false)
//	private eubrazil.atmosphere.qualitymodel.Metric metric;
	
	public Data() {
	}

	public DataPK getId() {
		return this.id;
	}

	public void setId(DataPK id) {
		this.id = id;
	}

	public double getValue() {
		return this.value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Data [id=" + id + ", value=" + value + "]";
	}

}