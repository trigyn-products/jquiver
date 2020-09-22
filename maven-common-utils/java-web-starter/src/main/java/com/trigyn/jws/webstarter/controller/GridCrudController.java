package com.trigyn.jws.webstarter.controller;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;

@RestController
@RequestMapping("/cf")
public class GridCrudController {
	
	private final static Logger logger = LogManager.getLogger(GridCrudController.class);

    @Autowired
    private DBTemplatingService templateService = null;

    @Autowired
    private TemplatingUtils templateEngine = null;
    
    @GetMapping(value = "/gd", produces = MediaType.TEXT_HTML_VALUE)
    public String gridUtilsPage() throws Exception {
        TemplateVO templateVO = templateService.getTemplateByName("grid-listing");
        return templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), new HashMap<>());
    }
}
