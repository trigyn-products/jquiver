package com.trigyn.jws.webstarter.controller;

import java.util.HashMap;
import java.util.List;
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
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDaoDetail;
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
	public String loadDynarestListing(HttpServletRequest httpServletRequest) throws Exception {
		Map<String, Object> modelMap = new HashMap<>();
		String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
		modelMap.put("environment", environment);
		String uri = httpServletRequest.getRequestURI();
		String url = httpServletRequest.getRequestURL().toString();
		StringBuilder urlPrefix = new StringBuilder();
		url = url.replace(uri, "");
		urlPrefix.append(url).append("/api/");
		modelMap.put("urlPrefix", urlPrefix);
		return menuService.getTemplateWithSiteLayout("dynarest-details-listing", modelMap);
	}
    
	@PostMapping(value = "/ddr")
	public void downloadAllDynamicRestCodeToLocalDirectory(HttpSession session, HttpServletRequest request) throws Exception {
		 dynarestCrudService.downloadDynamicRestTemplate(null);
	}

	@PostMapping(value = "/udr")
	public void uploadAllDynamicRestCodesToDB(HttpSession session, HttpServletRequest request) throws Exception {
		 dynarestCrudService.uploadDynamicRestTemplate(null);
	}
	
	@PostMapping(value = "/sdq",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Integer saveDynamicRestSaveQueries(@RequestBody MultiValueMap<String, String> formData) throws Exception{
		dynarestCrudService.deleteDAOQueries(formData);
		List<JwsDynamicRestDaoDetail> dynamicRestDaoDetails = dynarestCrudService.saveDAOQueries(formData);
		return dynarestCrudService.downloadCodeToLocal(dynamicRestDaoDetails);
	}
	
	@PostMapping(value = "/ddrbi")
	public void downloadDynarestByIdToLocalDirectory(HttpSession session, HttpServletRequest request) throws Exception {
		Integer dynarestDetailsId = request.getParameter("dynarestDetailsId") == null ? null : Integer.parseInt(request.getParameter("dynarestDetailsId"));
		dynarestCrudService.downloadDynamicRestTemplate(dynarestDetailsId);
	}
	
	@PostMapping(value = "/udrbn")
	public void uploadDynarestByNameToDB(HttpSession session, HttpServletRequest request) throws Exception {
		String dynarestMethodName = request.getParameter("dynarestMethodName");
		dynarestCrudService.uploadDynamicRestTemplate(dynarestMethodName);
	}
    
}
