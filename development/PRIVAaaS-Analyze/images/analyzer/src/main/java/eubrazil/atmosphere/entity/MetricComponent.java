package eubrazil.atmosphere.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the MetricComponent database table.
 * 
 */
@Entity(name="metriccomponent")
@NamedQuery(name="metriccomponent.findAll", query="SELECT m FROM metriccomponent m")
public class MetricComponent implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MetricComponentPK id;

	private int attributeAggregationOperator;

	private int numSamples;

	private double weight;

	public MetricComponent() {
	}

	public MetricComponentPK getId() {
		return this.id;
	}

	public void setId(MetricComponentPK id) {
		this.id = id;
	}

	public int getAttributeAggregationOperator() {
		return this.attributeAggregationOperator;
	}

	public void setAttributeAggregationOperator(int attributeAggregationOperator) {
		this.attributeAggregationOperator = attributeAggregationOperator;
	}

	public int getNumSamples() {
		return this.numSamples;
	}

	public void setNumSamples(int numSamples) {
		this.numSamples = numSamples;
	}

	public double getWeight() {
		return this.weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

}