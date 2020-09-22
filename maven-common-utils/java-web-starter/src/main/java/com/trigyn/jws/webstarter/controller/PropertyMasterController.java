package com.trigyn.jws.webstarter.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;

@RestController
@RequestMapping("/cf")
public class PropertyMasterController {
	
	private final static Logger logger = LogManager.getLogger(PropertyMasterController.class);

	@Autowired
    private DBTemplatingService templatingService = null;

    
    @Autowired
    private TemplatingUtils templateEngine = null;
    
    @GetMapping(value = "/pml", produces = MediaType.TEXT_HTML_VALUE)
    public String propertyMasterListing() throws Exception {
        TemplateVO templateVO = templatingService.getTemplateByName("property-master-listing");
        return templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), new HashMap<>());
    }
    
    @PostMapping(value = "/spm")
    public void savePropertyMasterDetails(HttpServletRequest httpServletRequest) throws Exception {
    	
    }
    
}
