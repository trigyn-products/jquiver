package com.trigyn.jws.webstarter.service;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.spi.PropertyMasterDetails;
import com.trigyn.jws.dbutils.utils.ActivityLog;
import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dbutils.vo.ModuleDetailsVO;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynamicform.dao.DynamicFormCrudDAO;
import com.trigyn.jws.dynamicform.dao.IDynamicFormQueriesRepository;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.dynamicform.entities.DynamicFormSaveQuery;
import com.trigyn.jws.dynamicform.service.DynamicFormHelperService;
import com.trigyn.jws.dynamicform.service.DynamicFormIoService;
import com.trigyn.jws.dynamicform.service.DynamicFormService;
import com.trigyn.jws.dynarest.entities.FileUploadConfig;
import com.trigyn.jws.dynarest.service.FileUploadConfigService;
import com.trigyn.jws.formio.entities.FormIO;
import com.trigyn.jws.formio.utils.FormIOUtils;
import com.trigyn.jws.gridutils.dao.GridDetailsDAO;
import com.trigyn.jws.gridutils.entities.GridDetails;
import com.trigyn.jws.gridutils.utility.Constants;
import com.trigyn.jws.resourcebundle.dao.ResourceBundleDAO;
import com.trigyn.jws.resourcebundle.service.ResourceBundleService;
import com.trigyn.jws.templating.entities.TemplateMaster;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.templating.service.ModuleService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.repository.JwsMasterModulesRepository;
import com.trigyn.jws.usermanagement.vo.JwsEntityRoleVO;
import com.trigyn.jws.webstarter.utils.JQuiverProperties;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional(readOnly = false)
public class MasterCreatorService {

	@Autowired
	private DynamicFormCrudDAO				dynamicFormDAO					= null;

	@Autowired
	private MenuService						menuService						= null;

	@Autowired
	private GridDetailsDAO					gridDetailsDAO					= null;

	@Autowired
	private DynamicFormService				dynamicFormService				= null;

	@Autowired
	private IDynamicFormQueriesRepository	dynamicFormQueriesRepository	= null;

	@Autowired
	private DBTemplatingService				dbTemplatingService				= null;

	@Autowired
	private TemplatingUtils					templatingUtils					= null;

	@Autowired
	private ModuleService					moduleService					= null;

	@Autowired
	private ResourceBundleService			resourceBundleService			= null;

	@Autowired
	private JwsMasterModulesRepository		jwsMasterModulesRepository		= null;

	@Autowired
	private UserManagementService			userManagementService			= null;

	@Autowired
	private TemplateCrudService				templateCrudService				= null;

	@Autowired
	private PropertyMasterService			propertyMasterService			= null;

	@Autowired
	private DynamicFormCrudService			dynamicFormCrudService			= null;

	@Autowired
	private ResourceBundleDAO				resourceBundleDAO				= null;

	@Autowired
	private PropertyMasterDetails			propertyMasterDetails			= null;

	@Autowired
	private IUserDetailsService				detailsService					= null;

	@Autowired
	private ActivityLog						activitylog						= null;

	@Autowired
	private FileUploadConfigService			fileUploadConfigService			= null;

	@Autowired
	private DynamicFormIoService			dynamicFormIoService			= null;

	@Autowired
	private FormIOMasterCreatorService		formIOMasterCreatorService		= null;

	@Autowired
	private DynamicFormHelperService		dynamicFormHelperService		= null;
	
	@Autowired
	private JQuiverProperties 				jQuiverPropeties 			= null;

	private static final String				PRIMARY_KEY						= "PK";

	private final static Logger				logger							= LoggerFactory
			.getLogger(MasterCreatorService.class);

	private static final String				FILE_BIN_ID						= "fileBinId";
	private static final String				TOGGLE_FILEBIN					= "toggleFileBin";

	public String getModuleDetails(HttpServletRequest httpServletRequest) throws Exception, CustomStopException {
		try {
			Map<String, Object> templateMap = new HashMap<>();
			// List<String> tables = dynamicFormDAO.getAllTablesListInSchema();
			// List<String> views = dynamicFormDAO.getAllViewsListInSchema();
			String jQuiverVersion = propertyMasterDetails.getSystemPropertyValue("version");
			List<ModuleDetailsVO> moduleListingVOList = moduleService.getAllParentModules("");
			String uri = httpServletRequest.getRequestURI().substring(httpServletRequest.getContextPath().length());
			String url = getServerBaseURL(httpServletRequest);
			StringBuilder urlPrefix = new StringBuilder();
			url = url.replace(uri, "");
			urlPrefix.append(url).append(jQuiverPropeties.getViewPath()+"/");

			if (StringUtils.isBlank(jQuiverVersion) == false && jQuiverVersion.contains("SNAPSHOT")) {
				templateMap.put("isDev", true);
			} else {
				templateMap.put("isDev", false);
			}
			templateMap.put("urlPrefix", urlPrefix);

			// if (!CollectionUtils.isEmpty(views)) {
			// tables.addAll(views);
			// }
			// templateMap.put("tables", tables);
			// templateMap.put("tables", tables);
			templateMap.put("moduleListingVOList", moduleListingVOList);

			return menuService.getTemplateWithSiteLayout("master-creator", templateMap);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured while loading Master Genertor page.", custStopException);
			throw custStopException;
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Map<String, Object> initMasterCreationScript(MultiValueMap<String, String> inputDetails) throws Exception {
		String environment = propertyMasterService.findPropertyMasterValue("system", "system", "profile");
		String dataSourceId = null;
		FormIO fmio = new FormIO();
		Map<String, Object> createdMasterDetails = new HashMap<>();
		Map<String, Object> formData = processFormData(inputDetails.getFirst("formData"));
		Integer insideMenu = formData.get("isMenuAddActive") == null ? 0
				: Integer.parseInt((String) formData.get("isMenuAddActive"));
		ModuleDetailsVO menuData = new ModuleDetailsVO();
		if (insideMenu.equals(Constant.IS_INSIDE_MENU)) {
			menuData = processMenu(inputDetails.getFirst("menuDetails"));
		}
		String fileBinId = null;
		// Saving File Bin if file Bin is selected
		if (null!=formData.get(TOGGLE_FILEBIN).toString() && "1".equalsIgnoreCase(formData.get(TOGGLE_FILEBIN).toString())) {
			fileBinId = formData.get(FILE_BIN_ID).toString();
			FileUploadConfig existingfileUploadConfig = fileUploadConfigService.getFileUploadConfigByBinId(fileBinId);
			if (existingfileUploadConfig == null) {
				FileUploadConfig fileUploadConfig = saveFileUploadConfigDetails(formData);
				createdMasterDetails.put("fileUploadConfig", fileUploadConfig);
				//activitylog.activitylog(requestParams);
			} else {
				logger.error("Error in master.", fileBinId);
				throw new RuntimeException(HttpStatus.PRECONDITION_FAILED.toString());
			}

		}
		boolean isFormIo = Integer.parseInt((String) formData.get("isFormIo")) == 1;
		if(isFormIo == true) {
			fmio = formIOMasterCreatorService.updateFormIoDetails(inputDetails, formData, dataSourceId, fileBinId);
		}
		ModuleDetailsVO dynamicFormModuleDetails = new ModuleDetailsVO();
		dynamicFormModuleDetails = processMenu(inputDetails.getFirst("dynamicFormModuleDetails"));
		dynamicFormModuleDetails.setIsInsideMenu(Constant.IS_NOT_INSIDE_MENU);

		DynamicForm dynamicForm = createDynamicFormDetails(inputDetails, formData, menuData.getModuleUrl(),
				inputDetails.get("dbProductName").toString());

		Map<String, String> requestParams = logActivity();
		requestParams.put("entityName", dynamicForm.getFormName());
		requestParams.put("masterModuleType", Constants.Modules.DYNAMICFORM.getModuleName());
		activitylog.activitylog(requestParams);
		GridDetails gridDetails = createGridDetailsInfo(formData);
		requestParams.put("entityName", gridDetails.getGridName());
		requestParams.put("masterModuleType", Constants.Modules.GRIDUTILS.getModuleName());

		activitylog.activitylog(requestParams);
		TemplateMaster templateMaster = saveTemplateMasterDetails(inputDetails, gridDetails.getGridId(),
				dynamicForm.getFormId(), formData, dynamicFormModuleDetails.getModuleUrl(), isFormIo);
		requestParams.put("entityName", templateMaster.getTemplateName());
		requestParams.put("masterModuleType", Constants.Modules.TEMPLATING.getModuleName());
		activitylog.activitylog(requestParams);
		menuData.setHeaderJson(
				"{\"Powered-By\":\"JQuiver\",\"Content-Type\":\"text/html; charset=UTF-8\",\"Content-Language\":\"en\"}");
		if (insideMenu.equals(Constant.IS_INSIDE_MENU)) {
			insertIntoMenu(menuData, templateMaster);

		}
		dynamicFormModuleDetails.setHeaderJson(
				"{\"Powered-By\":\"JQuiver\",\"Content-Type\":\"text/html; charset=UTF-8\",\"Content-Language\":\"en\"}");
		insertIntoMenu(dynamicFormModuleDetails, dynamicForm);
		requestParams.put("entityName", dynamicFormModuleDetails.getModuleName());
		requestParams.put("masterModuleType", Constants.Modules.MASTERGENERATOR.getModuleName());
		activitylog.activitylog(requestParams);

		createdMasterDetails.put("dynamicForm", dynamicForm);
		createdMasterDetails.put("gridDetails", gridDetails);
		createdMasterDetails.put("templateMaster", templateMaster);
		createdMasterDetails.put("menuData", menuData);
		createdMasterDetails.put("dynamicFormModuleDetails", dynamicFormModuleDetails);
		createdMasterDetails.put("formIoData", fmio);
		return createdMasterDetails;
	}

	private FileUploadConfig saveFileUploadConfigDetails(Map<String, Object> formData) throws Exception {
		FileUploadConfig fileUploadConfig = new FileUploadConfig();
		fileUploadConfig.setFileBinId(formData.get(FILE_BIN_ID).toString());
		fileUploadConfig.setFileTypSupported(formData.get("fileTypeSupported").toString());
		
		String maxFileCount = propertyMasterDetails.getSystemPropertyValue("max-file-count");
		if(Integer.parseInt(formData.get("noOfFiles").toString()) < 256) {
			maxFileCount = formData.get("noOfFiles").toString();
		}
		
		fileUploadConfig.setMaxFileSize(new BigDecimal(formData.get("maxFileSize").toString()));
		fileUploadConfig.setNoOfFiles(Integer.parseInt(maxFileCount));
		fileUploadConfig.setIsFileStorageEnable(Integer.parseInt(formData.get("fileStorageTxt").toString()));
		if("1".equalsIgnoreCase(formData.get("fileStorageTxt").toString())) {
		  fileUploadConfig.setCustomFileStorageClass(formData.get("customFileStorageClass").toString());
		}
		fileUploadConfig.setIsDeleted(0);
		fileUploadConfig.setLastUpdatedTs(new Date());
		if (detailsService.getUserDetails().getFullName() != null) {
			fileUploadConfig.setCreatedBy(detailsService.getUserDetails().getUserName());
			fileUploadConfig.setLastUpdatedBy(detailsService.getUserDetails().getUserName());
		} else {
			logger.error("Error in user details, user details user name cannot be null",
					detailsService.getUserDetails().getUserName());
			throw new RuntimeException("Invalid user, can't create File Bin");
		}
		fileUploadConfig.setIsCustomUpdated(1);
		fileUploadConfigService.saveFileUploadConfig(fileUploadConfig);
		return fileUploadConfig;
	}

	public void downloadFiles(TemplateMaster templateMaster, DynamicForm dynamicForm) throws Exception {
		templateCrudService.downloadTemplates(templateMaster.getTemplateId());
		dynamicFormCrudService.downloadDynamicFormsTemplate(dynamicForm.getFormId());
	}

	/**
	 * Purpose of this method is to log activities</br>
	 * in Master Generator Module.
	 * 
	 * @author Bibhusrita.Nayak
	 * 
	 */
	private Map<String, String> logActivity() {
		Map<String, String> requestParams = new HashMap<>();
		UserDetailsVO detailsVO = detailsService.getUserDetails();
		Date activityTimestamp = new Date();
		requestParams.put("action", Constants.Action.ADD.getAction());
		requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
		requestParams.put("userName", detailsVO.getUserName());
		requestParams.put("fullName", detailsVO.getFullName());
		requestParams.put("message", "");
		requestParams.put("date", activityTimestamp.toString());
		return requestParams;
	}

	private void insertIntoMenu(ModuleDetailsVO menuData, Object entity) throws Exception {

		if (entity instanceof TemplateMaster) {
			menuData.setTargetTypeId(((TemplateMaster) entity).getTemplateId());
			menuData.setTargetLookupId(5);
		} else if (entity instanceof DynamicForm) {
			menuData.setTargetTypeId(((DynamicForm) entity).getFormId());
			menuData.setTargetLookupId(2);
		}

		if (entity instanceof DynamicForm == false) {
			if (StringUtils.isNotBlank(menuData.getParentModuleId())) {
				menuData.setSequence(moduleService.getMaxSequenceByParent(menuData.getParentModuleId()));
			} else {
				menuData.setSequence(moduleService.getModuleMaxSequence());
			}
		} else {
			menuData.setIsInsideMenu(0);
			menuData.setSequence(null);
		}
		String menuId = moduleService.saveModuleDetails(menuData);
		menuData.setModuleId(menuId);
	}

	@Transactional(readOnly = false)
	public void saveEntityRolesForMasterGenerator(Map<String, Object> createdObjDetails, List<String> roleIds) {
		DynamicForm dynamicForm = (DynamicForm) createdObjDetails.get("dynamicForm");
		GridDetails gridDetails = (GridDetails) createdObjDetails.get("gridDetails");
		TemplateMaster templateMaster = (TemplateMaster) createdObjDetails.get("templateMaster");
		ModuleDetailsVO menuData = (ModuleDetailsVO) createdObjDetails.get("menuData");
		ModuleDetailsVO dynamicFormModuleDetails = (ModuleDetailsVO) createdObjDetails.get("dynamicFormModuleDetails");
		FileUploadConfig fileUploadConfig = (FileUploadConfig) createdObjDetails.get("fileUploadConfig");
		FormIO formIoData = (FormIO) createdObjDetails.get("formIoData");
		JwsEntityRoleVO jwsDynamicEntity = new JwsEntityRoleVO();
		jwsDynamicEntity.setEntityId(dynamicForm.getFormId());
		jwsDynamicEntity.setEntityName(dynamicForm.getFormName());
		jwsDynamicEntity.setRoleIds(roleIds);
		String dynamicModuleId = jwsMasterModulesRepository
				.findBymoduleName(com.trigyn.jws.usermanagement.utils.Constants.Modules.DYNAMICFORM.getModuleName())
				.getModuleId();
		jwsDynamicEntity.setModuleId(dynamicModuleId);
		userManagementService.deleteAndSaveEntityRole(jwsDynamicEntity);

		JwsEntityRoleVO jwsGridEntity = new JwsEntityRoleVO();
		jwsGridEntity.setEntityId(gridDetails.getGridId());
		jwsGridEntity.setEntityName(gridDetails.getGridName());
		jwsGridEntity.setRoleIds(roleIds);
		String gridModuleId = jwsMasterModulesRepository
				.findBymoduleName(com.trigyn.jws.usermanagement.utils.Constants.Modules.GRIDUTILS.getModuleName())
				.getModuleId();
		jwsGridEntity.setModuleId(gridModuleId);
		userManagementService.deleteAndSaveEntityRole(jwsGridEntity);

		JwsEntityRoleVO jwsTemplateEntity = new JwsEntityRoleVO();
		jwsTemplateEntity.setEntityId(templateMaster.getTemplateId());
		jwsTemplateEntity.setEntityName(templateMaster.getTemplateName());
		jwsTemplateEntity.setRoleIds(roleIds);
		String templateModuleId = jwsMasterModulesRepository
				.findBymoduleName(com.trigyn.jws.usermanagement.utils.Constants.Modules.TEMPLATING.getModuleName())
				.getModuleId();
		jwsTemplateEntity.setModuleId(templateModuleId);
		userManagementService.deleteAndSaveEntityRole(jwsTemplateEntity);
		
		if(formIoData != null && formIoData.getFormIoId() != null && formIoData.getFormIoId().isBlank() == false) {
			JwsEntityRoleVO jwsFormIOEntity = new JwsEntityRoleVO();
			jwsFormIOEntity.setEntityId(formIoData.getFormIoId());
			jwsFormIOEntity.setEntityName(formIoData.getFormName());
			jwsFormIOEntity.setRoleIds(roleIds);
			String formIoModuleId = jwsMasterModulesRepository
					.findBymoduleName(com.trigyn.jws.usermanagement.utils.Constants.Modules.FORMIO.getModuleName())
					.getModuleId();
			jwsFormIOEntity.setModuleId(formIoModuleId);
			userManagementService.deleteAndSaveEntityRole(jwsFormIOEntity);
		}

		if (StringUtils.isNotBlank(menuData.getModuleId())) {
			JwsEntityRoleVO jwsMenuEntity = new JwsEntityRoleVO();
			jwsMenuEntity.setEntityId(menuData.getModuleId());
			jwsMenuEntity.setEntityName(menuData.getModuleName());
			jwsMenuEntity.setRoleIds(roleIds);
			String menuModuleId = jwsMasterModulesRepository
					.findBymoduleName(com.trigyn.jws.usermanagement.utils.Constants.Modules.ROUTER.getModuleName())
					.getModuleId();
			jwsMenuEntity.setModuleId(menuModuleId);
			userManagementService.deleteAndSaveEntityRole(jwsMenuEntity);
		}

		if (StringUtils.isNotBlank(dynamicFormModuleDetails.getModuleId())) {
			JwsEntityRoleVO jwsMenuEntity = new JwsEntityRoleVO();
			jwsMenuEntity.setEntityId(dynamicFormModuleDetails.getModuleId());
			jwsMenuEntity.setEntityName(dynamicFormModuleDetails.getModuleName());
			jwsMenuEntity.setRoleIds(roleIds);
			String menuModuleId = jwsMasterModulesRepository
					.findBymoduleName(com.trigyn.jws.usermanagement.utils.Constants.Modules.ROUTER.getModuleName())
					.getModuleId();
			jwsMenuEntity.setModuleId(menuModuleId);
			userManagementService.deleteAndSaveEntityRole(jwsMenuEntity);
		}
		if (null != fileUploadConfig) {
			if (StringUtils.isNotBlank(fileUploadConfig.getFileBinId())) {
				JwsEntityRoleVO jwsFileBinEntity = new JwsEntityRoleVO();
				jwsFileBinEntity.setEntityId(fileUploadConfig.getFileBinId());
				jwsFileBinEntity.setEntityName(fileUploadConfig.getFileBinId());
				jwsFileBinEntity.setRoleIds(roleIds);
				String fileBinModuleId = jwsMasterModulesRepository
						.findBymoduleName(com.trigyn.jws.usermanagement.utils.Constants.Modules.FILEBIN.getModuleName())
						.getModuleId();
				jwsFileBinEntity.setModuleId(fileBinModuleId);
				userManagementService.deleteAndSaveEntityRole(jwsFileBinEntity);

			}
		}
	}

	private ModuleDetailsVO processMenu(String menuDetails) {

		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(menuDetails); // response will be the json
		// String
		ModuleDetailsVO moduleDetailsVO = gson.fromJson(object, ModuleDetailsVO.class);
		moduleDetailsVO.setIsInsideMenu(Constant.IS_INSIDE_MENU);
		moduleDetailsVO.setIncludeLayout(Constant.INCLUDE_LAYOUT);

		return moduleDetailsVO;
	}

	private Map<String, Object> processFormData(String formData) {
		List<String> masterDetails = Lists.newArrayList(formData.split("&"));
		Map<String, Object> masterDetailsMap = new HashMap<String, Object>();
		for (String details : masterDetails) {
			String[] fieldDetails = details.split("=");
			if (fieldDetails.length == 2) {
				masterDetailsMap.put(fieldDetails[0], URLDecoder.decode(fieldDetails[1]));
			}
		}
		return masterDetailsMap;
	}

	private DynamicForm createDynamicFormDetails(MultiValueMap<String, String> inputDetails,
			Map<String, Object> formData, String moduleURL, String dbProductName) throws Exception {
		String tableName = formData.get("selectTable").toString();
		String dataSourceId = null;
		if (formData.get("dataSourceId") != null) {
			dataSourceId = formData.get("dataSourceId").toString();
		}
		String primaryKey = null;
		if (formData.get("primaryKey").toString() != null) {
			primaryKey = formData.get("primaryKey").toString();
		}
		String formIoId =  (String) formData.get("formIoId");
		Boolean toggleCaptcha = false;
		Boolean toggleCsrf = false;
		String fileBinId = null;
		String fileAssociationId = null;
		Boolean toggleFileBin = false;
		boolean isFormIo = Integer.parseInt((String) formData.get("isFormIo")) == 1;
		DynamicForm dynamicForm = new DynamicForm();
		if (formData.get("toggleCaptcha").toString() != null && formData.get("toggleCaptcha").toString().equalsIgnoreCase("1")) {
			toggleCaptcha = true;
			dynamicForm.setIsCaptchaEnabled(1);
		}
		if(isFormIo == false) {
			if (formData.get("toggleCsrf").toString() != null && formData.get("toggleCsrf").toString().equalsIgnoreCase("1")) {
				toggleCsrf = true;
				dynamicForm.setIsCsrfEnabled(1);
			}
		}
		//Taken outside of isFormIo if because i have to set fileBin Condition in formio-default-html-template otherwise file bin would not work
		if (formData.get(TOGGLE_FILEBIN).toString().equalsIgnoreCase("1")) {
			toggleFileBin = true;
			fileBinId = formData.get(FILE_BIN_ID).toString();
			fileAssociationId = primaryKey;
		}
		String moduleName = formData.get("moduleName") + "-form";
		String description = formData.get("moduleName") + " Form";
		List<String> formDetailsString = new ObjectMapper().convertValue(inputDetails.get("formDetails"), List.class);

		String jsonString = formDetailsString.get(0).toString();
		List<Map<String, Object>> formDetails = new ObjectMapper().readValue(jsonString, List.class);
		for (Map<String, Object> map : formDetails) {
			saveResourseKey(map);

		}
		String saveQuery = null;
		String htmlTemplate = null;
		String selectQuery = null;
		Map<String, String> dynamicFormDetails = new HashMap<>();
		if (isFormIo == true) {
			selectQuery = formIOMasterCreatorService.generateSelectQueryForFormIO(tableName, formDetails, primaryKey, dataSourceId, dbProductName);
		} else {
			selectQuery = generateSelectQueryForForm(tableName, formDetails, primaryKey, dataSourceId, dbProductName);
		}
		dynamicFormDetails = generateHtmlTemplate(dataSourceId, dbProductName, tableName,
				formDetails, moduleURL, toggleCaptcha, toggleCsrf, toggleFileBin, fileBinId, fileAssociationId, isFormIo);
		saveQuery = dynamicFormDetails.get("save-template");
		htmlTemplate = dynamicFormDetails.get("form-template");
		

		if (detailsService.getUserDetails().getFullName() != null) {
			dynamicForm.setLastUpdatedBy(detailsService.getUserDetails().getUserName());
			dynamicForm.setCreatedBy(detailsService.getUserDetails().getUserName());
		} else {
			logger.error("Error in user details, user details user name cannot be null",
					detailsService.getUserDetails().getUserName());
			throw new RuntimeException("Invalid user, can't create form");
		}

		dynamicForm.setFormDescription(description);
		dynamicForm.setFormSelectQuery(selectQuery);
		dynamicForm.setFormBody(htmlTemplate);
		dynamicForm.setFormName(moduleName);
		if(isFormIo == true) {
			dynamicForm.setSelectQueryType(2);
		} else {
			dynamicForm.setSelectQueryType(1);
		}
		dynamicForm.setCreatedDate(new Date());
		dynamicForm.setDatasourceId(dataSourceId);
		dynamicForm.setIsCustomUpdated(1);
		dynamicForm.setFormIoId(formIoId);
		dynamicFormDAO.saveDynamicForm(dynamicForm);
		DynamicFormSaveQuery dynamicFormSaveQuery = new DynamicFormSaveQuery();
		dynamicFormSaveQuery.setSequence(1);
		dynamicFormSaveQuery.setResultVariableName("resultSet");
		if(isFormIo == true) {
			dynamicFormSaveQuery.setDaoQueryType(4);
		} else {
			dynamicFormSaveQuery.setDaoQueryType(2);
		}
		dynamicFormSaveQuery.setDynamicFormId(dynamicForm.getFormId());
		dynamicFormSaveQuery.setDynamicFormSaveQuery(saveQuery);
		dynamicFormQueriesRepository.save(dynamicFormSaveQuery);
		return dynamicForm;
	}

	private void saveResourseKey(Map<String, Object> map) throws Exception {
		String i18nResourceKey = map.get("i18nResourceKey").toString();
		String displayName = map.get("displayName").toString();
		if (i18nResourceKey.isBlank() == false) {
			Boolean keyAlreadyExist = resourceBundleService.checkResourceKeyExist(i18nResourceKey);
			resourceBundleDAO.addResourceBundle(i18nResourceKey, Constant.DEFAULT_LANGUAGE_ID, displayName);
		}
	}

	private Map<String, String> generateHtmlTemplate(String dataSourceId, String dbProductName, String tableName,
			List<Map<String, Object>> formDetails, String moduleURL, Boolean toggleCaptcha,Boolean toggleCsrf, Boolean toggleFileBin,
			String fileBinId, String fileAssociationId, boolean isFormIo) throws Exception {
		Map<String, String> templateDetails =  new HashMap<String, String>();
		List<Map<String, Object>> tableDetails = dynamicFormDAO.getTableDetailsByTableName(dataSourceId, tableName);
		dynamicFormHelperService.getMatchedColumnTableDetails(formDetails, tableDetails, isFormIo);
		if(isFormIo == true) {
			templateDetails = dynamicFormIoService.createFormIoHtmlByTableName(tableName, tableDetails,
					moduleURL, dataSourceId, dbProductName, toggleCaptcha, toggleCsrf, toggleFileBin, fileBinId,
					fileAssociationId);
		} else {
			templateDetails = dynamicFormService.createDefaultFormByTableName(tableName, tableDetails,
					moduleURL, dataSourceId, dbProductName, toggleCaptcha,toggleCsrf, toggleFileBin, fileBinId, fileAssociationId);
		}
		return templateDetails;
	}
	
	private String generateSelectQueryForForm(String tableName, List<Map<String, Object>> formDetails,
			String primaryKey, String dataSourceId, String dbProductName) throws Exception {
		StringBuilder selectQuery = new StringBuilder("SELECT ");
		StringJoiner columns = new StringJoiner(",");
		
		List<Map<String, Object>> tableDetails = dynamicFormService.getTableDetailsByTableName(tableName, dataSourceId);
		boolean isPostgres = dbProductName.contains(com.trigyn.jws.dynamicform.utils.Constant.POSTGRESQL);
		
		for (Map<String, Object> details : formDetails) {
		    String columnName = details.get("column").toString();
            
		    String displayName = details.get("displayName").toString();
		    String paramName = displayName.replaceAll("_", ""); // Usually not needed for SELECT, unless aliasing
		    if(details.get("datatype")!=null) {
		    	String dataType = details.get("datatype").toString();

			    // Use getCastExpressionForSelect with dummy param (for SELECT, we don't need :param, only casted columns)
			    String castedColumn = FormIOUtils.getCastExpressionForSelect(columnName, dataType, dbProductName);
			    columns.add(castedColumn);
		    }
		}

		String pkDataType = null;
		selectQuery.append(columns.toString()).append(" FROM ").append(tableName).append(" WHERE ");
		StringJoiner whereClause = new StringJoiner(" AND ");
		List<String> primaryKeys = Lists.newArrayList(primaryKey.split(","));
		String value = null;

		boolean isStringID = false;
		for (Map<String, Object> info : tableDetails) {
			if (info.get("columnType") == null) {

				continue;
			}
			String dataType = info.get("dataType").toString();
			String columnKey = info.get("columnKey").toString();
			if (columnKey != null && columnKey.equals(PRIMARY_KEY)) {
				pkDataType = dataType;
				if (dataType.equalsIgnoreCase("text")) {
					isStringID = true;
				}
			}
		}
		String ifCond = null;
		for (String key : primaryKeys) {
			String coloumnName = key;
			ifCond = " <#if " + key.replaceAll("_", "") + "??>";
			value = coloumnName + " = " + ifCond + " :" + key.replaceAll("_", "");
			if (dbProductName.contains(Constant.MSSQLSERVER) || dbProductName.contains(Constant.ORACLE)) {
				whereClause.add(value.replace("\\", "") + " <#else> NULL </#if>");
			} else if(isPostgres) {
                if(pkDataType!=null && pkDataType.equalsIgnoreCase("int")) {
                    value = coloumnName + ifCond + " = CAST( :" + key.replaceAll("_", "")+" AS INTEGER)";
                } else {
                	value = coloumnName + ifCond + " = :" + key.replaceAll("_", "");
                }
                whereClause.add(value.replace("\\", "") + " <#else> IS NULL </#if>");
			} else {
				whereClause.add(value.replace("\\", "") + " <#else> 'null' </#if>");
			}
		}
		selectQuery.append(whereClause.toString());

		return selectQuery.toString();
	}


	private GridDetails createGridDetailsInfo(Map<String, Object> formData) throws Exception {

		UserDetailsVO detailsVO = detailsService.getUserDetails();
		String moduleName = formData.get("moduleName") + "Grid".replaceAll("-", "");
		String description = formData.get("moduleName") + " Listing";
		String tableName = formData.get("selectTable").toString();
		String columns = formData.get("columns").toString();
		Date date = new Date();
		String dataSourceId = formData.get("dataSourceId") == null ? null : formData.get("dataSourceId").toString();
		String customFilterCriteria = formData.get("customFilterCriteria") == null ? null
				: formData.get("customFilterCriteria").toString();

		GridDetails details = new GridDetails(moduleName, moduleName, description, tableName, columns,
				Constants.queryImplementationType.VIEW.getType(), Constants.CUSTOM_GRID, detailsVO.getUserName(), date,
				dataSourceId, customFilterCriteria, null, date);
		return gridDetailsDAO.saveGridDetails(details);
	}

	private TemplateMaster saveTemplateMasterDetails(MultiValueMap<String, String> inputDetails, String gridId,
			String formId, Map<String, Object> formData, String moduleURL, boolean isFormIo) throws Exception, CustomStopException {
		try {
			List<String> gridDetailsString = new ObjectMapper().convertValue(inputDetails.get("gridDetails"),
					List.class);
			String moduleName = formData.get("moduleName") + "-template";
			String jsonString = gridDetailsString.get(0).toString();
			List<Map<String, Object>> gridDetails = new ObjectMapper().readValue(jsonString, List.class);
			String primaryKey = formData.get("primaryKey").toString();
			List<String> primaryKeysIds = Lists.newArrayList(primaryKey.split(",")).stream()
					.map(element -> element.replaceAll("_", "")).collect(Collectors.toList());
			
			HashMap<String, Object> details = new HashMap<String, Object>();

			String optionString = "title: '" + formData.getOrDefault("menuDisplayName", "Page Title") + "'";
			String dateField = "";
			String statusField = "";
			String excludeField = "";
			for (Map<String, Object> map : gridDetails) {
				saveResourseKey(map);
				if(map.get("hidden") != null && Boolean.valueOf(map.get("hidden").toString()) == true) {
					if(excludeField != "") excludeField += ",";
					excludeField += "'" + map.get("column").toString() + "'";
				}
			}
			if(excludeField != "") {
				optionString += ", excludeFields: [" + excludeField + "]";
			}
			
			String tableName = formData.get("selectTable").toString();
			String dataSourceId = null;
			if (formData.get("dataSourceId") != null) {
				dataSourceId = formData.get("dataSourceId").toString();
			}
			List<Map<String, Object>> tableDetails = dynamicFormDAO.getTableDetailsByTableName(dataSourceId, tableName);
			for (Map<String, Object> info : tableDetails) {
				if (info.get("columnType") == null) {
					continue;
				}
				String	columnName		= info.get("tableColumnName").toString();
				String	columnType		= info.get("columnType").toString();
				if("date".equals(columnType) ||  "time".equals(columnType) || "datetime".equals(columnType)) {
					if(dateField != "") dateField += ", ";
					dateField += "'" + columnName + "'";
				} else if("boolean".equals(columnType)) {
					if(statusField != "") statusField += ", ";
					statusField += "'" + columnName + "'";
				}
			}
			
			if(dateField != "") {
				optionString += ", dateFields: [" + dateField + "]";
			}
			if(statusField != "") {
				optionString += ", statusFields: [" + statusField + "]";
			}
			

			for (String ids : primaryKeysIds) {
				details.put(ids, "");
			}
			List<String> primaryKeys = Lists.newArrayList(primaryKey.split(","));
			Map<String, Object> templateMap = new HashMap<String, Object>();
			templateMap.put("isFormIo", isFormIo);
			templateMap.put("formId", formId);
			templateMap.put("dfModuleURL", moduleURL);
			templateMap.put("gridId", gridId);
			templateMap.put("primaryKeysIds", primaryKeysIds);
			templateMap.put("primaryKeys", primaryKeys);
			templateMap.put("gridDetails", gridDetails);
			templateMap.put("primaryKeyObject", new ObjectMapper().writeValueAsString(details));
			templateMap.put("pageTitle", formData.getOrDefault("menuDisplayName", "Page Title"));
			templateMap.put("options", optionString);
			TemplateVO templateVO = dbTemplatingService.getTemplateByName("system-listing-template");
			String template = templatingUtils.processTemplateContents(templateVO.getTemplate(),
					templateVO.getTemplateName(), templateMap);
			TemplateMaster templateMaster = new TemplateMaster();
			templateMaster.setTemplateName(moduleName);
			templateMaster.setTemplate(template);
			templateMaster.setUpdatedDate(new Date());
			if (detailsService.getUserDetails().getFullName() != null) {
				templateMaster.setCreatedBy(detailsService.getUserDetails().getUserName());
				templateMaster.setUpdatedBy(detailsService.getUserDetails().getUserName());
			} else {
				logger.error("Error in user details, user details user name cannot be null",
						detailsService.getUserDetails().getUserName());
				throw new RuntimeException("Invalid user, can't create template");
			}
			templateMaster.setIsCustomUpdated(1);
			return dbTemplatingService.saveTemplateMaster(templateMaster);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in loadDynamicForm.", custStopException);
			throw custStopException;
		}
	}

	public List<Map<String, Object>> getTableDetails(String tableName) {
		return dynamicFormDAO.getTableInformationByName(tableName);
	}

	public List<Map<String, Object>> getTableDetailsByTableName(String tableName, String additionalDataSourceId)
			throws Exception {
		return dynamicFormDAO.getTableDetailsByTableName(additionalDataSourceId, tableName);
	}

	public String getServerBaseURL(HttpServletRequest a_httpServletRequest) throws Exception {
		String baseURL = propertyMasterDetails.getPropertyValueFromPropertyMaster("system", "system", "base-url");
		if (StringUtils.isBlank(baseURL) == false && a_httpServletRequest.getContextPath().isBlank() == false) {
			baseURL = baseURL + a_httpServletRequest.getContextPath();
		}
		return baseURL;
	}
	
}
