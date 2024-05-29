package com.trigyn.jws.webstarter.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trigyn.jws.dbutils.entities.AdditionalDatasourceRepository;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.service.DownloadUploadModule;
import com.trigyn.jws.dbutils.service.ModuleVersionService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.ActivityLog;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dbutils.utils.IMonacoSuggestion;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynamicform.dao.DynamicFormCrudDAO;
import com.trigyn.jws.dynamicform.dao.IDynamicFormQueriesRepository;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.dynamicform.entities.DynamicFormSaveQuery;
import com.trigyn.jws.dynamicform.utils.Constant;
import com.trigyn.jws.dynamicform.vo.DynamicFormSaveQueryVO;
import com.trigyn.jws.dynamicform.vo.DynamicFormVO;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibrary;
import com.trigyn.jws.templating.entities.TemplateMaster;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.webstarter.controller.DynamicFormCrudController;

@Service
@Transactional
public class DynamicFormCrudService {

	private final static Logger logger = LogManager.getLogger(DynamicFormCrudService.class);
	
	@Autowired
	private DynamicFormCrudDAO					dynamicFormDAO					= null;

	@Autowired
	private IDynamicFormQueriesRepository		dynamicFormQueriesRepository	= null;

	@Autowired
	private DownloadUploadModule<DynamicForm>	downloadUploadModule			= null;

	@Autowired
	private ModuleVersionService				moduleVersionService			= null;

	@Autowired
	private MenuService							menuService						= null;

	@Autowired
	private PropertyMasterDAO					propertyMasterDAO				= null;

	@Autowired
	private FileUtilities						fileUtilities					= null;

	@Autowired
	private IUserDetailsService					userDetailsService				= null;

	@Autowired
	private ActivityLog							activitylog						= null;
	
	@Autowired
	private AdditionalDatasourceRepository additionalDatasourceRepository;

	@Transactional(readOnly = true)
	public String addEditForm(String formId) throws Exception, CustomStopException {
		try {
		Map<String, Object>	templateMap	= new HashMap<>();
		DynamicForm			dynamicForm	= new DynamicForm();
		if (StringUtils.isNotEmpty(formId)) {
			dynamicForm = dynamicFormDAO.findDynamicFormById(formId);
			dynamicForm.setFormBody(dynamicForm.getFormBody());
			dynamicForm.setFormSelectQuery(dynamicForm.getFormSelectQuery());
		}
		/* Populating ContextPath and JavaScript Suggestions in Monaco Editor */
		String contextSuggestions = IMonacoSuggestion.getTemplateDynamicFormSuggestion();
		String jSSuggestions = IMonacoSuggestion.getJSSuggestion(additionalDatasourceRepository);
		templateMap.put("suggestions", contextSuggestions);
		templateMap.put("JSsuggestions", jSSuggestions);
		templateMap.put("dynamicForm", dynamicForm);
		return menuService.getTemplateWithSiteLayout("dynamic-form-manage-details", templateMap);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured while loading Dynamic Form page.", custStopException);
			throw custStopException;
		}
	}

//	@Transactional(readOnly = false)
	@Transactional(propagation = Propagation.REQUIRED)
	public DynamicForm saveDynamicFormDetails(MultiValueMap<String, String> formData, Integer sourceTypeId)
			throws Exception {

		DynamicForm					dynamicForm				= null;
		Date						date					= new Date();
		UserDetailsVO				userDetailsVO			= userDetailsService.getUserDetails();
		String						formId					= formData.getFirst("formId");
		String						dataSourceId			= formData.getFirst("dataSourceId");
		String						formName				= formData.getFirst("formName");
		String						selectQueryType			= formData.getFirst("selectQueryType");
		if (StringUtils.isNotEmpty(formId)) {
			dynamicForm = dynamicFormDAO.findDynamicFormById(formId);
		}
		if (dynamicForm == null) {
			dynamicForm = new DynamicForm();
			dynamicForm.setCreatedBy(userDetailsVO.getUserName());
			dynamicForm.setCreatedDate(date);
		}
		dynamicForm.setLastUpdatedBy(userDetailsVO.getUserName());
		if (StringUtils.isBlank(dataSourceId) == false) {
			dynamicForm.setDatasourceId(dataSourceId);
		}
		if (!StringUtils.isBlank(formName)) {
			dynamicForm.setFormName(formName);
		}
		dynamicForm.setFormDescription(formData.getFirst("formDescription").toString());
		dynamicForm.setFormSelectQuery(formData.getFirst("formSelectQuery").toString());
		dynamicForm.setSelectQueryType(Integer.parseInt(selectQueryType));
		dynamicForm.setFormBody(formData.getFirst("formBody").toString());
		dynamicForm.setLastUpdatedTs(date);
		dynamicForm.setIsCustomUpdated(1);
		dynamicFormDAO.saveDynamicForm(dynamicForm);
		return dynamicForm;
	
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String saveDynamicFormDetails2(MultiValueMap<String, String> formData,DynamicForm dynamicForm,Integer sourceTypeId)
			throws Exception {
		String						formId					= formData.getFirst("formId");
		List<DynamicFormSaveQuery>	dynamicFormSaveQueries	= new ArrayList<>();
		List<DynamicFormSaveQuery> formSaveQueries = saveDynamicFormQueries(formData, dynamicForm.getFormId(),
				dynamicFormSaveQueries, formId);
		dynamicForm.setDynamicFormSaveQueries(formSaveQueries);
		String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
		if (environment.equalsIgnoreCase("dev")) {
			String downloadFolderLocation = propertyMasterDAO.findPropertyMasterValue("system", "system",
					"template-storage-path");
			downloadUploadModule.downloadCodeToLocal(dynamicForm, downloadFolderLocation);
		}
		DynamicFormVO	dynamicFormVO	= convertEntityToVO(dynamicForm,formData);
		
		String			templateName	= dynamicFormVO.getFormName();
		Integer			typeSelect		= dynamicFormVO.getFormTypeId();
		/* Method called for implementing Activity Log */
		logActivity(formId, templateName, typeSelect);
		saveScriptLibraryDetails(formData,sourceTypeId,dynamicFormVO);
		moduleVersionService.saveModuleVersion(dynamicFormVO, null, dynamicForm.getFormId(), "jq_dynamic_form",
				sourceTypeId);

		return dynamicForm.getFormId();
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String saveScriptLibraryDetails(MultiValueMap<String, String> formData,Integer sourceTypeId,DynamicFormVO	dynamicFormVO)
			throws Exception {
		ObjectMapper					objectMapper			= new ObjectMapper();
		String formSaveQueryId;
		String scriptLibInsertId;
		String scriptLibDeleteId;
		String moduleId;
		if(null == formData.getFirst("formSaveQueryId")) {
			formSaveQueryId = formData.getFirst("formQueryId");
		}else {
			formSaveQueryId = formData.getFirst("formSaveQueryId");
		}
		if(null == formData.getFirst("moduleId")) {
			moduleId = Constant.DYNAFORM_MOD_ID;
		}else {
			moduleId = formData.getFirst("moduleId");
		}
		
		List<String> formSaveQueryIdList = objectMapper.readValue(formSaveQueryId, List.class);
		
		List<ScriptLibrary>	scriptLibInsert	= new ArrayList<>();
		if(sourceTypeId == Constant.REVISION_SOURCE_VERSION_TYPE) {
			String scriptLibId = dynamicFormVO.getScriptLibId();
			List<String>  scriptLibIdList = objectMapper.readValue(scriptLibId, List.class);
			dynamicFormDAO.scriptLibSave(formSaveQueryIdList,scriptLibIdList,scriptLibInsert,moduleId,sourceTypeId);
		
		}else {
			scriptLibInsertId = formData.getFirst("scriptLibInsert");
			scriptLibDeleteId = formData.getFirst("scriptLibDelete");
			List<String> scriptLibDeleteList = objectMapper.readValue(scriptLibDeleteId, List.class);
			List<String> scriptLibInsertList = objectMapper.readValue(scriptLibInsertId, List.class);
			dynamicFormDAO.scriptLibDelete(formSaveQueryIdList,scriptLibDeleteList,moduleId);
			dynamicFormDAO.scriptLibSave(formSaveQueryIdList,scriptLibInsertList,scriptLibInsert,moduleId,sourceTypeId);
		}
		return null;
	}


	

	/**
	 * Purpose of this method is to log activities</br>
	 * in Form Builder Module.
	 * 
	 * @author            Bibhusrita.Nayak
	 * @param  entityName
	 * @param  typeSelect
	 * @param  formId
	 * @throws Exception
	 */

	private void logActivity(String formId, String entityName, Integer typeSelect) throws Exception {
		Map<String, String>	requestParams	= new HashMap<>();
		UserDetailsVO		detailsVO		= userDetailsService.getUserDetails();
		DynamicForm			dynamicForm		= null;
		String				action			= "";
		if (StringUtils.isNotEmpty(formId)) {
			dynamicForm = dynamicFormDAO.findDynamicFormById(formId);
		}
		if (dynamicForm == null) {
			action = Constants.Action.ADD.getAction();
		} else {
			action = Constants.Action.EDIT.getAction();
		}
		Date activityTimestamp = new Date();
		if (typeSelect == Constants.Changetype.CUSTOM.getChangeTypeInt()) {
			requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
		} else {
			requestParams.put("typeSelect", Constants.Changetype.SYSTEM.getChangetype());
		}
		requestParams.put("action", action);
		requestParams.put("entityName", entityName);
		requestParams.put("masterModuleType", Constants.Modules.DYNAMICFORM.getModuleName());
		requestParams.put("userName", detailsVO.getUserName());
		requestParams.put("message", "");
		requestParams.put("date", activityTimestamp.toString());
		activitylog.activitylog(requestParams);
	}

	private List<DynamicFormSaveQuery> saveDynamicFormQueries(MultiValueMap<String, String> formData,
			String dynamicFormId, List<DynamicFormSaveQuery> dynamicFormSaveQueries, String formId)
			throws JsonProcessingException, JsonMappingException, Exception {

		String	queriesList			= formData.getFirst("daoQueryDetails");
		String	variableName		= formData.getFirst("variableName");
		String	queryType			= formData.getFirst("queryType");
		String	datasourceDetails	= formData.getFirst("datasourceDetails");
		String 	formSaveQueryId 	= formData.getFirst("formSaveQueryId");

		if (queriesList == null && variableName != null) {
			queriesList = formData.getFirst("formSaveQuery");
		}
		if (formSaveQueryId == null && variableName != null) {
			formSaveQueryId = formData.getFirst("formQueryId");
		}
		ObjectMapper					objectMapper			= new ObjectMapper();
		TypeReference<List<Integer>>	listOfInteger			= new TypeReference<List<Integer>>() {
																};
		List<String>					queries					= objectMapper.readValue(queriesList, List.class);
		List<String>					variableNameList		= objectMapper.readValue(variableName, List.class);
		List<Integer>					queryTypeList			= objectMapper.readValue(queryType, listOfInteger);
		List<String>					datasourceDetailsList	= objectMapper.readValue(datasourceDetails, List.class);
		List<String>					formSaveQueryIdList		= objectMapper.readValue(formSaveQueryId, List.class);

		for (int queryCounter = 0; queryCounter < queries.size(); queryCounter++) {
			DynamicFormSaveQuery dynamicFormSaveQuery = new DynamicFormSaveQuery();
			dynamicFormSaveQuery.setDynamicFormId(dynamicFormId);
			dynamicFormSaveQuery.setDynamicFormQueryId(formSaveQueryIdList.get(queryCounter));
			dynamicFormSaveQuery.setDynamicFormSaveQuery(queries.get(queryCounter));
			dynamicFormSaveQuery.setSequence(queryCounter + 1);
			dynamicFormSaveQuery.setDaoQueryType(queryTypeList.get(queryCounter));
			dynamicFormSaveQuery.setDatasourceId(datasourceDetailsList.get(queryCounter));
			dynamicFormSaveQuery.setResultVariableName(variableNameList.get(queryCounter));
			dynamicFormSaveQueries.add(dynamicFormSaveQuery);
			
			
		}

		return dynamicFormQueriesRepository.saveAll(dynamicFormSaveQueries);
	}

	public List<Map<String, Object>> getAllFormQueriesById(String formId) throws Exception {
		List<Map<String, Object>>	dynamicFormList				= new ArrayList<>();
		List<DynamicFormSaveQuery>	dynamicFormSaveQueryList	= dynamicFormDAO.findDynamicFormQueriesById(formId);
		DynamicForm					dynamicForm					= dynamicFormDAO.findDynamicFormById(formId);
		for (DynamicFormSaveQuery dynamicFormSaveQuery : dynamicFormSaveQueryList) {
			Map<String, Object> formSaveQueryMap = new HashMap<>();
			formSaveQueryMap.put("formQueryId", dynamicFormSaveQuery.getDynamicFormQueryId());
			formSaveQueryMap.put("formSaveQuery", dynamicFormSaveQuery.getDynamicFormSaveQuery());
			formSaveQueryMap.put("sequence", dynamicFormSaveQuery.getSequence());
			formSaveQueryMap.put("formBody", dynamicForm.getFormBody());
			formSaveQueryMap.put("datasourceId", dynamicFormSaveQuery.getDatasourceId());
			formSaveQueryMap.put("queryType", dynamicFormSaveQuery.getDaoQueryType());
			formSaveQueryMap.put("variableName", dynamicFormSaveQuery.getResultVariableName());
			//List<Map<String, Object>> scriptLibList = getScriptLibDetatils(dynamicFormSaveQuery.getDynamicFormQueryId());
			dynamicFormList.add(formSaveQueryMap);
		
		}
		return dynamicFormList;
	}
	
	public List<Map<String, Object>> getScriptLibDetatils(String formQueryId) throws Exception {
		List<Map<String, Object>> scriptLibList = dynamicFormDAO.getScriptLibDetatils(formQueryId);
		return scriptLibList;
	}

	public String getFormContentById(String formId) throws Exception {
		DynamicForm dynamicForm = dynamicFormDAO.findDynamicFormById(formId);
		return dynamicForm.getFormBody();
	}

	@Transactional(readOnly = true)
	public String checkFormName(String formName) {
		return dynamicFormDAO.checkFormName(formName);
	}

	public void downloadDynamicFormsTemplate(String formId) throws Exception {
		String downloadFolderLocation = propertyMasterDAO.findPropertyMasterValue("system", "system",
				"template-storage-path");
		if (!StringUtils.isBlank(formId)) {
			DynamicForm dynamicForm = dynamicFormDAO.findDynamicFormById(formId);
			downloadUploadModule.downloadCodeToLocal(dynamicForm, downloadFolderLocation);
		} else {
			downloadUploadModule.downloadCodeToLocal(null, downloadFolderLocation);
		}
	}

	public void uploadFormsToDB(String formName) throws Exception {
		downloadUploadModule.uploadCodeToDB(formName);
	}

	public DynamicFormVO convertEntityToVO(DynamicForm dynamicForm,MultiValueMap<String, String> formData) throws Exception {
		DynamicFormVO dynamicFormVO = new DynamicFormVO();
		ObjectMapper					objectMapper		= new ObjectMapper();
		dynamicFormVO.setFormId(dynamicForm.getFormId());
		dynamicFormVO.setFormName(dynamicForm.getFormName());
		
		dynamicFormVO.setFormDescription(dynamicForm.getFormDescription());
		dynamicFormVO.setFormBody(dynamicForm.getFormBody());
		dynamicFormVO.setFormSelectQuery(dynamicForm.getFormSelectQuery());
		dynamicFormVO.setSelectQueryType(dynamicForm.getSelectQueryType());
		dynamicFormVO.setFormTypeId(dynamicForm.getFormTypeId());
		dynamicFormVO.setCreatedBy(dynamicForm.getCreatedBy());
		dynamicFormVO.setCreatedDate(dynamicForm.getCreatedDate());
		List<String>					variableName		= new ArrayList<>();
		List<String>					dataSourceId		= new ArrayList<>();
		List<Integer>					formInsertQueryType	= new ArrayList<>();
		List<String>					formQueryId		    = new ArrayList<>();
		
		String scriptLibraryId = formData.getFirst("scriptLibId"); 
		List<String> 					scriptLibIds 	= objectMapper.readValue(scriptLibraryId, List.class);
		List<String>					scriptLibId		= new ArrayList<>();
		for(int iScriptIdCounter = 0 ; iScriptIdCounter<scriptLibIds.size(); iScriptIdCounter++) {
			scriptLibId.add("\"" +scriptLibIds.get(iScriptIdCounter)+ "\"");
			String scriptLibrayIds = scriptLibId.toString();
			String scriptLibIDs = scriptLibrayIds.replaceAll("\\[", "").replaceAll("\\]","");
			List<String> scriptLibraryIds = new ArrayList<String>();
			scriptLibraryIds.add(scriptLibIDs);
			dynamicForm.setScriptLibraryId(scriptLibraryIds.toString());
		}
		
		List<DynamicFormSaveQuery>		formSaveQueries		= dynamicForm.getDynamicFormSaveQueries();
		List<DynamicFormSaveQueryVO>	formSaveQueryVOs	= new ArrayList<>();
		for (DynamicFormSaveQuery formSaveQuery : formSaveQueries) {
			DynamicFormSaveQueryVO formSaveQueryVO = convertEntityToVO(formSaveQuery);
			formSaveQueryVOs.add(formSaveQueryVO);
			variableName.add("\"" + formSaveQuery.getResultVariableName() + "\"");
			dataSourceId.add("\"" + formSaveQuery.getDatasourceId() + "\"");
			formInsertQueryType.add(formSaveQuery.getDaoQueryType());
			formQueryId.add("\"" + formSaveQuery.getDynamicFormQueryId() + "\"");
		}
		dynamicFormVO.setDynamicFormSaveQueries(formSaveQueryVOs);
		dynamicFormVO.setVariableName(variableName.toString());
		dynamicFormVO.setDatasourceDetails(dataSourceId.toString());
		dynamicFormVO.setQueryType(formInsertQueryType.toString());
		
		dynamicFormVO.setScriptLibId(dynamicForm.getScriptLibraryId());
		if(null == formData.getFirst("formSaveQueryId")) {
			dynamicFormVO.setFormQueryId(formQueryId.toString());
		}else {
			dynamicFormVO.setFormQueryId(formData.getFirst("formSaveQueryId"));
		}
		
		return dynamicFormVO;
	}

	public DynamicFormSaveQueryVO convertEntityToVO(DynamicFormSaveQuery dynamicFormSaveQuery) throws Exception {
		DynamicFormSaveQueryVO formSaveQueryVO = new DynamicFormSaveQueryVO();
		formSaveQueryVO.setDynamicFormId(dynamicFormSaveQuery.getDynamicFormQueryId());
		formSaveQueryVO.setFormSaveQuery(dynamicFormSaveQuery.getDynamicFormSaveQuery());
		formSaveQueryVO.setSequence(dynamicFormSaveQuery.getSequence());
		return formSaveQueryVO;
	}

}
