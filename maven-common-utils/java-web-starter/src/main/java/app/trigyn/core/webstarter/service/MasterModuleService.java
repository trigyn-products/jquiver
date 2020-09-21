package app.trigyn.core.webstarter.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.trigyn.core.menu.vo.ModuleDetailsVO;
import app.trigyn.core.service.ModuleService;
import app.trigyn.core.templating.service.DBTemplatingService;
import app.trigyn.core.templating.utils.TemplatingUtils;
import app.trigyn.core.templating.vo.TemplateVO;

@Service
@Transactional
public class MasterModuleService {

	@Autowired
	private DBTemplatingService templatingService 	= null;

	@Autowired
	private ModuleService moduleService 			= null;

	@Autowired
	private TemplatingUtils templateEngine 			= null;

	public String getTemplateWithSiteLayout(String templateName, Map<String,Object> templateDetails) throws Exception {
		Map<String, Object> templateMap = new HashMap<>();
		List<ModuleDetailsVO> moduleDetailsVOList = moduleService.getAllMenuModules();
		templateMap.put("moduleDetailsVOList", moduleDetailsVOList);
		TemplateVO templateVO = templatingService.getTemplateByName("home-page");
		TemplateVO templateInnerVO = templatingService.getTemplateByName(templateName);
		Map<String, String> childTemplateDetails = new HashMap<>();
		String innerTemplate = templateEngine.processTemplateContents(templateInnerVO.getTemplate(), templateInnerVO.getTemplateName(), templateDetails);
		childTemplateDetails.put("template-body", innerTemplate);
		return templateEngine.processMultipleTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), templateMap, childTemplateDetails);

	}
}
