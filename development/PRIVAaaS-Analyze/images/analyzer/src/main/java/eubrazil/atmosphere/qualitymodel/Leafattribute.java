package eubrazil.atmosphere.qualitymodel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import eubrazil.atmosphere.commons.utils.ListUtils;
import eubrazil.atmosphere.entity.Data;
import eubrazil.atmosphere.exceptions.UndefinedMetricException;
import eubrazil.atmosphere.service.TrustworthinessService;

/**
 * The persistent class for the leafattribute database table.
 * @author JorgeLuiz
 */
@Entity(name="leafattribute")
@NamedQuery(name="leafattribute.findAll", query="SELECT l FROM leafattribute l")
public class Leafattribute extends Attribute implements Serializable {

	private static final long serialVersionUID = 5498567272022007160L;
	
	@Enumerated(EnumType.ORDINAL)
	private MetricNormalizationKind normalizationKind = MetricNormalizationKind.BENEFIT;

	private double normalizationMax = 1.0;

	private double normalizationMin = 0.0;

	private int numSamples = 1;

	@Enumerated(EnumType.ORDINAL)
	private MetricAggregationOperator operator = MetricAggregationOperator.AVERAGE;

	//bi-directional one-to-one association to Metric
	@OneToOne(mappedBy="attribute")
	private Metric metric;

	public Leafattribute() {
	}

	public Leafattribute(int attributeId, MetricNormalizationKind normalizationKind, double normalizationMax, double normalizationMin,
			int numSamples, MetricAggregationOperator operator) {
		super();
		setAttributeId(attributeId);
		this.normalizationKind = normalizationKind;
		this.normalizationMax = normalizationMax;
		this.normalizationMin = normalizationMin;
		this.numSamples = numSamples;
		this.operator = operator;
	}

	public HistoricalData calculate(ConfigurationProfile profile) throws UndefinedMetricException {

		if (profile == null || ListUtils.isEmpty(profile.getMetrics())) {
			throw new UndefinedMetricException("No defined metric for leaf attribute " + this.getName());
		}

		HistoricalData d = new HistoricalData();
		d.setInstant(new Timestamp(System.currentTimeMillis()));
		d.setAttribute(profile.getPreference(this).getAttribute());
		
		System.out.println("Attribute leaf: " + profile.getPreference(this).getAttribute());

		switch (operator) {
		case AVERAGE:
			d.setValue(calculateAverage(profile));
			break;
		case MINIMUM:
			d.setValue(calculateMinimum(profile));
			break;
		case MAXIMUM:
			d.setValue(calculateMaximum(profile));
			break;
		case SUM:
			d.setValue(calculateSum(profile));
			break;
		default:
			throw new UnsupportedOperationException();
		}

		TrustworthinessService privacyService = SpringContextBridge.services().getTrustworthinessService();
		privacyService.save(d);
		
		return d;
	}

	protected double calculateAverage(ConfigurationProfile profile) {
		double average = 0;
		double amount = 0;
		Iterator<Metric> iterMetric = profile.getMetrics().iterator();
		while (iterMetric.hasNext()) {
			Metric metric = iterMetric.next();
			
			if (metric.getAttribute().equals(this)) {
				// The user-defined metric concerns the same leaf attribute (metric definition)
				List<Data> data = metric.updateData();
				amount += (double) data.size();
				Iterator<Data> iterData = data.iterator();
				while (iterData.hasNext()) {
					Data measure = iterData.next();
					average += measure.getValue();
				}
			}
		}

		return average / amount;
	}

	protected double calculateMinimum(ConfigurationProfile profile) {
		double minimum = 0;
		Iterator<Metric> iterMetric = profile.getMetrics().iterator();
		while (iterMetric.hasNext()) {
			Metric metric = iterMetric.next();

			if (metric.getAttribute().equals(this)) {
				// The user-defined metric concerns the same leaf attribute (metric definition)
				List<Data> data = metric.updateData();
				Iterator<Data> iterData = data.iterator();
				while (iterData.hasNext()) {
					Data measure = iterData.next();
					if (measure.getValue() < minimum)
						minimum = measure.getValue();
				}
			}
		}

		return minimum;
	}

	protected double calculateMaximum(ConfigurationProfile profile) {
		double maximum = 0;
		Iterator<Metric> iterMetric = profile.getMetrics().iterator();
		while (iterMetric.hasNext()) {
			Metric metric = iterMetric.next();

			if (metric.getAttribute().equals(this)) {
				// The user-defined metric concerns the same leaf attribute (metric definition)
				List<Data> data = metric.updateData();
				Iterator<Data> iterData = data.iterator();
				while (iterData.hasNext()) {
					Data measure = iterData.next();
					if (measure.getValue() > maximum)
						maximum = measure.getValue();
				}
			}
		}

		return maximum;
	}

	protected double calculateSum(ConfigurationProfile profile) {
		double sum = 0;
		Iterator<Metric> iterMetric = profile.getMetrics().iterator();
		while (iterMetric.hasNext()) {
			Metric metric = iterMetric.next();

			if (metric.getAttribute().equals(this)) {
				// The user-defined metric concerns the same leaf attribute (metric definition)
				List<Data> data = metric.updateData();
				Iterator<Data> iterData = data.iterator();
				while (iterData.hasNext()) {
					Data measure = iterData.next();
					sum += measure.getValue();
				}
			}
		}

		return sum;
	}

	public MetricNormalizationKind getNormalizationKind() {
		return normalizationKind;
	}

	public void setNormalizationKind(MetricNormalizationKind normalizationKind) {
		this.normalizationKind = normalizationKind;
	}

	public double getNormalizationMax() {
		return this.normalizationMax;
	}

	public void setNormalizationMax(double normalizationMax) {
		this.normalizationMax = normalizationMax;
	}

	public double getNormalizationMin() {
		return this.normalizationMin;
	}

	public void setNormalizationMin(double normalizationMin) {
		this.normalizationMin = normalizationMin;
	}

	public int getNumSamples() {
		return this.numSamples;
	}

	public void setNumSamples(int numSamples) {
		this.numSamples = numSamples;
	}

	public MetricAggregationOperator getOperator() {
		return operator;
	}

	public void setOperator(MetricAggregationOperator operator) {
		this.operator = operator;
	}

	public Metric getMetric() {
		return this.metric;
	}

	public void setMetric(Metric metric) {
		this.metric = metric;
	}
	
	@Override
	public String toString() {
		return "Leafattribute [normalizationKind=" + normalizationKind + ", normalizationMax=" + normalizationMax
				+ ", normalizationMin=" + normalizationMin + ", numSamples=" + numSamples + ", operator=" + operator
				+ ", metric=" + metric + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((metric == null) ? 0 : metric.hashCode());
		result = prime * result + ((normalizationKind == null) ? 0 : normalizationKind.hashCode());
		long temp;
		temp = Double.doubleToLongBits(normalizationMax);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(normalizationMin);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + numSamples;
		result = prime * result + ((operator == null) ? 0 : operator.hashCode());
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
		Leafattribute other = (Leafattribute) obj;
		if (metric == null) {
			if (other.metric != null)
				return false;
		} else if (!metric.equals(other.metric))
			return false;
		if (normalizationKind != other.normalizationKind)
			return false;
		if (Double.doubleToLongBits(normalizationMax) != Double.doubleToLongBits(other.normalizationMax))
			return false;
		if (Double.doubleToLongBits(normalizationMin) != Double.doubleToLongBits(other.normalizationMin))
			return false;
		if (numSamples != other.numSamples)
			return false;
		if (operator != other.operator)
			return false;
		return true;
	}

}