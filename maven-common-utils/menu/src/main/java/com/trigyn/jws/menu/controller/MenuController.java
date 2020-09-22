package com.trigyn.jws.menu.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.menu.service.ModuleService;
import com.trigyn.jws.menu.vo.ModuleDetailsVO;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;

@RestController
@RequestMapping("/cf")
public class MenuController {


    @Autowired
    private DBTemplatingService templateService 	= null;

    @Autowired
    private TemplatingUtils templateEngine 			= null;
    
    @Autowired
    private ModuleService moduleService 			= null;
    
    
    @GetMapping(value = "/mul", produces = MediaType.TEXT_HTML_VALUE)
    public String moduleListingPage() throws Exception {
        TemplateVO templateVO = templateService.getTemplateByName("menu-module-listing");
        return templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), new HashMap<>());
    }
    
    @GetMapping(value = "/mu", produces = MediaType.TEXT_HTML_VALUE)
    public String createMenu() throws Exception {
    	Map<String, Object> templateMap 			= new HashMap<>();
    	List<ModuleDetailsVO> moduleDetailsVOList 	= moduleService.getAllMenuModules();
    	templateMap.put("moduleDetailsVOList", moduleDetailsVOList);
        TemplateVO templateVO = templateService.getTemplateByName("home-page");
        TemplateVO templateInnerVO = templateService.getTemplateByName("home");
        Map<String, String> childTemplateDetails = new HashMap<>();
        childTemplateDetails.put("template-body", templateInnerVO.getTemplate());
        return templateEngine.processMultipleTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), templateMap, childTemplateDetails);
    }
}
