package app.trigyn.core.webstarter.controller;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.trigyn.common.gridutils.controller.GridUtilsController;
import app.trigyn.core.templating.service.DBTemplatingService;
import app.trigyn.core.templating.utils.TemplatingUtils;
import app.trigyn.core.templating.vo.TemplateVO;

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
