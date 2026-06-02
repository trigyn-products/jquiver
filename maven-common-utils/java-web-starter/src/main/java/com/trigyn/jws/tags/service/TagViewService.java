package com.trigyn.jws.tags.service;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.trigyn.jws.dashboard.entities.Dashlet;
import com.trigyn.jws.dashboard.service.DashletModule;
import com.trigyn.jws.dashboard.vo.DashboardVO;
import com.trigyn.jws.dashboard.vo.DashletVO;
import com.trigyn.jws.dbutils.entities.AdditionalDatasource;
import com.trigyn.jws.dbutils.entities.ModuleListing;
import com.trigyn.jws.dbutils.entities.PropertyMaster;
import com.trigyn.jws.dbutils.repository.AdditionalDatasourceDAO;
import com.trigyn.jws.dbutils.repository.ModuleVersionDAO;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.service.ModuleVersionService;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dynamicform.dao.DynamicFormCrudDAO;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.dynamicform.entities.DynamicFormSaveQuery;
import com.trigyn.jws.dynamicform.service.DynamicFormModule;
import com.trigyn.jws.dynamicform.service.DynamicFormService;
import com.trigyn.jws.dynamicform.vo.DynamicFormVO;
import com.trigyn.jws.dynarest.dao.JwsDynarestDAO;
import com.trigyn.jws.dynarest.entities.FileUploadConfig;
import com.trigyn.jws.dynarest.entities.JqApiClientDetails;
import com.trigyn.jws.dynarest.entities.JqEncAlgModPadKeyLookup;
import com.trigyn.jws.dynarest.entities.JqScheduler;
import com.trigyn.jws.dynarest.repository.ApiClientDetailsDAO;
import com.trigyn.jws.dynarest.repository.SchedulerDAO;
import com.trigyn.jws.dynarest.service.FileUploadConfigService;
import com.trigyn.jws.dynarest.vo.FileUploadConfigVO;
import com.trigyn.jws.formio.dao.IFormIORepository;
import com.trigyn.jws.formio.entities.FormIO;
import com.trigyn.jws.formio.vo.FormIOImportExportVO;
import com.trigyn.jws.gridutils.dao.GridDetailsDAO;
import com.trigyn.jws.gridutils.entities.GridDetails;
import com.trigyn.jws.resourcebundle.dao.ResourceBundleDAO;
import com.trigyn.jws.resourcebundle.service.ResourceBundleService;
import com.trigyn.jws.resourcebundle.vo.ResourceBundleVO;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibraryConnection;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibraryDetails;
import com.trigyn.jws.tags.entities.TagEntityDetails;
import com.trigyn.jws.tags.repository.interfaces.ITagEntityDetailsRepository;
import com.trigyn.jws.templating.dao.TemplateDAO;
import com.trigyn.jws.templating.entities.TemplateMaster;
import com.trigyn.jws.templating.service.ModuleService;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.typeahead.dao.TypeAheadDAO;
import com.trigyn.jws.typeahead.entities.Autocomplete;
import com.trigyn.jws.typeahead.model.AutocompleteVO;
import com.trigyn.jws.typeahead.service.TypeAheadService;
import com.trigyn.jws.usermanagement.entities.JwsEntityRoleAssociation;
import com.trigyn.jws.usermanagement.repository.JwsEntityRoleAssociationDAO;
import com.trigyn.jws.webstarter.dao.NotificationDAO;
import com.trigyn.jws.webstarter.service.DashboardCrudService;
import com.trigyn.jws.webstarter.service.DynarestCrudService;
import com.trigyn.jws.webstarter.service.ImportService;
import com.trigyn.jws.webstarter.service.ModuleRevisionService;
import com.trigyn.jws.webstarter.utils.Constant;
import com.trigyn.jws.webstarter.vo.GenericUserNotification;
import com.trigyn.jws.webstarter.vo.GenericUserNotificationJsonVO;
import com.trigyn.jws.webstarter.vo.GridDetailsJsonVO;
import com.trigyn.jws.webstarter.vo.PropertyMasterJsonVO;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class TagViewService {
	
	private final static Logger					logger						= LoggerFactory.getLogger(TagViewService.class);
	
	@Autowired
	private ITagEntityDetailsRepository	tagDetailsRepository;

	@Autowired
	private TemplateDAO					templateDAO				= null;

	@Autowired
	private ModuleVersionService		moduleVersionService	= null;

	@Autowired
	private FileUtilities				fileUtilities;
	
	@Autowired
	private DynamicFormCrudDAO			dynamicFormCrudDAO				= null;

	@Autowired
	private JwsDynarestDAO				jwsDynarestDAO					= null;
	
	@Autowired
	@Qualifier("dynamic-form")
	private DynamicFormModule			dynamicFormDownloadUploadModule	= null;

	@Autowired
	private ImportService				importService					= null;
	
	@Autowired
	private TypeAheadDAO				typeAheadDAO					= null;
	
	@Autowired
	private TypeAheadService			typeAheadService				= null;
	
	@Autowired
	private DashboardCrudService		dashboardCrudService			= null;

	@Autowired
	@Qualifier("dashlet")
	private DashletModule				dashletDownloadUploadModule		= null;
	
	@Autowired
	private GridDetailsDAO				gridDetailsDAO					= null;

	@Autowired
	private ResourceBundleDAO			resourceBundleDAO				= null;

	@Autowired
	private ResourceBundleService		resourceBundleService			= null;
	
	@Autowired
	private ApiClientDetailsDAO			apiClientDetailsDAO				= null;
	
	@Autowired
	private SchedulerDAO				schedulerDAO					= null;

	@Autowired
	private NotificationDAO				notificationDao					= null;

	@Autowired
	private IFormIORepository			formIORepository				= null;

	@Autowired
	private DynamicFormService			dynamicFormService				= null;

	@Autowired
	private PropertyMasterDAO			propertyMasterDAO				= null;
	
	@Autowired
	private ModuleVersionDAO			moduleVersionDAO				= null;
	
	@Autowired
	private JwsEntityRoleAssociationDAO	jwsEntityRoleAssociationDAO		= null;
	
	@Autowired
	private ModuleService				moduleService					= null;
	
	@Autowired
	private FileUploadConfigService		fileUploadConfigService			= null;
	
	@Autowired
	private AdditionalDatasourceDAO		additionalDatasourceDAO			= null;

	@Autowired
	private PropertyMasterService		propertyMasterService			= null;
	
	@Autowired
	private DynarestCrudService			dynarestCrudService				= null;
	
	@Autowired
	private ModuleRevisionService		moduleRevisionService			= null;
	

	@Transactional(readOnly = false)
	public String restoreTagData(HttpServletRequest request) throws Exception {
		String					tagId				= request.getParameter("tagId");
	//	Gson					g					= new GsonBuilder().setDateFormat("dd-MM-yyyy HH:mm:ss").create();
	Gson						g					= new GsonBuilder()
			.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
															@Override
															public Date deserialize(JsonElement json, Type typeOfT,
																	JsonDeserializationContext context)
																	throws JsonParseException {
																try {
																	// if it's a number (timestamp)
																	if (json.getAsJsonPrimitive().isNumber()) {
																		return new Date(json.getAsLong());
																	} else {
																		// fallback for string dates
																		String			dateStr	= json.getAsString()
																				.replaceAll("\u202F", " ")
																				.replaceAll("\u00A0", " ");
																		SimpleDateFormat sdf	= new SimpleDateFormat(
																				"dd-MM-yyyy HH:mm:ss");
																		return sdf.parse(dateStr);
																	}
																} catch (Exception e) {
																	throw new JsonParseException(e);
																}
															}
														})
			.create();
		List<TagEntityDetails>	tagEntityDetails	= tagDetailsRepository.getTagEntityDetailsByTagId(tagId);
		for (Iterator iterator = tagEntityDetails.iterator(); iterator.hasNext();) {
			TagEntityDetails	tagEntityData	= (TagEntityDetails) iterator.next();
			String				moduleId		= tagEntityData.getModuleId();
			String				moduleJson		= tagEntityData.getModuleJson();
			String				entityId		= tagEntityData.getEntityId();
			String				permissionJson	= tagEntityData.getPermissions();
			switch (moduleId) {
			    case Constant.TEMPLATEMODID:
			        restoreTemplateData(moduleJson, g,moduleId,entityId,permissionJson);
			        break;

			    case Constant.DYNAFORM_MOD_ID:
			        restoreFormData(moduleJson, g,moduleId,entityId,permissionJson);
			        break;
			        
			    case Constant.DYNA_REST_MOD_ID:
			        restoreRestAPIData(moduleJson, g,moduleId,entityId,permissionJson);
			        break;
			        
			    case Constant.AUTOCOMPLETEMODID:
			        restoreAutocompleteData(moduleJson, g,moduleId,entityId,permissionJson);
			        break;
			        
			    case Constant.DASHBOARDMODID:
			        restoreDashboardData(moduleJson, g,moduleId,entityId,permissionJson);
			        break;
			        
			    case Constant.DASHLETMODID:
			        restoreDashletData(moduleJson, g,moduleId,entityId,permissionJson);
			        break;
			        
			    case Constant.GRIDMODID:
			        restoreGridData(moduleJson, g,moduleId,entityId,permissionJson);
			        break;
			        
			    case Constant.RESOURCEBUNDLEMODID:
			        restoreResourceBundleData(moduleJson, g,moduleId,entityId,permissionJson);
			        break;
			        
			    case Constant.APICLIENTMODID:
			        restoreAPIClientData(moduleJson, g,moduleId,entityId,permissionJson);
			        break;
			        
			    case Constant.SCHEDULERMODID:
			        restoreSchedulerData(moduleJson, g,moduleId,entityId,permissionJson);
			        break;
			        
			    case Constant.NOTIFICATIONMODID:
			        restoreNotificationData(moduleJson, g,moduleId,entityId,permissionJson);
			        break;
			        
			    case Constant.FORMIOMODID:
			        restoreFormIOData(moduleJson, g,moduleId,entityId,permissionJson);
			        break;
			        
			    case Constant.SCRIPT_lIB_MOD_ID:
			        restoreScriptLibData(moduleJson, g,moduleId,entityId,permissionJson);
			        break;
			        
			    case Constant.FILEBINMODID:
			        restoreFileBinData(moduleJson, g,moduleId,entityId,permissionJson);
			        break;
			        
			    case Constant.ROUTERMODID:
			        restoreRouterData(moduleJson, g,moduleId,entityId,permissionJson);
			        break;
			        
			    case Constant.APPCONFIGURATIONMODID:
			        restoreAppConfigurationData(moduleJson, g,moduleId,entityId,permissionJson);
			        break;
			        
			    case Constant.ADDITIONALDATASOURCEMODID:
			        restoreAdditionalDataSourceData(moduleJson, g,moduleId,entityId,permissionJson);
			        break;
			        
			    default:
			        // handle unknown moduleId
			        logger.info("No matching module for: " + moduleId);
			        break;
			}
			
		}
		
		return null;

	}
	
	private void restoreAdditionalDataSourceData(String moduleJson,Gson g, String moduleId, String entityId, String permissionJson) throws Exception {
		AdditionalDatasource additionalDatasource = g.fromJson(moduleJson, AdditionalDatasource.class);
		additionalDatasourceDAO.saveAdditionalDatasource(additionalDatasource);
	}
	

	private void restorePermissionData(String permissionJson, Gson g,String moduleId, String entityId) throws Exception {
		// Delete old permissions first
		moduleVersionDAO.deleteExistingPermissionDetails(moduleId, entityId);
		
		Type listType = new TypeToken<List<JwsEntityRoleAssociation>>(){}.getType();
		List<JwsEntityRoleAssociation> roles = g.fromJson(permissionJson, listType);

		for (JwsEntityRoleAssociation role : roles) {
		    jwsEntityRoleAssociationDAO.saveJwsEntityRoleAssociation(role);
		}
//		JwsEntityRoleAssociation role = g.fromJson(permissionJson, JwsEntityRoleAssociation.class);
//		jwsEntityRoleAssociationDAO.saveJwsEntityRoleAssociation(role);
	}
	
	private void restoreTemplateData(String moduleJson,Gson g,String moduleId, String entityId, String permissionJson) throws Exception {
		TemplateMaster	template	= g.fromJson(moduleJson, TemplateMaster.class);
		template.setCreatedBy("anonymous");
		template.setUpdatedDate(new Date());
		if (moduleJson != null) {
			String checksum = fileUtilities.generateChecksum(moduleJson);
			template.setChecksum(checksum);
		}
		templateDAO.saveVelocityTemplateData(template);
		TemplateVO templateVO = new TemplateVO(template.getTemplateId(), template.getTemplateName(),
				template.getTemplate(), template.getTemplateTypeId(),template.getChecksum(),template.getCreatedBy(),template.getUpdatedBy(), new Date());
		moduleVersionService.saveModuleVersion(templateVO, null, template.getTemplateId(), "jq_template_master",
				Constant.REVISION_SOURCE_VERSION_TYPE);
		restorePermissionData(permissionJson, g, moduleId, entityId);
	}
	
	private void restoreFormData(String moduleJson,Gson g,String moduleId, String entityId, String permissionJson) throws Exception {
		DynamicForm	dynamicForm	= g.fromJson(moduleJson, DynamicForm.class);
		List<DynamicFormSaveQuery> dynamicFormNewSaveQueries = new ArrayList<>();
		List<DynamicFormSaveQuery> dynamicFormSaveQueries = dynamicForm.getDynamicFormSaveQueries();
		for (DynamicFormSaveQuery dfsq : dynamicFormSaveQueries) {
			DynamicFormSaveQuery dfs = new DynamicFormSaveQuery();
			dfs.setDynamicFormId(dynamicForm.getFormId());
			dfs.setFormSaveQuery(dfsq.getDynamicFormSaveQuery());
			dfs.setSequence(dfsq.getSequence());
			dfs.setDatasourceId(dfsq.getDatasourceId());
			dfs.setResultVariableName(dfsq.getResultVariableName());
			dfs.setDaoQueryType(dfsq.getDaoQueryType());
			dfs.setDynamicFormQueryId(dfsq.getDynamicFormId());
			dynamicFormNewSaveQueries.add(dfs);
		}
		dynamicForm.setLastUpdatedTs(new Date());
		if (moduleJson != null) {
			String checksum = fileUtilities.generateChecksum(moduleJson);
			dynamicForm.setFormBodyChecksum(checksum);
		}
		dynamicForm.setDynamicFormSaveQueries(dynamicFormNewSaveQueries);
		dynamicFormCrudDAO.saveDynamicForm(dynamicForm);
		if(dynamicForm.getDynamicFormSaveQueries() == null) {
			dynamicForm.setDynamicFormSaveQueries(dynamicFormNewSaveQueries);
		}
		DynamicFormVO dynamicFormVO = dynamicFormDownloadUploadModule.convertEntityToVO(dynamicForm);
		
		moduleVersionService.saveModuleVersion(dynamicFormVO, null, dynamicForm.getFormId(), "jq_dynamic_form",
				Constant.REVISION_SOURCE_VERSION_TYPE);
		
		restorePermissionData(permissionJson, g, moduleId, entityId);
	}
	
	private void restoreRestAPIData(String moduleJson,Gson g,String moduleId, String entityId, String permissionJson) throws Exception {
		ObjectMapper					objectMapper	= new ObjectMapper();
	//	HttpServletResponse httpServletResponse =  new 
		//							g				= new GsonBuilder().setDateFormat("dd-MM-yyyy HH:mm:ss")
			//	.create();
	//	UserDetailsVO detailsVO = null;
//		if ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes() != null) {
//			detailsVO = detailsService.getUserDetails();
//		} else {
//			detailsVO = new UserDetailsVO("admin@jquiver.io", "admin", Arrays.asList("ADMIN"), "admin");
//		}

		String			dbDateFormat	= propertyMasterService.getDateFormatByName(Constant.PROPERTY_MASTER_OWNER_TYPE,
				Constant.PROPERTY_MASTER_OWNER_ID, Constant.JWS_DATE_FORMAT_PROPERTY_NAME, com.trigyn.jws.dbutils.utils.Constant.JWS_JAVA_DATE_FORMAT_PROPERTY_NAME);
		DateFormat		dateFormat		= new SimpleDateFormat(dbDateFormat);
		objectMapper.setDateFormat(dateFormat);
		Map<String, String>				formData		= objectMapper.readValue(moduleJson, Map.class);
		MultiValueMap<String, String>	multivalueMap	= new LinkedMultiValueMap<>();
		for (Entry<String, String> formDataMap : formData.entrySet()) {
			List<String> multiValueString = new ArrayList<>();
			if (formDataMap.getValue() != null) {
				Object formValue = formDataMap.getValue();
				multiValueString.add(formValue.toString());
				multivalueMap.put(formDataMap.getKey(), multiValueString);
				if (formDataMap.getKey().equalsIgnoreCase("isEdit")) {
					multiValueString = new ArrayList<>();
					multiValueString.add(Constant.DYNAMIC_FORM_IS_EDIT);
					multivalueMap.put(formDataMap.getKey(), multiValueString);
				}
			}
		}
		dynamicFormService.saveDynamicForm(multivalueMap,null);
		dynarestCrudService.deleteDAOQueries(multivalueMap,Constant.REVISION_SOURCE_VERSION_TYPE);
		dynarestCrudService.saveDAOQueries(multivalueMap,Constant.REVISION_SOURCE_VERSION_TYPE);
		
		
		//		JwsDynamicRestDetail			dynarest					= g.fromJson(moduleJson,
//				JwsDynamicRestDetail.class);
//		dynarest.setLastUpdatedTs(new Date());
//		if (moduleJson != null) {
//			String checksum = fileUtilities.generateChecksum(moduleJson);
//			dynarest.setServiceLogicChecksum(checksum);
//		}
//		List<JwsDynamicRestDaoDetail>	jwsDynamicRestDaoDetails	= dynarest.getJwsDynamicRestDaoDetails();
//		dynarest.setJwsDynamicRestDaoDetails(null);
//		
//		jwsDynarestDAO.saveDynaRestDetail(dynarest, jwsDynamicRestDaoDetails);
//
//		dynarest.setJwsDynamicRestDaoDetails(jwsDynamicRestDaoDetails);
//	
//		RestApiDetailsJsonVO vo = importService.convertDynaRestEntityToVO(dynarest);
//		
//	moduleVersionService.saveModuleVersion(vo, null, dynarest.getJwsDynamicRestId(), "jq_dynamic_rest_details",
	//			Constant.REVISION_SOURCE_VERSION_TYPE);
		
		moduleRevisionService.saveModuleVersioning(multivalueMap, Constant.REVISION_SOURCE_VERSION_TYPE);
		
		restorePermissionData(permissionJson, g, moduleId, entityId);
	}
	
	private void restoreAutocompleteData(String moduleJson,Gson g,String moduleId, String entityId, String permissionJson) throws Exception {
		Autocomplete	autocomplete	= g.fromJson(moduleJson, Autocomplete.class);
		autocomplete.setLastUpdatedTs(new Date());
		AutocompleteVO	autocompleteVO	= typeAheadService.convertEntityToVO(autocomplete);
		typeAheadDAO.saveAutocomplete(autocomplete);
		
		moduleVersionService.saveModuleVersion(autocompleteVO, null, autocomplete.getAutocompleteId(), "jq_autocomplete_details",
				Constant.REVISION_SOURCE_VERSION_TYPE);
		
		restorePermissionData(permissionJson, g, moduleId, entityId);
	}
	
	private void restoreDashboardData(String moduleJson,Gson g,String moduleId, String entityId, String permissionJson) throws Exception {
		DashboardVO		dashboardVO		= g.fromJson(moduleJson, DashboardVO.class);
		dashboardCrudService.deleteAllDashletFromDashboard(dashboardVO);
		dashboardCrudService.deleteAllDashboardRoles(dashboardVO);
		dashboardCrudService.saveDashboardDetails(dashboardVO, null, Constant.MASTER_SOURCE_VERSION_TYPE);
		restorePermissionData(permissionJson, g, moduleId, entityId);
	}
	
	
	private void restoreDashletData(String moduleJson, Gson g,String moduleId, String entityId, String permissionJson) throws Exception {
		Dashlet dashlet = g.fromJson(moduleJson, Dashlet.class);
		dashlet.setLastUpdatedTs(new Date());
		if (moduleJson != null) {
			String checksum = fileUtilities.generateChecksum(moduleJson);
			dashlet.setDashletBodyChecksum(checksum);
		}
		DashletVO dashletVO = dashletDownloadUploadModule.convertDashletEntityToVO(dashlet);

		dashboardCrudService.saveDashlet(dashletVO, Constant.REVISION_SOURCE_VERSION_TYPE);

//		moduleVersionService.saveModuleVersion(dashletVO, null, dashlet.getDashletId(), "jq_dashlet",
//				Constant.REVISION_SOURCE_VERSION_TYPE);
		
		restorePermissionData(permissionJson, g, moduleId, entityId);
	}
	
	
	private void restoreGridData(String moduleJson, Gson g,String moduleId, String entityId, String permissionJson) throws Exception {
		GridDetails gridDetails = g.fromJson(moduleJson, GridDetails.class);
		gridDetails.setLastUpdatedTs(new Date());
		gridDetailsDAO.saveGridDetails(gridDetails);
		GridDetailsJsonVO vo = importService.convertGridEntityToVO(gridDetails);

		moduleVersionService.saveModuleVersion(vo, null, gridDetails.getGridId(), "jq_grid_details",
				Constant.REVISION_SOURCE_VERSION_TYPE);
		
		restorePermissionData(permissionJson, g, moduleId, entityId);
	}
	
	
	private void restoreResourceBundleData(String moduleJson, Gson g, String moduleId, String entityId, String permissionJson) throws Exception {
		TypeReference<List<ResourceBundleVO>> resourceBundleType = new TypeReference<List<ResourceBundleVO>>() {
		};
		ObjectMapper objectMapper = new ObjectMapper();
		List<ResourceBundleVO> resourceBundleList = new ArrayList<>();

		JsonNode node = objectMapper.readTree(moduleJson);
		String			dbDateFormat	= propertyMasterService.getDateFormatByName(Constant.PROPERTY_MASTER_OWNER_TYPE,
				Constant.PROPERTY_MASTER_OWNER_ID, Constant.JWS_DATE_FORMAT_PROPERTY_NAME, com.trigyn.jws.dbutils.utils.Constant.JWS_JAVA_DATE_FORMAT_PROPERTY_NAME);
		DateFormat		dateFormat		= new SimpleDateFormat(dbDateFormat);
		objectMapper.setDateFormat(dateFormat);
		if (node.isArray()) {
		    resourceBundleList = objectMapper.readValue(moduleJson, new TypeReference<List<ResourceBundleVO>>() {});
		} else {
		    ResourceBundleVO singleVO = objectMapper.readValue(moduleJson, ResourceBundleVO.class);
		    resourceBundleList.add(singleVO);
		}
		resourceBundleService.saveResourceBundleDetails(resourceBundleList, Constant.REVISION_SOURCE_VERSION_TYPE);
		
		restorePermissionData(permissionJson, g, moduleId, entityId);
	}
	
	private void restoreAPIClientData(String moduleJson,Gson g, String moduleId, String entityId, String permissionJson) throws Exception {
		JqApiClientDetails apiClientDetails = g.fromJson(moduleJson, JqApiClientDetails.class);
		apiClientDetails.setUpdatedDate(new Date());
		JsonObject jsonObject = JsonParser.parseString(moduleJson).getAsJsonObject();
		if(jsonObject.has("encryptionAlgoId")) {
			JqEncAlgModPadKeyLookup algModPadKeyLookup =  new JqEncAlgModPadKeyLookup();
			algModPadKeyLookup.setEncLookupId(jsonObject.get("encryptionAlgoId").getAsInt());
		    apiClientDetails.setJqEncAlgModPadKeyLookup(algModPadKeyLookup);
		}
		apiClientDetailsDAO.saveAdditionalDatasource(apiClientDetails);
		
	}
	
	
	private void restoreSchedulerData(String moduleJson,Gson g, String moduleId, String entityId, String permissionJson) throws Exception {
		JqScheduler scheduler = g.fromJson(moduleJson, JqScheduler.class);
		scheduler.setModifiedDate(new Date());
		schedulerDAO.saveScheduler(scheduler);
		importService.executeScheduler(scheduler);
	}
	
	private void restoreNotificationData(String moduleJson,Gson g, String moduleId, String entityId, String permissionJson) throws Exception {
		GenericUserNotification	notification	= g.fromJson(moduleJson, GenericUserNotification.class);
		notification.setUpdatedDate(new Date());
		notificationDao.saveGenericUserNotification(notification);
		GenericUserNotificationJsonVO vo = importService.convertNotificationEntityToVO(notification);
		
		moduleVersionService.saveModuleVersion(vo, null, notification.getNotificationId(), "jq_generic_user_notification",
				Constant.REVISION_SOURCE_VERSION_TYPE);
		
		restorePermissionData(permissionJson, g, moduleId, entityId);
	}
	
	private void restoreFormIOData(String moduleJson,Gson g, String moduleId, String entityId, String permissionJson) throws Exception {
		FormIO formIoDetails = g.fromJson(moduleJson, FormIO.class);
		formIoDetails.setLastUpdatedTs(new Date());
		if (moduleJson != null) {
			String checksum = fileUtilities.generateChecksum(moduleJson);
			formIoDetails.setFormIoChecksum(checksum);
		}
		formIORepository.saveAndFlush(formIoDetails);
		FormIOImportExportVO vo = importService.convertFormIOEntityToVO(formIoDetails);
		
		moduleVersionService.saveModuleVersion(vo, null, formIoDetails.getFormIoId(), "jq_form_io",
				Constant.REVISION_SOURCE_VERSION_TYPE);
		
		restorePermissionData(permissionJson, g, moduleId, entityId);
	}
	
	
	private void restoreScriptLibData(String moduleJson,Gson g, String moduleId, String entityId, String permissionJson) throws Exception {
		ScriptLibraryDetails	scriptLibDetails	= g.fromJson(moduleJson, ScriptLibraryDetails.class);
		scriptLibDetails.setUpdatedDate(new Date());
		dynamicFormService.saveScriptLibraryDetails(scriptLibDetails);
		if(scriptLibDetails.getScriptLibraryConnection() != null) {
			for(ScriptLibraryConnection slc : scriptLibDetails.getScriptLibraryConnection()) {
				dynamicFormService.saveScriptLibConnDetails(slc);
			}
		}
		
		restorePermissionData(permissionJson, g, moduleId, entityId);
	}
	
	private void restoreFileBinData(String moduleJson,Gson g, String moduleId, String entityId, String permissionJson) throws Exception {
		FileUploadConfigVO	fileUploadConfigVO	= g.fromJson(moduleJson,
				FileUploadConfigVO.class);
		FileUploadConfig	fileUploadConfig	= fileUploadConfigService.convertFileUploadVOToEntity(fileUploadConfigVO);
		fileUploadConfigService.saveFileUploadConfig(fileUploadConfig);
		restorePermissionData(permissionJson, g, moduleId, entityId);
	}
	
	private void restoreRouterData(String moduleJson,Gson g, String moduleId, String entityId, String permissionJson) throws Exception {
		ModuleListing			module				= g.fromJson(moduleJson, ModuleListing.class);
		module.setUpdatedDate(new Date());
		moduleService.saveModuleListing(module);
		restorePermissionData(permissionJson, g, moduleId, entityId);
	}
	
	private void restoreAppConfigurationData(String moduleJson,Gson g, String moduleId, String entityId, String permissionJson) throws Exception {
		PropertyMaster propertyMaster = g.fromJson(moduleJson, PropertyMaster.class);
		propertyMaster.setLastModifiedDate(new Date());
	//	propertyMaster.setIsDeleted(0);
		propertyMasterDAO.savePropertyMaster(propertyMaster);
		PropertyMasterJsonVO vo = importService.convertPropertyMasterEntityToVO(propertyMaster);
		moduleVersionService.saveModuleVersion(vo, null, propertyMaster.getPropertyMasterId(), "jq_property_master",
			Constant.REVISION_SOURCE_VERSION_TYPE);
		
		restorePermissionData(permissionJson, g, moduleId, entityId);
	}
}
