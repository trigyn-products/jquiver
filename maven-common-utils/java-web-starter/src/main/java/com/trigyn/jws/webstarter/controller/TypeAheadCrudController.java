package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.typeahead.model.AutocompleteVO;
import com.trigyn.jws.typeahead.service.TypeAheadService;

@RestController
@RequestMapping("/cf")
public class TypeAheadCrudController {
	
	private final static Logger logger 			= LogManager.getLogger(TypeAheadCrudController.class);

	@Autowired
	private TypeAheadService typeAheadService	= null;

	@Autowired
	private MenuService menuService 			= null;
	
	@GetMapping(value = "/adl", produces = MediaType.TEXT_HTML_VALUE)
    public String autocompleteListingsPage(HttpServletResponse httpServletResponse) throws IOException {
    	try {
			return menuService.getTemplateWithSiteLayout("autocomplete-listing",  new HashMap<>());
		} catch (Exception exception) {
			logger.error("Error ", exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
    }

	@GetMapping(value = "/da", produces = MediaType.TEXT_HTML_VALUE)
    public String demoAutocomplete() throws Exception {
        return menuService.getTemplateWithSiteLayout("autocomplete-demo",  new HashMap<>());
	}

	@GetMapping(value = "/aea", produces = MediaType.TEXT_HTML_VALUE)
    public String addEditAutocompleteDetails(HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException  {
		
		try {
			String autocompleteId 				= request.getParameter("acId");
			Map<String, Object> templateData 	= new HashMap<>();
			
			if (!StringUtils.isBlank(autocompleteId)) {
				AutocompleteVO autocompleteVO = typeAheadService.getAutocompleteDetailsId(autocompleteId);
				templateData.put("autocompleteVO", autocompleteVO);
			}
			return menuService.getTemplateWithSiteLayout("autocomplete-manage-details",  templateData);
		} catch (Exception exception) {
			logger.error("Error ", exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
    }
	
	@PostMapping(value = "/sacd",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
    public String saveAutocompleteDetails(
    		@RequestBody MultiValueMap<String, String> formDataMap) throws Exception {
		return typeAheadService.saveAutocompleteDetails(formDataMap);
    }
    
}
