package eubrazil.atmosphere.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the ConfigurationData database table.
 * 
 */
@Embeddable
public class ConfigurationDataPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="actionPlanId", insertable=false, updatable=false)
	private int actionPlanId;

	@Column(name="configurationId", insertable=false, updatable=false)
	private int configurationId;

	public ConfigurationDataPK() {
	}
	public int getActionPlanId() {
		return this.actionPlanId;
	}
	public void setActionPlanId(int actionPlanId) {
		this.actionPlanId = actionPlanId;
	}
	public int getConfigurationId() {
		return this.configurationId;
	}
	public void setConfigurationId(int configurationId) {
		this.configurationId = configurationId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ConfigurationDataPK)) {
			return false;
		}
		ConfigurationDataPK castOther = (ConfigurationDataPK)other;
		return 
			(this.actionPlanId == castOther.actionPlanId)
			&& (this.configurationId == castOther.configurationId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.actionPlanId;
		hash = hash * prime + this.configurationId;
		
		return hash;
	}
}