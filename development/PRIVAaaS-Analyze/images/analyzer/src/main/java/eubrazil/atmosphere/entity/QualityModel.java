package eubrazil.atmosphere.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the QualityModel database table.
 * 
 */
@Entity(name="qualitymodel")
@NamedQuery(name="qualitymodel.findAll", query="SELECT q FROM qualitymodel q")
public class QualityModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int qualityModelId;

	private int modelDescriptionReference;

	private String modelName;

	public QualityModel() {
	}

	public int getQualityModelId() {
		return this.qualityModelId;
	}

	public void setQualityModelId(int qualityModelId) {
		this.qualityModelId = qualityModelId;
	}

	public int getModelDescriptionReference() {
		return this.modelDescriptionReference;
	}

	public void setModelDescriptionReference(int modelDescriptionReference) {
		this.modelDescriptionReference = modelDescriptionReference;
	}

	public String getModelName() {
		return this.modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

}