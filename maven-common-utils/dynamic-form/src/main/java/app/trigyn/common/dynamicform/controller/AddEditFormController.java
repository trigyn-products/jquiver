package app.trigyn.common.dynamicform.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.trigyn.common.dynamicform.service.AddEditFormService;
import app.trigyn.common.dynamicform.vo.DynamicFormSaveQueryVO;

@RestController
@RequestMapping("/cf")
public class AddEditFormController {

	@Autowired
	private AddEditFormService addEditFormService = null;
	
	@PostMapping(value = "/aedf", produces = {MediaType.TEXT_HTML_VALUE})
	public String addEditForm(@RequestParam("form-id") String formId) throws Exception {
		return addEditFormService.addEditForm(formId);
	}
	
	@PostMapping(value = "/gfsq", produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<DynamicFormSaveQueryVO> getAllFormQueriesById(HttpServletRequest httpServletRequest) throws Exception {
		String formId = httpServletRequest.getParameter("formId");
		return addEditFormService.getAllFormQueriesById(formId);
	}
	
	@PostMapping(value="/sdfd",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void saveDynamicFormDetails(
			@RequestBody MultiValueMap<String, String> formData) throws Exception {
		 addEditFormService.saveDynamicFormDetails(formData);
	}
	
}
