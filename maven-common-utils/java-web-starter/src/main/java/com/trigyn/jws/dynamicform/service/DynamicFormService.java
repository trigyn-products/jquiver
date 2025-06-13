package com.trigyn.jws.dynamicform.service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trigyn.jws.dbutils.cipher.utils.CipherUtilFactory;
import com.trigyn.jws.dbutils.entities.AdditionalDatasourceRepository;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.service.ModuleVersionService;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.spi.PropertyMasterDetails;
import com.trigyn.jws.dbutils.utils.ActivityLog;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dbutils.utils.DBExtractor;
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
import com.trigyn.jws.dynarest.dao.JwsDynarestDAO;
import com.trigyn.jws.dynarest.entities.FileUploadConfig;
import com.trigyn.jws.dynarest.entities.JqScheduler;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDetail;
import com.trigyn.jws.dynarest.repository.JqschedulerRepository;
import com.trigyn.jws.dynarest.service.FileUploadConfigService;
import com.trigyn.jws.dynarest.service.FilesStorageService;
import com.trigyn.jws.dynarest.service.JwsDynamicRestDetailService;
import com.trigyn.jws.formio.dao.IFormIORepository;
import com.trigyn.jws.formio.entities.FormIO;
import com.trigyn.jws.formio.utils.FormIOUtils;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibraryConnection;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibraryDetails;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.webstarter.dao.ICaptchRepository;
import com.trigyn.jws.webstarter.service.CaptchaService;
import com.trigyn.jws.webstarter.vo.CaptchaDetails;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
@Transactional
public class DynamicFormService {

	private final static Logger				logger						= LoggerFactory.getLogger(DynamicFormService.class);

	private static final String				DATE						= "date";

	private static final String				TIMESTAMP					= "timestamp";

	private static final String				DECIMAL						= "decimal";

	private static final String				TEXT						= "text";

	private static final String				INT							= "int";

	private static final String				VARCHAR						= "varchar";

	private static final String				PRIMARY_KEY					= "PK";

	private static final String				BOOLEAN						= "boolean";
	private static final String				DATETIME					= "datetime";
	
	private static final String				TIME						= "time";
	
	private static final String				DATETIMEOFFSET				= "datetimeoffset";
	
	private static final String				XML							= "xml";

	@Autowired
	private TemplatingUtils					templateEngine				= null;

	@Autowired
	private DynamicFormCrudDAO				dynamicFormDAO				= null;

	@Autowired
	private PropertyMasterDAO				propertyMasterDAO			= null;

	@Autowired
	private DBTemplatingService				templateService				= null;

	@Autowired
	private FileUtilities					fileUtilities				= null;

	@Autowired
	private MenuService						menuService					= null;

	@Autowired
	private IUserDetailsService				userDetailsService			= null;

	@Autowired
	private PropertyMasterService			propertyMasterService		= null;

	@Autowired
	private PropertyMasterDetails			propertyMasterDetails		= null;

	@Autowired
	private ActivityLog						activitylog					= null;

	@Autowired
	private JqschedulerRepository			jqschedulerRepository		= null;

	@Autowired
	private AdditionalDatasourceRepository	additionalDatasourceRepository;

	@Autowired
	private FileUploadConfigService			fileUploadConfigService		= null;

	@Autowired
	private JwsDynarestDAO					dynamicRestDAO				= null;

	@Autowired
	protected SessionFactory				sessionFactory				= null;

	@Autowired
	private ModuleVersionService			moduleVersionService		= null;

	@Autowired
	private JwsDynamicRestDetailService		jwsDynamicRestDetailService	= null;

	@Autowired
	private FilesStorageService				filesStorageService			= null;

	@Autowired
	private IFormIORepository				formIORepository			= null;
	
	@Autowired
	private DynamicFormCrudDAO				dynamicFormCrudDAO			= null;
	
	@Autowired
	private ICaptchRepository				iCaptchRepository			= null;

	@Autowired
	private CaptchaService					captchaService				= null;

	@Autowired
	private DynamicFormHelperService		dynamicFormHelperService	= null;
	
	public String loadDynamicForm(String formId, Map<String, Object> requestParam, Map<String, Object> additionalParam)
			throws IOException, CustomStopException {
		logger.debug("Inside DynamicFormService.loadDynamicForm(formId: {}, requestParam: {}, additionalParam: {})",
				formId, requestParam, additionalParam);

		try {

			String				selectTemplateQuery	= null;
			String				selectQuery			= null;
			String				formBody			= null;
			Map<String, Object>	formHtmlTemplateMap	= new HashMap<>();
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
			requestParam.put("formId", form.getFormId());
			if (form != null && form.getFormIoId() != null) {
				requestParam.put("formIoId", form.getFormIoId());
				FormIOUtils	formIOUtils		= new FormIOUtils();
				Object		formMetaData	= formIOUtils.getFormMetaData(form.getFormId());
				if (formMetaData != null) {
					List<String> keyList = new ArrayList<>();
					requestParam.put("formMetaData", formMetaData);
					ObjectMapper	objectMapper	= new ObjectMapper();
					JsonNode		formIoJson		= objectMapper.readTree(formMetaData.toString());
					requestParam.put("isEdit", 0);
					FormIOUtils.traverse(formIoJson, form.getFormIoId(), keyList);
					for (String mprimaryKey : keyList) {
						if (requestParam.get(mprimaryKey) != null) {
							requestParam.put("primaryKeyValue", requestParam.get(mprimaryKey.replace("\"", "")));
							requestParam.put("isEdit", 1);
							requestParam.put("isCaptchaEnabled", form.getIsCaptchaEnabled());
							break;
						}
					}
				}
			}
			selectTemplateQuery = templateEngine.processTemplateContents(selectQuery, formName, requestParam);
			ScriptEngineManager	scriptEngineManager	= new ScriptEngineManager();
			ScriptEngine		scriptEngine		= null;
			scriptEngine = scriptEngineManager.getEngineByName(
					Constant.SelectQueryType.getqueryTypeID(form.getSelectQueryType()).getQueryTypeName());
			if (StringUtils.isNotEmpty(selectTemplateQuery)) {
				switch (form.getSelectQueryType()) {
					case Constant.SELECT:
						selectResultSet = dynamicFormDAO.executeQueries(form.getDatasourceId(),
								selectTemplateQuery.toString(), requestParam);
						break;
					case Constant.JAVASCRIPT:
						selectResultSet = loadScriptEngineExecution(scriptEngine, selectTemplateQuery, selectResultSet);
						break;
					case Constant.PYTHON:
						selectResultSet = loadScriptEngineExecution(scriptEngine, selectTemplateQuery, selectResultSet);
						break;
					case Constant.PHP:
						selectResultSet = loadScriptEngineExecution(scriptEngine, selectTemplateQuery, selectResultSet);
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
			String	contextSuggestions	= IMonacoSuggestion.getTemplateSuggestion();
			String	jSSuggestions		= IMonacoSuggestion.getJSSuggestion(additionalDatasourceRepository);
			String	filejSSuggestions	= IMonacoSuggestion.getfileJSSuggestion(additionalDatasourceRepository);
			formHtmlTemplateMap.put("formId", formId);
			formHtmlTemplateMap.put("userDetails", userDetails);
			formHtmlTemplateMap.put("requestDetails", requestParam);
			formHtmlTemplateMap.put("entityType", "form");
			formHtmlTemplateMap.put("entityName", formName);
			formHtmlTemplateMap.put("suggestions", contextSuggestions);
			formHtmlTemplateMap.put("JSsuggestions", jSSuggestions);
			formHtmlTemplateMap.put("JSfilesuggestions", filejSSuggestions);
			formHtmlTemplateMap.put("environment", environment);
			if (form != null && form.getFormIoId() != null) {
				requestParam.put("formId", form.getFormId());
				requestParam.put("formIoId", form.getFormIoId());
				FormIOUtils	formIOUtils		= new FormIOUtils();
				Object		formMetaData	= formIOUtils.getFormMetaData(form.getFormId());
				formHtmlTemplateMap.put("formMetaData", formMetaData);
				if (requestParam.get("primaryKeyValue") != null) {
					formHtmlTemplateMap.put("isEdit", 1);
					formHtmlTemplateMap.put("primaryKeyValue", requestParam.get("primaryKeyValue"));
				} else {
					formHtmlTemplateMap.put("primaryKeyValue", "");
				}
				formHtmlTemplateMap.put("formMetaData", formMetaData);
				formHtmlTemplateMap.put("isCaptchaEnabled", form.getIsCaptchaEnabled());
			}
			// templateHtml = templateEngine.processTemplateContents(formBody, formName,
			// formHtmlTemplateMap);
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

	public List<Map<String, Object>> loadScriptEngineExecution(ScriptEngine scriptEngine, String selectTemplateQuery,
			List<Map<String, Object>> selectResultSet) throws Exception, CustomStopException {
		StringBuilder		resultStringBuilder	= new StringBuilder();
		Map<String, Object>	map					= new HashMap<>();
		if (scriptEngine.getFactory().getLanguageName().equalsIgnoreCase("ECMAScript")) {
			TemplateVO templateVO = templateService.getTemplateByName("script-util");
			resultStringBuilder.append(templateVO.getTemplate()).append("\n");
		}
		resultStringBuilder.append(selectTemplateQuery.toString());
		try {
			StringWriter stringWriter = new StringWriter();
			scriptEngine.getContext().setWriter(new PrintWriter(stringWriter));
			Object scriptResult = scriptEngine.eval(resultStringBuilder.toString());
			if (scriptEngine.getFactory().getLanguageName().equalsIgnoreCase("python")) {
				// If the method has a return statement
				if (scriptEngine.get("result") != null) {
					Object result = scriptEngine.get("result");
					map = (Map<String, Object>) result;
				}
			} else if (scriptEngine.getFactory().getLanguageName().equalsIgnoreCase("php")) {
				System.out.println("Sys Out is necessary for PHP" + scriptResult);
				Object result = stringWriter.toString();
				if (result instanceof Map) {
					map = (Map<String, Object>) result;
				} else if (result instanceof String) {
					String			jsonString		= (String) result;
					ObjectMapper	objectMapper	= new ObjectMapper();
					map = objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {
					});
				}
			} else {
				map = (Map<String, Object>) scriptResult;
			}
			if (map != null) {
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
				JwsDynamicRestDetail	jwsDynamicRestDetial	= dynamicRestDAO
						.findDynamicRestByUrl(formData.getFirst("primaryId"));
				Integer					dynarestRequestTypeId	= jwsDynamicRestDetial.getJwsRequestTypeId();
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

	public Boolean saveDynamicForm(MultiValueMap<String, String> formData,HttpServletResponse httpServletResponse) throws Exception, CustomStopException {
		logger.debug("Inside DynamicFormService.saveDynamicForm(formData: {})", formData);
		try {
			String saveTemplateQuery = null;
			if (!"1".equalsIgnoreCase(formData.getFirst("edit")) && null != formData.getFirst("fileBinId")) {
				FileUploadConfig existingfileUploadConfig = fileUploadConfigService
						.getFileUploadConfigByBinId(formData.getFirst("fileBinId"));
				if (null != existingfileUploadConfig) {
					logger.error("Error in Dynamic Form, file bin already exist ", formData.getFirst("fileBinId"));
					throw new RuntimeException(HttpStatus.PRECONDITION_FAILED.toString());
				}
			}
			String		formId	= formData.getFirst("formId");
			DynamicForm	form	= dynamicFormDAO.findDynamicFormById(formId);
			form.setIsCustomUpdated(1);
			String				formName		= form.getFormName();
			Map<String, Object>	saveTemplateMap	= new HashMap<>();
			Map<String, Object>	resultSetMap	= new HashMap<>();
			saveTemplateMap.put("formData", formData);
			formData.forEach((key, value) -> saveTemplateMap.put(key, value));
			String						environment				= propertyMasterDAO.findPropertyMasterValue("system",
					"system", "profile");
			String						saveQuery				= "saveQuery-";
			List<DynamicFormSaveQuery>	dynamicFormSaveQueries	= dynamicFormDAO.findDynamicFormQueriesById(formId);
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
						for (int iCounter = 0; iCounter < objScriptLib.size(); iCounter++) {
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
						fileUtilities.customSendError(httpServletResponse, HttpStatus.BAD_REQUEST.value(),
								ExceptionUtils.getStackTrace(a_thr));
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
						fileUtilities.customSendError(httpServletResponse, HttpStatus.PRECONDITION_FAILED.value(),
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
				ScriptEngineManager	scriptEngineManager	= new ScriptEngineManager();
				ScriptEngine		scriptEngine		= null;
				scriptEngine = scriptEngineManager.getEngineByName(
						Constant.QueryType.getqueryTypeID(dynamicFormSaveQuery.getDaoQueryType()).getQueryTypeName());
				if (dynamicFormSaveQuery.getDaoQueryType() != null) {
					switch (dynamicFormSaveQuery.getDaoQueryType()) {
						case Constant.DML:
							try {
								Integer affectedRowCount = dynamicFormDAO.saveFormData(form.getDatasourceId(),
										saveTemplateQuery, saveTemplateMap);
								saveTemplateMap.put(dynamicFormSaveQuery.getResultVariableName(), affectedRowCount);
							} catch (Throwable a_thr) {
								logger.error(ExceptionUtils.getStackTrace(a_thr));
								fileUtilities.customSendError(httpServletResponse, HttpStatus.BAD_REQUEST.value(),
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
								fileUtilities.customSendError(httpServletResponse, HttpStatus.BAD_REQUEST.value(),
										ExceptionUtils.getStackTrace(a_thr));
							}
							break;
						case Constant.JS:
							scriptEngineExecution(fileMap, httpServletRequest, httpServletResponse, formDetails,
									saveTemplateQuery, null, dynamicFormSaveQuery, scriptEngine);
							break;
						case Constant.PY:
							scriptEngineExecution(fileMap, httpServletRequest, httpServletResponse, formDetails,
									saveTemplateQuery, null, dynamicFormSaveQuery, scriptEngine);
							break;
						case Constant.QPHP:
							scriptEngineExecution(fileMap, httpServletRequest, httpServletResponse, formDetails,
									saveTemplateQuery, null, dynamicFormSaveQuery, scriptEngine);
							break;
						default:
							return null;
					}
				}

			}
			httpServletRequest.getSession().removeAttribute(sessiongCaptcha.toString());
		} else if (formCaptcha.equals(generatedCaptcha) == false) {
			logger.error("Invalid Captcha while saving dynamic form. formCaptcha{}: ", formCaptcha);
			fileUtilities.customSendError(httpServletResponse, HttpStatus.PRECONDITION_FAILED.value(),
					"Invalid Captcha");
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

	public Map<String, Object> saveDynamicFormV2(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletRepsonse, Map<String, Object> parameterMap)
			throws CustomStopException, Exception {
		List<Map<String, String>>	formData			= null;
		Map<String, Object>			result				= new HashMap<>();
		String						generatedCaptcha	= null;
		String						formCaptcha			= null;
		String						formId				= httpServletRequest.getParameter("formId").toString();
		// StringBuilder sessiongCaptcha = new StringBuilder(formId).append("_captcha");
		String						keyInitials			= Constant.INVALID_CAPTCHA_KEY + "," + Constant.NO_CAPTCHA_KEY
				+ "," + Constant.CAPTCHA_EXPIRED_KEY;
		String						captchaRequest_Id	= httpServletRequest.getHeader("r");
		getResourceBundledata(keyInitials, httpServletRequest);

		if (parameterMap.get("isCaptchaEnabled") != null) {
			if (("1").equalsIgnoreCase(parameterMap.get("isCaptchaEnabled").toString()) == true) {
				// removed
				// httpServletRequest.getSession().getAttribute(sessiongCaptcha.toString()) !=
				// null &&
				if (("undefined").equalsIgnoreCase(parameterMap.get("formCaptcha").toString()) == false) {
					// Delete expired captcha irrespective of request id, check captcha with
					// required id, fetch captcha for DB and pass to generatedCaptcha variable and
					// if matches delete the captcha .
					captchaService.deleteExpiredCaptcha();
					CaptchaDetails captchaDetails = null;
					formCaptcha = parameterMap.get("formCaptcha").toString();
					if (formCaptcha != null && StringUtils.isBlank(captchaRequest_Id) == false
							&& captchaRequest_Id != null) {

						captchaDetails = iCaptchRepository.findById(captchaRequest_Id).orElse(null);
						if (null != captchaDetails) {
							generatedCaptcha = captchaDetails.getCaptcha();
						} else {
							logger.error("Captcha has Expired!");
							fileUtilities.customSendError(httpServletRepsonse, HttpStatus.PRECONDITION_FAILED.value(),
									Constant.CAPTCHA_EXPIRED);
							return null;
						}
					}

					if (formCaptcha.equals(generatedCaptcha) == false) {
						logger.error("Invalid Captcha while saving dynamic form, formCaptcha : {} .", formCaptcha);
						fileUtilities.customSendError(httpServletRepsonse, HttpStatus.PRECONDITION_FAILED.value(),
								Constant.INVALID_CAPTCHA_MESSAGE);
					} else if ((formCaptcha != null && formCaptcha.equals(generatedCaptcha) == true)) {
						if (("1").equalsIgnoreCase(parameterMap.get("isCsrfEnabled").toString()) == true) {
							formData = getDecryptFormData(httpServletRequest, parameterMap);
						} else {
							formData = new Gson().fromJson(httpServletRequest.getParameter("formData"), List.class);
						}
						result = saveDynamicFormV2(formData, httpServletRequest, httpServletRepsonse);
						saveFileBinV2(formData);
						// remove Captcha form DB
						if (StringUtils.isBlank(captchaRequest_Id) == false) {
							captchaService.deleteCaptcha(captchaRequest_Id);
						}
					}
				} else {
					logger.error("Captcha not found in request body.");
					fileUtilities.customSendError(httpServletRepsonse, HttpStatus.PRECONDITION_FAILED.value(),
							Constant.CAPTCHA_NOT_FOUND_MESSAGE);
				}
			}
		} else {
			formData	= new Gson().fromJson(httpServletRequest.getParameter("formData"), List.class);
			result		= saveDynamicFormV2(formData, httpServletRequest, httpServletRepsonse);
			saveFileBinV2(formData);
		}

		return result;
	}

	public void saveFileBinV2(List<Map<String, String>> formData) throws Exception {
		for (Map<String, String> formEntry : formData) {
			if (formEntry.containsKey("valueType") && formEntry.get("valueType").equalsIgnoreCase("fileBin")
					&& formEntry.get("fileUploadTempId") != null) {
				filesStorageService.commitChanges(formEntry.get("FileBinID"), formEntry.get("fileAssociationID"),
						formEntry.get("fileUploadTempId"));
			}
		}
	}

	public void getResourceBundledata(String keyInitials, HttpServletRequest httpServletRequest) {
		Map<String, String> getResourceBundledata = jwsDynamicRestDetailService.getResourceData(httpServletRequest,
				keyInitials);

		for (Entry<String, String> set : ((Map<String, String>) getResourceBundledata).entrySet()) {
			if (("jws.invalidCaptcha").equalsIgnoreCase(set.getKey())) {
				Constant.INVALID_CAPTCHA_MESSAGE = set.getValue();
			} else if (("jws.noCaptcha").equalsIgnoreCase(set.getKey())) {
				Constant.CAPTCHA_NOT_FOUND_MESSAGE = set.getValue();
			} else if (("jws.expiredCaptcha").equalsIgnoreCase(set.getKey())) {
				Constant.CAPTCHA_EXPIRED = set.getValue();
			}
		}
	}

	public Map<String, Object> saveDynamicFormV2(List<Map<String, String>> formData,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws Exception, CustomStopException {
		logger.debug("Inside DynamicFormService.saveDynamicForm(formData: {})", formData.get(0));

		Map<String, Object>	formDetails		= createParamterMap(formData);
		String				formId			= httpServletRequest.getParameter("formId").toString();
		Map<String, Object>	resultSetMap	= new HashMap<>();

		/* Method called for implementing Activity Log */
		logActivityV2(formData, "", "", "");
		Map<String, FileInfo>				fileMap				= new HashMap<>();
		Map<String, Object>					saveTemplateMap		= new HashMap<>();
		String								saveTemplateQuery	= null;
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
					fileUtilities.customSendError(httpServletResponse, HttpStatus.PRECONDITION_FAILED.value(),
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

			ScriptEngineManager	scriptEngineManager	= new ScriptEngineManager();
			ScriptEngine		scriptEngine		= null;
			scriptEngine = scriptEngineManager.getEngineByName(
					Constant.QueryType.getqueryTypeID(dynamicFormSaveQuery.getDaoQueryType()).getQueryTypeName());
			if (dynamicFormSaveQuery.getDaoQueryType() != null) {
				switch (dynamicFormSaveQuery.getDaoQueryType()) {
					case Constant.DML:
						try {
							Integer affectedRowCount = dynamicFormDAO.saveFormData(form.getDatasourceId(),
									saveTemplateQuery, saveTemplateMap);
							resultSetMap.put(dynamicFormSaveQuery.getResultVariableName(), affectedRowCount);
						} catch (Throwable a_thr) {
							logger.error(ExceptionUtils.getStackTrace(a_thr));
							fileUtilities.customSendError(httpServletResponse, HttpStatus.BAD_REQUEST.value(),
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
							fileUtilities.customSendError(httpServletResponse, HttpStatus.BAD_REQUEST.value(),
									ExceptionUtils.getStackTrace(a_thr));
						}
						break;
					case Constant.JS:
						scriptEngineExecution(fileMap, httpServletRequest, httpServletResponse, formDetails,
								saveTemplateQuery, resultSetMap, dynamicFormSaveQuery, scriptEngine);
						break;
					case Constant.PY:
						scriptEngineExecution(fileMap, httpServletRequest, httpServletResponse, formDetails,
								saveTemplateQuery, resultSetMap, dynamicFormSaveQuery, scriptEngine);
						break;
					case Constant.QPHP:
						scriptEngineExecution(fileMap, httpServletRequest, httpServletResponse, formDetails,
								saveTemplateQuery, resultSetMap, dynamicFormSaveQuery, scriptEngine);
						break;
					default:
						return null;
				}
			}
		}
		return resultSetMap;
	}

	public Map<String, Object> scriptEngineExecution(Map<String, FileInfo> fileMap,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Map<String, Object> formDetails, String saveTemplateQuery, Map<String, Object> resultSetMap,
			DynamicFormSaveQuery dynamicFormSaveQuery, ScriptEngine scriptEngine)
			throws Exception, CustomStopException {

		try {
			StringBuilder resultStringBuilder = new StringBuilder();

			if (scriptEngine.getFactory().getLanguageName().equalsIgnoreCase("ECMAScript")) {
				TemplateVO templateVO = templateService.getTemplateByName("script-util");
				resultStringBuilder.append(templateVO.getTemplate()).append("\n");
			}
			scriptEngine.put("requestDetails", formDetails);

			if (fileMap != null && fileMap.size() > 0) {
				scriptEngine.put("files", fileMap);
			}
			scriptEngine.put("httpRequestObject", httpServletRequest);
			resultStringBuilder.append(saveTemplateQuery.toString()).append("\n");
			List<Object> objScriptLib = dynamicFormDAO.scriptLibExecution(dynamicFormSaveQuery.getDynamicFormQueryId());
			for (int iCounter = 0; iCounter < objScriptLib.size(); iCounter++) {
				resultStringBuilder.append(objScriptLib.get(iCounter)).append("\n");
			}
			try {
				StringWriter stringWriter = new StringWriter();
				scriptEngine.getContext().setWriter(new PrintWriter(stringWriter));
				Object scriptResult = scriptEngine.eval(resultStringBuilder.toString());
				if (scriptEngine.getFactory().getLanguageName().equalsIgnoreCase("python")) {
					// If the method has a return statement
					if (scriptEngine.get("result") != null) {
						Object result = scriptEngine.get("result");
						resultSetMap.put(dynamicFormSaveQuery.getResultVariableName(), result);
					}
				} else if (scriptEngine.getFactory().getLanguageName().equalsIgnoreCase("php")) {
					System.out.println("Sys Out is necessary for PHP" + scriptResult);
					String result = stringWriter.toString();
					resultSetMap.put(dynamicFormSaveQuery.getResultVariableName(), result);
				} else {
					if (null != scriptResult) {
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
			fileUtilities.customSendError(httpServletResponse, HttpStatus.BAD_REQUEST.value(),
					ExceptionUtils.getStackTrace(a_thr));

		}
		return resultSetMap;

	}

	public List<Map<String, String>> getDecryptFormData(HttpServletRequest httpServletRequest,
			Map<String, Object> parameterMap) throws InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, Exception {
		List<Map<String, String>>	formData	= null;
		String						formCaptcha	= null;
		if (parameterMap.get("isCsrfEnabled") != null
				&& ("1").equalsIgnoreCase(parameterMap.get("isCsrfEnabled").toString()) == true) {
			if (parameterMap.get("formCaptcha") != null) {
				formCaptcha = parameterMap.get("formCaptcha").toString();
				String paddedKey = padOrTruncateKey(formCaptcha, 16);
				if (httpServletRequest.getParameter("formData") != null) {
					String	formEncryptData	= httpServletRequest.getParameter("formData");
					String	decryptedString	= CipherUtilFactory
							.getCipherUtil(Constant.AES, Constant.ECB, Constant.PKCS5PADDING, 128)
							.decrypt(formEncryptData, paddedKey, Constant.AES);
					formData = new Gson().fromJson(decryptedString, List.class);
				}
			}
		}
		return formData;
	}

	public static String padOrTruncateKey(String key, int length) {
		if (key.length() < length) {
			return String.format("%-" + length + "s", key).replace(' ', '0');
		} else if (key.length() > length) {
			return key.substring(0, length);
		}
		return key;
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
			String moduleURL, String additionalDataSourceId, String dbProductName, Boolean toggleCaptcha,
			Boolean toggleCsrf, Boolean toggleFileBin, String fileBinId, String fileAssociationId)
			throws CustomStopException, JsonProcessingException {
		logger.debug(
				"Inside DynamicFormService.createDefaultFormByTableName(tableName: {}, tableDetails: {}, moduleURL: {}, additionalDataSourceId: {}, dbProductName: {})",
				tableName, tableDetails, moduleURL, additionalDataSourceId, dbProductName, toggleCaptcha, toggleCsrf,
				toggleFileBin, fileBinId, fileAssociationId);

		Map<String, String>	templatesMap	= new HashMap<>();
		Map<String, Object>	parameters		= new HashMap<>();
		Map<String, String>	regexMap		= new HashMap<>();
		parameters.put("columnDetails", tableDetails);
		parameters.put("formName", tableName);
		parameters.put("toggleCaptcha", toggleCaptcha);
		parameters.put("toggleCsrf", toggleCsrf);
		parameters.put("toggleFileBin", toggleFileBin);
		parameters.put("fileBinId", fileBinId);
		parameters.put("fileAssociationId", "${(resultSetObject." + fileAssociationId + ")!''}");
		parameters.put("editCondition", fileAssociationId);
		if (StringUtils.isBlank(moduleURL) == false) {
			parameters.put("moduleURL", moduleURL);
		}
		StringBuilder jsonBuilder = new StringBuilder();
		jsonBuilder.append("[\n");
		int inc = 0;
		int totalValidFields = 0;
		for (Map<String, Object> info : tableDetails) {
		    if (info.get("regexValidation") != null 
		        && !info.get("regexValidation").toString().isEmpty()
		        && !"hidden".equals(info.get("columnType"))
		        && "false".equalsIgnoreCase(info.get("autoIncrement").toString())) {
		        totalValidFields++;
		    }
		}
		for (Map<String, Object> info : tableDetails) {
			String	columnName		= info.get("tableColumnName").toString();
			String	columnKey		= info.get("columnKey").toString();
			String	regexInfo		= info.get("regexValidation").toString();
			String	columnType		= info.get("columnType").toString();
			String	isAutoIncrement	= info.get("autoIncrement").toString();
			String	dataType		= info.get("dataType").toString();
			if(regexInfo != null && !regexInfo.isEmpty() && columnType.equals("hidden") == false && isAutoIncrement.equalsIgnoreCase("false")) {
				
				String displayName = columnName.replaceAll("_", "");
			    String escapedRegex =  StringEscapeUtils.escapeJson(regexInfo);
			    jsonBuilder.append("\t\t{\n");
			    jsonBuilder.append("\t\t    \"regexPattern\" : \"").append(escapedRegex).append("\",\n");
			    jsonBuilder.append("\t\t    \"fieldName\" : \"").append(displayName).append("\",\n");
			    jsonBuilder.append("\t\t    \"dataType\" : \"").append(dataType).append("\"\n");
			    jsonBuilder.append("\t\t}");
			    inc++;
			    if (inc < totalValidFields) {
		            jsonBuilder.append(",");
		        }
			    jsonBuilder.append("\n");
			    regexMap.put(columnName.replaceAll("_", ""), regexInfo);
			}
		}
		jsonBuilder.append("\t];\n");
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonRegex = mapper.writeValueAsString(regexMap);
 		parameters.put("fieldList", jsonBuilder);
 		parameters.put("dbProductName", dbProductName);
 		
		try {
			TemplateVO	templateVO	= templateService.getTemplateByName("system-form-html-template");
			String		template	= templateEngine.processTemplateContents(templateVO.getTemplate(),
					templateVO.getTemplateName(), parameters);
			templatesMap.put("form-template", template);

			Map<String, Object> selectParameters = new HashMap<>();
			selectParameters.put("tableName", tableName);
			templateVO	= templateService.getTemplateByName("system-form-select-template");
			template	= templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
					selectParameters);
			templatesMap.put("select-template", template);

		} catch (CustomStopException custStopException) {
			logger.error("Error occured in createDefaultFormByTableName.", custStopException);
			throw custStopException;
		} catch (Exception a_excep) {
			logger.error("Error occured in createDefaultFormByTableName.", a_excep);
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
			Map<String, String> templatesMap, String additionalDataSourceId, String dbProductName)
			throws CustomStopException, JsonProcessingException {
		logger.debug(
				"Inside DynamicFormService.getTableInformationByName(tableInformation: {}, tableName: {}, additionalDataSourceId: {}, dbProductName: {})",
				tableInformation, tableName, templatesMap, additionalDataSourceId, dbProductName);

		StringJoiner	insertJoiner		= new StringJoiner(",", "INSERT INTO " + tableName + " (", ")");
		StringJoiner	insertValuesJoiner	= null;
		boolean			isIntPK				= false;
		int				coloumnCounter		= tableInformation.size();
		boolean			isAutoID			= false;
		for (Map<String, Object> info : tableInformation) {
			if (info.get("columnType") == null) {

				continue;
			}
			String	columnName		= info.get("tableColumnName").toString();
			String	dataType		= info.get("dataType").toString();
			String	columnKey		= info.get("columnKey").toString();
			String	isAutoIncrement	= info.get("autoIncrement").toString();
			if (isAutoIncrement.equalsIgnoreCase("true")) {
				isAutoID = true;
			}
			insertValuesJoiner = dynamicFormHelperService.createInsertQuery(insertValuesJoiner, tableName, columnName,
					dataType, columnKey, dbProductName, isAutoIncrement, null);
			if (columnKey != null && PRIMARY_KEY.equals(columnKey)) {
				if ("false".equalsIgnoreCase(isAutoIncrement)) {
					insertJoiner.add(columnName);
				}
				if (INT.equalsIgnoreCase(dataType) || DECIMAL.equalsIgnoreCase(dataType)) {
					isIntPK = true;
				}
			}
		}

		for (Map<String, Object> info : tableInformation) {
			if (info.get("columnType") == null) {
				coloumnCounter--;
				continue;
			}

			String	columnName		= info.get("tableColumnName").toString();
			String	dataType		= info.get("dataType").toString();
			String	columnKey		= info.get("columnKey").toString();
			String	columnType		= info.get("columnType").toString();
			String	isAutoIncrement	= info.get("autoIncrement").toString();
			if (StringUtils.isBlank(columnKey) || PRIMARY_KEY.equals(columnKey) == false) {
				insertJoiner.add(columnName);
				joinQueryBuilder(insertValuesJoiner, columnName, dataType, false, columnType, dbProductName,
						coloumnCounter, isAutoIncrement, columnKey);
			}
			coloumnCounter--;
		}
		StringBuilder queryBuilder = new StringBuilder(insertJoiner.toString());
		queryBuilder.append(insertValuesJoiner);

		if (!isAutoID) {
			if (isIntPK) {
				queryBuilder.append(" FROM " + tableName);
			} else {
				queryBuilder.append(")");
			}
		} else {
			queryBuilder.append(")");
		}

		Map<String, Object> saveQueryparameters = new HashMap<>();
		saveQueryparameters.put("insertQuery", queryBuilder.toString());

		StringJoiner	updateQuery			= new StringJoiner(",", "UPDATE " + tableName + " SET ", "");
		StringJoiner	updateWhereQuery	= new StringJoiner(" AND ", " WHERE ", "");
		Map<String, String>	regexMap		= new HashMap<>();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		
		StringBuilder jsonBuilder = new StringBuilder();
		jsonBuilder.append("[\n");
		int inc = 0;
		int totalValidFields = 0;
		for (Map<String, Object> info : tableInformation) {
		    if (info.get("regexValidation") != null 
		        && !info.get("regexValidation").toString().isEmpty()
		        && !"hidden".equals(info.get("columnType"))
		        && "false".equalsIgnoreCase(info.get("autoIncrement").toString())) {
		        totalValidFields++;
		    }
		}
		for (Map<String, Object> info : tableInformation) {
			if (info.get("columnType") == null) {
				coloumnCounter--;
				continue;
			}
			String	columnName		= info.get("tableColumnName").toString();
			String	dataType		= info.get("dataType").toString();
			String	columnKey		= info.get("columnKey").toString();
			String	columnType		= info.get("columnType").toString();
			String	isAutoIncrement	= info.get("autoIncrement").toString();
			String	regexInfo		= info.get("regexValidation").toString();
			if ("PK".equals(columnKey)) {
				joinQueryBuilder(updateWhereQuery, columnName, dataType, true, columnType, dbProductName,
						coloumnCounter, isAutoIncrement, columnKey);
			} else {
				joinQueryBuilder(updateQuery, columnName, dataType, true, columnType, dbProductName, coloumnCounter,
						isAutoIncrement, columnKey);
			}
			if(regexInfo != null && !regexInfo.isEmpty() && "hidden".equals(columnType) == false && "false".equalsIgnoreCase(isAutoIncrement)) {
			    String displayName = columnName.replaceAll("_", "");
			    String escapedRegex =  StringEscapeUtils.escapeJson(regexInfo);
			   
			    jsonBuilder.append("\t{\n");
			    jsonBuilder.append("\t\t\"regexPattern\" : \"").append(escapedRegex).append("\",\n");
			    jsonBuilder.append("\t\t\"fieldValue\" : ").append(displayName).append("!''").append(",\n");
			    jsonBuilder.append("\t\t\"fieldName\" : \"").append(displayName).append("\",\n");
			    jsonBuilder.append("\t\t\"dataType\" : \"").append(dataType).append("\"\n");
			    jsonBuilder.append("\t}");

			    inc++;
			    if (inc < totalValidFields) {
		            jsonBuilder.append(",");
		        }
		        jsonBuilder.append("\n");
			}
		}
		jsonBuilder.append("]\n");
		
        saveQueryparameters.put("fieldList", jsonBuilder);
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
			logger.error("Error occured in createSaveUpdateQueryTemplate.", a_excep);
		}
	}

	private void joinQueryBuilder(StringJoiner insertValuesJoiner, String columnName, String dataType,
			boolean showColumnName, String columnType, String dbProductName, int columnCounter,
			String isAutoIncrement, String columnKey) {

		logger.debug(
				"Inside joinQueryBuilder(columnName: {}, dataType: {}, showColumnName: {}, columnType: {}, dbProductName: {})",
				columnName, dataType, showColumnName, columnType, dbProductName);


		if (insertValuesJoiner == null) {
			insertValuesJoiner = new StringJoiner("");
		}

		String	formFieldName	= columnName.replace("_", "");
		String	value;
		
		if (dbProductName != null && (dbProductName.contains(Constant.POSTGRESQL) || dbProductName.contains(Constant.DEFAULT) || dbProductName.contains(Constant.MARIADB))) {
			if ("false".equalsIgnoreCase(isAutoIncrement)) {
				value = DBExtractor.getCastExpression(dataType, columnName, formFieldName, showColumnName, columnType, dbProductName);
				insertValuesJoiner.add(value);
			} else {
				value = showColumnName ? columnName + " = :" + formFieldName : ":" + formFieldName;
				insertValuesJoiner.add(value.replace("\\", ""));
			}
			if (columnCounter > 1) {
				insertValuesJoiner.add(",");
			}
		} else {
			StringBuilder	formFieldVal	= new StringBuilder();

			if (isAutoIncrement.equalsIgnoreCase("false")) {
				if (TEXT.equalsIgnoreCase(dataType) ) {
					value = showColumnName ? columnName + " = :" + formFieldName : ":" + formFieldName;
					insertValuesJoiner.add(value.replace("\\", ""));
				} else if (Constant.UNIQUEID.equalsIgnoreCase(dataType) || XML.equalsIgnoreCase(dataType)){
					if (dbProductName != null && dbProductName.contains(Constant.MSSQLSERVER)) {
						String paramPlaceholder = "NULLIF(:" + formFieldName + ", '')";
						value = showColumnName ? columnName + " = " + paramPlaceholder : paramPlaceholder;
						insertValuesJoiner.add(value.replace("\\", ""));
					}
				} else if (INT.equalsIgnoreCase(dataType) || DECIMAL.equalsIgnoreCase(dataType)
						|| BOOLEAN.equalsIgnoreCase(dataType)) {
					String paramPlaceholder = null;
					if (dbProductName != null && dbProductName.contains(Constant.MSSQLSERVER)) {
						 paramPlaceholder = "NULLIF(:" + formFieldName + ", '')";
					}else if (dbProductName != null && dbProductName.contains(Constant.ORACLE)) {
						 paramPlaceholder = "NULLIF(:" + formFieldName + ", NULL)";
					}
					if(paramPlaceholder != null) {
						value = showColumnName ? columnName + " = " + paramPlaceholder : paramPlaceholder;
						insertValuesJoiner.add(value.replace("\\", ""));
					}
				} else if (DATE.equalsIgnoreCase(dataType) || DATETIME.equalsIgnoreCase(dataType) || DATETIMEOFFSET.equalsIgnoreCase(dataType)) {
					if ("hidden".equals(columnType) == false) {
						value = "";
						if (dbProductName != null && dbProductName.contains(Constant.POSTGRESQL)) {
							value = "TO_DATE(:".concat(formFieldName).concat(", 'DD-MONTH-YYYY') ");
						} else if (dbProductName != null && dbProductName.contains(Constant.MSSQLSERVER)) {
							if ("smalldatetime".equalsIgnoreCase(dataType)) {
								value = "CONVERT(datetime, :" + formFieldName + ", 120)";
							} else if ("datetime2".equalsIgnoreCase(dataType)) {
								value = "CONVERT(datetime2, :" + formFieldName + ", 120)";
							} else if ("datetimeoffset".equalsIgnoreCase(dataType)) {
								value = "CAST(:" + formFieldName + " AS datetimeoffset)";
							} else {
								value = "CONVERT(datetime, :" + formFieldName + ", 120)";
							}
						} else if (dbProductName != null && dbProductName.contains(Constant.ORACLE)) {
							value = "TO_TIMESTAMP(:".concat(formFieldName).concat(", 'DD-MONTH-YYYY HH:MI:SS AM') ");
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
				} else if (TIME.equalsIgnoreCase(dataType)) {
					if ("hidden".equals(columnType) == false) {
						value = "";
						if (dbProductName != null && dbProductName.contains(Constant.MSSQLSERVER)) {
							value = "CONVERT(time,:".concat(formFieldName).concat(") ");
							formFieldVal.append(value);
						} else {
							String param = ":" + formFieldName;
							String nullSafeParam = String.format("NULLIF(%s, '')", param); // or check for 'null' string if needed
							value = String.format("STR_TO_DATE(%s, '%%H:%%i:%%s')", nullSafeParam);
							formFieldVal.append(value);

						}
					}
					insertValuesJoiner.add(showColumnName ? columnName + " = " + formFieldVal : "" + formFieldVal);
				}
			} else {
				value = showColumnName ? columnName + " = :" + formFieldName : ":" + formFieldName;
				insertValuesJoiner.add(value.replace("\\", ""));
			}
			if (columnCounter > 1) {
				value = ",";
				insertValuesJoiner.add(value);
			}
		}
	}

	private Map<String, Object> createParamterMap(List<Map<String, String>> formData) {
		logger.debug("Inside DynamicFormService.createParamterMap(formData: {})", formData);
		List				tmpValue				= null;

		Map<String, Object>	formParameters			= new HashMap<String, Object>();
		String				formId					= null;
		String				saveQueryParametersType	= null;
		for (Map<String, String> fmData : formData) {
			if (fmData.get("name").toString().equalsIgnoreCase("formId")) {
				formId = fmData.get("value").toString();
				break;
			}
		}
		//
		DynamicForm form = dynamicFormDAO.findDynamicFormById(formId);
		if (formData != null) {
			for (Map<String, String> data : formData) {
				String					valueType			= data.getOrDefault("valueType", VARCHAR);
				String					tmpName				= data.get("name");
				boolean					isFormIO			= false;
				HashMap<String, String>	saveQueryParamMap	= new HashMap<>();
				if (form != null && form.getFormIoId() != null && form.getFormIoId().isBlank() == false
						&& form.getFormIoId().isEmpty() == false) {
					for (Map<String, String> fmData : formData) {
						if (fmData.get("name").toString().equalsIgnoreCase("saveQueryParametersType")) {
							saveQueryParametersType	= fmData.get("value").toString();
							saveQueryParamMap		= new Gson().fromJson(saveQueryParametersType,
									new TypeToken<HashMap<String, String>>() {
															}.getType());
							break;
						}
					}
					isFormIO = true;
				}
				Object value = getDataInTypeFormat(data.get("value"), valueType);
				if (isFormIO && form.getFormIoId() != null) {
					formParameters = setParamaterMapForFormIO(formParameters, data, form.getFormIoId(), tmpName, value,
							tmpValue, saveQueryParamMap);
				}
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

	private Map<String, Object> setParamaterMapForFormIO(Map<String, Object> formParameters, Map<String, String> data,
			String formIoId, String tmpName, Object value, List tmpValue, HashMap<String, String> saveQueryParamMap) {
		String	item			= tmpName;
		boolean	isInputElement	= false;
		String	rePattern		= "\\[(.*?)]";
		Pattern	pattern				= Pattern.compile(rePattern);
		Matcher	matcher				= pattern.matcher(item);
		if (item != null) {
			while (matcher.find()) {
				for (Map.Entry<String, String> entry : saveQueryParamMap.entrySet()) {
					String	key	= entry.getKey();
					String	val	= entry.getValue();
					if (matcher.group(1).contentEquals(key)) {
						tmpName			= matcher.group(1);
						isInputElement	= true;
						break;
					}

				}
			}
		}
		if (isInputElement == false) {
			return formParameters;
		}
		// TODO :: Can we move out side of for loop.
		FormIO formIo = (FormIO) formIORepository.findById(formIoId).orElse(null);
		if (formIo != null && tmpName != null && tmpName.isEmpty() == false) {
			String	fieldName	= tmpName;
			String	dataType	= saveQueryParamMap.get(fieldName);
			value = getDataInTypeFormat(data.get("value"), dataType);
		}
		
		if (formParameters != null && formParameters.containsKey(tmpName)) {
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
		return formParameters;
	}

	private Object getDataInTypeFormat(Object value, String valueType) {

		logger.debug("Inside DynamicFormService.getDataInTypeFormat(value: {}, valueType: {})", value, valueType);
		if (value == null) {
			return null;
		}

		String tmpValue = String.valueOf(value);
		if (valueType != null && tmpValue.isBlank() == false && tmpValue.isEmpty() == false) {
			if (INT.equalsIgnoreCase(valueType)) {
				if (StringUtils.isBlank(tmpValue) == false) {
					return (int) Double.parseDouble(tmpValue);
				}
			} else if (DECIMAL.equalsIgnoreCase(valueType)) {
				return Double.parseDouble(tmpValue);
			} else if (DATE.equalsIgnoreCase(valueType) || TIMESTAMP.equalsIgnoreCase(valueType)
					|| DATETIME.equalsIgnoreCase(valueType)) {
				Date dateData = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				try {
					if (null != tmpValue && false == tmpValue.isEmpty()) {
						dateData = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(tmpValue);
					}
				} catch (ParseException a_exc) {
					logger.warn("Error parsing the date : " + tmpValue + " Expected format is dd-MMM-yyyy hh:mm:ss a");
				}
				return sdf.format(dateData);
			} else if (BOOLEAN.equalsIgnoreCase(valueType)) {
				if ("false".equalsIgnoreCase(tmpValue) || "0".equalsIgnoreCase(tmpValue)) {
					return 0;
				} else {
					return 1;
				}
			}
		}
		return value;
	}

	@Transactional(readOnly = false)
	public String saveScriptLibraryDetails(MultiValueMap<String, String> formDataMap, Integer sourceTypeId)
			throws Exception {
		logger.debug("Inside DynamicFormService.saveScriptLibraryDetails(formDataMap: {}, sourceTypeId: {})",
				formDataMap, sourceTypeId);

		ScriptLibraryDetails	scriptLibrary	= new ScriptLibraryDetails();
		UserDetailsVO			userDetailsVO	= userDetailsService.getUserDetails();
		Date					date			= new Date();
		String					scriptlibId		= formDataMap.getFirst("scriptlibId");
		String					libraryName		= formDataMap.getFirst("libraryName");
		String					description		= formDataMap.getFirst("description");
		String					templateId		= formDataMap.getFirst("templateId");
		String					scriptType		= formDataMap.getFirst("scriptType");
		scriptLibrary.setScriptLibId(scriptlibId);
		scriptLibrary.setLibraryName(libraryName);
		scriptLibrary.setTemplateId(templateId);
		scriptLibrary.setDescription(description);
		scriptLibrary.setScriptType(scriptType);
		scriptLibrary.setUpdatedDate(date);
		scriptLibrary.setUpdatedBy(userDetailsVO.getUserName());
		scriptLibrary.setCreatedBy(userDetailsVO.getUserName());
		scriptLibrary.setIsCustomUpdated(1);
		ScriptLibraryVO scriptLibVO = convertEntityToVO(scriptLibrary);
		dynamicFormCrudDAO.getCurrentSession().merge(scriptLibrary);
		moduleVersionService.saveModuleVersion(scriptLibVO, null, scriptlibId, "jq_script_lib_details", sourceTypeId);
		return scriptlibId;

	}

	@Transactional(readOnly = false)
	public String saveScriptLibDetails(ScriptLibraryDetails scriptLibrary, Integer sourceTypeId, String tablename)
			throws Exception {
		UserDetailsVO	userDetailsVO	= userDetailsService.getUserDetails();
		Date			date			= new Date();
		scriptLibrary.setScriptLibId(scriptLibrary.getScriptLibId());
		scriptLibrary.setLibraryName(scriptLibrary.getLibraryName());
		scriptLibrary.setTemplateId(scriptLibrary.getTemplateId());
		scriptLibrary.setDescription(scriptLibrary.getDescription());
		scriptLibrary.setScriptType(scriptLibrary.getScriptType());
		scriptLibrary.setUpdatedDate(date);
		scriptLibrary.setUpdatedBy(userDetailsVO.getUserName());
		scriptLibrary.setCreatedBy(userDetailsVO.getUserName());
		scriptLibrary.setIsCustomUpdated(1);

		ScriptLibraryVO scriptLibVO = convertEntityToVO(scriptLibrary);
		if (scriptLibrary.getScriptLibId() == null
				|| Objects.isNull(findDynamicRestById(scriptLibrary.getScriptLibId()))) {
			dynamicFormCrudDAO.getCurrentSession().persist(scriptLibrary);
		} else {
			dynamicFormCrudDAO.getCurrentSession().merge(scriptLibrary);
		}
		moduleVersionService.saveModuleVersion(scriptLibVO, null, scriptLibrary.getScriptLibId(),
				"jq_script_lib_details", sourceTypeId);
		return scriptLibrary.getScriptLibId();

	}

	@Transactional(readOnly = false)
	public void saveScriptConnDetails(List<ScriptLibraryConnection> scriptLibConns, Integer sourceTypeId,
			String tablename) throws Exception {

		if (null != scriptLibConns && scriptLibConns.size() != 0 && scriptLibConns.isEmpty() == false) {
			for (ScriptLibraryConnection lib : scriptLibConns) {
				ScriptLibraryConnection scriptLibraryConn = new ScriptLibraryConnection();
				scriptLibraryConn.setScriptlibconnId(lib.getScriptlibconnId());
				scriptLibraryConn.setScriptLibId(lib.getScriptLibId());
				scriptLibraryConn.setModuletypeId(lib.getModuletypeId());
				scriptLibraryConn.setEntityId(lib.getEntityId());
				scriptLibraryConn.setCreatedBy(lib.getCreatedBy());
				scriptLibraryConn.setUpdatedBy(lib.getUpdatedBy());
				scriptLibraryConn.setUpdatedDate(lib.getUpdatedDate());
				scriptLibraryConn.setIsCustomUpdated(lib.getIsCustomUpdated());
				dynamicFormCrudDAO.getCurrentSession().merge(scriptLibraryConn);
			}
		}
		return;
	}

	@Transactional
	public ScriptLibraryDetails findDynamicRestById(String scriptLibraryId) {
		ScriptLibraryDetails scriptLibrary = dynamicFormCrudDAO.getCurrentSession().get(ScriptLibraryDetails.class,
				scriptLibraryId);
		if (scriptLibrary != null)
			dynamicFormCrudDAO.getCurrentSession().evict(scriptLibrary);
		return scriptLibrary;
	}

	public ScriptLibraryVO convertEntityToVO(ScriptLibraryDetails scriptLibrary) {

		ScriptLibraryVO scriptLibVO = new ScriptLibraryVO();
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
		ScriptEngineManager			manager				= new ScriptEngineManager();
		List<ScriptEngineFactory>	factories			= manager.getEngineFactories();
		boolean						isScriptEngPresent	= false;
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
