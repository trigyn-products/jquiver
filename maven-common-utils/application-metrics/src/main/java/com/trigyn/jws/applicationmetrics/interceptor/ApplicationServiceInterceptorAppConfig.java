
package com.trigyn.jws.applicationmetrics.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationServiceInterceptorAppConfig implements WebMvcConfigurer {

	@Autowired
	private ApplicationServiceInterceptor applicationServiceInterceptor = null;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(applicationServiceInterceptor).excludePathPatterns("/webjars/**");
	}

}
