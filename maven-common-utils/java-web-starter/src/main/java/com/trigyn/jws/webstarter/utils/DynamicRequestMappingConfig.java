package com.trigyn.jws.webstarter.utils;

import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.trigyn.jws.dynarest.controller.DynamicRestController;
import com.trigyn.jws.webstarter.controller.MasterModuleController;

import jakarta.annotation.PostConstruct;

@Configuration
public class DynamicRequestMappingConfig {

	@Autowired
	private JQuiverProperties				properties;

	@Autowired
	private MasterModuleController			masterModuleController;

	@Autowired
	private DynamicRestController			dynamicRestController;

	@Autowired
	private RequestMappingHandlerMapping	handlerMapping;

	@PostConstruct
	public void registerDynamicPath() throws NoSuchMethodException {
		String	dynamicPath	= properties.getViewPath() + "/**";
		Method	method		= MasterModuleController.class.getDeclaredMethod("loadModuleContent",
				HttpServletRequest.class, HttpServletResponse.class);

		handlerMapping.registerMapping(RequestMappingInfo.paths(dynamicPath).methods(RequestMethod.values()).build(),
				masterModuleController, method);

		String[]	dynamicApiPaths	= { properties.getApiPath() + "/**", "/japi/**" };
		Method		apimethod		= DynamicRestController.class.getDeclaredMethod("callDynamicEntity",
				HttpServletRequest.class, HttpServletResponse.class);

		handlerMapping.registerMapping(
				RequestMappingInfo.paths(dynamicApiPaths).methods(RequestMethod.values()).build(),
				dynamicRestController, apimethod);
	}

}
