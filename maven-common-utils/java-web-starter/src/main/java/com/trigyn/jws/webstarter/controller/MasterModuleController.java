package com.trigyn.jws.webstarter.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.usermanagement.security.config.Authorized;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.webstarter.service.MasterModuleService;

@RestController
@RequestMapping(value = "/view/**", produces = MediaType.TEXT_HTML_VALUE)
public class MasterModuleController {

	private final static Logger	logger				= LogManager.getLogger(MasterCreatorController.class);

	@Autowired
	private MasterModuleService	masterModuleService	= null;

	@RequestMapping()
	@Authorized(moduleName = Constants.SITELAYOUT)
	public String loadModuleContent(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws IOException {
		try {
			String moduleUrl = httpServletRequest.getRequestURI()
					.substring(httpServletRequest.getContextPath().length());
			moduleUrl = moduleUrl.replaceFirst("/view/", "");
			if (moduleUrl.indexOf("/") != -1) {
				moduleUrl = moduleUrl.substring(0, moduleUrl.indexOf("/"));
			}
			return masterModuleService.loadTemplate(httpServletRequest, moduleUrl, httpServletResponse);
		} catch (Exception a_exception) {
			logger.error("Error ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
		}
		return null;
	}

}
