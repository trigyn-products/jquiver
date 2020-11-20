package com.trigyn.jws.webstarter.service;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.trigyn.jws.dashboard.dao.DashboardDaoImpl;
import com.trigyn.jws.dashboard.entities.Dashboard;
import com.trigyn.jws.dashboard.entities.Dashlet;
import com.trigyn.jws.dashboard.repository.interfaces.IDashletRepository;
import com.trigyn.jws.dashboard.service.DashletModule;
import com.trigyn.jws.dashboard.utility.Constants;
import com.trigyn.jws.dashboard.vo.DashboardVO;
import com.trigyn.jws.dashboard.vo.DashletExportVO;
import com.trigyn.jws.dashboard.vo.DashletVO;
import com.trigyn.jws.dbutils.entities.ModuleListing;
import com.trigyn.jws.dbutils.repository.IModuleListingRepository;
import com.trigyn.jws.dbutils.repository.ModuleVersionDAO;
import com.trigyn.jws.dbutils.service.ModuleService;
import com.trigyn.jws.dbutils.service.ModuleVersionService;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.vo.ModuleDetailsVO;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynamicform.dao.DynamicFormCrudDAO;
import com.trigyn.jws.dynamicform.dao.FileUploadConfigRepository;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.dynamicform.entities.FileUploadConfig;
import com.trigyn.jws.dynamicform.service.DynamicFormModule;
import com.trigyn.jws.dynamicform.service.FileUploadConfigService;
import com.trigyn.jws.dynamicform.vo.DynamicFormExportVO;
import com.trigyn.jws.dynamicform.vo.DynamicFormVO;
import com.trigyn.jws.dynamicform.vo.FileUploadConfigVO;
import com.trigyn.jws.dynarest.dao.JwsDynarestDAO;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDaoDetail;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDetail;
import com.trigyn.jws.gridutils.dao.GridUtilsDAO;
import com.trigyn.jws.gridutils.entities.GridDetails;
import com.trigyn.jws.notification.dao.NotificationDAO;
import com.trigyn.jws.notification.entities.GenericUserNotification;
import com.trigyn.jws.resourcebundle.entities.ResourceBundle;
import com.trigyn.jws.resourcebundle.repository.interfaces.IResourceBundleRepository;
import com.trigyn.jws.resourcebundle.service.ResourceBundleService;
import com.trigyn.jws.resourcebundle.utils.ResourceBundleUtils;
import com.trigyn.jws.resourcebundle.vo.ResourceBundleVO;
import com.trigyn.jws.templating.dao.TemplateDAO;
import com.trigyn.jws.templating.entities.TemplateMaster;
import com.trigyn.jws.templating.service.TemplateModule;
import com.trigyn.jws.templating.vo.TemplateExportVO;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.typeahead.dao.TypeAheadRepository;
import com.trigyn.jws.typeahead.entities.Autocomplete;
import com.trigyn.jws.typeahead.model.AutocompleteVO;
import com.trigyn.jws.typeahead.service.TypeAheadService;
import com.trigyn.jws.usermanagement.entities.JwsEntityRoleAssociation;
import com.trigyn.jws.usermanagement.repository.JwsEntityRoleAssociationRepository;
import com.trigyn.jws.usermanagement.vo.JwsEntityRoleAssociationVO;
import com.trigyn.jws.webstarter.utils.Constant;
import com.trigyn.jws.webstarter.utils.Constant.EntityNameModuleTypeEnum;
import com.trigyn.jws.webstarter.utils.FileUtil;
import com.trigyn.jws.webstarter.utils.XMLUtil;
import com.trigyn.jws.webstarter.utils.ZipUtil;
import com.trigyn.jws.webstarter.vo.ExportModule;
import com.trigyn.jws.webstarter.vo.GenericUserNotificationJsonVO;
import com.trigyn.jws.webstarter.vo.GridDetailsJsonVO;
import com.trigyn.jws.webstarter.vo.Modules;
import com.trigyn.jws.webstarter.vo.RestApiDetailsJsonVO;
import com.trigyn.jws.webstarter.xml.AutocompleteXMLVO;
import com.trigyn.jws.webstarter.xml.DashboardXMLVO;
import com.trigyn.jws.webstarter.xml.DynaRestXMLVO;
import com.trigyn.jws.webstarter.xml.FileManagerXMLVO;
import com.trigyn.jws.webstarter.xml.GenericUserNotificationXMLVO;
import com.trigyn.jws.webstarter.xml.GridXMLVO;
import com.trigyn.jws.webstarter.xml.MetadataXMLVO;
import com.trigyn.jws.webstarter.xml.PermissionXMLVO;
import com.trigyn.jws.webstarter.xml.ResourceBundleXMLVO;
import com.trigyn.jws.webstarter.xml.SiteLayoutXMLVO;

@Service
@Transactional
public class ImportService {

	@Autowired
	private ModuleVersionDAO moduleVersionDAO = null;
	
	@Autowired
	private DashboardCrudService dashboardCrudService = null;
	
	@Autowired
	private ResourceBundleService resourceBundleService = null;

	@Autowired
	private ModuleVersionService moduleVersionService				= null;

	@Autowired
	private ModuleService moduleService				= null;

	@Autowired
	private UserManagementService userManagementService				= null;
	
	@Autowired
	private TemplateModule 			templateDownloadUploadModule 		= null;

	@Autowired
	@Qualifier("dynamic-form")
	private DynamicFormModule 		dynamicFormDownloadUploadModule 	= null;

    @Autowired
	@Qualifier("dashlet")
	private DashletModule 			dashletDownloadUploadModule 		= null;

    @Autowired
	private GridUtilsDAO gridUtilsDAO		= null;

	@Autowired
	private TypeAheadRepository typeAheadRepository 				= null;

	@Autowired
	private TypeAheadService	typeAheadService = null;
	
	@Autowired
	private DashboardDaoImpl dashboardDao 											= null;

	@Autowired
	private NotificationDAO notificationDao = null;
	
	@Autowired
	private JwsDynarestDAO  jwsDynarestDAO = null;
	
	@Autowired
	private FileUploadConfigRepository fileUploadConfigRepository = null;
	
	@Autowired
	private JwsEntityRoleAssociationRepository jwsEntityRoleAssociationRepository = null;
	
	@Autowired
	private IModuleListingRepository moduleListingRepository = null;
	
	@Autowired
	private TemplateDAO templateDAO = null;

	@Autowired
	private IDashletRepository dashletRepository = null;

	@Autowired
	private DynamicFormCrudDAO dynamicFormCrudDAO = null;
	
	@Autowired
	private IResourceBundleRepository resourceBundleRepository = null;
	
	@Autowired
	private FileUploadConfigService fileUploadConfigService = null;
	
	@Autowired
	private IUserDetailsService 	detailsService 						= null;

	@Autowired
	private PropertyMasterService propertyMasterService 	= null;
	
	public Map<String, Object> importConfig(Part file) throws Exception {
		
		String unZipFilePath = FileUtil.generateTemporaryFilePath(Constant.IMPORTTEMPPATH);
		ZipUtil.unzip(file.getInputStream(), unZipFilePath);
		
		MetadataXMLVO metadataXmlvo = readMetaDataXML(unZipFilePath);
		
		Map<String, Object> map = new HashMap<>();
		map.put("metadataVO", metadataXmlvo);
		map.put("unZipFilePath", unZipFilePath);
		
		return map;
	}
	
	public String getJsonArrayFromMetadataXMLVO(MetadataXMLVO metadataXmlvo) throws Exception {
		String jsonArray = metadataXmlvo.getInfo();
		
		if(jsonArray.startsWith("<![CDATA[")) {
			jsonArray = jsonArray.substring(9, jsonArray.length()-3);
		}
		return jsonArray;
	}
	
	private MetadataXMLVO readMetaDataXML(String filePath) throws JAXBException {
		MetadataXMLVO metadataXMLVO = null;
		File directory = new File(filePath);
		if(directory.isDirectory()) {
			File[] files = directory.listFiles();
			
			for(File file : files) {
				if(file.isFile() && file.getName().equals("metadata.xml")) {
					metadataXMLVO = (MetadataXMLVO) XMLUtil.unMarshaling(MetadataXMLVO.class, file.getAbsolutePath());
				}
			}
		}
		return metadataXMLVO;
	}
	
	public Map<String, Object> getXMLJsonDataMap(MetadataXMLVO metadataXMLVO, String filePath) throws Exception {
		Map<String, Object> outputMap = new HashMap<>();
		
		File directory = new File(filePath);
		File[] files = null;
		Map<String, File> fileMap = new HashMap<>();
		if(directory.isDirectory()) {
			files = directory.listFiles();
			for(File file : files) {
				if(file.isFile() && !file.getName().equals("metadata.xml")) {
					fileMap.put(file.getName(), file);
				}
			}
		}
		
		ExportModule exportModules = metadataXMLVO.getExportModules();
		List<Modules> modules = exportModules.getModule();
		
		for(Modules module : modules) {
			String moduleName = module.getModuleName();
			String moduleType = module.getModuleType();
			
			if(Constant.XML_EXPORT_TYPE.equals(moduleType)) {
				File file = fileMap.get(moduleName.toLowerCase()+".xml");
				String fileName = file.getName();
				
				if(fileName.toLowerCase().startsWith(Constant.MasterModuleType.GRID.getModuleType().toLowerCase()))  {
					outputMap = getImportGridDetails(outputMap, file);
				} else if(fileName.toLowerCase().startsWith(Constant.MasterModuleType.AUTOCOMPLETE.getModuleType().toLowerCase()))  {
					outputMap = getImportAutocompleteDetails(outputMap, file);
				} else if(fileName.toLowerCase().startsWith(Constant.MasterModuleType.RESOURCEBUNDLE.getModuleType().toLowerCase()))  {
					outputMap = getImportRBDetails(outputMap, file);
				} else if(fileName.toLowerCase().startsWith(Constant.MasterModuleType.DASHBOARD.getModuleType().toLowerCase()))  {
					outputMap = getImportDashboardDetails(outputMap, file);
				} else if(fileName.toLowerCase().startsWith(Constant.MasterModuleType.NOTIFICATION.getModuleType().toLowerCase()))  {
					outputMap = getImportNotificationDetails(outputMap, file);
				} else if(fileName.toLowerCase().startsWith(Constant.MasterModuleType.DYNAREST.getModuleType().toLowerCase()))  {
					outputMap = getImportDynaRestDetails(outputMap, file);
				} else if(fileName.toLowerCase().startsWith(Constant.MasterModuleType.FILEMANAGER.getModuleType().toLowerCase()))  {
					outputMap = getImportFileManagerDetails(outputMap, file);
				} else if(fileName.toLowerCase().startsWith(Constant.MasterModuleType.PERMISSION.getModuleType().toLowerCase()))  {
					outputMap = getImportPermissionDetails(outputMap, file);
				} else if(fileName.toLowerCase().startsWith(Constant.MasterModuleType.SITELAYOUT.getModuleType().toLowerCase())) {
					outputMap = getImportSiteLayoutDetails(outputMap, file);
				}
			} else if(Constant.FOLDER_EXPORT_TYPE.equals(moduleType)) {
				if(Constant.MasterModuleType.TEMPLATES.getModuleType().equalsIgnoreCase(moduleName)) {
					outputMap = getImportTemplatesDetails(outputMap, filePath);
				} else if(Constant.MasterModuleType.DASHLET.getModuleType().equalsIgnoreCase(moduleName)) {
					outputMap = getImportDashletDetails(outputMap, filePath);
				} else if(Constant.MasterModuleType.DYNAMICFORM.getModuleType().equalsIgnoreCase(moduleName)) {
					outputMap = getImportDynamicFormDetails(outputMap, filePath);
				}
			}
		}
		
		return outputMap;
	}
	
	private Map<String, Object> getImportGridDetails(Map<String, Object> outputMap, File file) throws Exception {
		GridXMLVO xmlVO = (GridXMLVO) XMLUtil.unMarshaling(GridXMLVO.class, file.getAbsolutePath());

		for(GridDetails grid : xmlVO.getGridDetails()) {
			GridDetailsJsonVO vo = convertGridEntityToVO(grid);
		    updateOutputMap(outputMap, grid.getGridId(), vo, grid, Constant.MasterModuleType.GRID.getModuleType().toLowerCase());
		}
		return outputMap;
	}

	private Map<String, Object> getImportAutocompleteDetails(Map<String, Object> outputMap, File file) throws Exception {
		AutocompleteXMLVO xmlVO = (AutocompleteXMLVO) XMLUtil.unMarshaling(AutocompleteXMLVO.class, file.getAbsolutePath());

		for(Autocomplete autocomplete : xmlVO.getAutocompleteDetails()) {
			AutocompleteVO autocompleteVO = typeAheadService.convertEntityToVO(autocomplete.getAutocompleteId(),
					autocomplete.getAutocompleteDesc(), autocomplete.getAutocompleteSelectQuery());
		    updateOutputMap(outputMap, autocomplete.getAutocompleteId(), autocompleteVO, autocomplete, 
		    		Constant.MasterModuleType.AUTOCOMPLETE.getModuleType().toLowerCase());
		}
		return outputMap;
	}
		
	private Map<String, Object> getImportRBDetails(Map<String, Object> outputMap, File file) throws Exception {
		ResourceBundleXMLVO xmlVO = (ResourceBundleXMLVO) XMLUtil.unMarshaling(ResourceBundleXMLVO.class, file.getAbsolutePath());

		Map<String, List<ResourceBundleVO>> map = new TreeMap();
		Map<String, List<ResourceBundle>> entityMap = new HashMap<>();
		Gson gson = new Gson();
		
		for(ResourceBundle rb : xmlVO.getResourceBundleDetails()) {
			List<ResourceBundleVO> list = new ArrayList<>();
			if(map.containsKey(rb.getId().getResourceKey())) {
				list = map.get(rb.getId().getResourceKey());
			}
			list.add(resourceBundleService.convertResourceBundleEntityToVO(rb));
			if (rb.getId().getLanguageId().equals(com.trigyn.jws.resourcebundle.utils.Constant.DEFAULT_LANGUAGE_ID)) {
				rb.setText(rb.getText());
			}else {
				rb.setText(ResourceBundleUtils.getUnicode(rb.getText()));
			}
			map.put(rb.getId().getResourceKey(), list);
			
			List<ResourceBundle> rbList = new ArrayList<>();
			if(entityMap.containsKey(rb.getId().getResourceKey())) {
				rbList = entityMap.get(rb.getId().getResourceKey());
			}
			rbList.add(rb);
			entityMap.put(rb.getId().getResourceKey(), rbList);			
			
		    outputMap = putEntityIntoMap(rb.getId().getResourceKey(), outputMap, rbList);
		    outputMap = putModuleTypeIntoMap(rb.getId().getResourceKey(), outputMap, Constant.MasterModuleType.RESOURCEBUNDLE.getModuleType().toLowerCase());
		}

		for(java.util.Map.Entry<String, List<ResourceBundleVO>> entry : map.entrySet()) {
			String key = entry.getKey();
			List<ResourceBundleVO> rbList = entry.getValue();
		    outputMap.put(key, gson.toJson(rbList));
		}
		return outputMap;
	}

	private Map<String, Object> getImportDashboardDetails(Map<String, Object> outputMap, File file) throws Exception {
		DashboardXMLVO xmlVO = (DashboardXMLVO) XMLUtil.unMarshaling(DashboardXMLVO.class, file.getAbsolutePath());

		for(Dashboard dashboard : xmlVO.getDashboardDetails()) {
		    updateOutputMap(outputMap, dashboard.getDashboardId(), dashboardCrudService.convertDashboarEntityToVO(dashboard), dashboard, 
		    		Constant.MasterModuleType.DASHBOARD.getModuleType().toLowerCase());
		}
		return outputMap;
	}

	private Map<String, Object> getImportNotificationDetails(Map<String, Object> outputMap, File file) throws Exception {
		GenericUserNotificationXMLVO xmlVO = (GenericUserNotificationXMLVO) XMLUtil.unMarshaling(GenericUserNotificationXMLVO.class, file.getAbsolutePath());

		for(GenericUserNotification notification : xmlVO.getGenericUserNotificationDetails()) {
			GenericUserNotificationJsonVO vo = convertNotificationEntityToVO(notification);
		    updateOutputMap(outputMap, notification.getNotificationId(), vo, notification, 
		    		Constant.MasterModuleType.NOTIFICATION.getModuleType().toLowerCase());
		}
		return outputMap;
	}

	private Map<String, Object> getImportDynaRestDetails(Map<String, Object> outputMap, File file) throws Exception {
		DynaRestXMLVO xmlVO = (DynaRestXMLVO) XMLUtil.unMarshaling(DynaRestXMLVO.class, file.getAbsolutePath());

		for(JwsDynamicRestDetail dynaRest : xmlVO.getDynaRestDetails()) {
			RestApiDetailsJsonVO vo = convertDynaRestEntityToVO(dynaRest);
		    updateOutputMap(outputMap, dynaRest.getJwsDynamicRestId(), vo, dynaRest, 
		    		Constant.MasterModuleType.DYNAREST.getModuleType().toLowerCase());
		}
		return outputMap;
	}

	private Map<String, Object> getImportFileManagerDetails(Map<String, Object> outputMap, File file) throws Exception {
		FileManagerXMLVO xmlVO = (FileManagerXMLVO) XMLUtil.unMarshaling(FileManagerXMLVO.class, file.getAbsolutePath());

		for(FileUploadConfig fileConfig : xmlVO.getFileUploadDetails()) {
			FileUploadConfigVO vo = fileUploadConfigService.convertFileUploadEntityToVO(fileConfig);
		    updateOutputMap(outputMap, fileConfig.getFileUploadConfigId(), vo, fileConfig, 
		    		Constant.MasterModuleType.FILEMANAGER.getModuleType().toLowerCase());
		}
		return outputMap;
	}

	private Map<String, Object> getImportPermissionDetails(Map<String, Object> outputMap, File file) throws Exception {
		PermissionXMLVO xmlVO = (PermissionXMLVO) XMLUtil.unMarshaling(PermissionXMLVO.class, file.getAbsolutePath());

		for(JwsEntityRoleAssociation jwsRole : xmlVO.getJwsRoleDetails()) {
			JwsEntityRoleAssociationVO vo = convertJwsEntityRoleAssociationEntityToVO(jwsRole);
		    updateOutputMap(outputMap, jwsRole.getEntityRoleId(), vo, jwsRole, Constant.MasterModuleType.PERMISSION.getModuleType().toLowerCase());
		}
		return outputMap;
	}

	private Map<String, Object> getImportSiteLayoutDetails(Map<String, Object> outputMap, File file) throws Exception {
		SiteLayoutXMLVO xmlVO = (SiteLayoutXMLVO) XMLUtil.unMarshaling(SiteLayoutXMLVO.class, file.getAbsolutePath());

		for(ModuleListing siteLayout : xmlVO.getModuleListingDetails()) {
			ModuleDetailsVO vo = moduleService.convertModuleEntityToVO(siteLayout);
			updateOutputMap(outputMap, siteLayout.getModuleId(), vo, siteLayout, Constant.MasterModuleType.SITELAYOUT.getModuleType().toLowerCase());
		}
		return outputMap;
	}
	
	private Map<String, Object> getImportTemplatesDetails(Map<String, Object> outputMap, String filePath) throws Exception {
		String templateFolderPath 	= filePath + File.separator + com.trigyn.jws.templating.utils.Constant.TEMPLATE_DIRECTORY_NAME;
		MetadataXMLVO metadataXMLVO = readMetaDataXML(templateFolderPath);
		for(Modules module : metadataXMLVO.getExportModules().getModule()) {
			String moduleName 	= module.getModuleName();
			String moduleID 	= module.getModuleID();
			TemplateExportVO templateExportVO = module.getTemplate();
			
			TemplateMaster template = (TemplateMaster) templateDownloadUploadModule.importData(templateFolderPath, moduleName, moduleID, templateExportVO);
			template = template.getObject();
			TemplateVO templateVO = new TemplateVO(template.getTemplateId(),
	        		template.getTemplateName(), template.getTemplate());
			
			updateOutputMap(outputMap, moduleID, templateVO, template, Constant.MasterModuleType.TEMPLATES.getModuleType().toLowerCase());
		}
		return outputMap;
	}

	private Map<String, Object> getImportDashletDetails(Map<String, Object> outputMap, String filePath) throws Exception {
		String dashletFolderPath 	= filePath + File.separator + Constants.DASHLET_DIRECTORY_NAME;
		MetadataXMLVO metadataXMLVO = readMetaDataXML(dashletFolderPath);
		for(Modules module : metadataXMLVO.getExportModules().getModule()) {
			String moduleName 	= module.getModuleName();
			String moduleID 	= module.getModuleID();
			DashletExportVO dashletExportVO = module.getDashlet();
			
			Dashlet dashlet = (Dashlet) dashletDownloadUploadModule.importData(dashletFolderPath, moduleName, moduleID, dashletExportVO);
			dashlet = dashlet.getObject();
			DashletVO dashletVO = dashletDownloadUploadModule.convertDashletEntityToVO(dashlet);
			
			updateOutputMap(outputMap, moduleID, dashletVO, dashlet, Constant.MasterModuleType.DASHLET.getModuleType().toLowerCase());
		}
		return outputMap;
	}

	private Map<String, Object> getImportDynamicFormDetails(Map<String, Object> outputMap, String filePath) throws Exception {
		String dynamicFormFolderPath 	= filePath + File.separator + com.trigyn.jws.dynamicform.utils.Constant.DYNAMIC_FORM_DIRECTORY_NAME;
		MetadataXMLVO metadataXMLVO = readMetaDataXML(dynamicFormFolderPath);
		for(Modules module : metadataXMLVO.getExportModules().getModule()) {
			String moduleName 	= module.getModuleName();
			String moduleID 	= module.getModuleID();
			DynamicFormExportVO dynamicFormExportVO = module.getDynamicForm();
			DynamicForm dynamicForm = (DynamicForm) dynamicFormDownloadUploadModule.importData(dynamicFormFolderPath, moduleName, moduleID, dynamicFormExportVO);
			dynamicForm = dynamicForm.getObject();

			DynamicFormVO dynamicFormVO = dynamicFormDownloadUploadModule.convertEntityToVO(dynamicForm);
			updateOutputMap(outputMap, moduleID, dynamicFormVO, dynamicForm, Constant.MasterModuleType.DYNAMICFORM.getModuleType().toLowerCase());
		}
		return outputMap;
	}
	
	private Map<String, Object> updateOutputMap(Map<String, Object> outputMap, String moduleID, 
			Object jsonObj, Object entity, String moduleType) throws Exception {
		Gson gson = new Gson();
		ObjectMapper objectMapper				= new ObjectMapper();
		String dbDateFormat			= propertyMasterService.getDateFormatByName(Constant.PROPERTY_MASTER_OWNER_TYPE,
				Constant.PROPERTY_MASTER_OWNER_ID, Constant.JWS_DATE_FORMAT_PROPERTY_NAME, com.trigyn.jws.dbutils.utils.Constant.JWS_JAVA_DATE_FORMAT_PROPERTY_NAME);
		DateFormat dateFormat					= new SimpleDateFormat(dbDateFormat);
		objectMapper.setDateFormat(dateFormat);
		Map<String, Object> objectMap 			= objectMapper.convertValue(jsonObj, TreeMap.class);
		String jsonStr = gson.toJson(objectMap);
	    outputMap.put(moduleType+moduleID, jsonStr);
	    outputMap = putEntityIntoMap(moduleType+moduleID, outputMap, entity);
	    outputMap = putModuleTypeIntoMap(moduleType+moduleID, outputMap, moduleType);
	    return outputMap;
	}
	
	private Map<String, Object> putEntityIntoMap(String id, Map<String, Object> outputMap, Object obj) {

	    Map<String, String> map = new HashMap<>();
	    if(outputMap.containsKey("exportedFormatObject")) {
	    	map = (Map<String, String>) outputMap.get("exportedFormatObject");
	    }
    	Gson gson = new Gson();
    	String jsonString = gson.toJson(obj);
    	map.put(id, jsonString);
    	
    	outputMap.put("exportedFormatObject", map);
    	return outputMap;
	}

	private Map<String, Object> putModuleTypeIntoMap(String id, Map<String, Object> outputMap, String moduleType) {

	    Map<String, String> map = new HashMap<>();
	    if(outputMap.containsKey("exportedModuleTypeObject")) {
	    	map = (Map<String, String>) outputMap.get("exportedModuleTypeObject");
	    }
    	map.put(id, moduleType);
    	
    	outputMap.put("exportedModuleTypeObject", map);
    	return outputMap;
	}

	public Map<String, Boolean> getLatestCRC(Map<String, Object> inputData) throws Exception {
		Gson g = new Gson(); 
		Map<String, String> moduleTypeMap = new HashMap<>();
		for(Entry<String, Object> input : inputData.entrySet() ) {
			String key = input.getKey();
			
			if(key.equals("exportedModuleTypeObject") ) {
				moduleTypeMap = (Map<String, String>) input.getValue();
			}
		}
		
		Map<String, Boolean> crcMap = new HashMap<>();
		for(Entry<String, Object> input : inputData.entrySet() ) {
			String key = input.getKey();
			
			if(!key.equals("completeZipJsonData") && !key.equals("exportedFormatObject") && !key.equals("exportedModuleTypeObject") ) {
				String value = (String) input.getValue();
				crcMap.put(key, getLatestCRC(key, moduleTypeMap.get(key), value));
			}
		}
		return crcMap;
	}
	
	private Boolean getLatestCRC(String id, String moduleType, String importedJson) throws Exception {
		id = id.replace(moduleType, "");
	    Boolean isCheckSumUpdated = true;
	    
	    if(moduleType.equalsIgnoreCase(Constant.MasterModuleType.FILEMANAGER.getModuleType().toLowerCase()) 
	    		|| moduleType.equalsIgnoreCase(Constant.MasterModuleType.PERMISSION.getModuleType().toLowerCase()) 
	    		|| moduleType.equalsIgnoreCase(Constant.MasterModuleType.SITELAYOUT.getModuleType().toLowerCase()) ) {
	    	String existingJson = "";
	    	if(moduleType.equalsIgnoreCase(Constant.MasterModuleType.FILEMANAGER.getModuleType().toLowerCase())) {
	    		existingJson = fileUploadConfigService.getFileUploadJson(id);
	    	} else if(moduleType.equalsIgnoreCase(Constant.MasterModuleType.PERMISSION.getModuleType().toLowerCase())) {
	    		existingJson = userManagementService.getJwsEntityRoleAssociationJson(id);
	    	} else if(moduleType.equalsIgnoreCase(Constant.MasterModuleType.SITELAYOUT.getModuleType().toLowerCase())) {
	    		existingJson = moduleService.getModuleListingJson(id);
	    	}
	    	
	    	isCheckSumUpdated = checkSumComparisonForNonVersioningModules(existingJson, importedJson);
	    } else {
			String entityName = EntityNameModuleTypeEnum.valueOf(moduleType.toUpperCase()).geTableName();		
	    	isCheckSumUpdated = checkSumComparison(id, entityName, importedJson);
	    }
    	
    	return isCheckSumUpdated;
	}

	private Boolean checkSumComparison(String entityTypeId, String entityName, String importedJson) throws Exception {
		String moduleJsonChecksum = moduleVersionService.generateJsonChecksum(importedJson);
		Boolean isDataUpdated = moduleVersionService.compareChecksum(entityTypeId, entityName, moduleJsonChecksum);
		return isDataUpdated;
	}

	private Boolean checkSumComparisonForNonVersioningModules(String existingJson, String importedJson) throws Exception {
		String existingJsonChecksum = null;
		if(existingJson != null) {
			existingJsonChecksum = moduleVersionService.generateJsonChecksum(existingJson);
		}
		String moduleJsonChecksum = null;
		if(importedJson != null) {
			moduleJsonChecksum = moduleVersionService.generateJsonChecksum(importedJson);
		}		
		Boolean isChecksumChanged = true;
		if(existingJsonChecksum != null && existingJsonChecksum.equals(moduleJsonChecksum)) {
			isChecksumChanged = false;
		}
		return isChecksumChanged;
	}

	public Map<String, String> getLatestVersion(Map<String, Object> inputData) throws Exception {
		Map<String, String> versionMap = new HashMap<>();
		for(Entry<String, Object> input : inputData.entrySet() ) {
			String key = input.getKey();
			
			if(key.equals("completeZipJsonData") ) {
				String jsonArrayString = (String) input.getValue();
				versionMap = getLatestVersion(jsonArrayString);
			}
		}
		return versionMap;
	}
	
	private Map<String, String> getLatestVersion(String jsonArrayString) throws Exception {
		Map<String, String> versionMap = new HashMap<>();
		JSONArray jsonArray = new JSONArray(jsonArrayString); 
		
		for (int i = 0; i < jsonArray.length(); i++) {
		    JSONObject explrObject = jsonArray.getJSONObject(i);
		    String entityID = explrObject.getString("moduleID");
		    String moduleType = explrObject.getString("moduleType");
		    
		    String version = "NA";

		    if(moduleType.equalsIgnoreCase(Constant.MasterModuleType.FILEMANAGER.getModuleType().toLowerCase()) 
		    		|| moduleType.equalsIgnoreCase(Constant.MasterModuleType.PERMISSION.getModuleType().toLowerCase()) 
		    		|| moduleType.equalsIgnoreCase(Constant.MasterModuleType.SITELAYOUT.getModuleType().toLowerCase()) ) {
		    	if(moduleType.equalsIgnoreCase(Constant.MasterModuleType.FILEMANAGER.getModuleType().toLowerCase())) {
		    		FileUploadConfig fileUploadConfig = fileUploadConfigService.getFileUploadConfigById(entityID);
		    		if(fileUploadConfig == null) {
		    			version = "NE";
		    		}
		    	} else if(moduleType.equalsIgnoreCase(Constant.MasterModuleType.PERMISSION.getModuleType().toLowerCase())) {
		    		JwsEntityRoleAssociation role = userManagementService.findByEntityRoleID(entityID);
		    		if(role == null) {
		    			version = "NE";
		    		}
		    	} else if(moduleType.equalsIgnoreCase(Constant.MasterModuleType.SITELAYOUT.getModuleType().toLowerCase())) {
		    		ModuleListing module = moduleService.getModuleListing(entityID);
		    		if(module == null) {
		    			version = "NE";
		    		}
		    	}
		    } else {
				String entityName = EntityNameModuleTypeEnum.valueOf(moduleType.toUpperCase()).geTableName();		
		    	version = String.valueOf(moduleVersionDAO.getVersionIdByEntityIdAndName(entityID, entityName));
			    if(version == null || "".equals(version) || "null".equals(version)) {
			    	version = "NA";
			    }

		    }
	    	
		    explrObject.put("existingVersion", version);
		    versionMap.put(moduleType.toLowerCase()+entityID, version);
		}
		
		return versionMap;
	}

	public String importConfig(String imporatableData, String importId, String moduleType) throws Exception {
		Gson g = new Gson(); 
		JSONObject imporatableDataJson = new JSONObject(imporatableData);

		JSONObject exportedFormatObject = (JSONObject) imporatableDataJson.get("exportedFormatObject");
		Map<String, String> entityStringMap = g.fromJson(exportedFormatObject.toString(), Map.class);
		String entityString = entityStringMap.get(moduleType.toLowerCase() + importId);
		
		saveEntity(moduleType, entityString);
		importId = importId.replace(moduleType, "");
	    
		String version = String.valueOf(moduleVersionDAO.getVersionIdByEntityId(importId));
		if (version == null || "".equals(version) || "null".equals(version)) {
			version = "NA";
		}
		return version;
	}

	public void importAll(String imporatableData, String importedIdJsonArray) throws Exception {
		Gson g = new Gson(); 
		JSONObject imporatableDataJson = new JSONObject(imporatableData);

		JSONObject exportedModuleTypeObject = (JSONObject) imporatableDataJson.get("exportedModuleTypeObject");
		Map<String, String> moduleTypeMap = g.fromJson(exportedModuleTypeObject.toString(), Map.class);
		
		JSONObject exportedFormatObject = (JSONObject) imporatableDataJson.get("exportedFormatObject");
		Map<String, String> entityStringMap = g.fromJson(exportedFormatObject.toString(), Map.class);
		
		List<String> importedIdList = g.fromJson(importedIdJsonArray, List.class);
		
		for(Entry<String, String> entry : entityStringMap.entrySet()) {
			String importId = entry.getKey();
			String entityString = entry.getValue();
			String moduleType = moduleTypeMap.get(importId);
			
			if(importedIdList == null || (importedIdList != null && !importedIdList.contains(importId))) {
				saveEntity(moduleType, entityString);
			}
		}
	}
	
	private void saveEntity(String moduleType, String entityString) throws Exception {

        UserDetailsVO detailsVO = detailsService.getUserDetails(); 
        Date date = new Date();
        String user = detailsVO.getUserName();
		
		Gson g = new Gson(); 
		ObjectMapper objectMapper				= new ObjectMapper();
		if(Constant.MasterModuleType.GRID.getModuleType().equalsIgnoreCase(moduleType)) {
			GridDetails gridDetails = g.fromJson(entityString, GridDetails.class);
			gridUtilsDAO.saveGridDetails(gridDetails);
			GridDetailsJsonVO vo = convertGridEntityToVO(gridDetails);
			moduleVersionService.saveModuleVersion(vo,null, gridDetails.getGridId(), "grid_details", Constant.IMPORT_SOURCE_VERSION_TYPE);
		} else if(Constant.MasterModuleType.AUTOCOMPLETE.getModuleType().equalsIgnoreCase(moduleType)) {
			Autocomplete autocomplete = g.fromJson(entityString, Autocomplete.class);
			AutocompleteVO autocompleteVO = typeAheadService.convertEntityToVO(autocomplete.getAutocompleteId(),
					autocomplete.getAutocompleteDesc(), autocomplete.getAutocompleteSelectQuery());
			typeAheadRepository.save(autocomplete);
			moduleVersionService.saveModuleVersion(autocompleteVO,null, autocomplete.getAutocompleteId(), "autocomplete_details", Constant.IMPORT_SOURCE_VERSION_TYPE);
		} else if(Constant.MasterModuleType.RESOURCEBUNDLE.getModuleType().equalsIgnoreCase(moduleType)) {
			TypeReference<List<ResourceBundle>> resourceBundleType  = new TypeReference<List<ResourceBundle>>() {};
			List<ResourceBundle> resourceBundleList = objectMapper.readValue(entityString, resourceBundleType);
			
			resourceBundleRepository.saveAll(resourceBundleList);
			
			List<ResourceBundleVO> resourceBundleVOList = new ArrayList<>();
			for(ResourceBundle entity : resourceBundleList) {
				ResourceBundleVO vo = resourceBundleService.convertResourceBundleEntityToVO(entity);
				resourceBundleVOList.add(vo);
			}
			String resourceBundleKey = resourceBundleVOList.get(0).getResourceKey();
			moduleVersionService.saveModuleVersion(resourceBundleVOList,null, resourceBundleKey, "resource_bundle", Constant.IMPORT_SOURCE_VERSION_TYPE);
		} else if(Constant.MasterModuleType.DASHBOARD.getModuleType().equalsIgnoreCase(moduleType)) {
			Dashboard dashboard = g.fromJson(entityString, Dashboard.class);
			
			Long existingObj = dashboardDao.getDashboardCount(dashboard.getDashboardId());
			if(existingObj == null || (existingObj != null && existingObj == 0)) {
				dashboard.setCreatedBy(user);
				dashboard.setCreatedDate(date);
			}
			dashboardDao.saveDashboardDetails(dashboard);
			DashboardVO dashboardVO = dashboardCrudService.convertDashboarEntityToVO(dashboard);
			moduleVersionService.saveModuleVersion(dashboardVO,null, dashboard.getDashboardId(), "dashboard", Constant.IMPORT_SOURCE_VERSION_TYPE);
		} else if(Constant.MasterModuleType.NOTIFICATION.getModuleType().equalsIgnoreCase(moduleType)) {
			GenericUserNotification notification = g.fromJson(entityString, GenericUserNotification.class);

			Long existingObj = notificationDao.getNotificationDetailsCount(notification.getNotificationId());
			if(existingObj == null || (existingObj != null && existingObj == 0)) {
				notification.setCreatedBy(user);
				notification.setCreationDate(date);
			}
			notification.setUpdatedBy(user);
			notification.setUpdatedDate(date);
			
			notificationDao.saveEditedNotification(notification);
			GenericUserNotificationJsonVO vo = convertNotificationEntityToVO(notification);
			moduleVersionService.saveModuleVersion(vo,null, notification.getNotificationId(), "generic_user_notification", Constant.IMPORT_SOURCE_VERSION_TYPE);
		} else if(Constant.MasterModuleType.DYNAREST.getModuleType().equalsIgnoreCase(moduleType)) {
			JwsDynamicRestDetail dynarest = g.fromJson(entityString, JwsDynamicRestDetail.class);
			jwsDynarestDAO.saveJwsDynamicRestDetail(dynarest);
			RestApiDetailsJsonVO vo = convertDynaRestEntityToVO(dynarest);
			moduleVersionService.saveModuleVersion(vo,null, dynarest.getJwsDynamicRestId(), "jws_dynamic_rest_details", Constant.IMPORT_SOURCE_VERSION_TYPE);
		} else if(Constant.MasterModuleType.FILEMANAGER.getModuleType().equalsIgnoreCase(moduleType)) {
			FileUploadConfig fileConfig = g.fromJson(entityString, FileUploadConfig.class);
			fileConfig.setUpdatedBy(user);
			fileConfig.setLastUpdatedBy(date);
			
			fileUploadConfigRepository.save(fileConfig);
		} else if(Constant.MasterModuleType.PERMISSION.getModuleType().equalsIgnoreCase(moduleType)) {
			JwsEntityRoleAssociation role = g.fromJson(entityString, JwsEntityRoleAssociation.class);
			role.setLastUpdatedBy(user);
			role.setLastUpdatedDate(date);
			
			jwsEntityRoleAssociationRepository.save(role);
		} else if(Constant.MasterModuleType.SITELAYOUT.getModuleType().equalsIgnoreCase(moduleType)) {
			ModuleListing module = g.fromJson(entityString, ModuleListing.class);
			moduleListingRepository.save(module);
		} else if(Constant.MasterModuleType.TEMPLATES.getModuleType().equalsIgnoreCase(moduleType)) {
			TemplateMaster template = g.fromJson(entityString, TemplateMaster.class);

			Long existingObj = templateDAO.getTemplateCount(template.getTemplateId());
			if(existingObj == null || (existingObj != null && existingObj == 0)) {
				template.setCreatedBy(user);
			}
			template.setUpdatedBy(user);
			template.setUpdatedDate(date);
			templateDAO.saveVelocityTemplateData(template);
			TemplateVO templateVO = new TemplateVO(template.getTemplateId(), template.getTemplateName(), template.getTemplate());
			moduleVersionService.saveModuleVersion(templateVO,null, template.getTemplateId(), "template_master", Constant.IMPORT_SOURCE_VERSION_TYPE);
		} else if(Constant.MasterModuleType.DASHLET.getModuleType().equalsIgnoreCase(moduleType)) {
			Dashlet dashlet = g.fromJson(entityString, Dashlet.class);

			Long existingObj = dashboardDao.getDashletsCount(dashlet.getDashletId());
			if(existingObj == null || (existingObj != null && existingObj == 0)) {
				dashlet.setCreatedBy(user);
				dashlet.setCreatedDate(date);
			}
			dashlet.setUpdatedBy(user);
			dashlet.setUpdatedDate(date);
			dashletRepository.saveAndFlush(dashlet);
			DashletVO  dashletVO = dashletDownloadUploadModule.convertDashletEntityToVO(dashlet);
			moduleVersionService.saveModuleVersion(dashletVO,null, dashlet.getDashletId(), "dashlet", Constant.IMPORT_SOURCE_VERSION_TYPE);
		} else if(Constant.MasterModuleType.DYNAMICFORM.getModuleType().equalsIgnoreCase(moduleType)) {
			DynamicForm dynamicForm = g.fromJson(entityString, DynamicForm.class);

			Long existingObj = dynamicFormCrudDAO.getDynamicFormCount(dynamicForm.getFormId());
			if(existingObj == null || (existingObj != null && existingObj == 0)) {
				dynamicForm.setCreatedBy(user);
				dynamicForm.setCreatedDate(date);
			}
			dynamicFormCrudDAO.saveDynamicFormData(dynamicForm);

			DynamicFormVO dynamicFormVO = dynamicFormDownloadUploadModule.convertEntityToVO(dynamicForm);
	        moduleVersionService.saveModuleVersion(dynamicFormVO,null, dynamicForm.getFormId(), "dynamic_form", Constant.IMPORT_SOURCE_VERSION_TYPE);
		}
	}

	private JwsEntityRoleAssociationVO convertJwsEntityRoleAssociationEntityToVO(JwsEntityRoleAssociation entityRoleAssociation) {
		JwsEntityRoleAssociationVO vo = new JwsEntityRoleAssociationVO();
		return vo.convertEntityToVO(entityRoleAssociation);
	}

	private GridDetailsJsonVO convertGridEntityToVO(GridDetails grid) {
		GridDetailsJsonVO vo = new GridDetailsJsonVO();
		vo.setEntityName("grid_details");
		vo.setFormId("8a80cb8174bebc3c0174bec1892c0000");
		vo.setGridColumnName(grid.getGridColumnName());
		vo.setGridDescription(grid.getGridDescription());
		vo.setGridId(grid.getGridId());
		vo.setGridName(grid.getGridName());		
		vo.setGridTableName(grid.getGridTableName());
		vo.setPrimaryKey(grid.getGridId());
		vo.setQueryType(grid.getQueryType() != null ? grid.getQueryType().toString() : "");
		
		return vo;
	}

	private GenericUserNotificationJsonVO convertNotificationEntityToVO(GenericUserNotification notitification) {
		GenericUserNotificationJsonVO vo = new GenericUserNotificationJsonVO();
		
		vo.setEntityName("generic_user_notification");
		vo.setFormId("e848b04c-f19b-11ea-9304-f48e38ab9348");
		vo.setFromDate(notitification.getMessageValidFrom());
		vo.setMessageFormat(notitification.getMessageFormat());
		vo.setMessageText(notitification.getMessageText());
		vo.setMessageType(notitification.getMessageType());
		vo.setNotificationId(notitification.getNotificationId());
		vo.setPrimaryKey(notitification.getNotificationId());
		vo.setSelectionCriteria(notitification.getSelectionCriteria());
		vo.setTargetPlatform(notitification.getTargetPlatform());
		vo.setToDate(notitification.getMessageValidTill());
		
		return vo;
	}

	private RestApiDetailsJsonVO convertDynaRestEntityToVO(JwsDynamicRestDetail dynaRest) {
		RestApiDetailsJsonVO vo = new RestApiDetailsJsonVO();
		
		vo.setAllowFiles(dynaRest.getJwsAllowFiles() != null ? dynaRest.getJwsAllowFiles().toString() : "");
		
		JSONArray daoDetailsId = new JSONArray();
		JSONArray queryDetails = new JSONArray();
		JSONArray variableNameDetails = new JSONArray();
		JSONArray queryTypes = new JSONArray();
		if(dynaRest.getJwsDynamicRestDaoDetails() != null)
			for(JwsDynamicRestDaoDetail dao : dynaRest.getJwsDynamicRestDaoDetails()) {
				daoDetailsId.put(dao.getJwsDaoDetailsId().toString());
				queryDetails.put(dao.getJwsDaoQueryTemplate());
				variableNameDetails.put(dao.getJwsResultVariableName());
				queryTypes.put(dao.getQueryType().toString());
			}
		vo.setDaoDetailsIds(daoDetailsId.toString());
		vo.setDaoQueryDetails(queryDetails.toString());
		vo.setVariableName(variableNameDetails.toString());
		vo.setQueryType(queryTypes.toString());
		
		vo.setSaveUpdateQuery("");
		
		vo.setDynarestId(dynaRest.getJwsDynamicRestId());
		vo.setDynarestMethodDescription(dynaRest.getJwsMethodDescription());
		vo.setDynarestMethodName(dynaRest.getJwsMethodName());
		vo.setDynarestPlatformId(dynaRest.getJwsPlatformId() != null ? dynaRest.getJwsPlatformId().toString():"");
		vo.setDynarestProdTypeId(dynaRest.getJwsResponseProducerTypeId() != null ? dynaRest.getJwsResponseProducerTypeId().toString() : "");
		vo.setDynarestRequestTypeId(dynaRest.getJwsRequestTypeId() != null ? dynaRest.getJwsRequestTypeId().toString(): "");
		vo.setDynarestUrl(dynaRest.getJwsDynamicRestUrl());
		vo.setEntityName("jws_dynamic_rest_details");
		vo.setFormId("8a80cb81749ab40401749ac2e7360000");
		vo.setIsEdit(dynaRest.getJwsRbacId()!= null ?dynaRest.getJwsRbacId().toString():"");
		vo.setPrimaryKey(dynaRest.getJwsDynamicRestId());
		vo.setServiceLogic(dynaRest.getJwsServiceLogic());
		
		return vo;
	}
}
