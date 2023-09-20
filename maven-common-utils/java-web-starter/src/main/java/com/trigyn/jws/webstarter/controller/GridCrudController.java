package com.trigyn.jws.webstarter.controller;

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

import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.templating.service.MenuService;

@RestController
@RequestMapping("/cf")
@PreAuthorize("hasPermission('module','Grid Utils')")
public class GridCrudController {

	private final static Logger	logger		= LogManager.getLogger(GridCrudController.class);

	@Autowired
	private MenuService			menuService	= null;

	@GetMapping(value = "/gd", produces = MediaType.TEXT_HTML_VALUE)
	public String gridUtilsPage(HttpServletResponse httpServletResponse) throws IOException, CustomStopException {
		try {
			return menuService.getTemplateWithSiteLayout("grid-listing", new HashMap<>());
		} catch (CustomStopException custStopException) {
			logger.error("Error occured while loading Grid Listing page.", custStopException);
			throw custStopException;
		} catch (Exception a_exception) {
			logger.error("Error while loading Grid Listing page ", a_exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}
}
