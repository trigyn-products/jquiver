/**
 * 
 */
package com.trigyn.jws.templating.controller;

import java.io.IOException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.templating.service.MenuService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/cf")
@PreAuthorize("hasPermission('module','REST API')")
public class ApiClientDetailsController {

	private final static Logger	logger		= LoggerFactory.getLogger(ApiClientDetailsController.class);

	@Autowired
	private MenuService			menuService		= null;
	
	@Autowired
	private FileUtilities 	    fileUtilities 	= null;

	@GetMapping(value = "/acd", produces = MediaType.TEXT_HTML_VALUE)
	public String apiClientDetailsTemplate(HttpServletResponse httpServletResponse) throws IOException, CustomStopException {
		try {
			return menuService.getTemplateWithSiteLayout("jq-api-client-details-template", new HashMap<>());
		} catch (CustomStopException custStopException) {
			logger.error("Error occured while loading API Client Details Listing page.", custStopException);
			throw custStopException;
		} catch (Exception a_exception) {
			logger.error("Error occured while loading API Client Details Listing Page.", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			fileUtilities.customSendError(httpServletResponse,HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

}
