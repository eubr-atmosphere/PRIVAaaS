package eubrazil.atmosphere.entity.dashboard;

import java.util.Arrays;

public class PropertiesJsonMap {
	
	private PropRelevance[] propRelevance;
	private Thresholds[] thresholds;
	private Periodicity[] periodicity;
	
	public PropRelevance[] getPropRelevance() {
		return propRelevance;
	}
	public void setPropRelevance(PropRelevance[] propRelevance) {
		this.propRelevance = propRelevance;
	}
	public Thresholds[] getThresholds() {
		return thresholds;
	}
	public void setThresholds(Thresholds[] thresholds) {
		this.thresholds = thresholds;
	}
	public Periodicity[] getPeriodicity() {
		return periodicity;
	}
	public void setPeriodicity(Periodicity[] periodicity) {
		this.periodicity = periodicity;
	}
	@Override
	public String toString() {
		return "PropertiesJsonMap [propRelevance=" + Arrays.toString(propRelevance) + ", thresholds="
				+ Arrays.toString(thresholds) + ", periodicity=" + Arrays.toString(periodicity) + "]";
	}
	
}
