package com.trigyn.jws.webstarter.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trigyn.jws.dbutils.service.DownloadUploadModule;
import com.trigyn.jws.dbutils.service.TemplateVersionService;
import com.trigyn.jws.dynamicform.dao.DynamicFormCrudDAO;
import com.trigyn.jws.dynamicform.dao.IDynamicFormQueriesRepository;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.dynamicform.entities.DynamicFormSaveQuery;
import com.trigyn.jws.dynamicform.vo.DynamicFormSaveQueryVO;
import com.trigyn.jws.templating.service.MenuService;

@Service
@Transactional
public class DynamicFormCrudService {

	@Autowired
	private DynamicFormCrudDAO dynamicFormDAO 								= null;

	@Autowired
	private IDynamicFormQueriesRepository dynamicFormQueriesRepository 		= null;
	
	@Autowired
	@Qualifier("dynamic-form")
	private DownloadUploadModule downloadUploadModule 						= null;
	
	@Autowired
	private TemplateVersionService templateVersionService					= null;
	
	@Autowired
	private MenuService menuService 										= null;
	
	@Transactional(readOnly = true)
	public String addEditForm(String formId) throws Exception {

		Map<String, Object> templateMap = new HashMap<>();
		DynamicForm dynamicForm = new DynamicForm();
		if (StringUtils.isNotEmpty(formId)) {
			
			dynamicForm = dynamicFormDAO.findDynamicFormById(formId);
			dynamicForm.setFormBody("<#noparse>" + dynamicForm.getFormBody() + "</#noparse>");
			dynamicForm.setFormSelectQuery("<#noparse>" + dynamicForm.getFormSelectQuery() + "</#noparse>");
			Map<Double, String> versionDetailsMap = templateVersionService.getVersionDetails(formId);
			templateMap.put("versionDetailsMap", versionDetailsMap);
		} else {
			List<String> tables = dynamicFormDAO.getAllTablesListInSchema();
			templateMap.put("tables", tables);
		}
		templateMap.put("dynamicForm", dynamicForm);
		return menuService.getTemplateWithSiteLayout("dynamic-form-manage-details", templateMap);
	}

	@Transactional(readOnly = false)
	public void saveDynamicFormDetails(MultiValueMap<String, String> formData) throws Exception {
		DynamicForm dynamicForm = new DynamicForm();
		List<DynamicFormSaveQuery> dynamicFormSaveQueries = new ArrayList<>();
		String formId = formData.getFirst("formId").toString();
		if (StringUtils.isNotEmpty(formId)) {
			dynamicForm = dynamicFormDAO.findDynamicFormById(formId);

		} else {
			dynamicForm.setCreatedBy("admin");
			dynamicForm.setCreatedDate(new Date());
		}
		dynamicForm.setFormDescription(formData.getFirst("formDescription").toString());
		dynamicForm.setFormSelectQuery(formData.getFirst("formSelectQuery").toString());
		dynamicForm.setFormBody(formData.getFirst("formBody").toString());
		dynamicForm.setFormName(formData.getFirst("formName").toString());
		dynamicForm.setDynamicFormSaveQueries(dynamicFormSaveQueries);
		dynamicFormDAO.saveDynamicFormData(dynamicForm);
		templateVersionService.saveTemplateVersion(dynamicForm.getFormBody(),null, dynamicForm.getFormId(), "dynamic_form");
		saveDynamicFormQueries(formData, dynamicForm.getFormId(), dynamicFormSaveQueries, formId);
	}

	
	private void saveDynamicFormQueries(MultiValueMap<String, String> formData, String dynamicFormId,
			List<DynamicFormSaveQuery> dynamicFormSaveQueries, String formId)throws JsonProcessingException, JsonMappingException, Exception {
		
		String formSaveQueryId 				= formData.getFirst("formSaveQueryId");
		String queriesList 					= formData.getFirst("formSaveQuery");
		List<String> formSaveQueryIdList 	= new ObjectMapper().readValue(formSaveQueryId, List.class);
		List<String> queries				= new ObjectMapper().readValue(queriesList, List.class);
		
		dynamicFormDAO.deleteFormQueriesByIds(formId,formSaveQueryIdList);
		
		for (int queryCounter = 0; queryCounter < queries.size(); queryCounter++) {
			DynamicFormSaveQuery dynamicFormSaveQuery = new DynamicFormSaveQuery();
			if(!CollectionUtils.isEmpty(formSaveQueryIdList) && formSaveQueryIdList.size() > queryCounter) {
				dynamicFormSaveQuery.setDynamicFormQueryId(formSaveQueryIdList.get(queryCounter));
			}
			dynamicFormSaveQuery.setDynamicFormId(dynamicFormId);
			dynamicFormSaveQuery.setDynamicFormSaveQuery(queries.get(queryCounter));
			dynamicFormSaveQuery.setSequence(queryCounter+1);
			dynamicFormSaveQueries.add(dynamicFormSaveQuery);
		}
		
		List<DynamicFormSaveQuery> formSaveQueries = dynamicFormQueriesRepository.saveAll(dynamicFormSaveQueries);
		for (DynamicFormSaveQuery dynamicFormSaveQuery : formSaveQueries) {
			templateVersionService.saveTemplateVersion(dynamicFormSaveQuery.getDynamicFormSaveQuery()
					,dynamicFormId, dynamicFormSaveQuery.getDynamicFormQueryId(), "dynamic_form_save_queries");
		}
		
	}

	
	public List<DynamicFormSaveQueryVO> getAllFormQueriesById(String formId) {
		return dynamicFormDAO.findDynamicFormQueriesById(formId).stream()
			.map(queries -> {
				try {
					return new DynamicFormSaveQueryVO(queries.getDynamicFormQueryId(), queries.getDynamicFormSaveQuery(), queries.getSequence()
					, templateVersionService.getVersionDetails(queries.getDynamicFormQueryId())
							);
				} catch (Exception exception) {
				
				}
				return null;
			})
		.collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public String checkFormName(String formName) {
		return dynamicFormDAO.checkFormName(formName);
	}

	public void downloadDynamicFormTemplates() throws Exception {
		downloadUploadModule.downloadCodeToLocal();
	}


	public void uploadAllFormsToDB() throws Exception {
		downloadUploadModule.uploadCodeToDB();
	}

}
