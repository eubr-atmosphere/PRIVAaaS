package eubrazil.atmosphere.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ConfigurationData database table.
 * 
 */
@Entity(name="configurationdata")
@NamedQuery(name="configurationdata.findAll", query="SELECT c FROM configurationdata c")
public class ConfigurationData implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ConfigurationDataPK id;

	private String value;

	public ConfigurationData() {
	}

	public ConfigurationDataPK getId() {
		return this.id;
	}

	public void setId(ConfigurationDataPK id) {
		this.id = id;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}