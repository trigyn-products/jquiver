package com.trigyn.jws.webstarter.controller;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.menu.service.MenuService;

@RestController
@RequestMapping("/cf")
public class GridCrudController {
	
	private final static Logger logger 						= LogManager.getLogger(GridCrudController.class);

	@Autowired
	private MenuService 	menuService			= null;
    
    @GetMapping(value = "/gd", produces = MediaType.TEXT_HTML_VALUE)
    public String gridUtilsPage() throws Exception {
        return menuService.getTemplateWithSiteLayout("grid-listing", new HashMap<>());    
    }
}
