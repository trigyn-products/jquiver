package app.trigyn.core.webstarter.controller;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.trigyn.core.webstarter.service.MasterModuleService;

@RestController
@RequestMapping("/cf")
public class HomeController {
	
	private final static Logger logger = LogManager.getLogger(HomeController.class);

    @Autowired
    private MasterModuleService moduleService = null;
    
    @GetMapping(value = "/home", produces = MediaType.TEXT_HTML_VALUE)
    public String homePage() throws Exception {
        return moduleService.getTemplateWithSiteLayout("home", new HashMap<String, Object>());
    }
    
}
