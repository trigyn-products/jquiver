package app.trigyn.common.dynamicform.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import app.trigyn.common.dbutils.repository.PropertyMasterDAO;
import app.trigyn.common.dynamicform.service.DynamicFormService;
import app.trigyn.core.templating.service.DBTemplatingService;
import app.trigyn.core.templating.utils.TemplatingUtils;
import app.trigyn.core.templating.vo.TemplateVO;

@RestController
@RequestMapping("/cf")
public class DynamicFormController {

	@Autowired
	private DynamicFormService dynamicFormService = null;
	
	@Autowired
	private DBTemplatingService templatingService = null;
	
	@Autowired
	private PropertyMasterDAO propertyMasterDAO = null;
	
	@Autowired
	private TemplatingUtils templateEngine = null;
	
	@PostMapping("/df")
	public String loadDynamicForm(@RequestParam(value = "formId",required = true) String formId,
			@RequestParam(value = "primaryId",required = true) String primaryId) throws Exception {
		return dynamicFormService.loadDynamicForm(formId,primaryId,null);
	}
	
	@PostMapping(value="/sdf",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Boolean saveDynamicForm(
			@RequestBody MultiValueMap<String, String> formData) throws Exception {
		return dynamicFormService.saveDynamicForm(formData);
	}
	
	@GetMapping(value = "/dfl", produces = MediaType.TEXT_HTML_VALUE)
	public String dynamicFormMasterListing() throws Exception {
		TemplateVO templateVO = templatingService.getTemplateByName("dynamic-form-listing");
		Map<String,Object>  modelMap = new HashMap<>();
		String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
		modelMap.put("environment", environment);
		return templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), modelMap);
	}
	
	
	@GetMapping(value = "/cdd")
	@ResponseBody
	public String checkDynamicFormData(HttpServletRequest request, HttpServletResponse response) {
		String formName = request.getParameter("formName");
		String formId = null;
		formId = dynamicFormService.checkFormName(formName);
		return formId;
	}
	
	@PostMapping(value = "/ddf")
	public void downloadAllFormsToLocalDirectory(HttpSession session, HttpServletRequest request) throws Exception {
		
		dynamicFormService.downloadDynamicFormTemplates();
	}

	@PostMapping(value = "/udf")
	public void uploadAllFormsToDB(HttpSession session, HttpServletRequest request) throws Exception {
		dynamicFormService.uploadAllFormsToDB();
	}
}
