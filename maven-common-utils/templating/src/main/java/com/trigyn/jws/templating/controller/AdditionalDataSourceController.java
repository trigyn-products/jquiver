package com.trigyn.jws.templating.controller;

import java.io.IOException;
import java.util.HashMap;

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

import com.trigyn.jws.templating.service.MenuService;

@RestController
@RequestMapping("/cf")
@PreAuthorize("hasPermission('module','REST API')")
public class AdditionalDataSourceController {

	private final static Logger	logger		= LogManager.getLogger(AdditionalDataSourceController.class);

	@Autowired
	private MenuService			menuService	= null;

	@GetMapping(value = "/ad", produces = MediaType.TEXT_HTML_VALUE)
	public String additionalDataSource(HttpServletResponse httpServletResponse) throws IOException {
		try {
			return menuService.getTemplateWithSiteLayout("jq-additional-datasource-template", new HashMap<>());
		} catch (Exception a_exception) {
			logger.error("Error occured while loading Additional DataSource Listing Page.", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

}
