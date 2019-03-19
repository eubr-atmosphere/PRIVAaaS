package eubrazil.atmosphere.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eubrazil.atmosphere.entity.dashboard.Properties;
import eubrazil.atmosphere.repository.DashboardRepository;

/**
 * Dashboard services
 * @author JorgeLuiz
 */
@Service
public class DashboardService {

	@Autowired
	private DashboardRepository repository;
	
	public void save(Properties prop) {
		repository.save(prop);
	}
	
}
