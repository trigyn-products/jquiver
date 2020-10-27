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
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.service.DownloadUploadModule;
import com.trigyn.jws.dbutils.service.ModuleVersionService;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dynamicform.dao.DynamicFormCrudDAO;
import com.trigyn.jws.dynamicform.dao.IDynamicFormQueriesRepository;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.dynamicform.entities.DynamicFormSaveQuery;
import com.trigyn.jws.dynamicform.vo.DynamicFormSaveQueryVO;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.usermanagement.utils.Constants;

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
	private ModuleVersionService moduleVersionService						= null;
	
	@Autowired
	private MenuService menuService 										= null;
	
	@Autowired
	private PropertyMasterDAO propertyMasterDAO 							= null;
	
	@Autowired
	private FileUtilities fileUtilities  									= null;
	
	@Transactional(readOnly = true)
	public String addEditForm(String formId) throws Exception {

		Map<String, Object> templateMap = new HashMap<>();
		DynamicForm dynamicForm = new DynamicForm();
		if (StringUtils.isNotEmpty(formId)) {
			
			dynamicForm = dynamicFormDAO.findDynamicFormById(formId);
			dynamicForm.setFormBody("<#noparse>" + dynamicForm.getFormBody() + "</#noparse>");
			dynamicForm.setFormSelectQuery("<#noparse>" + dynamicForm.getFormSelectQuery() + "</#noparse>");
			Map<Double, String> versionDetailsMap = moduleVersionService.getVersionDetails(formId);
			templateMap.put("versionDetailsMap", versionDetailsMap);
		} else {
			List<String> tables = dynamicFormDAO.getAllTablesListInSchema();
			templateMap.put("tables", tables);
		}
		templateMap.put("dynamicForm", dynamicForm);
		return menuService.getTemplateWithSiteLayout("dynamic-form-manage-details", templateMap);
	}

	@Transactional(readOnly = false)
	public String saveDynamicFormDetails(MultiValueMap<String, String> formData) throws Exception {
		DynamicForm dynamicForm = new DynamicForm();
		List<DynamicFormSaveQuery> dynamicFormSaveQueries = new ArrayList<>();
		String formId = formData.getFirst("formId").toString();
		if (StringUtils.isNotEmpty(formId)) {
			dynamicForm = dynamicFormDAO.findDynamicFormById(formId);
		} else {
			dynamicForm.setCreatedBy("admin");
			dynamicForm.setCreatedDate(new Date());
		}
		String formName = formData.getFirst("formName");
		if(!StringUtils.isBlank(formName)) {
			dynamicForm.setFormName(formName);
		}
		dynamicForm.setFormDescription(formData.getFirst("formDescription").toString());
		dynamicForm.setFormSelectQuery(formData.getFirst("formSelectQuery").toString());
		dynamicForm.setFormBody(formData.getFirst("formBody").toString());

		dynamicForm.setDynamicFormSaveQueries(dynamicFormSaveQueries);
        
		dynamicFormDAO.saveDynamicFormData(dynamicForm);
		
		List<DynamicFormSaveQuery> formSaveQueries = saveDynamicFormQueries(formData, dynamicForm.getFormId(), dynamicFormSaveQueries, formId);
		dynamicForm.setDynamicFormSaveQueries(formSaveQueries);
		
		String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
        if(environment.equalsIgnoreCase("dev")) {
        	downloadUploadModule.downloadCodeToLocal(dynamicForm);
        }
        
        moduleVersionService.saveModuleVersion(dynamicForm,null, dynamicForm.getFormId(), "dynamic_form");
        
        return dynamicForm.getFormId();
	}

	
	
	private List<DynamicFormSaveQuery> saveDynamicFormQueries(MultiValueMap<String, String> formData, String dynamicFormId,
			List<DynamicFormSaveQuery> dynamicFormSaveQueries, String formId)throws JsonProcessingException, JsonMappingException, Exception {
		
		String queriesList 					= formData.getFirst("formSaveQuery");
		List<String> queries				= new ObjectMapper().readValue(queriesList, List.class);
		
		dynamicFormDAO.deleteFormQueriesByIds(formId);
		
		for (int queryCounter = 0; queryCounter < queries.size(); queryCounter++) {
			DynamicFormSaveQuery dynamicFormSaveQuery = new DynamicFormSaveQuery();
			dynamicFormSaveQuery.setDynamicFormId(dynamicFormId);
			dynamicFormSaveQuery.setDynamicFormSaveQuery(queries.get(queryCounter));
			dynamicFormSaveQuery.setSequence(queryCounter+1);
			dynamicFormSaveQueries.add(dynamicFormSaveQuery);
		}
		
		return dynamicFormQueriesRepository.saveAll(dynamicFormSaveQueries);
	}

	
	public List<Map<String, Object>> getAllFormQueriesById(String formId) throws Exception {
		List<Map<String, Object>> dynamicFormList = new ArrayList<>();
		List<DynamicFormSaveQuery> dynamicFormSaveQueryList = dynamicFormDAO.findDynamicFormQueriesById(formId);
		DynamicForm dynamicForm = dynamicFormDAO.findDynamicFormById(formId);
		for (DynamicFormSaveQuery dynamicFormSaveQuery : dynamicFormSaveQueryList) {
			Map<Double, String> versionDetailsMap = moduleVersionService.getVersionDetails(dynamicFormSaveQuery.getDynamicFormQueryId());
			Map<String, Object> formSaveQueryMap = new HashMap<>();
			formSaveQueryMap.put("formQueryId", dynamicFormSaveQuery.getDynamicFormQueryId());
			formSaveQueryMap.put("formSaveQuery", dynamicFormSaveQuery.getDynamicFormSaveQuery());
			formSaveQueryMap.put("sequence", dynamicFormSaveQuery.getSequence());
			formSaveQueryMap.put("versionDetailsMap", versionDetailsMap);
			formSaveQueryMap.put("formBody", dynamicForm.getFormBody());
			dynamicFormList.add(formSaveQueryMap);
		}
		return dynamicFormList;
	}
	
	@Transactional(readOnly = true)
	public String checkFormName(String formName) {
		return dynamicFormDAO.checkFormName(formName);
	}

	public void downloadDynamicFormsTemplate(String formId) throws Exception {
		if(!StringUtils.isBlank(formId)) {
			DynamicForm dynamicForm = dynamicFormDAO.findDynamicFormById(formId);
			downloadUploadModule.downloadCodeToLocal(dynamicForm);
		}else {
			downloadUploadModule.downloadCodeToLocal(null);
		}
	}


	public void uploadFormsToDB(String formName) throws Exception {
		downloadUploadModule.uploadCodeToDB(formName);
	}
	
}
