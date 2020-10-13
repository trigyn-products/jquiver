package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.templating.service.MenuService;

@RestController
@RequestMapping("/cf")
public class FileController {

	private final static Logger logger = LogManager.getLogger(FileController.class);
	
	@Autowired
	private PropertyMasterService propertyMasterService	= null;

	@Autowired
	private MenuService menuService 					= null;
	
	@GetMapping(value = "/fucl", produces = MediaType.TEXT_HTML_VALUE)
	public String fileUploadConfigListing(HttpServletResponse httpServletResponse) throws IOException{
		try{
			Map<String,Object>  modelMap = new HashMap<>();
			String environment = propertyMasterService.findPropertyMasterValue("system", "system", "profile");
			modelMap.put("environment", environment);
			return menuService.getTemplateWithSiteLayout("file-upload-config-listing", modelMap);
		} catch (Exception exception) {
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
	}
}
