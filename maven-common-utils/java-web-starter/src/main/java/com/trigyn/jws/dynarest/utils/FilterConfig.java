package com.trigyn.jws.dynarest.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import com.trigyn.jws.dynarest.cipher.utils.SchedulerRequestFilter;
import com.trigyn.jws.usermanagement.security.config.JwtRequestFilter;
import com.trigyn.jws.webstarter.utils.JQuiverProperties;

@Configuration
@Import({ ApiClientFilter.class, SchedulerRequestFilter.class, JwtRequestFilter.class, XSSFilter.class })
public class FilterConfig {

	@Autowired
	private XSSFilter xssFilter = null;

	@Autowired
	private CSPNonceFilter cspNonceFilter = null;

	@Autowired
	private ApiClientFilter apiClientFilter = null;

	@Autowired
	private SchedulerRequestFilter schedulerRequestFilter = null;

	@Autowired
	private JwtRequestFilter jwtRequestFilter = null;

	@Autowired
	private HeaderFilter viewFilter = null;

	@Autowired
	private JQuiverProperties jQuiverPropeties = null;

	@Bean
	public FilterRegistrationBean<XSSFilter> xSSfilterRegistrationBean() {
		FilterRegistrationBean<XSSFilter> filterRegistrationBean = new FilterRegistrationBean<XSSFilter>();
		filterRegistrationBean.setFilter(xssFilter);
		filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);// O - Being the Highest precedence
		List<String> urlPatters = new ArrayList<>();
		urlPatters.add("/*");
		filterRegistrationBean.setUrlPatterns(urlPatters);
		return filterRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean<CSPNonceFilter> cspNoncefilterRegistrationBean() {
		FilterRegistrationBean<CSPNonceFilter> filterRegistrationBean = new FilterRegistrationBean<CSPNonceFilter>();
		filterRegistrationBean.setFilter(cspNonceFilter);
		filterRegistrationBean.setOrder(1);// O - Being the Highest precedence
		List<String> urlPatters = new ArrayList<>();
		urlPatters.add(jQuiverPropeties.getViewPath() + "/*");
		filterRegistrationBean.setUrlPatterns(urlPatters);
		return filterRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean<ApiClientFilter> loginRegistrationBean() {
		FilterRegistrationBean<ApiClientFilter> filterRegistrationBean = new FilterRegistrationBean<ApiClientFilter>();
		filterRegistrationBean.setFilter(apiClientFilter);
		filterRegistrationBean.setOrder(2);
		List<String> urlPatters = new ArrayList<>();
		urlPatters.add(jQuiverPropeties.getApiPath() + "/*");
		urlPatters.add("/japi/*");
		filterRegistrationBean.setUrlPatterns(urlPatters);
		return filterRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean<SchedulerRequestFilter> schedulerRequestFilterBean() {
		FilterRegistrationBean<SchedulerRequestFilter> filterRegistrationBean = new FilterRegistrationBean<SchedulerRequestFilter>();
		filterRegistrationBean.setFilter(schedulerRequestFilter);
		filterRegistrationBean.setOrder(3);
		List<String> urlPatters = new ArrayList<>();
		urlPatters.add("/sch-api/*");
		filterRegistrationBean.setUrlPatterns(urlPatters);
		return filterRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean<JwtRequestFilter> jwtRequestFilterBean() {
		FilterRegistrationBean<JwtRequestFilter> filterRegistrationBean = new FilterRegistrationBean<JwtRequestFilter>();
		filterRegistrationBean.setFilter(jwtRequestFilter);
		filterRegistrationBean.setOrder(4);
		List<String> urlPatters = new ArrayList<>();
		urlPatters.add("/japi/*");
		filterRegistrationBean.setUrlPatterns(urlPatters);
		return filterRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean<HeaderFilter> viewRequestFilterBean() {
		FilterRegistrationBean<HeaderFilter> filterRegistrationBean = new FilterRegistrationBean<HeaderFilter>();
		filterRegistrationBean.setFilter(viewFilter);
		filterRegistrationBean.setOrder(5);
		List<String> urlPatters = new ArrayList<>();
		urlPatters.add(jQuiverPropeties.getViewPath() + "/*");
		urlPatters.add("/cf/*");
		filterRegistrationBean.setUrlPatterns(urlPatters);
		return filterRegistrationBean;
	}

}
