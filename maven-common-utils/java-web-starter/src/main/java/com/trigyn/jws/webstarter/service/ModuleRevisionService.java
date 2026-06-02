package com.trigyn.jws.webstarter.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.trigyn.jws.dbutils.entities.AdditionalDatasource;
import com.trigyn.jws.dbutils.entities.PropertyMaster;
import com.trigyn.jws.dbutils.repository.AdditionalDatasourceDAO;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.service.ModuleVersionService;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dbutils.vo.ModuleVersionVO;
import com.trigyn.jws.dbutils.vo.ScriptLibraryVO;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.dynamicform.service.DynamicFormService;
import com.trigyn.jws.dynarest.entities.JqApiClientDetails;
import com.trigyn.jws.dynarest.entities.JqEncAlgModPadKeyLookup;
import com.trigyn.jws.dynarest.entities.JqScheduler;
import com.trigyn.jws.dynarest.repository.ApiClientDetailsDAO;
import com.trigyn.jws.dynarest.repository.SchedulerDAO;
import com.trigyn.jws.formio.entities.FormIO;
import com.trigyn.jws.formio.service.FormIOService;
import com.trigyn.jws.formio.utils.FormIOUtils;
import com.trigyn.jws.formio.vo.FormIOVO;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibraryConnection;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibraryDetails;
import com.trigyn.jws.typeahead.model.AutocompleteVO;
import com.trigyn.jws.typeahead.service.TypeAheadService;
import com.trigyn.jws.webstarter.dao.NotificationDAO;
import com.trigyn.jws.webstarter.utils.Constant;
import com.trigyn.jws.webstarter.vo.GenericUserNotification;
import com.trigyn.jws.webstarter.vo.GenericUserNotificationJsonVO;
import com.trigyn.jws.webstarter.vo.PropertyMasterJsonVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class ModuleRevisionService {

	@Autowired
	private ModuleVersionService	moduleVersionService	= null;

	@Autowired
	private DynamicFormService		dynamicFormService		= null;

	@Autowired
	private TypeAheadService		typeAheadService		= null;

	@Autowired
	private DynamicFormCrudService	dynamicFormCrudService	= null;

	@Autowired
	private PropertyMasterService	propertyMasterService	= null;

	@Autowired
	private DynarestCrudService		dynarestCrudService		= null;

	@Autowired
	private FormIOService			formIOService			= null;
	
	@Autowired
	private AdditionalDatasourceDAO	additionalDatasourceDAO	= null;
	
	@Autowired
	private ApiClientDetailsDAO		apiClientDetailsDAO		= null;
	
	@Autowired
	private SchedulerDAO			schedulerDAO			= null;
	
	@Autowired
	private NotificationDAO			notificationDao			= null;
	
	@Autowired
	private ImportService			importService			= null;
	
	@Autowired
	private IUserDetailsService		detailsService			= null;
	
	@Autowired
	private PropertyMasterDAO		propertyMasterDAO		= null;
	
	public void saveModuleVersioning(MultiValueMap<String, String> formData, Integer sourceTypeId) throws Exception {
		Map<String, Object>	versioningData	= new HashMap<>();
		String				primaryKey		= null;
		String				entityName		= null;
		for (Entry<String, List<String>> formDataMap : formData.entrySet()) {
			versioningData.put(formDataMap.getKey(), formDataMap.getValue().get(0));
		}
		if (versioningData.get("primaryKey") != null) {
			primaryKey = versioningData.get("primaryKey").toString();
		}
		if (versioningData.get("entityName") != null) {
			entityName = versioningData.get("entityName").toString();
		}
		moduleVersionService.saveModuleVersion(versioningData, null, primaryKey, entityName, sourceTypeId);
	}

	public void saveUpdatedContent(HttpServletRequest a_httpServletRequest,HttpServletResponse a_httpServletResponse) throws Exception {
		String							modifiedContent	= a_httpServletRequest.getParameter("modifiedContent");
		String							moduleType		= a_httpServletRequest.getParameter("moduleType");
		ObjectMapper					objectMapper	= new ObjectMapper();
		Gson							g				= new GsonBuilder().setDateFormat("dd-MM-yyyy HH:mm:ss")
				.create();
		UserDetailsVO detailsVO = null;
		if ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes() != null) {
			detailsVO = detailsService.getUserDetails();
		} else {
			detailsVO = new UserDetailsVO("admin@jquiver.io", "admin", Arrays.asList("ADMIN"), "admin");
		}

		String			dbDateFormat	= propertyMasterService.getDateFormatByName(Constant.PROPERTY_MASTER_OWNER_TYPE,
				Constant.PROPERTY_MASTER_OWNER_ID, Constant.JWS_DATE_FORMAT_PROPERTY_NAME, com.trigyn.jws.dbutils.utils.Constant.JWS_JAVA_DATE_FORMAT_PROPERTY_NAME);
		DateFormat		dateFormat		= new SimpleDateFormat(dbDateFormat);
		objectMapper.setDateFormat(dateFormat);
		Map<String, String>				formData		= objectMapper.readValue(modifiedContent, Map.class);
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
		if (moduleType.equals(Constant.ModuleType.AUTOCOMPLETE.getModuleType())) {
			typeAheadService.saveAutocompleteDetails(multivalueMap, Constant.REVISION_SOURCE_VERSION_TYPE);
		} else if (moduleType.equals(Constant.ModuleType.DYNAMICFORM.getModuleType())) {
			DynamicForm dynamicform = dynamicFormCrudService.saveDynamicFormDetails(multivalueMap, Constant.REVISION_SOURCE_VERSION_TYPE);
			dynamicFormCrudService.saveDynamicFormDetails2(multivalueMap, dynamicform, Constant.REVISION_SOURCE_VERSION_TYPE);
		} else if (moduleType.equalsIgnoreCase(Constant.ModuleType.FORMIO.getModuleType())) {
			FormIO formIoEntity;
			FormIOVO formIoVo;
			try {
				formIoVo = objectMapper.readValue(modifiedContent, FormIOVO.class);
				formIoEntity = FormIOUtils.convertToFormIoEntity(formIoVo);
				formIOService.saveFormIOByVersion(formIoEntity, formIoVo);
			} catch (JsonProcessingException jpe) {
				throw new Exception("Error while saving the contents.");
			} catch (CustomStopException cse) {
				throw new CustomStopException("Error while saving the contents.");
			}
		}else if (Constant.MasterModuleType.ADDITIONALDATASOURCE.getModuleType().equalsIgnoreCase(moduleType)) {
				AdditionalDatasource additionalDatasource = g.fromJson(modifiedContent, AdditionalDatasource.class);
				additionalDatasourceDAO.saveAdditionalDatasource(additionalDatasource);
		
		} else if (Constant.MasterModuleType.SCRIPTLIBRARY.getModuleType().equalsIgnoreCase(moduleType)) {
			modifiedContent = modifiedContent.replaceAll("\u202F", " ").replaceAll("\u00A0", " ");
			Gson gs = new GsonBuilder()
			        .setDateFormat("dd-MM-yyyy HH:mm:ss")
			        .create();
			ScriptLibraryDetails	scriptLibDetails	= gs.fromJson(modifiedContent, ScriptLibraryDetails.class);
			dynamicFormService.saveScriptLibDetails(scriptLibDetails);
			ScriptLibraryVO	scriptLibraryVO	= importService.convertScriptLibEntityToVO(scriptLibDetails);
			moduleVersionService.saveModuleVersion(scriptLibraryVO, null, scriptLibDetails.getScriptLibId(), "jq_script_lib_details",
					Constant.REVISION_SOURCE_VERSION_TYPE);
			if (scriptLibDetails.getScriptLibraryConnection() != null) {
				for (ScriptLibraryConnection slc : scriptLibDetails.getScriptLibraryConnection()) {
					dynamicFormService.saveScriptLibConnDetails(slc);
				}
			}
		} else if (Constant.MasterModuleType.APICLIENTDETAILS.getModuleType().equalsIgnoreCase(moduleType)) {
			JqApiClientDetails apiClientDetails = g.fromJson(modifiedContent, JqApiClientDetails.class);
			JsonObject jsonObject = JsonParser.parseString(modifiedContent).getAsJsonObject();
			if(jsonObject.has("encryptionAlgoId")) {
				JqEncAlgModPadKeyLookup algModPadKeyLookup =  new JqEncAlgModPadKeyLookup();
				algModPadKeyLookup.setEncLookupId(jsonObject.get("encryptionAlgoId").getAsInt());
			    apiClientDetails.setJqEncAlgModPadKeyLookup(algModPadKeyLookup);
			}
				apiClientDetailsDAO.saveAdditionalDatasource(apiClientDetails);
			} else if (Constant.MasterModuleType.SCHEDULER.getModuleType().equalsIgnoreCase(moduleType)) {
				JqScheduler scheduler = g.fromJson(modifiedContent, JqScheduler.class);
				schedulerDAO.saveScheduler(scheduler);
				// executeScheduler(scheduler);
			} else if (Constant.MasterModuleType.NOTIFICATION.getModuleType().equalsIgnoreCase(moduleType)) {
				GenericUserNotification	notification	= g.fromJson(modifiedContent, GenericUserNotification.class);
					notification.setUpdatedBy(detailsVO.getUserName());
					notification.setUpdatedDate(new Date());
	
					notificationDao.saveGenericUserNotification(notification);
					GenericUserNotificationJsonVO vo = importService.convertNotificationEntityToVO(notification);
					moduleVersionService.saveModuleVersion(vo, null, notification.getNotificationId(), "jq_generic_user_notification",
						Constant.REVISION_SOURCE_VERSION_TYPE);
				} else if (Constant.ModuleType.APPLICATIONCONFIGURATION.getModuleType()
						.equalsIgnoreCase(moduleType)) {
					PropertyMaster propertyMaster = g.fromJson(modifiedContent, PropertyMaster.class);
					propertyMaster.setLastModifiedDate(new Date());
					propertyMasterDAO.savePropertyMaster(propertyMaster);
					PropertyMasterJsonVO vo = importService.convertPropertyMasterEntityToVO(propertyMaster);
					moduleVersionService.saveModuleVersion(vo, null, propertyMaster.getPropertyMasterId(),
							"jq_property_master", Constant.REVISION_SOURCE_VERSION_TYPE);
				} else {
			dynamicFormService.saveDynamicForm(multivalueMap,a_httpServletResponse);
			if (moduleType.equals(Constant.ModuleType.DYNAREST.getModuleType())) {
				dynarestCrudService.deleteDAOQueries(multivalueMap,Constant.REVISION_SOURCE_VERSION_TYPE);
				dynarestCrudService.saveDAOQueries(multivalueMap,Constant.REVISION_SOURCE_VERSION_TYPE);
			} 
			saveModuleVersioning(multivalueMap, Constant.REVISION_SOURCE_VERSION_TYPE);
		}
	}

	public Map<String, Object> getModuleVersioningData(HttpServletRequest a_httpServletRequest) throws Exception {
		String					moduleType		= a_httpServletRequest.getParameter("moduleType");
		String					entityId		= a_httpServletRequest.getParameter("entityId");
		String					entityName		= a_httpServletRequest.getParameter("cmvEntityName");
		String					saveUrl			= a_httpServletRequest.getParameter("saveUrl");
		String					previousPageUrl	= a_httpServletRequest.getParameter("previousPageUrl");
		String					formId			= a_httpServletRequest.getParameter("formId");
		String					dateFormat		= propertyMasterService.getDateFormatByName(
				Constant.PROPERTY_MASTER_OWNER_TYPE, Constant.PROPERTY_MASTER_OWNER_ID,
				Constant.JWS_DATE_FORMAT_PROPERTY_NAME, Constant.JWS_DB_DATE_FORMAT_PROPERTY_NAME);

		List<ModuleVersionVO>	versionVOs		= moduleVersionService.fetchModuleVersionDetails(entityId, entityName);
		String					moduleName		= a_httpServletRequest.getParameter("moduleName");

		Map<String, Object>		templateMap		= new HashMap<>();
		templateMap.put("moduleType", moduleType);
		templateMap.put("cmvEntityName", entityName);
		templateMap.put("entityId", entityId);
		templateMap.put("revesionDetailsVOs", versionVOs);
		templateMap.put("moduleName", moduleName);
		templateMap.put("formId", formId);
		templateMap.put("saveUrl", saveUrl);
		templateMap.put("dateFormat", dateFormat);
		templateMap.put("previousPageUrl", previousPageUrl);

		String	isImport		= a_httpServletRequest.getParameter("isImport");
		String	isTagView		= a_httpServletRequest.getParameter("isTagView");
		String	isDeveloperView	= a_httpServletRequest.getParameter("isDeveloperView");
		if ("true".equals(isTagView) || "true".equals(isDeveloperView)) {
			if (null != isTagView) {
				templateMap.put("isTagView", isTagView);
			} else {
				templateMap.put("isTagView", "false");
			}
			if (null != isDeveloperView) {
				templateMap.put("isDeveloperView", isDeveloperView);
			} else {
				templateMap.put("isDeveloperView", "false");
			}

			String	tagModuleJson			= a_httpServletRequest.getParameter("moduleJson");
			String	moduleId				= a_httpServletRequest.getParameter("moduleId");
			String	cmvEntityName			= a_httpServletRequest.getParameter("cmvEntityName");
			String	tmoduleType				= a_httpServletRequest.getParameter("moduleType");
			String	saveURL					= a_httpServletRequest.getParameter("saveUrl");
			String	isNonVersioningModule	= a_httpServletRequest.getParameter("isNonVersioningModule");
			String	nonVersioningFetchURL	= a_httpServletRequest.getParameter("nonVersioningFetchURL");
			String	permissionJson			= a_httpServletRequest.getParameter("permissionJson");
			String	versioningFetchURL		= a_httpServletRequest.getParameter("versioningFetchURL");

			templateMap.put("cmvEntityName", cmvEntityName);
			templateMap.put("moduleType", tmoduleType);
			templateMap.put("saveUrl", saveURL);
			Gson gson = new GsonBuilder().create();
			templateMap.put("tagModuleJson", gson.toJson(tagModuleJson));
			templateMap.put("moduleId", moduleId);
			templateMap.put("isNonVersioningModule", isNonVersioningModule);
			templateMap.put("nonVersioningFetchURL", nonVersioningFetchURL);
			templateMap.put("permissionJson", permissionJson);
			templateMap.put("versioningFetchURL", versioningFetchURL);
		} else {
			templateMap.put("isTagView", "false");
			templateMap.put("isDeveloperView", "false");
		}

		if ("true".equals(isImport)) {
			String	importJson	= a_httpServletRequest.getParameter("importJson");
			Gson	gson		= new GsonBuilder().create();
			templateMap.put("importJson", gson.toJson(importJson));
			templateMap.put("isImport", isImport);
			templateMap.put("isNonVersioningModule", a_httpServletRequest.getParameter("isNonVersioningModule"));
			templateMap.put("nonVersioningFetchURL", a_httpServletRequest.getParameter("nonVersioningFetchURL"));

		} else {
			templateMap.put("isImport", "false");
		}

		return templateMap;
	}

}
