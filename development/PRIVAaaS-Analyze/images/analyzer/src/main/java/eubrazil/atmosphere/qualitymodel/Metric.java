package eubrazil.atmosphere.qualitymodel;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.domain.PageRequest;

import eubrazil.atmosphere.entity.Data;
import eubrazil.atmosphere.service.TrustworthinessService;

/**
 * The persistent class for the metric database table.
 * @author JorgeLuiz
 */
@Entity(name="metricqm")
@NamedQuery(name="metricqm.findAll", query="SELECT m FROM metricqm m")
public class Metric {
	
	@Id
	@GeneratedValue
	private int attributeId;

	private String descriptionName;

	private String probeName;

	private String resourceName;

	//bi-directional many-to-one association to Configurationprofile
	@ManyToOne
	@JoinColumn(name="configurationprofileId")
	private ConfigurationProfile configurationprofile;

	//bi-directional one-to-one association to Leafattribute
	@OneToOne
	@JoinColumn(name="attributeId")
	@LazyCollection(LazyCollectionOption.FALSE)
	private Leafattribute attribute;

	//bi-directional many-to-one association to Data
//	@OneToMany(mappedBy="metricId")
	@Transient
	private List<Data> data;
	
	public int getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(int attributeId) {
		this.attributeId = attributeId;
	}

	public String getDescriptionName() {
		return this.descriptionName;
	}

	public void setDescriptionName(String descriptionName) {
		this.descriptionName = descriptionName;
	}

	public String getProbeName() {
		return this.probeName;
	}

	public void setProbeName(String probeName) {
		this.probeName = probeName;
	}

	public String getResourceName() {
		return this.resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public ConfigurationProfile getConfigurationprofile() {
		return this.configurationprofile;
	}

	public void setConfigurationprofile(ConfigurationProfile configurationprofile) {
		this.configurationprofile = configurationprofile;
	}

	public Leafattribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Leafattribute attribute) {
		this.attribute = attribute;
	}

	public List<Data> getData() {
		if (this.data == null) {
			this.data = new ArrayList<Data>();
		}
		return this.data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public List<Data> updateData() {
		TrustworthinessService privacyService = SpringContextBridge.services().getTrustworthinessService();
		// TODO - Verify how to get and pass parameters for this method (for now it is hard coded)
		return privacyService.getLimitedDataListById(new Integer(8), new Integer(30), new Integer(8),
				new PageRequest(0, this.attribute.getNumSamples()));
	}
	
	@Override
	public String toString() {
		return "Metric [descriptionName=" + descriptionName + ", probeName=" + probeName + ", resourceName="
				+ resourceName + "]";
	}

}