package com.trigyn.jws.webstarter.controller;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.templating.service.MenuService;

@RestController
@RequestMapping("/cf")
public class PropertyMasterController {
	
	private final static Logger logger	 				= LogManager.getLogger(PropertyMasterController.class);

	@Autowired
	private MenuService menuService		= null;
    
    @GetMapping(value = "/pml", produces = MediaType.TEXT_HTML_VALUE)
    public String propertyMasterListing() {
        try {
			return menuService.getTemplateWithSiteLayout("property-master-listing", new HashMap<>());
		} catch (Exception exception) {
			throw new RuntimeException(exception.getMessage());
		}
    }
    
    
}
