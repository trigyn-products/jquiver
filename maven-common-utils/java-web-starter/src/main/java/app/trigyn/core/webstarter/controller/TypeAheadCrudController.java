package app.trigyn.core.webstarter.controller;

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

import app.trigyn.common.typeahead.model.AutocompleteVO;
import app.trigyn.common.typeahead.service.TypeAheadService;
import app.trigyn.core.templating.service.DBTemplatingService;
import app.trigyn.core.templating.utils.TemplatingUtils;
import app.trigyn.core.templating.vo.TemplateVO;

@RestController
@RequestMapping("/cf")
public class TypeAheadCrudController {
	
	private final static Logger logger = LogManager.getLogger(TypeAheadCrudController.class);
    
    @Autowired
	private DBTemplatingService templateService = null;
	
	@Autowired
	private TemplatingUtils templateEngine = null;

	@Autowired
	private TypeAheadService typeAheadService = null;

	@GetMapping(value = "/adl", produces = MediaType.TEXT_HTML_VALUE)
    public String autocompleteListingsPage() throws Exception {
        TemplateVO templateVO = templateService.getTemplateByName("autocompleteListing");
        return templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), new HashMap<>());
    }

	@GetMapping(value = "/da", produces = MediaType.TEXT_HTML_VALUE)
    public String demoAutocomplete() throws Exception {
        TemplateVO templateVO = templateService.getTemplateByName("demoAutocomplete");
        return templateVO.getTemplate();
	}

	@GetMapping(value = "/aea", produces = MediaType.TEXT_HTML_VALUE)
    public String addEditAutocompleteDetails(HttpServletRequest request) throws Exception {
        TemplateVO templateVO = templateService.getTemplateByName("addEditAutocompleteDetails");
		String autocompleteId = request.getParameter("acId");
		Map<String, Object> templateData = new HashMap<>();
		if (autocompleteId != null) {
			AutocompleteVO autocompleteVO = typeAheadService.getAutocompleteDetailsId(autocompleteId);
			templateData.put("autocompleteVO", autocompleteVO);
		}
		return templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), templateData);
    }
    
}
