package com.trigyn.jws.quartz.config;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzSchedulerConfig {

	@Autowired
	private final DataSource			quartzDataSource		= null;

	@Autowired
	private ApplicationContext			applicationContext		= null;

	@Autowired
	private TriggerListner				triggerListner			= null;

	@Autowired
	private JobsListener				jobsListener			= null;

	/**
	 * create scheduler
	 * 
	 * @throws IOException is thrown
	 * @return the scheduler bean
	 */
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setOverwriteExistingJobs(true);
		factory.setDataSource(quartzDataSource);
		factory.setQuartzProperties(quartzProperties());

		// Register listeners to get notification on Trigger misfire etc
		factory.setGlobalTriggerListeners(triggerListner);
		factory.setGlobalJobListeners(jobsListener);

		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		factory.setJobFactory(jobFactory);

		return factory;
	}

	/**
	 * Configure quartz using properties file
	 * 
	 * @throws IOException is thrown
	 * @return quartz properties
	 */
	@Bean
	public Properties quartzProperties() throws IOException {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocation(new ClassPathResource("/application.properties"));
		propertiesFactoryBean.afterPropertiesSet();
		return propertiesFactoryBean.getObject();
	}

}