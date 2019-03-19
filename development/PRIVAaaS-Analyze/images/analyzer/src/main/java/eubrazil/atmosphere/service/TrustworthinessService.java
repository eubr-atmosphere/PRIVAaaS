package eubrazil.atmosphere.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import eubrazil.atmosphere.entity.Data;
import eubrazil.atmosphere.qualitymodel.ConfigurationProfile;
import eubrazil.atmosphere.qualitymodel.HistoricalData;

/**
 * Trustworthiness services
 * @author JorgeLuiz
 */
public interface TrustworthinessService {

	public List<Data> getLimitedDataListById(Integer probeId, Integer descriptionId, Integer resourceId,
			Pageable numSamples);

	public void save(HistoricalData historicalData);

	public List<ConfigurationProfile> findPrivacyInstance();

}
