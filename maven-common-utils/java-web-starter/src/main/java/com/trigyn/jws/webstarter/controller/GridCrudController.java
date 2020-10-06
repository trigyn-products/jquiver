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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.templating.service.MenuService;

@RestController
@RequestMapping("/cf")
public class GridCrudController {
	
	private final static Logger logger 						= LogManager.getLogger(GridCrudController.class);

	@Autowired
	private MenuService 	menuService						= null;
    
    @GetMapping(value = "/gd", produces = MediaType.TEXT_HTML_VALUE)
    public String gridUtilsPage(HttpServletResponse httpServletResponse) throws IOException  {
    	try {
    		return menuService.getTemplateWithSiteLayout("grid-listing", new HashMap<>());
    	}catch (Exception exception) {
    		logger.error("Error while loding gridUtils page ", exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
    }
}
