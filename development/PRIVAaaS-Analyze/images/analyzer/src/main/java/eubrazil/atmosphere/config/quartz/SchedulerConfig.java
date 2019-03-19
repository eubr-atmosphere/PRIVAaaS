package eubrazil.atmosphere.config.quartz;

import java.io.IOException;
import java.util.List;

import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Quartz scheduler settings
 * @author JorgeLuiz
 */
@Configuration
public class SchedulerConfig {

	private static final Logger LOG = LoggerFactory.getLogger(SchedulerConfig.class);

	@Autowired
	List<Trigger> triggers;

	@Bean
	public JobFactory jobFactory(ApplicationContext applicationContext) {
	    AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
	    jobFactory.setApplicationContext(applicationContext);
	    return jobFactory;
	}

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory)  throws IOException {
	    SchedulerFactoryBean factory = new SchedulerFactoryBean();
	    factory.setAutoStartup(true);
	    factory.setJobFactory(jobFactory);
	    //factory.setQuartzProperties(quartzProperties());
	    if (triggers != null && !triggers.isEmpty()) {
	         LOG.info("starting jobs... Total Triggers - " + triggers.size());
	        factory.setTriggers(triggers.toArray(new Trigger[triggers.size()]));
	    }

	    return factory;
	}

//	@Bean
//	public Properties quartzProperties() throws IOException {
//	    PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
//	    propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
//	    propertiesFactoryBean.afterPropertiesSet();
//	    return propertiesFactoryBean.getObject();
//	}

	public static CronTriggerFactoryBean createCronTrigger(JobDetail jobDetail, String cronExpression) {
	    CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
	    factoryBean.setJobDetail(jobDetail);
	    factoryBean.setCronExpression(cronExpression);
	    factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
	    return factoryBean;
	}

	@SuppressWarnings("rawtypes")
	public static JobDetailFactoryBean createJobDetail(Class jobClass) {
	    JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
	    factoryBean.setJobClass(jobClass);
	    factoryBean.setDurability(true);
	    return factoryBean;
	}
	
}
