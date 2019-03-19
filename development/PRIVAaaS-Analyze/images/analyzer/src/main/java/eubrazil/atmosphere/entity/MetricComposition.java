package eubrazil.atmosphere.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the MetricComposition database table.
 * 
 */
@Entity(name="metriccomposition")
@NamedQuery(name="metriccomposition.findAll", query="SELECT m FROM metriccomposition m")
public class MetricComposition implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MetricCompositionPK id;

	public MetricComposition() {
	}

	public MetricCompositionPK getId() {
		return this.id;
	}

	public void setId(MetricCompositionPK id) {
		this.id = id;
	}

}