package com.trigyn.jws.tags.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trigyn.jws.dashboard.entities.Dashboard;
import com.trigyn.jws.dashboard.entities.Dashlet;
import com.trigyn.jws.dashboard.service.DashletModule;
import com.trigyn.jws.dashboard.service.DashletService;
import com.trigyn.jws.dashboard.vo.DashboardVO;
import com.trigyn.jws.dashboard.vo.DashletVO;
import com.trigyn.jws.dbutils.entities.JwsModuleVersion;
import com.trigyn.jws.dbutils.entities.ModuleListing;
import com.trigyn.jws.dbutils.entities.PropertyMaster;
import com.trigyn.jws.dbutils.repository.IModuleListingRepository;
import com.trigyn.jws.dbutils.repository.ModuleVersionDAO;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dbutils.vo.ModuleListingVO;
import com.trigyn.jws.dbutils.vo.ScriptLibraryVO;
import com.trigyn.jws.dbutils.vo.xml.FileUploadExportVO;
import com.trigyn.jws.dynamicform.dao.DynamicFormCrudDAO;
import com.trigyn.jws.dynarest.dao.FileUploadConfigDAO;
import com.trigyn.jws.dynarest.dao.JwsDynarestDAO;
import com.trigyn.jws.dynarest.entities.FileUpload;
import com.trigyn.jws.dynarest.entities.FileUploadTags;
import com.trigyn.jws.dynarest.entities.JqScheduler;
import com.trigyn.jws.dynarest.repository.FileUploadRepository;
import com.trigyn.jws.dynarest.repository.FileUploadTagRepository;
import com.trigyn.jws.dynarest.repository.IApiClientDetailsRepository;
import com.trigyn.jws.dynarest.repository.JqschedulerRepository;
import com.trigyn.jws.dynarest.service.FileUploadConfigService;
import com.trigyn.jws.formio.dao.IFormIORepository;
import com.trigyn.jws.formio.entities.FormIO;
import com.trigyn.jws.formio.vo.FormIOImportExportVO;
import com.trigyn.jws.formio.vo.FormIOVO;
import com.trigyn.jws.gridutils.dao.GridDetailsDAO;
import com.trigyn.jws.gridutils.entities.GridDetails;
import com.trigyn.jws.resourcebundle.repository.interfaces.IResourceBundleRepository;
import com.trigyn.jws.resourcebundle.service.ResourceBundleService;
import com.trigyn.jws.resourcebundle.vo.ResourceBundleVO;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibraryDetails;
import com.trigyn.jws.tags.entities.TagData;
import com.trigyn.jws.tags.entities.TagEntityDetails;
import com.trigyn.jws.tags.entities.TagEntityDetailsRequest;
import com.trigyn.jws.tags.repository.interfaces.ITagDataRepository;
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
import com.trigyn.jws.usermanagement.entities.JwsMasterModules;
import com.trigyn.jws.usermanagement.repository.JwsEntityRoleAssociationRepository;
import com.trigyn.jws.usermanagement.repository.JwsMasterModulesRepository;
import com.trigyn.jws.usermanagement.vo.JwsEntityRoleAssociationVO;
import com.trigyn.jws.webstarter.controller.TemplateCrudController;
import com.trigyn.jws.webstarter.dao.NotificationDAO;
import com.trigyn.jws.webstarter.service.DashboardCrudService;
import com.trigyn.jws.webstarter.service.ImportService;
import com.trigyn.jws.webstarter.utils.Constant;
import com.trigyn.jws.webstarter.vo.GridDetailsJsonVO;
import com.trigyn.jws.webstarter.vo.PropertyMasterJsonVO;

@Service
public class TagService {
	
	private final static Logger					logger						= LoggerFactory.getLogger(TagService.class);

	@Autowired
	private JwsMasterModulesRepository			jwsmasterModuleRepository;
	@Autowired
	private ITagDataRepository					tagDataRepository;
	@Autowired
	private ITagEntityDetailsRepository			tagDetailsRepository;
	@Autowired
	private FileUtilities						fileUtilities;
	@Autowired
	private ModuleVersionDAO					moduleVersionDAO;
	@Autowired
	private ImportService						importService;
	@Autowired
	private JqschedulerRepository				schedulerRepository			= null;
	@Autowired
	private ModuleService						moduleService				= null;
	@Autowired
	private IModuleListingRepository			iModuleListingRepository	= null;
	@Autowired
	private JwsEntityRoleAssociationRepository	jwsEntityRoleAssociationRepository;

	@Autowired
	private IUserDetailsService					detailsService				= null;

	@Autowired
	private FileUploadConfigDAO					fileUploadConfigDAO			= null;

	@Autowired
	private FileUploadConfigService				fileUploadConfigService		= null;

	@Autowired
	private FileUploadTagRepository				fileUploadTagRepository		= null;

	@Autowired
	private FileUploadRepository				fileUploadRepository		= null;

	@Autowired
	private PropertyMasterService				propertyMasterService		= null;

	@Autowired
	private IApiClientDetailsRepository			apiClientDetailsRepository	= null;

	@Autowired
	private ResourceBundleService				resourceBundleService		= null;

	@Autowired
	private IResourceBundleRepository			iResourceBundleRepository	= null;

	@Autowired
	private IFormIORepository					iFormIORepository			= null;

	@Autowired
	private PropertyMasterDAO					propertyMasterDAO			= null;

	@Autowired
	private GridDetailsDAO						gridDetailsDAO				= null;

	@Autowired
	private TemplateDAO							templateDAO					= null;

	@Autowired
	private DashboardCrudService				dashboardCrudService		= null;

	@Autowired
	private DashletModule						dashletModule				= null;
	@Autowired
	private DashletService						dashletService				= null;

	@Autowired
	private TypeAheadDAO						typeAheadDAO				= null;
	@Autowired
	private TypeAheadService					typeAheadService			= null;
	
	@Autowired
	private DynamicFormCrudDAO				dynamicFormCrudDAO			= null;
	
	@Autowired
	private JwsDynarestDAO						jwsDynarestDAO					= null;

	/**
	 * Saves tag and its associated entities
	 */
	@Transactional
	public String saveTagWithEntities(TagEntityDetailsRequest request) {
		Gson			gson			= new Gson();
		ObjectMapper	objectMapper	= new ObjectMapper();

		// Save Tag Master
		TagData			tagMaster		= new TagData();
		tagMaster.setTagId(UUID.randomUUID().toString());
		tagMaster.setTagName(request.getTagName());

		tagMaster.setCreatedBy(detailsService.getUserDetails().getUserName());
		tagMaster.setCreatedDate(new Date());
		tagMaster.setLastUpdatedBy(detailsService.getUserDetails().getUserName());
		tagMaster.setLastUpdatedTs(new Date());
		tagDataRepository.save(tagMaster);

		// Fetch all modules once
		List<JwsMasterModules> masterModules = jwsmasterModuleRepository.findAll();

		for (TagEntityDetailsRequest.EntityMapping mapping : request.getEntityMappings()) {
			JwsMasterModules module = masterModules.stream().filter(m -> m.getModuleId().equals(mapping.getModuleId()))
					.findFirst().orElse(null);

			if (module == null) {
				logger.info(" Module not found for ID: " + mapping.getModuleId());
				continue;
			}

			try {
				String moduleJson = processModuleEntity(module, mapping.getEntityId(), gson, objectMapper, tagMaster);

				if (moduleJson != null) {
					String checksum = fileUtilities.generateChecksum(moduleJson);
					saveTagEntityDetails(tagMaster.getTagId(), mapping, moduleJson, checksum);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return "Tag '" + request.getTagName() + "' saved successfully.";
	}

	// -------------------------------------------------------
	// MAIN DISPATCHER
	// -------------------------------------------------------
	private String processModuleEntity(JwsMasterModules module, String entityId, Gson gson, ObjectMapper objectMapper,
			TagData tagMaster) throws Exception {
		gson = new GsonBuilder().serializeNulls().create();
		Constant.MasterModuleType type = Constant.MasterModuleType.valueOfModuleType(module.getModuleType());
		switch (type) {
			case SCRIPTLIBRARY:
				return handleScriptLibrary(entityId, objectMapper, gson);
			case DYNAREST:
			case DYNAMICFORM:
				return getModuleVersionJson(entityId);
			case DASHBOARD:
				return handleDashboard(entityId, objectMapper, gson);
			case DASHLET:
				return handleDashlet(entityId, objectMapper, gson);
			case AUTOCOMPLETE:
				return handleAutocomplete(entityId, objectMapper, gson);
			case TEMPLATES:
				return handleTemplate(entityId, objectMapper, gson);
			case GRID:
				return handleGridUtils(entityId, objectMapper, gson);
			case APPLICATIONCONFIGURATION:
				return handleApplicationConfiguration(entityId, objectMapper, gson);
			case ADDITIONALDATASOURCE:
				return handleAdditionDataSource(entityId, objectMapper, gson);
			case HELPMANUAL:
				return "{}";
			case FORMIO:
				return handleFormIO(entityId, objectMapper, gson);
			case NOTIFICATION:
				return handleNotification(entityId, objectMapper, gson);
			case RESOURCEBUNDLE:
				//return getModuleVersionJson(entityId);
			return handleResourceBundle(entityId, objectMapper, gson);
			case APICLIENTDETAILS:
				return handleAPIClient(entityId, objectMapper, gson);
			case FILEMANAGER:
				return handleFileManager(entityId, objectMapper, gson);
			case ROUTER:
				return handleRouter(entityId, objectMapper, gson);
			case FILEIMPEXPDETAILS:
				return handleFileImpExpDetails(entityId, objectMapper, gson, tagMaster.getTagName());
			case SCHEDULER:
				return handleScheduler(entityId, objectMapper, gson);
			default:
				logger.info(" Unsupported module type: " + module.getModuleType());
				return null;
		}
	}

	private String handleGridUtils(String entityId, ObjectMapper mapper, Gson gson) throws Exception {
		GridDetails			gridDetails	= gridDetailsDAO.findGridDetailsById(entityId);
		GridDetailsJsonVO	vo			= importService.convertGridEntityToVO(gridDetails);
		return convertEntityToJson(vo, mapper, gson);
	}

	private String handleDashboard(String entityId, ObjectMapper mapper, Gson gson) throws Exception {
		Dashboard	dashboard	= dashboardCrudService.findDashboardByDashboardId(entityId);
		DashboardVO	vo			= dashboardCrudService.convertDashboardEntityToVO(dashboard);
		return convertEntityToJson(vo, mapper, gson);
	}

	private String handleDashlet(String entityId, ObjectMapper mapper, Gson gson) throws Exception {
		Dashlet		dashlet	= dashletService.getDashletById(entityId);
		DashletVO	vo		= dashletModule.convertDashletEntityToVO(dashlet);
		return convertEntityToJson(vo, mapper, gson);
	}

	private String handleAutocomplete(String entityId, ObjectMapper mapper, Gson gson) throws Exception {
		Autocomplete	autocomplete	= typeAheadDAO.findAutocomplete(entityId);
		AutocompleteVO	vo				= typeAheadService.convertEntityToVO(autocomplete);
		return convertEntityToJson(vo, mapper, gson);
	}

	private String handleTemplate(String entityId, ObjectMapper mapper, Gson gson) throws Exception {
		TemplateMaster	template	= templateDAO.findTemplateById(entityId);
		TemplateVO		vo			= importService.convertTemplateEntityToVO(template);
		return convertEntityToJson(vo, mapper, gson);
	}

	private String handleApplicationConfiguration(String entityId, ObjectMapper mapper, Gson gson) throws Exception {
		PropertyMaster			propertyObj	= propertyMasterDAO.findPropertyMasterById(entityId);

		PropertyMasterJsonVO	vo			= importService.convertPropertyMasterEntityToVO(propertyObj);
		return convertEntityToJson(vo, mapper, gson);
	}

	// -------------------------------------------------------
	// GENERIC HELPERS
	// -------------------------------------------------------
	private String getModuleVersionJson(String entityId) throws Exception {
		JwsModuleVersion version = moduleVersionDAO.getModuleVersionDetails(entityId);
		return version != null ? version.getModuleJson() : "{}";
	}

	private void saveTagEntityDetails(String tagId, TagEntityDetailsRequest.EntityMapping mapping, String moduleJson,
			String checksum) {

		TagEntityDetails tagDetails = new TagEntityDetails();
		tagDetails.setTagEntityDetailId(UUID.randomUUID().toString());
		tagDetails.setTagId(tagId);
		tagDetails.setModuleId(mapping.getModuleId());
		tagDetails.setEntityId(mapping.getEntityId());
		tagDetails.setModuleJson(moduleJson);
		tagDetails.setModuleJsonChecksum(checksum);
		tagDetails.setCreatedBy(detailsService.getUserDetails().getUserName());
		tagDetails.setCreatedDate(new Date());
		tagDetails.setLastUpdatedBy(detailsService.getUserDetails().getUserName());
		String permissionJson = buildPermissionJson(mapping.getEntityId(), mapping.getModuleId());
		tagDetails.setPermissions(permissionJson);
		tagDetails.setLastUpdatedTs(new Date());
		tagDetailsRepository.save(tagDetails);
	}

	// -------------------------------------------------------
	// SPECIFIC MODULE HANDLERS
	// -------------------------------------------------------

	private String handleScheduler(String entityId, ObjectMapper mapper, Gson gson) throws Exception {
		JqScheduler	scheduler	= schedulerRepository.retrieveSchedulerById(1, entityId);
		Object		schedulerVO	= importService.convertSchedulerEntityToVO(scheduler);
		return convertEntityToJson(schedulerVO, mapper, gson);
		// return importService.getSchedulerJson(entityId);
	}

	private String handleRouter(String entityId, ObjectMapper mapper, Gson gson) throws Exception {
		ModuleListing	siteLayout	= iModuleListingRepository.findById(entityId).orElse(null);
		ModuleListingVO	vo			= moduleService.convertEntityToVO(siteLayout);
		return convertEntityToJson(vo, mapper, gson);
	}

	private String handleNotification(String entityId, ObjectMapper mapper, Gson gson) throws Exception {
		// GenericUserNotification notification =
		// notificationDao.getNotificationDetails(entityId);
		// GenericUserNotificationJsonVO vo =
		// importService.convertNotificationEntityToVO(notification);
		// return convertEntityToJson(vo, mapper, gson);
		return importService.getNotificationJson(entityId);
	}

	private String handleAdditionDataSource(String entityId, ObjectMapper mapper, Gson gson) throws Exception {
		return importService.getAdditionalDatasourceJson(entityId);
	}

	private String handleFormIO(String entityId, ObjectMapper mapper, Gson gson) throws Exception {
		FormIO		formIoEntity;
		FormIOVO	formIoVo;
		formIoEntity = iFormIORepository.findById(entityId).orElse(null);
		FormIOImportExportVO vo = importService.convertFormIOEntityToVO(formIoEntity);
		return convertEntityToJson(vo, mapper, gson);
	}

	private String handleFileImpExpDetails(String entityId, ObjectMapper mapper, Gson gson, String tagName)
			throws Exception {
		// Fetch the existing file upload record
		FileUpload existingFile = fileUploadRepository.findFileBinIdByUploadId(entityId);
		if (existingFile == null) {
			throw new FileNotFoundException("No FileUpload record found for entityId: " + entityId);
		}
		// create new file location
		LocalDate		localDate		= LocalDate.now();
		Integer			year			= localDate.getYear();
		Integer			month			= localDate.getMonthValue();
		Integer			todayDate		= localDate.getDayOfMonth();
		String			fileUploadDir	= propertyMasterService.findPropertyMasterValue("file-upload-location");
		StringJoiner	location		= new StringJoiner("" + File.separatorChar);
		location.add(fileUploadDir);
		location.add("tags");
		location.add(tagName);
		location.add(year.toString()).add(month.toString()).add(todayDate.toString());
		Path newLocation = Paths.get(location.toString());
		if (Boolean.FALSE.equals(new File(location.toString()).exists()) /* && !fileUploadList.isEmpty() */) {
			Files.createDirectories(newLocation);
		}

		// Determine source and target paths
		Path sourcePath = Paths.get(existingFile.getFilePath(), existingFile.getPhysicalFileName());
		if (Files.notExists(sourcePath)) {
			throw new FileNotFoundException("Source file not found: " + sourcePath.toString());
		}

		Path targetPath = newLocation.resolve(existingFile.getPhysicalFileName());
		Files.deleteIfExists(targetPath);
		Files.copy(sourcePath, targetPath);
		// Create a new FileUploadTags record for the new location
		FileUploadTags fileUploadTag = new FileUploadTags();
		fileUploadTag.setFileUploadId(UUID.randomUUID().toString());
		fileUploadTag.setFileBinId(existingFile.getFileBinId());
		fileUploadTag.setPhysicalFileName(existingFile.getPhysicalFileName());
		fileUploadTag.setFilePath(newLocation.toString());
		fileUploadTag.setOriginalFileName(existingFile.getOriginalFileName());
		fileUploadTag.setUpdatedBy(detailsService.getUserDetails().getUserName());
		fileUploadTag.setLastUpdatedTs(new Date());
		fileUploadTagRepository.save(fileUploadTag);
		FileUploadExportVO fileUploadExportVO = new FileUploadExportVO(fileUploadTag.getFileUploadId(),
				fileUploadTag.getPhysicalFileName(), fileUploadTag.getOriginalFileName(), fileUploadTag.getFilePath(),
				fileUploadTag.getUpdatedBy(), fileUploadTag.getLastUpdatedTs(), fileUploadTag.getFileBinId(),
				fileUploadTag.getFileAssociationId());
		return convertEntityToJson(fileUploadExportVO, mapper, gson);
	}

	private String handleFileManager(String entityId, ObjectMapper mapper, Gson gson) throws Exception {
		// FileUploadConfig fileConfigObj =
		// fileUploadConfigDAO.getFileUploadConfig(entityId);
		// Object fileConfigObjVO =
		// fileUploadConfigService.convertFileUploadEntityToVO(fileConfigObj);
		// return convertEntityToJson(fileConfigObjVO, mapper, gson);
		return fileUploadConfigService.getFileUploadJson(entityId);
	}

	private String handleAPIClient(String entityId, ObjectMapper mapper, Gson gson) throws Exception {
		// JqApiClientDetails acd =
		// apiClientDetailsRepository.findById(entityId).orElse(null);
		// String jsonString = "";
		// if (acd != null) {
		// acd = acd.getObject();
		// ApiClientDetailsVO vo = acd.convertEntityToVO(acd);
		// jsonString = convertEntityToJson(vo, mapper, gson);
		// }

		return importService.getApiClientDetailseJson(entityId);
	}

	private String handleResourceBundle(String entityId, ObjectMapper mapper, Gson gson) throws Exception {
		List<ResourceBundleVO>	resourceBundleVOList	= iResourceBundleRepository.findResourceBundleByKey(entityId);
//		List<ResourceBundleVO> latestResourceBundleVOList = new ArrayList<>();
//		for (ResourceBundleVO resourceBundleVO : resourceBundleVOList) {
//			ResourceBundleVO rbvo=convertResourceBundleEntityToVO(resourceBundle);
//			latestResourceBundleVOList.add(rbvo);
//		}

		return convertEntityToJson(resourceBundleVOList, mapper, gson);

	}

	private String handleScriptLibrary(String entityId, ObjectMapper mapper, Gson
		 gson) throws Exception {
		//ScriptLibraryDetails scriptLibraryDetails = dynamicFormCrudDAO.getCurrentSession().get(ScriptLibraryDetails.class, entityId);
		ScriptLibraryDetails			scriptLibraryDetails	= jwsDynarestDAO
				.getScriptLibDetails(entityId); 
		ScriptLibraryVO vo =		 importService.convertScriptLibEntityToVO(scriptLibraryDetails);
		 return convertEntityToJson(vo, mapper, gson);
		
		 }
	// -------------------------------------------------------
	// JSON CONVERTER
	// -------------------------------------------------------
	private String convertEntityToJson(Object data, ObjectMapper mapper, Gson gson) throws Exception {
		if (data == null)
			return "{}";
		String		moduleJson		= null;
		String		dbDateFormat	= propertyMasterService.getDateFormatByName(
				com.trigyn.jws.dbutils.utils.Constant.PROPERTY_MASTER_OWNER_TYPE,
				com.trigyn.jws.dbutils.utils.Constant.PROPERTY_MASTER_OWNER_ID,
				com.trigyn.jws.dbutils.utils.Constant.JWS_DATE_FORMAT_PROPERTY_NAME,
				com.trigyn.jws.dbutils.utils.Constant.JWS_JAVA_DATE_FORMAT_PROPERTY_NAME);
		DateFormat	dateFormat		= new SimpleDateFormat(dbDateFormat);
		mapper.setDateFormat(dateFormat);
		try {
			if (data instanceof Collection<?>) {
				List<Map<String, Object>> list = mapper.convertValue(data, List.class);
				return gson.toJson(list);
			}
			Map<String, Object> map = mapper.convertValue(data, TreeMap.class);
			return gson.toJson(map);
		} catch (Exception e) {
			e.printStackTrace();
			return "{}";
		}
	}

	public boolean isTagNameExists(String tagName) {
		return tagDataRepository.existsByTagNameIgnoreCase(tagName);
	}

	public String buildPermissionJson(String entityId, String moduleId) {
		try {
			List<JwsEntityRoleAssociation>		permissionList	= jwsEntityRoleAssociationRepository
					.getEntityRoles(entityId, moduleId);
			List<JwsEntityRoleAssociationVO>	voList			= permissionList.stream().map(p -> {
																	JwsEntityRoleAssociationVO vo = importService
																			.convertJwsEntityRoleAssociationEntityToVO(
																					p);
																	return vo;
																})
					.collect(Collectors.toList());

			ObjectMapper						mapper			= new ObjectMapper();
			return mapper.writeValueAsString(voList);
		} catch (Exception e) {
			e.printStackTrace();
			return "{}";
		}
	}

}
