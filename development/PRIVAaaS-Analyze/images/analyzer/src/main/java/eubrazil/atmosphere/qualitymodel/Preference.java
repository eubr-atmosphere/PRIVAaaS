package eubrazil.atmosphere.qualitymodel;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;


/**
 * The persistent class for the preference database table.
 * @author JorgeLuiz
 */
@Entity(name="preference")
@NamedQuery(name="preference.findAll", query="SELECT p FROM preference p")
public class Preference implements Serializable {

	private static final long serialVersionUID = 4897045709573210431L;

	@Id
	@GeneratedValue
	private int attributeId;

	private double threshold;

	private double weight;

	//bi-directional many-to-one association to Configurationprofile
	@ManyToOne
	@JoinColumn(name="configurationprofileId")
	private ConfigurationProfile configurationprofile;

	//bi-directional one-to-one association to Attribute
	@OneToOne (cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="attributeId")
	private Attribute attribute;

	public Preference() {
	}

	public Preference(int attributeId, double threshold, double weight) {
		this.attributeId = attributeId;
		this.threshold = threshold;
		this.weight = weight;
	}

	public double getThreshold() {
		return this.threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public double getWeight() {
		return this.weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public ConfigurationProfile getConfigurationprofile() {
		return this.configurationprofile;
	}

	public void setConfigurationprofile(ConfigurationProfile configurationprofile) {
		this.configurationprofile = configurationprofile;
	}

	public Attribute getAttribute() {
		return this.attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	@Override
	public String toString() {
		return "Preference [threshold=" + threshold + ", weight=" + weight + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attribute == null) ? 0 : attribute.getAttributeId());
		long temp;
		temp = Double.doubleToLongBits(threshold);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(weight);
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
		Preference other = (Preference) obj;
		if (attribute == null) {
			if (other.attribute != null)
				return false;
		} else if (!attribute.equals(other.attribute))
			return false;
		if (Double.doubleToLongBits(threshold) != Double.doubleToLongBits(other.threshold))
			return false;
		if (Double.doubleToLongBits(weight) != Double.doubleToLongBits(other.weight))
			return false;
		return true;
	}

}