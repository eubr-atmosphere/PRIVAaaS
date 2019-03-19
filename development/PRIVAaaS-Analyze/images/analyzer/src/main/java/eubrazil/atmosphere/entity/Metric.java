package eubrazil.atmosphere.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the Metric database table.
 * 
 */
@Entity(name="metric")
@NamedQuery(name="metric.findAll", query="SELECT m FROM metric m")
public class Metric implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int metricId;

	private int blockLevel;

	private int metricAggregationOperator;

	private String metricName;

	private String normalizationKind;

	private int qualityModelId;

	private double threshold;

	public Metric() {
	}

	public int getMetricId() {
		return this.metricId;
	}

	public void setMetricId(int metricId) {
		this.metricId = metricId;
	}

	public int getBlockLevel() {
		return this.blockLevel;
	}

	public void setBlockLevel(int blockLevel) {
		this.blockLevel = blockLevel;
	}

	public int getMetricAggregationOperator() {
		return this.metricAggregationOperator;
	}

	public void setMetricAggregationOperator(int metricAggregationOperator) {
		this.metricAggregationOperator = metricAggregationOperator;
	}

	public String getMetricName() {
		return this.metricName;
	}

	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}

	public String getNormalizationKind() {
		return this.normalizationKind;
	}

	public void setNormalizationKind(String normalizationKind) {
		this.normalizationKind = normalizationKind;
	}

	public int getQualityModelId() {
		return this.qualityModelId;
	}

	public void setQualityModelId(int qualityModelId) {
		this.qualityModelId = qualityModelId;
	}

	public double getThreshold() {
		return this.threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

}