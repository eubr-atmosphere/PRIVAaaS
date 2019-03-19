package eubrazil.atmosphere.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the Configuration database table.
 * 
 */
@Entity(name="configuration")
@NamedQuery(name="configuration.findAll", query="SELECT c FROM configuration c")
public class Configuration implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int configurationId;

	private int actionId;

	private String domain;

	private String keyName;

	public Configuration() {
	}

	public int getConfigurationId() {
		return this.configurationId;
	}

	public void setConfigurationId(int configurationId) {
		this.configurationId = configurationId;
	}

	public int getActionId() {
		return this.actionId;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public String getDomain() {
		return this.domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getKeyName() {
		return this.keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

}