package eubrazil.atmosphere.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import eubrazil.atmosphere.entity.Data;
import eubrazil.atmosphere.qualitymodel.ConfigurationProfile;
import eubrazil.atmosphere.qualitymodel.HistoricalData;
import eubrazil.atmosphere.repository.ConfigurationProfileRepository;
import eubrazil.atmosphere.repository.DataRepository;
import eubrazil.atmosphere.repository.HistoricalDataRepository;
import eubrazil.atmosphere.service.TrustworthinessService;

/**
 * Implementation of trustworthiness services
 * @author JorgeLuiz
 */
@Service
public class TrustworthinessServiceImpl implements TrustworthinessService {

	@Autowired
	private DataRepository dataRepository;
	
	@Autowired
	private HistoricalDataRepository historicalDataRepository;
	
	@Autowired
	private ConfigurationProfileRepository configurationProfileRepository;
	
	@Override
	public List<Data> getLimitedDataListById(Integer probeId, Integer descriptionId, Integer resourceId,
			Pageable numSamples) {
		return dataRepository.getLimitedDataListById(probeId, descriptionId, resourceId, numSamples);
	}
	
	@Override
	public void save(HistoricalData historicalData) {
		historicalDataRepository.save(historicalData);
	}
	
	@Override
	public List<ConfigurationProfile> findPrivacyInstance() {
		return configurationProfileRepository.findPrivacyInstance();
	}

}
