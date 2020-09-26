package com.trigyn.jws.webstarter.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.menu.service.MenuService;
import com.trigyn.jws.typeahead.model.AutocompleteVO;
import com.trigyn.jws.typeahead.service.TypeAheadService;

@RestController
@RequestMapping("/cf")
public class TypeAheadCrudController {
	
	private final static Logger logger = LogManager.getLogger(TypeAheadCrudController.class);

	@Autowired
	private TypeAheadService typeAheadService = null;

	@Autowired
	private MenuService menuService = null;
	
	@GetMapping(value = "/adl", produces = MediaType.TEXT_HTML_VALUE)
    public String autocompleteListingsPage() throws Exception {
    	return menuService.getTemplateWithSiteLayout("autocompleteListing",  new HashMap<>());
    }

	@GetMapping(value = "/da", produces = MediaType.TEXT_HTML_VALUE)
    public String demoAutocomplete() throws Exception {
        return menuService.getTemplateWithSiteLayout("demoAutocomplete",  new HashMap<>());
	}

	@GetMapping(value = "/aea", produces = MediaType.TEXT_HTML_VALUE)
    public String addEditAutocompleteDetails(HttpServletRequest request) throws Exception {
		
		String autocompleteId 				= request.getParameter("acId");
		Map<String, Object> templateData 	= new HashMap<>();
		
		if (autocompleteId != null) {
			AutocompleteVO autocompleteVO = typeAheadService.getAutocompleteDetailsId(autocompleteId);
			templateData.put("autocompleteVO", autocompleteVO);
		}
		return menuService.getTemplateWithSiteLayout("addEditAutocompleteDetails",  templateData);
    }
    
}
