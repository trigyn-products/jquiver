package com.trigyn.jws.dynamicform.service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trigyn.jws.dbutils.entities.AdditionalDatasourceRepository;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.service.ModuleVersionService;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.spi.PropertyMasterDetails;
import com.trigyn.jws.dbutils.utils.ActivityLog;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dbutils.utils.IMonacoSuggestion;
import com.trigyn.jws.dbutils.vo.FileInfo;
import com.trigyn.jws.dbutils.vo.FileInfo.FileType;
import com.trigyn.jws.dbutils.vo.ScriptLibraryVO;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynamicform.dao.DynamicFormCrudDAO;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.dynamicform.entities.DynamicFormSaveQuery;
import com.trigyn.jws.dynamicform.utils.Constant;
import com.trigyn.jws.dynarest.dao.FileUploadConfigDAO;
import com.trigyn.jws.dynarest.dao.JwsDynarestDAO;
import com.trigyn.jws.dynarest.entities.FileUploadConfig;
import com.trigyn.jws.dynarest.entities.JqScheduler;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDetail;
import com.trigyn.jws.dynarest.repository.JqschedulerRepository;
import com.trigyn.jws.dynarest.service.FileUploadConfigService;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibraryDetails;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.utils.Constants;


@Service
@Transactional
public class DynamicFormService {

	private final static Logger				logger					= LogManager.getLogger(DynamicFormService.class);

	private static final String				DATE					= "date";

	private static final String				TIMESTAMP				= "timestamp";

	private static final String				DECIMAL					= "decimal";

	private static final String				TEXT					= "text";

	private static final String				INT						= "int";

	private static final String				VARCHAR					= "varchar";

	private static final String				PRIMARY_KEY				= "PK";

	private static final String				BOOLEAN					= "boolean";

	@Autowired
	private TemplatingUtils					templateEngine			= null;

	@Autowired
	private DynamicFormCrudDAO				dynamicFormDAO			= null;

	@Autowired
	private PropertyMasterDAO				propertyMasterDAO		= null;

	@Autowired
	private DBTemplatingService				templateService			= null;

	@Autowired
	private FileUtilities					fileUtilities			= null;

	@Autowired
	private MenuService						menuService				= null;

	@Autowired
	private IUserDetailsService				userDetailsService		= null;

	@Autowired
	private PropertyMasterService			propertyMasterService	= null;

	@Autowired
	private PropertyMasterDetails			propertyMasterDetails	= null;

	@Autowired
	private ActivityLog						activitylog				= null;

	@Autowired
	private JqschedulerRepository			jqschedulerRepository	= null;

	@Autowired
	private AdditionalDatasourceRepository	additionalDatasourceRepository;
	
	@Autowired
	private FileUploadConfigService			fileUploadConfigService	= null;
	
	@Autowired
	private JwsDynarestDAO					dynamicRestDAO			= null;
	
	@Autowired
	protected SessionFactory				sessionFactory			= null;
	
	@Autowired
	private ModuleVersionService			moduleVersionService	= null;
	
	@Autowired
	private FileUploadConfigDAO 			fileUploadConfigDAO 	= null;

	public String loadDynamicForm(String formId, Map<String, Object> requestParam,
			Map<String, Object> additionalParam) throws IOException, CustomStopException {
		logger.debug("Inside DynamicFormService.loadDynamicForm(formId: {}, requestParam: {}, additionalParam: {})",
				formId, requestParam, additionalParam);

		try {

			String				selectTemplateQuery	= null;
			String				selectQuery			= null;
			String				formBody			= null;
			Map<String, Object>	formHtmlTemplateMap	= new HashMap<>();
			Map<String, Object> map                 = new HashMap<>();
			String				selectQueryFile		= "selectQuery";
			String				htmlBodyFile		= "htmlContent";

			DynamicForm			form				= dynamicFormDAO.findDynamicFormById(formId);
			UserDetailsVO		userDetails			= userDetailsService.getUserDetails();
			String				formName			= form.getFormName();

			String				suffix				= propertyMasterDetails.getAllProperties().get("template_suffix");
			if (suffix != null && suffix.isBlank() == false) {
				String suffixedFormID = dynamicFormDAO.checkFormName(formName + suffix);
				if (suffixedFormID != null) {
					form		= dynamicFormDAO.findDynamicFormById(suffixedFormID);
					formName	= form.getFormName();
				}
			}

			String							primaryId			= (String) requestParam.get("primaryId");
			MultiValueMap<String, String>	requestActivityLog	= new LinkedMultiValueMap<String, String>();
			requestActivityLog.add("formName", formName);
			requestActivityLog.add("formId", formId);
			requestActivityLog.add("primaryId", primaryId);
			requestActivityLog.add("fileBinId", (String) requestParam.get("fileBinId"));
			requestActivityLog.add("notificationid", (String) requestParam.get("notificationid"));
			requestActivityLog.add("propertyMasterId", (String) requestParam.get("propertyMasterId"));
			requestActivityLog.add("clientid", (String) requestParam.get("clientid"));
			requestActivityLog.add("schedulerid", (String) requestParam.get("schedulerid"));
			logActivity(requestActivityLog);

			String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
			if (environment.equalsIgnoreCase("dev")) {
				selectQuery	= getContentForDevEnvironment(form, form.getFormSelectQuery(), selectQueryFile);
				formBody	= getContentForDevEnvironment(form, form.getFormBody(), htmlBodyFile);
			} else {
				selectQuery	= form.getFormSelectQuery();
				formBody	= form.getFormBody();
			}
			List<Map<String, Object>> selectResultSet = null;

			if (additionalParam != null) {
				requestParam.putAll(additionalParam);
			}
			selectTemplateQuery = templateEngine.processTemplateContents(selectQuery, formName, requestParam);
			ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
			ScriptEngine scriptEngine = null;
			scriptEngine = scriptEngineManager.getEngineByName(Constant.SelectQueryType.getqueryTypeID(form.getSelectQueryType()).getQueryTypeName());
			if (StringUtils.isNotEmpty(selectTemplateQuery)){
				switch(form.getSelectQueryType()) {
					case Constant.SELECT:
						selectResultSet = dynamicFormDAO.executeQueries(form.getDatasourceId(), selectTemplateQuery.toString(), 
								requestParam);
						break;
					case Constant.JAVASCRIPT:
						selectResultSet = loadScriptEngineExecution(scriptEngine,selectTemplateQuery,selectResultSet);
						break;
					case Constant.PYTHON:
						selectResultSet = loadScriptEngineExecution(scriptEngine,selectTemplateQuery,selectResultSet);
						break;
					case Constant.PHP:
						selectResultSet = loadScriptEngineExecution(scriptEngine,selectTemplateQuery,selectResultSet);
						break;
					 default:
						return null;
					}
			}
			formHtmlTemplateMap.put("resultSet", selectResultSet);
			if (selectResultSet != null && selectResultSet.size() > 0) {
				formHtmlTemplateMap.put("resultSetObject", selectResultSet.get(0));
			} else {
				formHtmlTemplateMap.put("resultSetObject", new HashMap<>());
			}
			/* Populating ContextPath and JavaScript Suggestions in Monaco Editor */
			String contextSuggestions = IMonacoSuggestion.getTemplateSuggestion();
			String jSSuggestions = IMonacoSuggestion.getJSSuggestion(additionalDatasourceRepository);
			String filejSSuggestions = IMonacoSuggestion.getfileJSSuggestion(additionalDatasourceRepository);
			formHtmlTemplateMap.put("formId", formId);
			formHtmlTemplateMap.put("userDetails", userDetails);
			formHtmlTemplateMap.put("requestDetails", requestParam);
			formHtmlTemplateMap.put("entityType", "form");
			formHtmlTemplateMap.put("entityName", formName);
			formHtmlTemplateMap.put("suggestions", contextSuggestions);
			formHtmlTemplateMap.put("JSsuggestions", jSSuggestions);
			formHtmlTemplateMap.put("JSfilesuggestions", filejSSuggestions);
//			templateHtml = templateEngine.processTemplateContents(formBody, formName, formHtmlTemplateMap);
			Boolean includeLayout = requestParam.get("includeLayout") == null ? Boolean.TRUE
					: Boolean.parseBoolean(requestParam.get("includeLayout").toString());
			if (Boolean.TRUE.equals(includeLayout)) {
				return menuService.getTemplateWithSiteLayoutWithoutProcess(formBody, formHtmlTemplateMap);
			} else {
				return templateEngine.processTemplateContents(formBody, formName, formHtmlTemplateMap);
			}
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in loadDynamicForm.", custStopException);
			throw custStopException;
		} catch (Exception a_exc) {
			logger.error("Error occured in loadDynamicForm() : form(formId : {})", formId, a_exc);
			throw new RuntimeException(a_exc.getMessage());
		}
	}
	
	public List<Map<String, Object>> loadScriptEngineExecution(ScriptEngine scriptEngine,String selectTemplateQuery,List<Map<String, Object>> selectResultSet)
			throws Exception, CustomStopException {
		StringBuilder	resultStringBuilder	= new StringBuilder();
		Map<String, Object> map = new HashMap<>();
		if(scriptEngine.getFactory().getLanguageName().equalsIgnoreCase("ECMAScript")) {
			TemplateVO		templateVO			= templateService.getTemplateByName("script-util");
			resultStringBuilder.append(templateVO.getTemplate()).append("\n");
		}
		resultStringBuilder.append(selectTemplateQuery.toString());
		try {
		    StringWriter stringWriter = new StringWriter();
		    scriptEngine.getContext().setWriter(new PrintWriter(stringWriter));
		    Object scriptResult = scriptEngine.eval(resultStringBuilder.toString());
	        if(scriptEngine.getFactory().getLanguageName().equalsIgnoreCase("python")) {
	        	// If the method has a return statement
	        	if(scriptEngine.get("result") != null) {
	        		Object result = scriptEngine.get("result");
	        		map =  (Map<String, Object>) result;	
	        	}
	        }else if(scriptEngine.getFactory().getLanguageName().equalsIgnoreCase("php")) {
	        	System.out.println("Sys Out is necessary for PHP"+scriptResult);
	        	Object result = stringWriter.toString();
	        	if (result instanceof Map) {
	        		map = (Map<String, Object>) result;
	        	} else if (result instanceof String) {
	        	    String jsonString = (String) result;
	        	    ObjectMapper objectMapper = new ObjectMapper();
	        	    map = objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
	        	}
	        } else {
	        	map = (Map<String, Object>) scriptResult;	
			}
	        if (map != null ) {
				selectResultSet = new ArrayList<>();
				selectResultSet.add(map);
			}
	    } catch (ScriptException scrExc) {
	        logger.error("Error occured in scriptEngineExecution.", scrExc);
	    }
		
		return selectResultSet;
	}

	/**
	 * Purpose of this method is to log activities</br>
	 * in Dynamic Forms Module.
	 * 
	 * @author           Bibhusrita.Nayak
	 * @param  formData
	 * @throws Exception
	 */
	private void logActivity(MultiValueMap<String, String> formData) throws Exception {
		Map<String, String>	requestParams		= new HashMap<>();
		Map<String, Object>	saveTemplateMap		= new HashMap<>();
		String				query				= null;
		UserDetailsVO		detailsVO			= userDetailsService.getUserDetails();
		DynamicForm			form				= dynamicFormDAO.findDynamicFormById(formData.getFirst("formId"));
		String				formName			= formData.getFirst("formName");
		String				entityName			= formData.getFirst("entityName");
		Date				activityTimestamp	= new Date();
		String				action				= "";
		Integer				typeSelect			= null;

		if (null != entityName && "jq_grid_details".equalsIgnoreCase(entityName)) {
			if (null != formData.getFirst("isEdit") && formData.getFirst("isEdit").isEmpty() == false) {
				action = formData.getFirst("isEdit");
				if (action.equalsIgnoreCase(Constants.ISEDIT)) {
					action = Constants.Action.ADD.getAction();
				} else {
					action = Constants.Action.EDIT.getAction();
				}
			}
			requestParams.put("entityName", formData.getFirst("gridId"));
			requestParams.put("masterModuleType", Constants.Modules.GRIDUTILS.getModuleName());
			requestParams.put("action", action);
			typeSelect = Integer.parseInt(formData.getFirst("queryType"));
			if (typeSelect == Constants.Changetype.CUSTOM.getChangeTypeInt()) {
				requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
			} else {
				requestParams.put("typeSelect", Constants.Changetype.SYSTEM.getChangetype());
			}
		} else if (null != formName && formName.equalsIgnoreCase("grid-details-form")) {
			if (formData.getFirst("primaryId").isEmpty() == false) {
				action = Constants.Action.OPEN.getAction();
				String selectQuery = form.getFormSelectQuery();
				saveTemplateMap.put("primaryId", formData.getFirst("primaryId"));
				query = templateEngine.processTemplateContents(selectQuery, "grid-details-form", saveTemplateMap);
				List<Map<String, Object>> list = dynamicFormDAO.executeQueries(form.getDatasourceId(), query,
						saveTemplateMap);
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					Map<String, Object>	map			= (Map<String, Object>) iterator.next();
					Integer				queryType	= (Integer) map.get("queryType");
					if (queryType == Constants.Changetype.CUSTOM.getChangeTypeInt()) {
						requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
					} else {
						requestParams.put("typeSelect", Constants.Changetype.SYSTEM.getChangetype());
					}
				}
				requestParams.put("entityName", formData.getFirst("primaryId"));
				requestParams.put("action", action);
				requestParams.put("masterModuleType", Constants.Modules.GRIDUTILS.getModuleName());
			}
		}
		if (null != entityName && "jq_dynamic_rest_details".equalsIgnoreCase(entityName)) {
			action = formData.getFirst("isEdit");
			if (action.equalsIgnoreCase(Constants.ISEDIT)) {
				action = Constants.Action.ADD.getAction();
			} else {
				action = Constants.Action.EDIT.getAction();
			}
			requestParams.put("action", action);
			typeSelect = Integer.parseInt(formData.getFirst("dynarestRequestTypeId"));
			if (typeSelect == Constants.Changetype.CUSTOM.getChangeTypeInt()) {
				requestParams.put("typeSelect", Constants.Changetype.SYSTEM.getChangetype());
			} else {
				requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
			}
			requestParams.put("entityName", formData.getFirst("dynarestUrl"));
			requestParams.put("masterModuleType", Constants.Modules.DYNAMICREST.getModuleName());
		} else if (formName != null && formName.equalsIgnoreCase("dynamic-rest-form")) {
			if (formData.getFirst("primaryId").isEmpty() == false) {
				JwsDynamicRestDetail jwsDynamicRestDetial = dynamicRestDAO.findDynamicRestByUrl(formData.getFirst("primaryId"));
				Integer				dynarestRequestTypeId	= jwsDynamicRestDetial.getJwsRequestTypeId();
				if (dynarestRequestTypeId == Constants.Changetype.SYSTEM.getChangeTypeInt()) {
					requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
				} else if (dynarestRequestTypeId == Constants.Changetype.CUSTOM.getChangeTypeInt()) {
					requestParams.put("typeSelect", Constants.Changetype.SYSTEM.getChangetype());
				}
				requestParams.put("entityName", formData.getFirst("primaryId"));
				requestParams.put("action", Constants.Action.OPEN.getAction());
				requestParams.put("masterModuleType", Constants.Modules.DYNAMICREST.getModuleName());
			}
		}
		if (null != formData.getFirst("noOfFiles")) {
			action = formData.getFirst("edit");
			if (null == action) {
				action = Constants.Action.ADD.getAction();
			} else {
				action = Constants.Action.EDIT.getAction();
			}
			requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
			requestParams.put("action", action);
			requestParams.put("entityName", formData.getFirst("fileBinId"));
			requestParams.put("masterModuleType", Constants.Modules.FILEBIN.getModuleName());
		} else if ("common-file-form".equalsIgnoreCase(formName) == false
				&& formName.equalsIgnoreCase("file-upload-config")) { 
			if (formData.getFirst("fileBinId").isEmpty() == false) {
				action = Constants.Action.OPEN.getAction();
				requestParams.put("action", action);
				requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
				requestParams.put("entityName", formData.getFirst("fileBinId"));
				requestParams.put("masterModuleType", Constants.Modules.FILEBIN.getModuleName());
			}
		}
		if (entityName != null && entityName.equalsIgnoreCase("jq_property_master")) {
			String selectQuery = form.getFormSelectQuery();
			saveTemplateMap.put("propertyMasterId", formData.getFirst("propertyMasterId"));
			query = templateEngine.processTemplateContents(selectQuery, "property-master-form", saveTemplateMap);
			List<Map<String, Object>> list = dynamicFormDAO.executeQueries(form.getDatasourceId(), query,
					saveTemplateMap);
			if (list.isEmpty() == true) {
				action = Constants.Action.ADD.getAction();
			} else {
				action = Constants.Action.EDIT.getAction();
			}
			requestParams.put("action", action);
			requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
			requestParams.put("entityName", formData.getFirst("ownerId"));
			requestParams.put("masterModuleType", Constants.Modules.APPLICATIONCONFIGURATION.getModuleName());
		} else if (formName != null && formName.equalsIgnoreCase("property-master-form")) {
			if (formData.getFirst("propertyMasterId").isEmpty() == false) { 
				action = Constants.Action.OPEN.getAction();
				requestParams.put("action", action);
				requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
				requestParams.put("entityName", formName);
				requestParams.put("masterModuleType", Constants.Modules.APPLICATIONCONFIGURATION.getModuleName());
			}
		}
		if (true == "notification".equalsIgnoreCase(formName)) {
			if (null != formData.getFirst("notificationid")) {
				action = Constants.Action.OPEN.getAction();
				requestParams.put("action", action);
				requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
				requestParams.put("entityName", formName);
				requestParams.put("masterModuleType", Constants.Modules.NOTIFICATION.getModuleName());
			}
		}
		if (formName != null && formName.equalsIgnoreCase("api-client-details-form")) {
			if (formData.getFirst("clientid").isEmpty() == false) { 
				action = Constants.Action.OPEN.getAction();
				requestParams.put("action", action);
				requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
				requestParams.put("entityName", formName);
				requestParams.put("masterModuleType", Constants.Modules.APICLIENTS.getModuleName());
			}
		}
		if (formName != null && formName.equalsIgnoreCase("jq-scheduler-form")) {
			if (formData.getFirst("schedulerid").isEmpty() == false) { 
				action = Constants.Action.OPEN.getAction();
				requestParams.put("action", action);
				JqScheduler	jwsScheduler	= jqschedulerRepository.getOne(formData.getFirst("schedulerid"));
				Integer		schedulertypeid	= jwsScheduler.getSchedulerTypeId();
				if (schedulertypeid == Constants.Changetype.CUSTOM.getChangeTypeInt()) {
					requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
				} else {
					requestParams.put("typeSelect", Constants.Changetype.SYSTEM.getChangetype());
				}
				requestParams.put("entityName", formName);
				requestParams.put("masterModuleType", Constants.Modules.SCHEDULER.getModuleName());
			}
		}
			requestParams.put("userName", detailsVO.getUserName());
			requestParams.put("message", "");
			requestParams.put("date", activityTimestamp.toString());
			activitylog.activitylog(requestParams);
		
	}

	public Boolean saveDynamicForm(MultiValueMap<String, String> formData) throws Exception, CustomStopException {
		logger.debug("Inside DynamicFormService.saveDynamicForm(formData: {})", formData);
		try {
			String saveTemplateQuery = null;
			if(!"1".equalsIgnoreCase(formData.getFirst("edit")) && null !=formData.getFirst("fileBinId")){
				FileUploadConfig existingfileUploadConfig=fileUploadConfigService.getFileUploadConfigByBinId(formData.getFirst("fileBinId"));
				if(null!=existingfileUploadConfig) {
					logger.error("Error in Dynamic Form, file bin already exist ", formData.getFirst("fileBinId"));
					throw new RuntimeException(HttpStatus.PRECONDITION_FAILED.toString());
				}
			}
			String formId = formData.getFirst("formId");
			DynamicForm form = dynamicFormDAO.findDynamicFormById(formId);
			form.setIsCustomUpdated(1);
			String formName = form.getFormName();
			Map<String, Object> saveTemplateMap = new HashMap<>();
			Map<String, Object>	resultSetMap		= new HashMap<>();
			saveTemplateMap.put("formData", formData);
			formData.forEach((key, value) -> saveTemplateMap.put(key, value));
			String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
			String saveQuery = "saveQuery-";
			List<DynamicFormSaveQuery> dynamicFormSaveQueries = dynamicFormDAO.findDynamicFormQueriesById(formId);
			formData.add("formName", formName);
			logActivity(formData);
			for (DynamicFormSaveQuery dynamicFormSaveQuery : dynamicFormSaveQueries) {
				String formSaveQuery = null;
				if (environment.equalsIgnoreCase("dev")) {
					formSaveQuery = getContentForDevEnvironment(form, dynamicFormSaveQuery.getDynamicFormSaveQuery(),
							saveQuery + dynamicFormSaveQuery.getSequence());
				} else {
					formSaveQuery = dynamicFormSaveQuery.getDynamicFormSaveQuery();
				}

				saveTemplateQuery = templateEngine.processTemplateContents(formSaveQuery, formName, saveTemplateMap);
				List<Map<String, Object>> resultSet = new ArrayList<>();
				if (Constant.QueryType.DML.getQueryType() == dynamicFormSaveQuery.getDaoQueryType()) {
					try {
						Integer affectedRowCount = dynamicFormDAO.saveFormData(form.getDatasourceId(),
								saveTemplateQuery, saveTemplateMap);
						resultSetMap.put(dynamicFormSaveQuery.getResultVariableName(), affectedRowCount);
					} catch (Throwable a_thr) {
						logger.error(ExceptionUtils.getStackTrace(a_thr));
					}
				} else if (Constant.QueryType.SP.getQueryType() == dynamicFormSaveQuery.getDaoQueryType()) {
					try {
						resultSet = dynamicFormDAO.executeQueries(dynamicFormSaveQuery.getDatasourceId(),
								saveTemplateQuery, saveTemplateMap);
						resultSetMap.put(dynamicFormSaveQuery.getResultVariableName(), resultSet);
					} catch (Throwable a_thr) {
						logger.error(ExceptionUtils.getStackTrace(a_thr));
					}
				} else {
					try {
						TemplateVO		templateVO			= templateService.getTemplateByName("script-util");
						StringBuilder	resultStringBuilder	= new StringBuilder();
						resultStringBuilder.append(templateVO.getTemplate()).append("\n");

						ScriptEngineManager	scriptEngineManager	= new ScriptEngineManager();

						ScriptEngine		scriptEngine		= scriptEngineManager.getEngineByName("nashorn");
						scriptEngine.put("requestDetails", saveTemplateMap);
					
						List<Object> objScriptLib = dynamicFormDAO.scriptLibExecution(formId);
						for(int iCounter = 0; iCounter<objScriptLib.size(); iCounter++) {
			 				resultStringBuilder.append(objScriptLib.get(iCounter));
						}
						resultStringBuilder.append(saveTemplateQuery.toString());
						Object result = scriptEngine.eval(resultStringBuilder.toString());
						resultSetMap.put(dynamicFormSaveQuery.getResultVariableName(), result);
					} catch (CustomStopException custStopException) {
						logger.error("Error occured in saveDynamicForm.", custStopException);
						throw custStopException;
					} catch (Throwable a_thr) {
						logger.error(ExceptionUtils.getStackTrace(a_thr));
					}
				}
			}
			return true;
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in saveDynamicForm.", custStopException);
			throw custStopException;
		}
	}
	
	public Boolean saveDynamicForm(List<Map<String, String>> formData, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws Exception, CustomStopException {
		logger.debug("Inside DynamicFormService.saveDynamicForm(formData: {})", formData);

		String				saveTemplateQuery	= null;
		String				generatedCaptcha	= null;
		String				formCaptcha			= null;
		Map<String, Object>	formDetails			= createParamterMap(formData);
		String				formId				= formDetails.get("formId").toString();
		StringBuilder		sessiongCaptcha		= new StringBuilder(formId).append("_captcha");
		Map<String, Object>	saveTemplateMap		= new HashMap<>();

		if (httpServletRequest.getSession().getAttribute(sessiongCaptcha.toString()) != null) {
			generatedCaptcha	= httpServletRequest.getSession().getAttribute(sessiongCaptcha.toString()).toString();
			formCaptcha			= formDetails.get("formCaptcha").toString();
		}
		if (generatedCaptcha == null || (formCaptcha != null && formCaptcha.equals(generatedCaptcha) == true)) {

			Map<String, FileInfo> fileMap = new HashMap<>();
			if (httpServletRequest instanceof StandardMultipartHttpServletRequest) {

				String								fileCopyPath		= propertyMasterService
						.findPropertyMasterValue("file-copy-path");
				StandardMultipartHttpServletRequest	multipartRequest	= (StandardMultipartHttpServletRequest) httpServletRequest;
				for (Map.Entry<String, MultipartFile> uf : multipartRequest.getFileMap().entrySet()) {
					String		absolutePath	= fileCopyPath + File.separator + UUID.randomUUID().toString();
					FileInfo	fileInfo		= new FileInfo();
					fileInfo.setFileId(uf.getValue().getName());
					fileInfo.setFileName(uf.getValue().getOriginalFilename());
					fileInfo.setFileType(FileType.Physical);
					fileInfo.setSizeInBytes(uf.getValue().getSize());
					fileInfo.setAbsolutePath(absolutePath);
					fileInfo.setCreatedTime(new Date().getTime());
					fileMap.put(uf.getKey(), fileInfo);

					if (new File(fileCopyPath).exists() == false) {
						logger.error("Copy path does not exist.");
						httpServletResponse.sendError(HttpStatus.PRECONDITION_FAILED.value(),
								"Copy path does not exist.");
						return false;
					}
					uf.getValue().transferTo(new File(absolutePath));
				}
			}

			DynamicForm form = dynamicFormDAO.findDynamicFormById(formId);
			form.setIsCustomUpdated(1);
			String isEdit = "0";
			if (formDetails.get("isEdit") != null) {
				isEdit = formDetails.get("isEdit").toString();
			}
			String formName = form.getFormName();
			/* Method called for implementing Activity Log */
			if (formName.equalsIgnoreCase("jq-scheduler-form")) {
				logActivityV2(formData, isEdit, formId, formDetails.get("schedulername").toString());
			} else {
				logActivityV2(formData, isEdit, formId, "");
			}
			String						environment				= propertyMasterDAO.findPropertyMasterValue("system",
					"system", "profile");
			String						saveQuery				= "saveQuery-";
			List<DynamicFormSaveQuery>	dynamicFormSaveQueries	= dynamicFormDAO.findDynamicFormQueriesById(formId);
			for (DynamicFormSaveQuery dynamicFormSaveQuery : dynamicFormSaveQueries) {
				String formSaveQuery = null;
				if (environment.equalsIgnoreCase("dev")) {
					formSaveQuery = getContentForDevEnvironment(form, dynamicFormSaveQuery.getDynamicFormSaveQuery(),
							saveQuery + dynamicFormSaveQuery.getSequence());
				} else {
					formSaveQuery = dynamicFormSaveQuery.getDynamicFormSaveQuery();
				}
				saveTemplateQuery = templateEngine.processTemplateContents(formSaveQuery, formName, formDetails);
				List<Map<String, Object>> resultSet = new ArrayList<>();
				saveTemplateMap.putAll(formDetails);
				ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
				ScriptEngine scriptEngine = null;
				scriptEngine = scriptEngineManager.getEngineByName(Constant.QueryType.getqueryTypeID(dynamicFormSaveQuery.getDaoQueryType()).getQueryTypeName());
				if (dynamicFormSaveQuery.getDaoQueryType() != null){
					switch(dynamicFormSaveQuery.getDaoQueryType()) {
						case Constant.DML:
							try {
								Integer affectedRowCount = dynamicFormDAO.saveFormData(form.getDatasourceId(),
										saveTemplateQuery, saveTemplateMap);
								saveTemplateMap.put(dynamicFormSaveQuery.getResultVariableName(), affectedRowCount);
							} catch (Throwable a_thr) {
								logger.error(ExceptionUtils.getStackTrace(a_thr));
								httpServletResponse.sendError(HttpStatus.BAD_REQUEST.value(),
										ExceptionUtils.getStackTrace(a_thr));
							}
							break;
						case Constant.SP:
							try {
								resultSet = dynamicFormDAO.executeQueries(dynamicFormSaveQuery.getDatasourceId(),
										saveTemplateQuery, saveTemplateMap);
								saveTemplateMap.put(dynamicFormSaveQuery.getResultVariableName(), resultSet);
							} catch (Throwable a_thr) {
								logger.error(ExceptionUtils.getStackTrace(a_thr));
								httpServletResponse.sendError(HttpStatus.BAD_REQUEST.value(),
										ExceptionUtils.getStackTrace(a_thr));
							}
							break;
						case Constant.JS:
							scriptEngineExecution(fileMap, httpServletRequest,httpServletResponse,
									formDetails, saveTemplateQuery,null,dynamicFormSaveQuery,scriptEngine);
							break;
						case Constant.PY:
							scriptEngineExecution(fileMap, httpServletRequest,httpServletResponse,
									formDetails, saveTemplateQuery,null,dynamicFormSaveQuery,scriptEngine);
							break;
						case Constant.QPHP:
							scriptEngineExecution(fileMap, httpServletRequest,httpServletResponse,
									formDetails, saveTemplateQuery,null,dynamicFormSaveQuery,scriptEngine);
							break;
						 default:
							return null;
						}
				}
				
			}
			httpServletRequest.getSession().removeAttribute(sessiongCaptcha.toString());
		} else if (formCaptcha.equals(generatedCaptcha) == false) {
			logger.error("Invalid Captcha while saving dynamic form. formCaptcha{}: ", formCaptcha);
			httpServletResponse.sendError(HttpStatus.PRECONDITION_FAILED.value(), "Invalid Captcha");
		}
		return true;
	}

	/**
	 * Purpose of this method is to log activities</br>
	 * in Notification and API CLIENTS Module.
	 * 
	 * @author           Bibhusrita.Nayak
	 * @param  formData
	 * @throws Exception
	 */
	private void logActivityV2(List<Map<String, String>> formData, String isEdit, String formId, String entityName)
			throws Exception {
		Map<String, Object>	formParameters		= new HashMap<String, Object>();
		Map<String, String>	requestParams		= new HashMap<>();
		UserDetailsVO		detailsVO			= userDetailsService.getUserDetails();
		Date				activityTimestamp	= new Date();
		if (formData != null) {
			// TODO :- Activity Log not implemented for Custom entities.
			for (Map<String, String> data : formData) {
				String	action		= "";
				String	valueType	= data.getOrDefault("valueType", VARCHAR);
				Object	value		= getDataInTypeFormat(data.get("value"), valueType);
				if (null != data.get("name") && data.get("name").equalsIgnoreCase("notificationid")) {
					if (value.toString().isEmpty() == true) {
						action = Constants.Action.ADD.getAction();
					} else {
						action = Constants.Action.EDIT.getAction();
					}
					requestParams.put("entityName", Constants.NOTIFICATION);
					requestParams.put("masterModuleType", Constants.Modules.NOTIFICATION.getModuleName());
					requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
					requestParams.put("action", action);
				} else if (null != data.get("name") && data.get("name").equalsIgnoreCase("clientid")) {
					if (isEdit.equals(Constants.ISEDIT)) {
						action = Constants.Action.ADD.getAction();
					} else {
						action = Constants.Action.EDIT.getAction();
					}
					requestParams.put("entityName", Constants.APICLIENTS);
					requestParams.put("masterModuleType", Constants.Modules.APICLIENTS.getModuleName());
					requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
					requestParams.put("action", action);
				} else if (null != data.get("name") && data.get("name").equalsIgnoreCase("schedulerid")) {
					if (isEdit.equals(Constants.ISEDIT)) {
						action = Constants.Action.ADD.getAction();
						requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
					} else {
						action = Constants.Action.EDIT.getAction();
						formParameters.put("schedulerid", value);
						JqScheduler	jwsScheduler	= jqschedulerRepository
								.getOne((String) formParameters.get("schedulerid"));
						Integer		schedulertypeid	= jwsScheduler.getSchedulerTypeId();
						if (schedulertypeid == Constants.Changetype.CUSTOM.getChangeTypeInt()) {
							requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
						} else {
							requestParams.put("typeSelect", Constants.Changetype.SYSTEM.getChangetype());
						}
					}
					requestParams.put("entityName", entityName);
					requestParams.put("masterModuleType", Constants.Modules.SCHEDULER.getModuleName());
					requestParams.put("action", action);
				}
				
			}
				requestParams.put("userName", detailsVO.getUserName());
				requestParams.put("message", "");
				requestParams.put("date", activityTimestamp.toString());
				activitylog.activitylog(requestParams);
		}
	}

	public Map<String, Object> saveDynamicFormV2(List<Map<String, String>> formData,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception, CustomStopException {
		logger.debug("Inside DynamicFormService.saveDynamicForm(formData: {})", formData.get(0));

		String				saveTemplateQuery	= null;
		String				generatedCaptcha	= null;
		String				formCaptcha			= null;
		Map<String, Object>	formDetails			= createParamterMap(formData);
		String				formId				= httpServletRequest.getParameter("formId").toString();
		StringBuilder		sessiongCaptcha		= new StringBuilder(formId).append("_captcha");
		Map<String, Object>	saveTemplateMap		= new HashMap<>();
		Map<String, Object>	resultSetMap		= new HashMap<>();

		/* Method called for implementing Activity Log */
		logActivityV2(formData, "", "", "");

		if (httpServletRequest.getSession().getAttribute(sessiongCaptcha.toString()) != null) {
			generatedCaptcha	= httpServletRequest.getSession().getAttribute(sessiongCaptcha.toString()).toString();
			formCaptcha			= formDetails.get("formCaptcha").toString();
		}
		if (generatedCaptcha == null || (formCaptcha != null && formCaptcha.equals(generatedCaptcha) == true)) {

			Map<String, FileInfo>				fileMap				= new HashMap<>();
			StandardMultipartHttpServletRequest	multipartRequest	= new StandardMultipartHttpServletRequest(
					httpServletRequest);
			if (multipartRequest.getFileMap().isEmpty() == false) {

				String fileCopyPath = propertyMasterService.findPropertyMasterValue("file-copy-path");
				for (Map.Entry<String, MultipartFile> uf : multipartRequest.getFileMap().entrySet()) {

					String		absolutePath	= fileCopyPath + File.separator + UUID.randomUUID().toString();
					FileInfo	fileInfo		= new FileInfo();
					fileInfo.setFileId(uf.getValue().getName());
					fileInfo.setFileName(uf.getValue().getOriginalFilename());
					fileInfo.setFileType(FileType.Physical);
					fileInfo.setSizeInBytes(uf.getValue().getSize());
					fileInfo.setAbsolutePath(absolutePath);
					fileInfo.setCreatedTime(new Date().getTime());
					fileMap.put(uf.getKey(), fileInfo);

					if (new File(fileCopyPath).exists() == false) {
						logger.error("Copy path doesnot exist.");
						httpServletResponse.sendError(HttpStatus.PRECONDITION_FAILED.value(),
								"Copy path doesnot exist.");
						return saveTemplateMap;
					}

					uf.getValue().transferTo(new File(absolutePath));
				}
			}
			DynamicForm form = dynamicFormDAO.findDynamicFormById(formId);
			form.setIsCustomUpdated(1);
			String						formName				= form.getFormName();
			String						environment				= propertyMasterDAO.findPropertyMasterValue("system",
					"system", "profile");
			String						saveQuery				= "saveQuery-";
			List<DynamicFormSaveQuery>	dynamicFormSaveQueries	= dynamicFormDAO.findDynamicFormQueriesById(formId);
			for (DynamicFormSaveQuery dynamicFormSaveQuery : dynamicFormSaveQueries) {
				String formSaveQuery = null;
				if (environment.equalsIgnoreCase("dev")) {
					formSaveQuery = getContentForDevEnvironment(form, dynamicFormSaveQuery.getDynamicFormSaveQuery(),
							saveQuery + dynamicFormSaveQuery.getSequence());
				} else {
					formSaveQuery = dynamicFormSaveQuery.getDynamicFormSaveQuery();
				}
				saveTemplateQuery = templateEngine.processTemplateContents(formSaveQuery, formName, formDetails);
				List<Map<String, Object>> resultSet = new ArrayList<>();
				saveTemplateMap.putAll(formDetails);
				
				ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
				ScriptEngine scriptEngine = null;
				scriptEngine = scriptEngineManager.getEngineByName(Constant.QueryType.getqueryTypeID(dynamicFormSaveQuery.getDaoQueryType()).getQueryTypeName());
				if (dynamicFormSaveQuery.getDaoQueryType() != null){
					switch(dynamicFormSaveQuery.getDaoQueryType()) {
						case Constant.DML:
							try {
								Integer affectedRowCount = dynamicFormDAO.saveFormData(form.getDatasourceId(),
										saveTemplateQuery, saveTemplateMap);
								resultSetMap.put(dynamicFormSaveQuery.getResultVariableName(), affectedRowCount);
							} catch (Throwable a_thr) {
								logger.error(ExceptionUtils.getStackTrace(a_thr));
								httpServletResponse.sendError(HttpStatus.BAD_REQUEST.value(),
										ExceptionUtils.getStackTrace(a_thr));
							}
							break;
						case Constant.SP:
							try {
								resultSet = dynamicFormDAO.executeQueries(dynamicFormSaveQuery.getDatasourceId(),
										saveTemplateQuery, saveTemplateMap);
								resultSetMap.put(dynamicFormSaveQuery.getResultVariableName(), resultSet);
							} catch (Throwable a_thr) {
								logger.error(ExceptionUtils.getStackTrace(a_thr));
								httpServletResponse.sendError(HttpStatus.BAD_REQUEST.value(),
										ExceptionUtils.getStackTrace(a_thr));
							}
							break;
						case Constant.JS:
							scriptEngineExecution(fileMap, httpServletRequest,httpServletResponse,
									formDetails, saveTemplateQuery, resultSetMap,dynamicFormSaveQuery,scriptEngine);
							break;
						case Constant.PY:
							scriptEngineExecution(fileMap, httpServletRequest,httpServletResponse,
									formDetails, saveTemplateQuery, resultSetMap,dynamicFormSaveQuery,scriptEngine);
							break;
						case Constant.QPHP:
							scriptEngineExecution(fileMap, httpServletRequest,httpServletResponse,
									formDetails, saveTemplateQuery, resultSetMap,dynamicFormSaveQuery,scriptEngine);
							break;
						 default:
							return null;
						}
				}
			}
			httpServletRequest.getSession().removeAttribute(sessiongCaptcha.toString());
		} else if (formCaptcha.equals(generatedCaptcha) == false) {
			logger.error("Invalid Captcha while saving dynamic form. formCaptcha{}: ", formCaptcha);
			httpServletResponse.sendError(HttpStatus.PRECONDITION_FAILED.value(), "Invalid Captcha");
		}

		return resultSetMap;
	}
	
	public Map<String, Object> scriptEngineExecution(Map<String, FileInfo> fileMap, HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,
			Map<String, Object> formDetails, String	saveTemplateQuery, Map<String, Object> resultSetMap,DynamicFormSaveQuery dynamicFormSaveQuery,ScriptEngine scriptEngine)
			throws Exception, CustomStopException {
		
		try {
			StringBuilder	resultStringBuilder	= new StringBuilder();
			
			if(scriptEngine.getFactory().getLanguageName().equalsIgnoreCase("ECMAScript")) {
				TemplateVO		templateVO			= templateService.getTemplateByName("script-util");
				resultStringBuilder.append(templateVO.getTemplate()).append("\n");
			}
			scriptEngine.put("requestDetails", formDetails);

			if (fileMap != null && fileMap.size() > 0) {
				scriptEngine.put("files", fileMap);
			}
			scriptEngine.put("httpRequestObject", httpServletRequest);
			resultStringBuilder.append(saveTemplateQuery.toString()).append("\n");
			List<Object> objScriptLib = dynamicFormDAO.scriptLibExecution(dynamicFormSaveQuery.getDynamicFormQueryId());
			for(int iCounter = 0; iCounter<objScriptLib.size(); iCounter++) {
				resultStringBuilder.append(objScriptLib.get(iCounter)).append("\n");
			}
		    try {
			    StringWriter stringWriter = new StringWriter();
			    scriptEngine.getContext().setWriter(new PrintWriter(stringWriter));
		        Object scriptResult = scriptEngine.eval(resultStringBuilder.toString());
		        if(scriptEngine.getFactory().getLanguageName().equalsIgnoreCase("python")) {
		        	// If the method has a return statement
		        	if(scriptEngine.get("result") != null) {
		        		Object result = scriptEngine.get("result");
						resultSetMap.put(dynamicFormSaveQuery.getResultVariableName(), result);
		        	}
		        }else if(scriptEngine.getFactory().getLanguageName().equalsIgnoreCase("php")) {
		        	System.out.println("Sys Out is necessary for PHP"+scriptResult);
			        String result = stringWriter.toString();
					resultSetMap.put(dynamicFormSaveQuery.getResultVariableName(), result);
		        } else {
		        	if(null != scriptResult) {
		        		resultSetMap.put(dynamicFormSaveQuery.getResultVariableName(), scriptResult);
		        	}
				}
		    } catch (ScriptException e) {
		        logger.error("Error occured in scriptEngineExecution.", e);
		    }
		    
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in scriptEngineExecution.", custStopException);
			throw custStopException;
		} catch (Throwable a_thr) {
			logger.error(ExceptionUtils.getStackTrace(a_thr));
			httpServletResponse.sendError(HttpStatus.BAD_REQUEST.value(),
					ExceptionUtils.getStackTrace(a_thr));
			
		}
		return resultSetMap;
	
	}

	public String getContentForDevEnvironment(DynamicForm form, String dbContent, String fileName) throws Exception {
		logger.debug("Inside DynamicFormService.getContentForDevEnvironment(form: {}, dbContent: {}, fileName: {})",
				form, dbContent, fileName);

		String	ftlCustomExtension	= ".tgn";
		String	templateDirectory	= "DynamicForm";
		String	folderLocation		= propertyMasterDAO.findPropertyMasterValue("system", "system",
				"template-storage-path");
		folderLocation = folderLocation + File.separator + templateDirectory + File.separator + form.getFormName();
		File directory = new File(folderLocation);
		if (!directory.exists()) {
			return dbContent;
		}

		File selectFile = new File(folderLocation + File.separator + fileName + ftlCustomExtension);
		if (selectFile.exists()) {
			return fileUtilities.readContentsOfFile(selectFile.getAbsolutePath());
		} else {
			return dbContent;
		}
	}

	public Map<String, String> createDefaultFormByTableName(String tableName, List<Map<String, Object>> tableDetails,
			String moduleURL, String additionalDataSourceId, String dbProductName, Boolean toggleCaptcha,Boolean toggleFileBin,String fileBinId,String fileAssociationId) throws CustomStopException{
		logger.debug(
				"Inside DynamicFormService.createDefaultFormByTableName(tableName: {}, tableDetails: {}, moduleURL: {}, additionalDataSourceId: {}, dbProductName: {})",
				tableName, tableDetails, moduleURL, additionalDataSourceId, dbProductName,toggleCaptcha, toggleFileBin, fileBinId, fileAssociationId);

		Map<String, String>	templatesMap	= new HashMap<>();
		Map<String, Object>	parameters		= new HashMap<>();
		parameters.put("columnDetails", tableDetails);
		parameters.put("formName", tableName);
		parameters.put("toggleCaptcha", toggleCaptcha);
		parameters.put("toggleFileBin", toggleFileBin);
		parameters.put("fileBinId", fileBinId);
		parameters.put("fileAssociationId", "${(resultSetObject."+fileAssociationId+")!''}");
		parameters.put("editCondition", fileAssociationId);
		if (StringUtils.isBlank(moduleURL) == false) {
			parameters.put("moduleURL", moduleURL);
		}
		try {
			TemplateVO	templateVO	= templateService.getTemplateByName("system-form-html-template");
			String		template	= templateEngine.processTemplateContents(templateVO.getTemplate(),
					templateVO.getTemplateName(), parameters);
			templatesMap.put("form-template", template);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in createDefaultFormByTableName.", custStopException);
			throw custStopException;
		} catch (Exception a_excep) {
			logger.error("Error occured in createDefaultFormByTableName.",a_excep);
		}

		createSaveUpdateQueryTemplate(tableDetails, tableName, templatesMap, additionalDataSourceId, dbProductName);
		return templatesMap;
	}

	public List<Map<String, Object>> getTableInformationByName(String tableName) {
		logger.debug("Inside DynamicFormService.getTableInformationByName(tableName: {})", tableName);
		return dynamicFormDAO.getTableInformationByName(tableName);
	}

	public List<Map<String, Object>> getTableDetailsByTableName(String tableName, String additionalDataSourceId)
			throws Exception {
		logger.debug("Inside DynamicFormService.getTableInformationByName(tableName: {}, additionalDataSourceId: {})",
				tableName, additionalDataSourceId);
		return dynamicFormDAO.getTableDetailsByTableName(additionalDataSourceId, tableName);
	}

	private void createSaveUpdateQueryTemplate(List<Map<String, Object>> tableInformation, String tableName,
			Map<String, String> templatesMap, String additionalDataSourceId, String dbProductName) throws CustomStopException{
		logger.debug(
				"Inside DynamicFormService.getTableInformationByName(tableInformation: {}, tableName: {}, additionalDataSourceId: {}, dbProductName: {})",
				tableInformation, tableName, templatesMap, additionalDataSourceId, dbProductName);

		StringJoiner	insertJoiner		= new StringJoiner(",", "INSERT INTO " + tableName + " (", ")");
		StringJoiner	insertValuesJoiner	= null;
		boolean			isIntPK				= false;
		int				coloumnCounter		= tableInformation.size();
		boolean 		isAutoID 			= false;
		for (Map<String, Object> info : tableInformation) {
			if(info.get("columnType") == null) {
				
				continue;
			}
			String	columnName	= info.get("tableColumnName").toString();
			String	dataType	= info.get("dataType").toString();
			String	columnKey	= info.get("columnKey").toString();
		    String isAutoIncrement = info.get("autoIncrement").toString();
		    if(isAutoIncrement.equalsIgnoreCase("true")) {
		    	isAutoID = true;
		    }
			insertValuesJoiner = createInsertQuery(insertValuesJoiner, tableName, columnName, dataType, columnKey,
					dbProductName, isAutoIncrement);
			if (columnKey != null && columnKey.equals(PRIMARY_KEY)) {
				if(isAutoIncrement.equalsIgnoreCase("false")) {
				insertJoiner.add(columnName);
				}
				if (dataType.equalsIgnoreCase(INT) || dataType.equalsIgnoreCase(DECIMAL)) {
					isIntPK = true;
				}
			}
		}
		
		for (Map<String, Object> info : tableInformation) {
			if(info.get("columnType") == null) {
				coloumnCounter--;
				continue;
			}

			String	columnName	= info.get("tableColumnName").toString();
			String	dataType	= info.get("dataType").toString();
			String	columnKey	= info.get("columnKey").toString();
			String	columnType	= info.get("columnType").toString();
			String isAutoIncrement = info.get("autoIncrement").toString();
			if (StringUtils.isBlank(columnKey) || columnKey.equals(PRIMARY_KEY) == false) {
				insertJoiner.add(columnName);
				joinQueryBuilder(insertValuesJoiner, columnName, dataType, false, columnType, dbProductName,
						coloumnCounter,isAutoIncrement);
			}
			coloumnCounter--;
		}
		StringBuilder queryBuilder = new StringBuilder(insertJoiner.toString());
		queryBuilder.append(insertValuesJoiner);
		
		if(!isAutoID) {
			if (isIntPK) {
				queryBuilder.append(" FROM " + tableName);
			}
			else {
				queryBuilder.append(")");
			}
		}
		else {
			queryBuilder.append(")");
		}

		Map<String, Object> saveQueryparameters = new HashMap<>();
		saveQueryparameters.put("insertQuery", queryBuilder.toString());

		StringJoiner	updateQuery			= new StringJoiner(",", "UPDATE " + tableName + " SET ", "");
		StringJoiner	updateWhereQuery	= new StringJoiner(" AND ", " WHERE ", "");
		for (Map<String, Object> info : tableInformation) {
			if(info.get("columnType") == null) {
				coloumnCounter--;
				continue;
			}
			String	columnName	= info.get("tableColumnName").toString();
			String	dataType	= info.get("dataType").toString();
			String	columnKey	= info.get("columnKey").toString();
			String	columnType	= info.get("columnType").toString();
			String isAutoIncrement = info.get("autoIncrement").toString();
			if ("PK".equals(columnKey)) {
				joinQueryBuilder(updateWhereQuery, columnName, dataType, true, columnType,dbProductName,
						coloumnCounter,isAutoIncrement);
			} else {
				joinQueryBuilder(updateQuery, columnName, dataType, true, columnType, dbProductName,
						coloumnCounter,isAutoIncrement);
			}
		}
		StringBuilder updateQueryBuilder = new StringBuilder(updateQuery.toString());
		updateQueryBuilder.append(updateWhereQuery);
		saveQueryparameters.put("updateQuery", updateQueryBuilder.toString());

		try {
			TemplateVO	templateVO	= templateService.getTemplateByName("system-form-save-query-template");
			String		template	= templateEngine.processTemplateContents(templateVO.getTemplate(),
					templateVO.getTemplateName(), saveQueryparameters);
			templatesMap.put("save-template", template);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in createSaveUpdateQueryTemplate.", custStopException);
			throw custStopException;
		} catch (Exception a_excep) {
			a_excep.printStackTrace();
			logger.error(a_excep);
		}
	}

	private StringJoiner createInsertQuery(StringJoiner insertValuesJoiner, String tableName, String columnName,
			String dataType, String columnKey, String dbProductName, String isAutoIncrement) {

		logger.debug(
				"Inside DynamicFormService.createInsertQuery(insertValuesJoiner: {}, tableName: {}, columnName: {}, dataType: {}, columnKey: {}, dbProductName: {})",
				insertValuesJoiner, tableName, columnName, dataType, columnKey, dbProductName);
		if (insertValuesJoiner == null) {
			insertValuesJoiner = new StringJoiner("");
		}
		if(isAutoIncrement.equalsIgnoreCase("false")) {
		if (columnKey != null && columnKey.equals(PRIMARY_KEY)) {
			if (dataType.equalsIgnoreCase(TEXT)) {
				
				String value = " VALUES (UUID(),";
				if (dbProductName.contains(Constant.POSTGRESQL)) {
					value = " VALUES (uuid_generate_v4(),";
				} else if (dbProductName.contains(Constant.MSSQLSERVER)) {
					value = " VALUES (NEWID(),";
				} else if (dbProductName.contains(Constant.ORACLE)) {
					value = " VALUES (sys_guid(),";
				}

				insertValuesJoiner.add(value.replace("\\", ""));
			} else if (dataType.equalsIgnoreCase(INT) || dataType.equalsIgnoreCase(DECIMAL)) {
				
				String value = " SELECT  COALESCE(MAX(" + columnName + "),0) + 1 ,";
				insertValuesJoiner.add(value.replace("\\", ""));
			}
		}
		}
		else {
			if (columnKey != null && columnKey.equals(PRIMARY_KEY)) {
				String value =  " VALUES(";
				insertValuesJoiner.add(value.replace("\\", ""));
			}
		}
		return insertValuesJoiner;
	}

	private void joinQueryBuilder(StringJoiner insertValuesJoiner, String columnName, String dataType,
			boolean showColumnName, String columnType, String dbProductName, int coloumnCounter, String isAutoIncrement) {
		logger.debug(
				"Inside DynamicFormService.joinQueryBuilder(insertValuesJoiner: {}, columnName: {}, dataType: {}, showColumnName: {}, columnType: {}, dbProductName: {})",
				insertValuesJoiner, columnName, dataType, showColumnName, columnType, dbProductName);

		if(insertValuesJoiner==null) {
			insertValuesJoiner = new StringJoiner("");
		}
		
		String			formFieldName	= columnName.replace("_", "");
		StringBuilder	formFieldVal	= new StringBuilder();
		String			value			= null;
		
		if(isAutoIncrement.equalsIgnoreCase("false")) {
		if (dataType.equalsIgnoreCase(TEXT)) {
			value = showColumnName ? columnName + " = :" + formFieldName : ":" + formFieldName;
			insertValuesJoiner.add(value.replace("\\", ""));
		} else if (dataType.equalsIgnoreCase(INT) || dataType.equalsIgnoreCase(DECIMAL) || dataType.equalsIgnoreCase(BOOLEAN) ) {
			value = showColumnName ? columnName + " = :" + formFieldName : ":" + formFieldName;
			insertValuesJoiner.add(value.replace("\\", ""));
		}
		else if (dataType.equalsIgnoreCase(DATE)) {
			if (columnType.equals("hidden") == false) {
				value = "";
				if (dbProductName != null && dbProductName.contains(Constant.POSTGRESQL)) {
					value = "TO_DATE(:".concat(formFieldName).concat(", 'DD-MONTH-YYYY') ");
				} else if (dbProductName != null && dbProductName.contains(Constant.MSSQLSERVER)) {
					value = "CONVERT(datetime,:".concat(formFieldName).concat(") ");
				} else if (dbProductName != null && dbProductName.contains(Constant.ORACLE)) {
					value = "TO_DATE(:".concat(formFieldName).concat(", 'DD-MONTH-YYYY') ");
				} else {
					value = "STR_TO_DATE(:".concat(formFieldName).concat(", \"%d-%M-%Y\") ");
				}
				formFieldVal.append(value);

			} else {
				if (dbProductName != null && dbProductName.contains(Constant.MSSQLSERVER)) {
					formFieldVal.append("GETDATE()");
				} else if (dbProductName != null && dbProductName.contains(Constant.ORACLE)) {
					formFieldVal.append("SYSDATE");
				} else {
					formFieldVal.append("NOW()");
				}
			}
			insertValuesJoiner.add(showColumnName ? columnName + " = " + formFieldVal : "" + formFieldVal);
		}
		}
		else {
			value = showColumnName ? columnName + " = :" + formFieldName : ":" + formFieldName;
			insertValuesJoiner.add(value.replace("\\", ""));
		}
		if (coloumnCounter > 1) {
			value = ",";
			insertValuesJoiner.add(value);
		}
	}

	private Map<String, Object> createParamterMap(List<Map<String, String>> formData) {
		logger.debug("Inside DynamicFormService.createParamterMap(formData: {})", formData);
		List				tmpValue		= null;

		Map<String, Object>	formParameters	= new HashMap<String, Object>();
		if (formData != null) {
			for (Map<String, String> data : formData) {
				String	valueType	= data.getOrDefault("valueType", VARCHAR);
				String	tmpName		= data.get("name");

				Object	value		= getDataInTypeFormat(data.get("value"), valueType);
				if (formParameters.containsKey(tmpName)) {

					tmpValue = (List) formParameters.get(tmpName + "_");

					if (tmpValue == null) {
						tmpValue = new ArrayList();
						formParameters.put(tmpName + "_", tmpValue);
						tmpValue.add(formParameters.get(tmpName));
					}

					tmpValue.add(value);

				} else {
					formParameters.put(tmpName, value);
				}
			}
		}
		return formParameters;
	}

	private Object getDataInTypeFormat(Object value, String valueType) {

		logger.debug("Inside DynamicFormService.getDataInTypeFormat(value: {}, valueType: {})", value, valueType);
		if (value == null) {
			return null;
		}

		String tmpValue = String.valueOf(value);
		if (valueType.equalsIgnoreCase(INT)) {
			if (StringUtils.isBlank(tmpValue) == false) {
				return (int) Double.parseDouble(tmpValue);
			}
		} else if (valueType.equalsIgnoreCase(DECIMAL)) {
			return Double.parseDouble(tmpValue);
		} else if (valueType.equalsIgnoreCase(DATE) || valueType.equalsIgnoreCase(TIMESTAMP)) {
			Date dateData = new Date();
			try {
				if (null != tmpValue && false == tmpValue.isEmpty()) {
					dateData = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a").parse(tmpValue);
					// dateData = DateFormat.getInstance().parse(value);
				}
			} catch (ParseException a_exc) {
				logger.warn("Error parsing the date : " + tmpValue + " Expected format is dd-MMM-yyyy hh:mm:ss a");
			}
			return dateData;
		}

		return value;
	}
	
	@Transactional(readOnly = false)
	public String saveScriptLibraryDetails(MultiValueMap<String, String> formDataMap, Integer sourceTypeId)
			throws Exception {
		logger.debug("Inside DynamicFormService.saveScriptLibraryDetails(formDataMap: {}, sourceTypeId: {})", formDataMap,
				sourceTypeId);

		ScriptLibraryDetails scriptLibrary  = new ScriptLibraryDetails();
		UserDetailsVO			userDetailsVO			= userDetailsService.getUserDetails();
		Date					date					= new Date();
		String					scriptlibId				= formDataMap.getFirst("scriptlibId");
		String					libraryName				= formDataMap.getFirst("libraryName");
		String					description				= formDataMap.getFirst("description");
		String					templateId				= formDataMap.getFirst("templateId");
		String					scriptType				= formDataMap.getFirst("scriptType");
		scriptLibrary.setScriptLibId(scriptlibId);
		scriptLibrary.setLibraryName(libraryName);
		scriptLibrary.setTemplateId(templateId);
		scriptLibrary.setDescription(description);
		scriptLibrary.setScriptType(scriptType);
		scriptLibrary.setUpdatedDate(date);
		scriptLibrary.setUpdatedBy(userDetailsVO.getUserName());
		scriptLibrary.setCreatedBy(userDetailsVO.getUserName());
		scriptLibrary.setIsCustomUpdated(1);
		ScriptLibraryVO	scriptLibVO	= convertEntityToVO(scriptLibrary);
		getCurrentSession().save(scriptLibrary);
		moduleVersionService.saveModuleVersion(scriptLibVO, null, scriptlibId, "jq_script_lib_details",
				sourceTypeId);
		return scriptlibId;

	}
	
	@Transactional(readOnly = false)
	public String saveScriptLibDetails(ScriptLibraryDetails scriptLibrary, Integer sourceTypeId,String tablename)
			throws Exception {
		UserDetailsVO			userDetailsVO			= userDetailsService.getUserDetails();
		Date					date					= new Date();
		scriptLibrary.setScriptLibId(scriptLibrary.getScriptLibId());
		scriptLibrary.setLibraryName(scriptLibrary.getLibraryName());
		scriptLibrary.setTemplateId(scriptLibrary.getTemplateId());
		scriptLibrary.setDescription(scriptLibrary.getDescription());
		scriptLibrary.setScriptType(scriptLibrary.getScriptType());
		scriptLibrary.setUpdatedDate(date);
		scriptLibrary.setUpdatedBy(userDetailsVO.getUserName());
		scriptLibrary.setCreatedBy(userDetailsVO.getUserName());
		scriptLibrary.setIsCustomUpdated(1);
		
		ScriptLibraryVO	scriptLibVO	= convertEntityToVO(scriptLibrary);
		getCurrentSession().save(scriptLibrary);
		moduleVersionService.saveModuleVersion(scriptLibVO, null, scriptLibrary.getScriptLibId(), "jq_script_lib_details",
				sourceTypeId);
		return scriptLibrary.getScriptLibId();

	}
	
	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public ScriptLibraryVO convertEntityToVO(ScriptLibraryDetails scriptLibrary) {

		ScriptLibraryVO	scriptLibVO = new ScriptLibraryVO();
		scriptLibVO.setScriptlibId(scriptLibrary.getScriptLibId());
		scriptLibVO.setLibraryName(scriptLibrary.getLibraryName());
		scriptLibVO.setTemplateId(scriptLibrary.getTemplateId());
		scriptLibVO.setScriptType(scriptLibrary.getScriptType());
		scriptLibVO.setDescription(scriptLibrary.getDescription());
		scriptLibVO.setCreatedBy(scriptLibrary.getCreatedBy());
		scriptLibVO.setUpdatedBy(scriptLibrary.getUpdatedBy());
		scriptLibVO.setUpdatedDate(scriptLibrary.getUpdatedDate());
		scriptLibVO.setIscustomUpdated(scriptLibrary.getIsCustomUpdated());
		
		return scriptLibVO;
	}
	
	public Object listScriptEngines(String platformType) {
		ScriptEngineManager manager = new ScriptEngineManager();
		List<ScriptEngineFactory> factories = manager.getEngineFactories();
		boolean isScriptEngPresent = false;
		for (ScriptEngineFactory factory : factories) {
			for (String name : factory.getNames()) {
				if (name.trim().equalsIgnoreCase(platformType.trim())) {
					isScriptEngPresent = true;
					break;
				}
			}
			if (isScriptEngPresent) {
				break;
			}
		}
		return isScriptEngPresent;
	}
}

