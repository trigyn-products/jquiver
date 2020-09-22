package com.trigyn.jws.webstarter.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trigyn.jws.dynamicform.dao.DynamicFormCrudDAO;
import com.trigyn.jws.dynamicform.dao.IDynamicFormQueriesRepository;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.dynamicform.entities.DynamicFormSaveQuery;
import com.trigyn.jws.dynamicform.vo.DynamicFormSaveQueryVO;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.webstarter.utils.DownloadUploadModule;
import com.trigyn.jws.webstarter.utils.DownloadUploadModuleFactory;

@Service
@Transactional
public class DynamicFormCrudService {

	@Autowired
	private DynamicFormCrudDAO dynamicFormDAO = null;

	@Autowired
	private TemplatingUtils templateEngine = null;
	
	@Autowired
	private DownloadUploadModuleFactory moduleFactory = null;
	
	@Autowired
	private DBTemplatingService templatingService = null;

	@Autowired
	private DynamicFormCrudDAO formDAO = null;

	@Autowired
	private IDynamicFormQueriesRepository dynamicFormQueriesRepository = null;

	public String addEditForm(String formId) throws Exception {

		Map<String, Object> templateMap = new HashMap<>();
		TemplateVO templateVO = templatingService.getTemplateByName("dynamic-form-manage-details");
		DynamicForm dynamicForm = new DynamicForm();
		if (StringUtils.isNotEmpty(formId)) {
			dynamicForm = formDAO.findDynamicFormById(formId);
		}
		templateMap.put("dynamicForm", dynamicForm);
		return templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
				templateMap);
	}

	@Transactional(readOnly = false)
	public void saveDynamicFormDetails(MultiValueMap<String, String> formData) throws Exception {
		DynamicForm dynamicForm = new DynamicForm();
		List<DynamicFormSaveQuery> dynamicFormSaveQueries = new ArrayList<>();
		String formId = formData.getFirst("formId").toString();
		if (StringUtils.isNotEmpty(formId)) {
			dynamicForm = formDAO.findDynamicFormById(formId);

		} else {
			dynamicForm.setCreatedBy("admin");
			dynamicForm.setCreatedDate(new Date());
		}
		dynamicForm.setFormDescription(formData.getFirst("formDescription").toString());
		dynamicForm.setFormSelectQuery(formData.getFirst("formSelectQuery").toString());
		dynamicForm.setFormBody(formData.getFirst("formBody").toString());
		dynamicForm.setFormName(formData.getFirst("formName").toString());
		dynamicForm.setDynamicFormSaveQueries(dynamicFormSaveQueries);
		formDAO.saveDynamicFormData(dynamicForm);
		if(!dynamicForm.getDynamicFormSaveQueries().isEmpty()) {
			formDAO.deleteFormQueries(formId);
		}	
		String queriesList = formData.getFirst("formSaveQuery");
		List<String> queries = new ObjectMapper().readValue(queriesList, List.class);
		int counter = 1;
		for (String query : queries) {
			DynamicFormSaveQuery dynamicFormSaveQuery = new DynamicFormSaveQuery();
			//dynamicFormSaveQuery.setDynamicFormQueryId(UUID.randomUUID().toString());
			dynamicFormSaveQuery.setDynamicFormId(dynamicForm.getFormId());
			dynamicFormSaveQuery.setDynamicFormSaveQuery(query);
			dynamicFormSaveQuery.setSequence(counter);
			counter++;
			dynamicFormSaveQueries.add(dynamicFormSaveQuery);
		}

		dynamicFormQueriesRepository.saveAll(dynamicFormSaveQueries);
		List<DynamicForm> form = new ArrayList<DynamicForm>();
		form.add(dynamicForm);
		//dynamicFormService.downloadFormsToLocal(form);
	}

	public List<DynamicFormSaveQueryVO> getAllFormQueriesById(String formId) {
		return dynamicFormDAO.findDynamicFormQueriesById(formId).stream()
							 .map(queries -> new DynamicFormSaveQueryVO(queries.getDynamicFormSaveQuery(), queries.getSequence()))
							 .collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public String checkFormName(String formName) {
		return dynamicFormDAO.checkFormName(formName);
	}

	public void downloadDynamicFormTemplates() throws Exception {
		DownloadUploadModule downloadUploadModule = moduleFactory.getModule("dynarest");
		downloadUploadModule.downloadCodeToLocal();
	}


	public void uploadAllFormsToDB() throws Exception {
		DownloadUploadModule downloadUploadModule = moduleFactory.getModule("dynarest");
		downloadUploadModule.uploadCodeToLocal();
	}

}
