package app.trigyn.common.dbutils.configurations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.trigyn.common.dbutils.spi.DefaultUserDetailsServiceImpl;
import app.trigyn.common.dbutils.spi.IUserDetailsService;

@Configuration
public class SpiServiceConfiguration {
	
	private final static Logger logger = LogManager.getLogger(SpiServiceConfiguration.class);

	@ConditionalOnMissingBean
	@Bean
	public IUserDetailsService userDetailsServiceImpl() {
		logger.warn("Found no implementation for UserSpiService, wiring default implementation");
		return new DefaultUserDetailsServiceImpl();
	}
	
	@Bean
	public FilterRegistrationBean<JwsAuthenticationFilter> loggingFilter(){
	    FilterRegistrationBean<JwsAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
	    registrationBean.setFilter(new JwsAuthenticationFilter());
	    registrationBean.addUrlPatterns("/cf/*");
	    return registrationBean;    
	}
}
