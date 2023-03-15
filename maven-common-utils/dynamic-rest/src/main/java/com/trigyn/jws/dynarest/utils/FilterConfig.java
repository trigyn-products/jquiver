package com.trigyn.jws.dynarest.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.trigyn.jws.dynarest.cipher.utils.SchedulerRequestFilter;
import com.trigyn.jws.usermanagement.security.config.JwtRequestFilter;

@Configuration
@Import({ ApiClientFilter.class, SchedulerRequestFilter.class, JwtRequestFilter.class })
public class FilterConfig {

	@Autowired
	private ApiClientFilter			apiClientFilter			= null;

	@Autowired
	private SchedulerRequestFilter	schedulerRequestFilter	= null;

	@Autowired
	private JwtRequestFilter		jwtRequestFilter		= null;

	@Bean
	public FilterRegistrationBean<ApiClientFilter> loginRegistrationBean() {
		FilterRegistrationBean<ApiClientFilter> filterRegistrationBean = new FilterRegistrationBean<ApiClientFilter>();
		filterRegistrationBean.setFilter(apiClientFilter);
		filterRegistrationBean.setOrder(0);// O - Being the Highest precedence
		List<String> urlPatters = new ArrayList<>();
		urlPatters.add("/api/*");
		urlPatters.add("/japi/*");
		filterRegistrationBean.setUrlPatterns(urlPatters);
		return filterRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean<SchedulerRequestFilter> schedulerRequestFilterBean() {
		FilterRegistrationBean<SchedulerRequestFilter> filterRegistrationBean = new FilterRegistrationBean<SchedulerRequestFilter>();
		filterRegistrationBean.setFilter(schedulerRequestFilter);
		filterRegistrationBean.setOrder(1);
		List<String> urlPatters = new ArrayList<>();
		urlPatters.add("/sch-api/*");
		filterRegistrationBean.setUrlPatterns(urlPatters);
		return filterRegistrationBean;
	}
	
	@Bean
	public FilterRegistrationBean<JwtRequestFilter> jwtRequestFilterBean() {
		FilterRegistrationBean<JwtRequestFilter> filterRegistrationBean = new FilterRegistrationBean<JwtRequestFilter>();
		filterRegistrationBean.setFilter(jwtRequestFilter);
		filterRegistrationBean.setOrder(2);
		List<String> urlPatters = new ArrayList<>();
		urlPatters.add("/japi/*");
		filterRegistrationBean.setUrlPatterns(urlPatters);
		return filterRegistrationBean;
	}

}
