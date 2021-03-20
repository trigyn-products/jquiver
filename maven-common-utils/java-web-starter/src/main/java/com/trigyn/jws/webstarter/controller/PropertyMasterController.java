package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dbutils.spi.PropertyMasterDetails;
import com.trigyn.jws.templating.service.MenuService;

@RestController
@RequestMapping("/cf")
public class PropertyMasterController {

	private final static Logger		logger					= LogManager.getLogger(PropertyMasterController.class);

	@Autowired
	private MenuService				menuService				= null;

	@Autowired
	private PropertyMasterDetails	propertyMasterDetails	= null;

	@GetMapping(value = "/pml", produces = MediaType.TEXT_HTML_VALUE)
	public String propertyMasterListing(HttpServletResponse httpServletResponse) throws IOException {
		try {
			return menuService.getTemplateWithSiteLayout("property-master-listing", new HashMap<>());
		} catch (Exception a_exception) {
			logger.error("Error ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@GetMapping(value = "/upml", produces = MediaType.TEXT_HTML_VALUE)
	public void updatePropertyMasterDetails(HttpServletResponse httpServletResponse) throws IOException {
		try {
			propertyMasterDetails.resetPropertyMasterDetails();
		} catch (Exception a_exception) {
			logger.error("Error ", a_exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
		}
	}

	@PostMapping(value = "/rp", produces = MediaType.TEXT_HTML_VALUE)
	public void refreshPropertyMasterDetails(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException {
		try {
			String	ownerId			= httpServletRequest.getParameter("ownerId");
			String	ownerType		= httpServletRequest.getParameter("ownerType");
			String	propertyName	= httpServletRequest.getParameter("propertyName");
			String	propertyValue	= httpServletRequest.getParameter("propertyValue");
			propertyMasterDetails.setPropertyMasterDetails(ownerId, ownerType, propertyName, propertyValue);
		} catch (Exception a_exception) {
			logger.error("Error ", a_exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
		}
	}

}
