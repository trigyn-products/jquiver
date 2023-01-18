package com.trigyn.jws.dynarest.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

@Configuration
@Import({ApiClientFilter.class})
public class FilterConfig {

	@Autowired
	private ApiClientFilter	apiClientFilter	= null;

	  @Bean
	  public FilterRegistrationBean<ApiClientFilter> loginRegistrationBean() {
	    FilterRegistrationBean<ApiClientFilter> filterRegistrationBean = new FilterRegistrationBean<ApiClientFilter>();
	    filterRegistrationBean.setFilter(apiClientFilter);
	    filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
	    List<String> urlPatters = new ArrayList<>();
	    urlPatters.add("/api/*");
	    urlPatters.add("/japi/*");
	    filterRegistrationBean.setUrlPatterns(urlPatters);
	    return filterRegistrationBean;
	  }

}
