package com.trigyn.jws.dbutils.configurations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.trigyn.jws.dbutils.spi.DefaultUserDetailsServiceImpl;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;

@Configuration
public class SpiServiceConfiguration {

	private final static Logger logger = LogManager.getLogger(SpiServiceConfiguration.class);

	@ConditionalOnMissingBean
	@Bean
	public IUserDetailsService userDetailsServiceImpl() {
		logger.warn("Found no implementation for UserSpiService, wiring default implementation");
		return new DefaultUserDetailsServiceImpl();
	}

}
