package com.trigyn.jws.webstarter.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.webstarter.service.DynarestCrudService;

@RestController
@RequestMapping("/cf")
public class DynarestCrudController {
	
	private final static Logger logger 						= LogManager.getLogger(DynarestCrudController.class);

	@Autowired
	private PropertyMasterDAO propertyMasterDAO 			= null;
	
	@Autowired
	private DynarestCrudService dynarestCrudService 		= null;
	
	@Autowired
	private MenuService			menuService					= null;
	
    
    @GetMapping(value = "/dynl", produces = MediaType.TEXT_HTML_VALUE)
    public String loadDynarestListing() throws Exception {
        Map<String,Object>  modelMap = new HashMap<>();
		String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
		modelMap.put("environment", environment);
        return menuService.getTemplateWithSiteLayout("dynarest-details-listing", modelMap);
    }
    
	@PostMapping(value = "/ddr")
	public void downloadAllDynamicRestCodeToLocalDirectory(HttpSession session, HttpServletRequest request) throws Exception {
		 dynarestCrudService.downloadDynamicRestCode();
	}

	@PostMapping(value = "/udr")
	public void uploadAllDynamicRestCodesToDB(HttpSession session, HttpServletRequest request) throws Exception {
		 dynarestCrudService.uploadDynamicRestCode();
	}
	
	@PostMapping(value = "/sdq",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Boolean saveDynamicRestSaveQueries(@RequestBody MultiValueMap<String, String> formData) throws Exception{
		dynarestCrudService.deleteDAOQueries(formData);
		return dynarestCrudService.saveDAOQueries(formData);
	}
    
}
