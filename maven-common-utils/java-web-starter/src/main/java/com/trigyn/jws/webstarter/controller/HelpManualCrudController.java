package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.templating.service.MenuService;

@RestController
@RequestMapping("/cf")
public class HelpManualCrudController {

	private final static Logger	logger		= LogManager.getLogger(HelpManualCrudController.class);

	@Autowired
	private MenuService			menuService	= null;

	@GetMapping(value = "/help", produces = MediaType.TEXT_HTML_VALUE)
	@PreAuthorize("hasPermission('module','Help Manual')")
	public String manualListingPage(HttpServletResponse httpServletResponse) throws IOException, CustomStopException {
		try {
			return menuService.getTemplateWithSiteLayout("manual-type-template", new HashMap<>());
		} catch (CustomStopException custStopException) {
			logger.error("Error occured while loading Manual Listing page.", custStopException);
			throw custStopException;
		} catch (Exception a_exception) {
			logger.error("Error while loading Manual Listing page. ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@GetMapping(value = "manual", produces = MediaType.TEXT_HTML_VALUE)
	public String showManual(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, CustomStopException {
		try {
			String				manualType		= httpServletRequest.getParameter("mt");
			Map<String, Object>	parameterMap	= new HashMap<>();
			parameterMap.put("mt", manualType);
			return menuService.getTemplateWithSiteLayout("manual-display", parameterMap);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured while loading Manual page.", custStopException);
			throw custStopException;
		} catch (Exception a_exception) {
			logger.error("Error while loading Manual page. ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}
}
