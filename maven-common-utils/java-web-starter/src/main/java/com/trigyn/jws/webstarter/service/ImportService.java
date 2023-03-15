package com.trigyn.jws.webstarter.service;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;
import java.util.TreeMap;

import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trigyn.jws.dashboard.entities.Dashboard;
import com.trigyn.jws.dashboard.service.DashletModule;
import com.trigyn.jws.dashboard.utility.Constants;
import com.trigyn.jws.dbutils.entities.AdditionalDatasource;
import com.trigyn.jws.dbutils.entities.AdditionalDatasourceRepository;
import com.trigyn.jws.dbutils.entities.ModuleListing;
import com.trigyn.jws.dbutils.entities.ModuleListingI18n;
import com.trigyn.jws.dbutils.entities.PropertyMaster;
import com.trigyn.jws.dbutils.repository.AdditionalDatasourceDAO;
import com.trigyn.jws.dbutils.repository.IModuleListingI18nRepository;
import com.trigyn.jws.dbutils.repository.IModuleListingRepository;
import com.trigyn.jws.dbutils.repository.ModuleDAO;
import com.trigyn.jws.dbutils.repository.ModuleVersionDAO;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.repository.PropertyMasterRepository;
import com.trigyn.jws.dbutils.service.ModuleService;
import com.trigyn.jws.dbutils.service.ModuleVersionService;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.vo.AdditionalDatasourceVO;
import com.trigyn.jws.dbutils.vo.ModuleDetailsVO;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dbutils.vo.xml.DynaRestExportVO;
import com.trigyn.jws.dbutils.vo.xml.DynamicFormExportVO;
import com.trigyn.jws.dbutils.vo.xml.ExportModule;
import com.trigyn.jws.dbutils.vo.xml.FileUploadConfigExportVO;
import com.trigyn.jws.dbutils.vo.xml.HelpManualTypeExportVO;
import com.trigyn.jws.dbutils.vo.xml.MetadataXMLVO;
import com.trigyn.jws.dbutils.vo.xml.Modules;
import com.trigyn.jws.dbutils.vo.xml.TemplateExportVO;
import com.trigyn.jws.dynamicform.dao.DynamicFormCrudDAO;
import com.trigyn.jws.dynamicform.dao.HelpManualDAO;
import com.trigyn.jws.dynamicform.dao.IDynamicFormRepository;
import com.trigyn.jws.dynamicform.dao.IManualEntryDetailsRepository;
import com.trigyn.jws.dynamicform.dao.IManualTypeRepository;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.dynamicform.entities.DynamicFormSaveQuery;
import com.trigyn.jws.dynamicform.entities.ManualEntryDetails;
import com.trigyn.jws.dynamicform.entities.ManualType;
import com.trigyn.jws.dynamicform.service.DynamicFormModule;
import com.trigyn.jws.dynamicform.vo.DynamicFormVO;
import com.trigyn.jws.dynarest.dao.FileUploadConfigDAO;
import com.trigyn.jws.dynarest.dao.FileUploadConfigRepository;
import com.trigyn.jws.dynarest.dao.JwsDynarestDAO;
import com.trigyn.jws.dynarest.entities.ApiClientDetails;
import com.trigyn.jws.dynarest.entities.FileUpload;
import com.trigyn.jws.dynarest.entities.FileUploadConfig;
import com.trigyn.jws.dynarest.entities.JqScheduler;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDaoDetail;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDetail;
import com.trigyn.jws.dynarest.repository.ApiClientDetailsDAO;
import com.trigyn.jws.dynarest.repository.FileUploadRepository;
import com.trigyn.jws.dynarest.repository.IApiClientDetailsRepository;
import com.trigyn.jws.dynarest.repository.IFileUploadConfigRepository;
import com.trigyn.jws.dynarest.repository.JqschedulerRepository;
import com.trigyn.jws.dynarest.repository.SchedulerDAO;
import com.trigyn.jws.dynarest.service.DynaRestModule;
import com.trigyn.jws.dynarest.service.FileUploadConfigService;
import com.trigyn.jws.dynarest.vo.ApiClientDetailsVO;
import com.trigyn.jws.dynarest.vo.SchedulerVO;
import com.trigyn.jws.gridutils.dao.GridDetailsDAO;
import com.trigyn.jws.gridutils.dao.GridDetailsRepository;
import com.trigyn.jws.gridutils.entities.GridDetails;
import com.trigyn.jws.resourcebundle.dao.ResourceBundleDAO;
import com.trigyn.jws.resourcebundle.entities.ResourceBundle;
import com.trigyn.jws.resourcebundle.repository.interfaces.IResourceBundleRepository;
import com.trigyn.jws.resourcebundle.service.ResourceBundleService;
import com.trigyn.jws.resourcebundle.utils.ResourceBundleUtils;
import com.trigyn.jws.resourcebundle.vo.ResourceBundleVO;
import com.trigyn.jws.templating.dao.DBTemplatingRepository;
import com.trigyn.jws.templating.dao.TemplateDAO;
import com.trigyn.jws.templating.entities.TemplateMaster;
import com.trigyn.jws.templating.service.TemplateModule;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.typeahead.dao.TypeAheadDAO;
import com.trigyn.jws.typeahead.dao.TypeAheadRepository;
import com.trigyn.jws.typeahead.entities.Autocomplete;
import com.trigyn.jws.typeahead.model.AutocompleteVO;
import com.trigyn.jws.typeahead.service.TypeAheadService;
import com.trigyn.jws.usermanagement.entities.JwsEntityRoleAssociation;
import com.trigyn.jws.usermanagement.entities.JwsRole;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.repository.JwsEntityRoleAssociationDAO;
import com.trigyn.jws.usermanagement.repository.JwsEntityRoleAssociationRepository;
import com.trigyn.jws.usermanagement.repository.JwsRoleRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.UserManagementDAO;
import com.trigyn.jws.usermanagement.vo.JwsEntityRoleAssociationVO;
import com.trigyn.jws.usermanagement.vo.JwsRoleVO;
import com.trigyn.jws.usermanagement.vo.JwsUserVO;
import com.trigyn.jws.webstarter.dao.INotificationRepository;
import com.trigyn.jws.webstarter.dao.NotificationDAO;
import com.trigyn.jws.webstarter.utils.Constant;
import com.trigyn.jws.webstarter.utils.Constant.EntityNameModuleTypeEnum;
import com.trigyn.jws.webstarter.utils.FileUploadExportModule;
import com.trigyn.jws.webstarter.utils.FileUtil;
import com.trigyn.jws.webstarter.utils.HelpManualImportExportModule;
import com.trigyn.jws.webstarter.utils.XMLUtil;
import com.trigyn.jws.webstarter.utils.ZipUtil;
import com.trigyn.jws.webstarter.vo.FileUploadConfigImportEntity;
import com.trigyn.jws.webstarter.vo.GenericUserNotification;
import com.trigyn.jws.webstarter.vo.GenericUserNotificationJsonVO;
import com.trigyn.jws.webstarter.vo.GridDetailsJsonVO;
import com.trigyn.jws.webstarter.vo.HelpManual;
import com.trigyn.jws.webstarter.vo.PropertyMasterJsonVO;
import com.trigyn.jws.webstarter.vo.RestApiDetailsJsonVO;
import com.trigyn.jws.webstarter.xml.AdditionalDatasourceXMLVO;
import com.trigyn.jws.webstarter.xml.ApiClientDetailsXMLVO;
import com.trigyn.jws.webstarter.xml.AutocompleteXMLVO;
import com.trigyn.jws.webstarter.xml.DashboardXMLVO;
import com.trigyn.jws.webstarter.xml.GenericUserNotificationXMLVO;
import com.trigyn.jws.webstarter.xml.GridXMLVO;
import com.trigyn.jws.webstarter.xml.PermissionXMLVO;
import com.trigyn.jws.webstarter.xml.PropertyMasterXMLVO;
import com.trigyn.jws.webstarter.xml.ResourceBundleXMLVO;
import com.trigyn.jws.webstarter.xml.RoleXMLVO;
import com.trigyn.jws.webstarter.xml.SchedulerXMLVO;
import com.trigyn.jws.webstarter.xml.SiteLayoutXMLVO;
import com.trigyn.jws.webstarter.xml.UserXMLVO;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class ImportService {

	private final static Logger						logger								= LogManager
			.getLogger(ExportService.class);

	@Autowired
	private ModuleVersionDAO						moduleVersionDAO					= null;

	@Autowired
	private DashboardCrudService					dashboardCrudService				= null;

	@Autowired
	private ResourceBundleService					resourceBundleService				= null;

	@Autowired
	private ModuleVersionService					moduleVersionService				= null;

	@Autowired
	private ModuleService							moduleService						= null;

	@Autowired
	private UserManagementService					userManagementService				= null;

	@Autowired
	private TemplateModule							templateDownloadUploadModule		= null;

	@Autowired
	@Qualifier("dynamic-form")
	private DynamicFormModule						dynamicFormDownloadUploadModule		= null;

	@Autowired
	@Qualifier("dashlet")
	private DashletModule							dashletDownloadUploadModule			= null;

	@Autowired
	private GridDetailsRepository					gridDetailsRepository				= null;

	@Autowired
	private GridDetailsDAO							gridDetailsDAO						= null;

	@Autowired
	private TypeAheadRepository						typeAheadRepository					= null;

	@Autowired
	private TypeAheadService						typeAheadService					= null;

	@Autowired
	private TypeAheadDAO							typeAheadDAO						= null;

//	@Autowired
//	private DashboardDaoImpl						dashboardDao						= null;
//
//	@Autowired
//	private DashboardCrudDAO						dashboardCrudDAO					= null;
//
//	@Autowired
//	private IDashboardRepository					dashboardRepository					= null;
//
//	@Autowired
//	private IDashletRepository						dashletRepository					= null;
//
//	@Autowired
//	private IDashboardRoleAssociationRepository		iDashboardRoleRepository			= null;
//
//	@Autowired
//	private IDashboardDashletAssociationRepository	iDashboardDashletRepository			= null;

	@Autowired
	private NotificationDAO							notificationDao						= null;

	@Autowired
	private INotificationRepository					notificationRepository				= null;

	@Autowired
	private JwsDynarestDAO							jwsDynarestDAO						= null;

	@Autowired
	private FileUploadConfigRepository				fileUploadConfigRepository			= null;

	@Autowired
	private JwsEntityRoleAssociationRepository		jwsEntityRoleAssociationRepository	= null;

	@Autowired
	private FileUploadConfigDAO						fileUploadConfigDAO					= null;

	@Autowired
	private JwsEntityRoleAssociationDAO				jwsEntityRoleAssociationDAO			= null;

	@Autowired
	private IModuleListingRepository				moduleListingRepository				= null;

	@Autowired
	private IModuleListingI18nRepository			iModuleListingI18nRepository		= null;

	@Autowired
	private ModuleDAO								moduleDAO							= null;

	@Autowired
	private DynamicFormCrudDAO						dynamicFormCrudDAO					= null;

	@Autowired
	private IDynamicFormRepository					dynamicFormRepository				= null;

	@Autowired
	private IResourceBundleRepository				resourceBundleRepository			= null;

	@Autowired
	private ResourceBundleDAO						resourceBundleDAO					= null;

	@Autowired
	private FileUploadConfigService					fileUploadConfigService				= null;

	@Autowired
	private IUserDetailsService						detailsService						= null;

	@Autowired
	private PropertyMasterService					propertyMasterService				= null;

	@Autowired
	private TemplateDAO								templateDAO							= null;

	@Autowired
	private DBTemplatingRepository					dbTemplatingRepository				= null;

	@Autowired
	private PropertyMasterRepository				propertyMasterRepository			= null;

	@Autowired
	private JwsUserRepository						jwsUserRepository					= null;

	@Autowired
	private UserManagementDAO						userManagementDAO					= null;
	@Autowired
	private JwsRoleRepository						jwsRoleRepository					= null;

	@Autowired
	private HelpManualImportExportModule			helpManualImportExportModule		= null;

	@Autowired
	private IManualTypeRepository					iManualTypeRepository				= null;

	@Autowired
	private HelpManualDAO							helpManualDAO						= null;

	@Autowired
	private IManualEntryDetailsRepository			iManualEntryDetailsRepository		= null;

	@Autowired
	private FileUploadRepository					ifileUploadRepository				= null;

	@Autowired
	private IFileUploadConfigRepository				iFileUploadConfigRepository			= null;

	@Autowired
	private FileUploadExportModule					fileUploadExportModule				= null;

	@Autowired
	private AdditionalDatasourceRepository			additionalDatasourceRepository		= null;

	@Autowired
	private AdditionalDatasourceDAO					additionalDatasourceDAO				= null;

	@Autowired
	private IApiClientDetailsRepository				apiClientDetailsRepository			= null;

	@Autowired
	private ApiClientDetailsDAO						apiClientDetailsDAO					= null;

	private String									unZipFilePath;

	@Autowired
	private PropertyMasterDAO						propertyMasterDAO					= null;

	@Autowired
	private DynaRestModule							dynaRestModule						= null;

	@Autowired
	private JqschedulerRepository					schedulerRepository					= null;

	@Autowired
	private SchedulerDAO							schedulerDAO						= null;

	public Map<String, Object> importConfig(Part file, boolean isImportfromLocal) throws Exception {

		if (isImportfromLocal == false) {
			unZipFilePath = FileUtil.generateTemporaryFilePath(Constant.IMPORTTEMPPATH, null);
			unZipFilePath = ZipUtil.unzip(file.getInputStream(), unZipFilePath);
		} else {
			unZipFilePath = propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
		}

		MetadataXMLVO		metadataXmlvo	= readMetaDataXML(unZipFilePath);

		Map<String, Object>	map				= new HashMap<>();
		map.put("metadataVO", metadataXmlvo);
		map.put("unZipFilePath", unZipFilePath);

		return map;
	}

	public Map<String, Object> importFile(File file, boolean isDevMode) throws Exception {

		if (isDevMode == false) {
			unZipFilePath	= FileUtil.generateTemporaryFilePath(Constant.IMPORTTEMPPATH, null);
			unZipFilePath	= ZipUtil.unzip(new FileInputStream(file), unZipFilePath);
			unZipFilePath	= unZipFilePath + "webui";
		} else {
			unZipFilePath = propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
			// unZipFilePath = "C:\\Users\\mini.pillai\\Downloads\\webui_all";
		}

		MetadataXMLVO		metadataXmlvo	= readMetaDataXML(unZipFilePath);

		Map<String, Object>	map				= new HashMap<>();
		map.put("metadataVO", metadataXmlvo);
		map.put("unZipFilePath", unZipFilePath);

		return map;
	}

	public String getJsonArrayFromMetadataXMLVO(MetadataXMLVO metadataXmlvo) throws Exception {
		String jsonArray = metadataXmlvo.getInfo();

		if (jsonArray.startsWith("<![CDATA[")) {
			jsonArray = jsonArray.substring(9, jsonArray.length() - 3);
		}
		return jsonArray;
	}

	private MetadataXMLVO readMetaDataXML(String filePath) throws JAXBException {
		MetadataXMLVO	metadataXMLVO	= null;
		File			directory		= new File(filePath);
		if (directory.isDirectory()) {
			File[] files = directory.listFiles();

			for (File file : files) {
				if (file.isFile() && file.getName().equals("metadata.xml")) {
					metadataXMLVO = (MetadataXMLVO) XMLUtil.unMarshaling(MetadataXMLVO.class, file.getAbsolutePath());
					break;
				}
			}
		}
		return metadataXMLVO;
	}

	public Map<String, Object> getXMLJsonDataMap(MetadataXMLVO metadataXMLVO, String filePath) throws Exception {
		Map<String, Object>	outputMap	= new HashMap<>();

		File				directory	= new File(filePath);
		File[]				files		= null;
		Map<String, File>	fileMap		= new HashMap<>();
		if (directory.isDirectory()) {
			files = directory.listFiles();
			for (File file : files) {
				if (file.isFile() && !file.getName().equals("metadata.xml")) {
					fileMap.put(file.getName(), file);
				}
			}
		}

		ExportModule	exportModules	= metadataXMLVO.getExportModules();
		List<Modules>	modules			= exportModules.getModule();

		for (Modules module : modules) {
			String	moduleName	= module.getModuleName();
			String	moduleType	= module.getModuleType();

			if (Constant.XML_EXPORT_TYPE.equals(moduleType)) {
				File	file		= fileMap.get(moduleName.toLowerCase() + ".xml");
				String	fileName	= file.getName();

				if (fileName.toLowerCase().startsWith(Constant.MasterModuleType.GRID.getModuleType().toLowerCase())) {
					outputMap = getImportGridDetails(outputMap, file);
				} else if (fileName.toLowerCase()
						.startsWith(Constant.MasterModuleType.AUTOCOMPLETE.getModuleType().toLowerCase())) {
					outputMap = getImportAutocompleteDetails(outputMap, file);
				} else if (fileName.toLowerCase()
						.startsWith(Constant.MasterModuleType.RESOURCEBUNDLE.getModuleType().toLowerCase())) {
					outputMap = getImportRBDetails(outputMap, file);
				} else if (fileName.toLowerCase()
						.startsWith(Constant.MasterModuleType.DASHBOARD.getModuleType().toLowerCase())) {
					outputMap = getImportDashboardDetails(outputMap, file);
				} else if (fileName.toLowerCase()
						.startsWith(Constant.MasterModuleType.NOTIFICATION.getModuleType().toLowerCase())) {
					outputMap = getImportNotificationDetails(outputMap, file);
				} else if (fileName.toLowerCase()
						.startsWith(Constant.MasterModuleType.PERMISSION.getModuleType().toLowerCase())) {
					outputMap = getImportPermissionDetails(outputMap, file);
				} else if (fileName.toLowerCase()
						.startsWith(Constant.MasterModuleType.SITELAYOUT.getModuleType().toLowerCase())) {
					outputMap = getImportSiteLayoutDetails(outputMap, file);
				} else if (fileName.toLowerCase()
						.startsWith(Constant.MasterModuleType.APPLICATIONCONFIGURATION.getModuleType().toLowerCase())) {
					outputMap = getImportAppConfigtDetails(outputMap, file);
				} else if (fileName.toLowerCase()
						.startsWith(Constant.MasterModuleType.MANAGEUSERS.getModuleType().toLowerCase())) {
					outputMap = getImportUsersDetails(outputMap, file);
				} else if (fileName.toLowerCase()
						.startsWith(Constant.MasterModuleType.MANAGEROLES.getModuleType().toLowerCase())) {
					outputMap = getImportRolesDetails(outputMap, file);
				} else if (fileName.toLowerCase()
						.startsWith(Constant.MasterModuleType.APICLIENTDETAILS.getModuleType().toLowerCase())) {
					outputMap = getApiClientDetails(outputMap, file);
				} else if (fileName.toLowerCase()
						.startsWith(Constant.MasterModuleType.ADDITIONALDATASOURCE.getModuleType().toLowerCase())) {
					outputMap = getAdditionalDatasource(outputMap, file);
				} else if (fileName.toLowerCase()
						.startsWith(Constant.MasterModuleType.SCHEDULER.getModuleType().toLowerCase())) {
					outputMap = getScheduler(outputMap, file);
				}
			} else if (Constant.FOLDER_EXPORT_TYPE.equals(moduleType)) {
				if (Constant.MasterModuleType.TEMPLATES.getModuleType().equalsIgnoreCase(moduleName)) {
					outputMap = getImportTemplatesDetails(outputMap, filePath);
				} else if (Constant.MasterModuleType.DASHLET.getModuleType().equalsIgnoreCase(moduleName)) {
					outputMap = getImportDashletDetails(outputMap, filePath);
				} else if (Constant.MasterModuleType.DYNAMICFORM.getModuleType().equalsIgnoreCase(moduleName)) {
					outputMap = getImportDynamicFormDetails(outputMap, filePath);
				} else if (Constant.MasterModuleType.HELPMANUAL.getModuleType().equalsIgnoreCase(moduleName)) {
					outputMap = getImportHelpManualDetails(outputMap, filePath);
				} else if (Constant.MasterModuleType.FILEMANAGER.getModuleType().equalsIgnoreCase(moduleName)) {
					outputMap = getImportFileManagerDetails(outputMap, filePath);
				} else if (Constant.MasterModuleType.DYNAREST.getModuleType().equalsIgnoreCase(moduleName)) {
					outputMap = getImportDynaRestDetails(outputMap, filePath);
				}
			}
		}

		return outputMap;
	}

	private Map<String, Object> getImportGridDetails(Map<String, Object> outputMap, File file) throws Exception {
		GridXMLVO xmlVO = (GridXMLVO) XMLUtil.unMarshaling(GridXMLVO.class, file.getAbsolutePath());

		for (GridDetails grid : xmlVO.getGridDetails()) {
			GridDetailsJsonVO vo = convertGridEntityToVO(grid);
			updateOutputMap(outputMap, grid.getGridId(), vo, grid,
					Constant.MasterModuleType.GRID.getModuleType().toLowerCase());
		}
		return outputMap;
	}

	private Map<String, Object> getImportAutocompleteDetails(Map<String, Object> outputMap, File file)
			throws Exception {
		AutocompleteXMLVO xmlVO = (AutocompleteXMLVO) XMLUtil.unMarshaling(AutocompleteXMLVO.class,
				file.getAbsolutePath());

		for (Autocomplete autocomplete : xmlVO.getAutocompleteDetails()) {
			AutocompleteVO autocompleteVO = typeAheadService.convertEntityToVO(autocomplete.getAutocompleteId(),
					autocomplete.getAutocompleteDesc(), autocomplete.getAutocompleteSelectQuery());
			updateOutputMap(outputMap, autocomplete.getAutocompleteId(), autocompleteVO, autocomplete,
					Constant.MasterModuleType.AUTOCOMPLETE.getModuleType().toLowerCase());
		}
		return outputMap;
	}

	private Map<String, Object> getImportRBDetails(Map<String, Object> outputMap, File file) throws Exception {
		ResourceBundleXMLVO					xmlVO		= (ResourceBundleXMLVO) XMLUtil
				.unMarshaling(ResourceBundleXMLVO.class, file.getAbsolutePath());

		Map<String, List<ResourceBundleVO>>	map			= new TreeMap();
		Map<String, List<ResourceBundle>>	entityMap	= new HashMap<>();
		Gson								gson		= new Gson();

		for (ResourceBundle rb : xmlVO.getResourceBundleDetails()) {
			List<ResourceBundleVO> list = new ArrayList<>();
			if (map.containsKey(rb.getId().getResourceKey())) {
				list = map.get(rb.getId().getResourceKey());
			}
			list.add(resourceBundleService.convertResourceBundleEntityToVO(rb));
			if (rb.getId().getLanguageId().equals(com.trigyn.jws.resourcebundle.utils.Constant.DEFAULT_LANGUAGE_ID)) {
				rb.setText(rb.getText());
			} else {
				rb.setText(ResourceBundleUtils.getUnicode(rb.getText()));
			}
			map.put(rb.getId().getResourceKey(), list);

			List<ResourceBundle> rbList = new ArrayList<>();
			if (entityMap.containsKey(rb.getId().getResourceKey())) {
				rbList = entityMap.get(rb.getId().getResourceKey());
			}
			rbList.add(rb);
			entityMap.put(rb.getId().getResourceKey(), rbList);

			outputMap	= putEntityIntoMap(rb.getId().getResourceKey(), outputMap, rbList);
			outputMap	= putModuleTypeIntoMap(rb.getId().getResourceKey(), outputMap,
					Constant.MasterModuleType.RESOURCEBUNDLE.getModuleType().toLowerCase());
		}

		for (java.util.Map.Entry<String, List<ResourceBundleVO>> entry : map.entrySet()) {
			String					key		= entry.getKey();
			List<ResourceBundleVO>	rbList	= entry.getValue();
			outputMap.put(key, gson.toJson(rbList));
		}
		return outputMap;
	}

	private Map<String, Object> getImportDashboardDetails(Map<String, Object> outputMap, File file) throws Exception {
		DashboardXMLVO xmlVO = (DashboardXMLVO) XMLUtil.unMarshaling(DashboardXMLVO.class, file.getAbsolutePath());

		for (Dashboard dashboard : xmlVO.getDashboardDetails()) {
			updateOutputMap(outputMap, dashboard.getDashboardId(),
					dashboardCrudService.convertDashboarEntityToVO(dashboard), dashboard,
					Constant.MasterModuleType.DASHBOARD.getModuleType().toLowerCase());
		}
		return outputMap;
	}

	private Map<String, Object> getImportNotificationDetails(Map<String, Object> outputMap, File file)
			throws Exception {
		GenericUserNotificationXMLVO xmlVO = (GenericUserNotificationXMLVO) XMLUtil
				.unMarshaling(GenericUserNotificationXMLVO.class, file.getAbsolutePath());

		for (GenericUserNotification notification : xmlVO.getGenericUserNotificationDetails()) {
			GenericUserNotificationJsonVO vo = convertNotificationEntityToVO(notification);
			updateOutputMap(outputMap, notification.getNotificationId(), vo, notification,
					Constant.MasterModuleType.NOTIFICATION.getModuleType().toLowerCase());
		}
		return outputMap;
	}

	private Map<String, Object> getImportDynaRestDetails(Map<String, Object> outputMap, String filePath)
			throws Exception {
		String			dynaRestFolderPath	= filePath + File.separator
				+ com.trigyn.jws.dynarest.utils.Constants.DYNAMIC_REST_DIRECTORY_NAME;
		MetadataXMLVO	metadataXMLVO		= readMetaDataXML(dynaRestFolderPath);
		for (Modules module : metadataXMLVO.getExportModules().getModule()) {
			String					moduleName			= module.getModuleName();
			String					moduleID			= module.getModuleID();
			DynaRestExportVO		dynaRestExportVO	= module.getDynaRestExportVO();

			JwsDynamicRestDetail	dynaRest			= (JwsDynamicRestDetail) dynaRestModule
					.importData(dynaRestFolderPath, moduleName, moduleID, dynaRestExportVO);
			dynaRest = dynaRest.getObject();
			RestApiDetailsJsonVO vo = convertDynaRestEntityToVO(dynaRest);

			updateOutputMap(outputMap, moduleID, vo, dynaRest,
					Constant.MasterModuleType.DYNAREST.getModuleType().toLowerCase());
		}
		return outputMap;
	}

	private Map<String, Object> getImportFileManagerDetails(Map<String, Object> outputMap, String filePath)
			throws Exception {
		String			dynamicFormFolderPath	= filePath + File.separator + Constant.FILE_UPLOAD_DIRECTORY_NAME;
		MetadataXMLVO	metadataXMLVO			= readMetaDataXML(dynamicFormFolderPath);
		for (Modules module : metadataXMLVO.getExportModules().getModule()) {
			String							moduleName						= module.getModuleName();
			String							moduleID						= module.getModuleID();
			FileUploadConfigExportVO		fileBin							= module.getFileBin();
			FileUploadConfigImportEntity	fileUploadConfigImportEntity	= (FileUploadConfigImportEntity) fileUploadExportModule
					.importData(dynamicFormFolderPath, moduleName, moduleID, fileBin);

			updateOutputMap(outputMap, moduleID, fileBin, fileUploadConfigImportEntity,
					Constant.MasterModuleType.FILEMANAGER.getModuleType().toLowerCase());
		}
		return outputMap;
	}

	private Map<String, Object> getImportPermissionDetails(Map<String, Object> outputMap, File file) throws Exception {
		PermissionXMLVO xmlVO = (PermissionXMLVO) XMLUtil.unMarshaling(PermissionXMLVO.class, file.getAbsolutePath());

		for (JwsEntityRoleAssociation jwsRole : xmlVO.getJwsRoleDetails()) {
			JwsEntityRoleAssociationVO vo = convertJwsEntityRoleAssociationEntityToVO(jwsRole);
			updateOutputMap(outputMap, jwsRole.getEntityRoleId(), vo, jwsRole,
					Constant.MasterModuleType.PERMISSION.getModuleType().toLowerCase());
		}
		return outputMap;
	}

	private Map<String, Object> getImportSiteLayoutDetails(Map<String, Object> outputMap, File file) throws Exception {
		SiteLayoutXMLVO xmlVO = (SiteLayoutXMLVO) XMLUtil.unMarshaling(SiteLayoutXMLVO.class, file.getAbsolutePath());

		for (ModuleListing siteLayout : xmlVO.getModuleListingDetails()) {
			ModuleDetailsVO vo = moduleService.convertModuleEntityToVO(siteLayout);
			updateOutputMap(outputMap, siteLayout.getModuleId(), vo, siteLayout,
					Constant.MasterModuleType.SITELAYOUT.getModuleType().toLowerCase());
		}
		return outputMap;
	}

	private Map<String, Object> getImportAppConfigtDetails(Map<String, Object> outputMap, File file) throws Exception {
		PropertyMasterXMLVO xmlVO = (PropertyMasterXMLVO) XMLUtil.unMarshaling(PropertyMasterXMLVO.class,
				file.getAbsolutePath());

		for (PropertyMaster propertyMaster : xmlVO.getApplicationConfiguration()) {
			PropertyMasterJsonVO vo = convertPropertyMasterEntityToVO(propertyMaster);
			updateOutputMap(outputMap, propertyMaster.getPropertyMasterId(), vo, propertyMaster,
					Constant.MasterModuleType.APPLICATIONCONFIGURATION.getModuleType().toLowerCase());
		}
		return outputMap;
	}

	private Map<String, Object> getImportUsersDetails(Map<String, Object> outputMap, File file) throws Exception {
		UserXMLVO xmlVO = (UserXMLVO) XMLUtil.unMarshaling(UserXMLVO.class, file.getAbsolutePath());

		for (JwsUser user : xmlVO.getUserDetails()) {
			JwsUserVO vo = user.convertEntityToVO(user);
			updateOutputMap(outputMap, user.getUserId(), vo, user,
					Constant.MasterModuleType.MANAGEUSERS.getModuleType().toLowerCase());
		}
		return outputMap;
	}

	private Map<String, Object> getImportRolesDetails(Map<String, Object> outputMap, File file) throws Exception {
		RoleXMLVO xmlVO = (RoleXMLVO) XMLUtil.unMarshaling(RoleXMLVO.class, file.getAbsolutePath());

		for (JwsRole role : xmlVO.getRoleDetails()) {
			JwsRoleVO vo = role.convertEntityToVO(role);
			updateOutputMap(outputMap, role.getRoleName(), vo, role,
					Constant.MasterModuleType.MANAGEROLES.getModuleType().toLowerCase());
		}
		return outputMap;
	}

	private Map<String, Object> getApiClientDetails(Map<String, Object> outputMap, File file) throws Exception {
		ApiClientDetailsXMLVO xmlVO = (ApiClientDetailsXMLVO) XMLUtil.unMarshaling(ApiClientDetailsXMLVO.class,
				file.getAbsolutePath());

		for (ApiClientDetails apiClientDetails : xmlVO.getApiClientDetails()) {
			ApiClientDetailsVO vo = apiClientDetails.convertEntityToVO(apiClientDetails);
			updateOutputMap(outputMap, apiClientDetails.getClientId(), vo, apiClientDetails,
					Constant.MasterModuleType.APICLIENTDETAILS.getModuleType().toLowerCase());
		}
		return outputMap;
	}

	private Map<String, Object> getAdditionalDatasource(Map<String, Object> outputMap, File file) throws Exception {
		AdditionalDatasourceXMLVO xmlVO = (AdditionalDatasourceXMLVO) XMLUtil
				.unMarshaling(AdditionalDatasourceXMLVO.class, file.getAbsolutePath());

		for (AdditionalDatasource additionalDS : xmlVO.getAdditionalDatasource()) {
			AdditionalDatasourceVO vo = additionalDS.convertEntityToVO(additionalDS);
			updateOutputMap(outputMap, additionalDS.getAdditionalDatasourceId(), vo, additionalDS,
					Constant.MasterModuleType.ADDITIONALDATASOURCE.getModuleType().toLowerCase());
		}
		return outputMap;
	}

	private Map<String, Object> getScheduler(Map<String, Object> outputMap, File file) throws Exception {
		SchedulerXMLVO xmlVO = (SchedulerXMLVO) XMLUtil.unMarshaling(SchedulerXMLVO.class, file.getAbsolutePath());

		for (JqScheduler scheduler : xmlVO.getSchedulerDetails()) {
			SchedulerVO schedulerVO = convertSchedulerEntityToVO(scheduler);
			updateOutputMap(outputMap, scheduler.getSchedulerId(), schedulerVO, scheduler,
					Constant.MasterModuleType.SCHEDULER.getModuleType().toLowerCase());
		}
		return outputMap;
	}

	private Map<String, Object> getImportTemplatesDetails(Map<String, Object> outputMap, String filePath)
			throws Exception {
		String			templateFolderPath	= filePath + File.separator
				+ com.trigyn.jws.templating.utils.Constant.TEMPLATE_DIRECTORY_NAME;
		MetadataXMLVO	metadataXMLVO		= readMetaDataXML(templateFolderPath);
		for (Modules module : metadataXMLVO.getExportModules().getModule()) {
			String				moduleName			= module.getModuleName();
			String				moduleID			= module.getModuleID();
			TemplateExportVO	templateExportVO	= module.getTemplate();

			TemplateMaster		template			= (TemplateMaster) templateDownloadUploadModule
					.importData(templateFolderPath, moduleName, moduleID, templateExportVO);
			template = template.getObject();
			TemplateVO templateVO = new TemplateVO(template.getTemplateId(), template.getTemplateName(),
					template.getTemplate(), template.getUpdatedDate());

			updateOutputMap(outputMap, moduleID, templateVO, template,
					Constant.MasterModuleType.TEMPLATES.getModuleType().toLowerCase());
		}
		return outputMap;
	}

	private Map<String, Object> getImportDashletDetails(Map<String, Object> outputMap, String filePath)
			throws Exception {
		String			dashletFolderPath	= filePath + File.separator + Constants.DASHLET_DIRECTORY_NAME;
		MetadataXMLVO	metadataXMLVO		= readMetaDataXML(dashletFolderPath);
		// for (Modules module : metadataXMLVO.getExportModules().getModule()) {
		// String moduleName = module.getModuleName();
		// String moduleID = module.getModuleID();
		// DashletExportVO dashletExportVO = module.getDashlet();
		//
		// Dashlet dashlet = (Dashlet)
		// dashletDownloadUploadModule.importData(dashletFolderPath,
		// moduleName, moduleID, dashletExportVO);
		// dashlet = dashlet.getObject();
		// DashletVO dashletVO =
		// dashletDownloadUploadModule.convertDashletEntityToVO(dashlet);
		//
		// updateOutputMap(outputMap, moduleID, dashletVO, dashlet,
		// Constant.MasterModuleType.DASHLET.getModuleType().toLowerCase());
		// }
		return outputMap;
	}

	private Map<String, Object> getImportDynamicFormDetails(Map<String, Object> outputMap, String filePath)
			throws Exception {
		String			dynamicFormFolderPath	= filePath + File.separator
				+ com.trigyn.jws.dynamicform.utils.Constant.DYNAMIC_FORM_DIRECTORY_NAME;
		MetadataXMLVO	metadataXMLVO			= readMetaDataXML(dynamicFormFolderPath);
		for (Modules module : metadataXMLVO.getExportModules().getModule()) {
			String				moduleName			= module.getModuleName();
			String				moduleID			= module.getModuleID();
			DynamicFormExportVO	dynamicFormExportVO	= module.getDynamicForm();
			DynamicForm			dynamicForm			= (DynamicForm) dynamicFormDownloadUploadModule
					.importData(dynamicFormFolderPath, moduleName, moduleID, dynamicFormExportVO);
			dynamicForm = dynamicForm.getObject(true);

			DynamicFormVO dynamicFormVO = dynamicFormDownloadUploadModule.convertEntityToVO(dynamicForm);
			updateOutputMap(outputMap, moduleID, dynamicFormVO, dynamicForm,
					Constant.MasterModuleType.DYNAMICFORM.getModuleType().toLowerCase());
		}
		return outputMap;
	}

	private Map<String, Object> getImportHelpManualDetails(Map<String, Object> outputMap, String filePath)
			throws Exception {
		String			dynamicFormFolderPath	= filePath + File.separator + Constant.HELP_MANUAL_DIRECTORY_NAME;
		MetadataXMLVO	metadataXMLVO			= readMetaDataXML(dynamicFormFolderPath);
		for (Modules module : metadataXMLVO.getExportModules().getModule()) {
			String					moduleName			= module.getModuleName();
			String					moduleID			= module.getModuleID();
			HelpManualTypeExportVO	helpManualExportVO	= module.getHelpManual();
			HelpManual				helpManual			= (HelpManual) helpManualImportExportModule
					.importData(dynamicFormFolderPath, moduleName, moduleID, helpManualExportVO);

			updateOutputMap(outputMap, moduleID, helpManualExportVO, helpManual,
					Constant.MasterModuleType.HELPMANUAL.getModuleType().toLowerCase());
		}
		return outputMap;
	}

	private Map<String, Object> updateOutputMap(Map<String, Object> outputMap, String moduleID, Object jsonObj,
			Object entity, String moduleType) throws Exception {
		Gson			gson			= new Gson();
		ObjectMapper	objectMapper	= new ObjectMapper();
		String			dbDateFormat	= propertyMasterService.getDateFormatByName(Constant.PROPERTY_MASTER_OWNER_TYPE,
				Constant.PROPERTY_MASTER_OWNER_ID, Constant.JWS_DATE_FORMAT_PROPERTY_NAME,
				com.trigyn.jws.dbutils.utils.Constant.JWS_JAVA_DATE_FORMAT_PROPERTY_NAME);
		DateFormat		dateFormat		= new SimpleDateFormat(dbDateFormat);
		objectMapper.setDateFormat(dateFormat);
		Map<String, Object>	objectMap	= objectMapper.convertValue(jsonObj, TreeMap.class);
		String				jsonStr		= gson.toJson(objectMap);
		outputMap.put(moduleType + moduleID, jsonStr);
		outputMap	= putEntityIntoMap(moduleType + moduleID, outputMap, entity);
		outputMap	= putModuleTypeIntoMap(moduleType + moduleID, outputMap, moduleType);
		return outputMap;
	}

	private Map<String, Object> putEntityIntoMap(String id, Map<String, Object> outputMap, Object obj) {

		Map<String, String> map = new HashMap<>();
		if (outputMap.containsKey("exportedFormatObject")) {
			map = (Map<String, String>) outputMap.get("exportedFormatObject");
		}
		Gson	gson		= new Gson();
		String	jsonString	= gson.toJson(obj);
		map.put(id, jsonString);

		outputMap.put("exportedFormatObject", map);
		return outputMap;
	}

	private Map<String, Object> putModuleTypeIntoMap(String id, Map<String, Object> outputMap, String moduleType) {

		Map<String, String> map = new HashMap<>();
		if (outputMap.containsKey("exportedModuleTypeObject")) {
			map = (Map<String, String>) outputMap.get("exportedModuleTypeObject");
		}
		map.put(id, moduleType);

		outputMap.put("exportedModuleTypeObject", map);
		return outputMap;
	}

	public Map<String, Boolean> getLatestCRC(Map<String, Object> inputData) throws Exception {
		Gson				g				= new Gson();
		Map<String, String>	moduleTypeMap	= new HashMap<>();
		for (Entry<String, Object> input : inputData.entrySet()) {
			String key = input.getKey();

			if (key.equals("exportedModuleTypeObject")) {
				moduleTypeMap = (Map<String, String>) input.getValue();
			}
		}

		Map<String, Boolean> crcMap = new HashMap<>();
		for (Entry<String, Object> input : inputData.entrySet()) {
			String key = input.getKey();

			if (!key.equals("completeZipJsonData") && !key.equals("exportedFormatObject")
					&& !key.equals("exportedModuleTypeObject") && !key.equals("versionMap")) {
				String value = (String) input.getValue();
				crcMap.put(key, getLatestCRC(key, moduleTypeMap.get(key), value));
			}
		}
		return crcMap;
	}

	private Boolean getLatestCRC(String id, String moduleType, String importedJson) throws Exception {
		id = id.replace(moduleType, "");
		Boolean isCheckSumUpdated = true;

		if (moduleType.equalsIgnoreCase(Constant.MasterModuleType.FILEMANAGER.getModuleType().toLowerCase())
				|| moduleType.equalsIgnoreCase(Constant.MasterModuleType.PERMISSION.getModuleType().toLowerCase())
				|| moduleType.equalsIgnoreCase(Constant.MasterModuleType.SITELAYOUT.getModuleType().toLowerCase())
				|| moduleType.equalsIgnoreCase(Constant.MasterModuleType.MANAGEUSERS.getModuleType().toLowerCase())
				|| moduleType.equalsIgnoreCase(Constant.MasterModuleType.MANAGEROLES.getModuleType().toLowerCase())
				|| moduleType.equalsIgnoreCase(Constant.MasterModuleType.HELPMANUAL.getModuleType().toLowerCase())
				|| moduleType.equalsIgnoreCase(Constant.MasterModuleType.APICLIENTDETAILS.getModuleType().toLowerCase())
				|| moduleType
						.equalsIgnoreCase(Constant.MasterModuleType.ADDITIONALDATASOURCE.getModuleType().toLowerCase())
				|| moduleType.equalsIgnoreCase(Constant.MasterModuleType.SCHEDULER.getModuleType().toLowerCase())) {
			String existingJson = "";
			/*
			 * if (moduleType.equalsIgnoreCase(Constant.MasterModuleType.FILEMANAGER.
			 * getModuleType().toLowerCase())) { existingJson =
			 * fileUploadConfigService.getFileUploadJson(id); } else
			 */if (moduleType.equalsIgnoreCase(Constant.MasterModuleType.PERMISSION.getModuleType().toLowerCase())) {
				existingJson = userManagementService.getJwsEntityRoleAssociationJson(id);
			} else if (moduleType
					.equalsIgnoreCase(Constant.MasterModuleType.SITELAYOUT.getModuleType().toLowerCase())) {
				existingJson = moduleService.getModuleListingJson(id);
			} else if (moduleType
					.equalsIgnoreCase(Constant.MasterModuleType.MANAGEUSERS.getModuleType().toLowerCase())) {
				existingJson = userManagementService.getJwsUserJson(id);
			} else if (moduleType
					.equalsIgnoreCase(Constant.MasterModuleType.MANAGEROLES.getModuleType().toLowerCase())) {
				existingJson = userManagementService.getJwsRoleJson(id);
			} else if (moduleType
					.equalsIgnoreCase(Constant.MasterModuleType.ADDITIONALDATASOURCE.getModuleType().toLowerCase())) {
				existingJson = getAdditionalDatasourceJson(id);
			} else if (moduleType
					.equalsIgnoreCase(Constant.MasterModuleType.APICLIENTDETAILS.getModuleType().toLowerCase())) {
				existingJson = getApiClientDetailseJson(id);
			} else if (moduleType.equalsIgnoreCase(Constant.MasterModuleType.SCHEDULER.getModuleType().toLowerCase())) {
				existingJson = getSchedulerJson(id);
			}

			isCheckSumUpdated = checkSumComparisonForNonVersioningModules(existingJson, importedJson);
			if (moduleType.equalsIgnoreCase(Constant.MasterModuleType.FILEMANAGER.getModuleType().toLowerCase())
					|| moduleType
							.equalsIgnoreCase(Constant.MasterModuleType.HELPMANUAL.getModuleType().toLowerCase())) {
				isCheckSumUpdated = true;
			}
		} else {
			String entityName = EntityNameModuleTypeEnum.valueOf(moduleType.toUpperCase()).geTableName();
			isCheckSumUpdated = checkSumComparison(id, entityName, importedJson);
		}

		return isCheckSumUpdated;
	}

	private Boolean checkSumComparison(String entityTypeId, String entityName, String importedJson) throws Exception {
		String	moduleJsonChecksum	= moduleVersionService.generateJsonChecksum(importedJson);
		Boolean	isDataUpdated		= moduleVersionService.compareChecksum(entityTypeId, entityName,
				moduleJsonChecksum);
		return isDataUpdated;
	}

	private Boolean checkSumComparisonForNonVersioningModules(String existingJson, String importedJson)
			throws Exception {
		String existingJsonChecksum = null;
		if (existingJson != null) {
			existingJsonChecksum = moduleVersionService.generateJsonChecksum(existingJson);
		}
		String moduleJsonChecksum = null;
		if (importedJson != null) {
			moduleJsonChecksum = moduleVersionService.generateJsonChecksum(importedJson);
		}
		Boolean isChecksumChanged = true;
		if (existingJsonChecksum != null && existingJsonChecksum.equals(moduleJsonChecksum)) {
			isChecksumChanged = false;
		}
		return isChecksumChanged;
	}

	public Map<String, String> getLatestVersion(Map<String, Object> inputData) throws Exception {
		Map<String, String> versionMap = new HashMap<>();
		for (Entry<String, Object> input : inputData.entrySet()) {
			String key = input.getKey();

			if (key.equals("completeZipJsonData")) {
				String jsonArrayString = (String) input.getValue();
				versionMap = getLatestVersion(jsonArrayString);
			}
		}
		return versionMap;
	}

	private Map<String, String> getLatestVersion(String jsonArrayString) throws Exception {
		Map<String, String>	versionMap	= new HashMap<>();
		JSONArray			jsonArray	= new JSONArray(jsonArrayString);

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject	explrObject	= jsonArray.getJSONObject(i);
			String		entityID	= explrObject.getString("moduleID");
			String		moduleType	= explrObject.getString("moduleType");

			String		version		= "NA";

			if (moduleType.equalsIgnoreCase(Constant.MasterModuleType.FILEMANAGER.getModuleType().toLowerCase())
					|| moduleType.equalsIgnoreCase(Constant.MasterModuleType.PERMISSION.getModuleType().toLowerCase())
					|| moduleType.equalsIgnoreCase(Constant.MasterModuleType.SITELAYOUT.getModuleType().toLowerCase())
					|| moduleType.equalsIgnoreCase(Constant.MasterModuleType.MANAGEUSERS.getModuleType().toLowerCase())
					|| moduleType.equalsIgnoreCase(Constant.MasterModuleType.HELPMANUAL.getModuleType().toLowerCase())
					|| moduleType.equalsIgnoreCase(
							Constant.MasterModuleType.ADDITIONALDATASOURCE.getModuleType().toLowerCase())
					|| moduleType
							.equalsIgnoreCase(Constant.MasterModuleType.APICLIENTDETAILS.getModuleType().toLowerCase())
					|| moduleType.equalsIgnoreCase(Constant.MasterModuleType.MANAGEROLES.getModuleType().toLowerCase())
					|| moduleType.equalsIgnoreCase(Constant.MasterModuleType.SCHEDULER.getModuleType().toLowerCase())) {

				if (moduleType.equalsIgnoreCase(Constant.MasterModuleType.FILEMANAGER.getModuleType().toLowerCase())) {
					FileUploadConfig fileUploadConfig = fileUploadConfigService.getFileUploadConfigByBinId(entityID);
					if (fileUploadConfig == null) {
						version = "NE";
					}
				} else if (moduleType
						.equalsIgnoreCase(Constant.MasterModuleType.PERMISSION.getModuleType().toLowerCase())) {
					JwsEntityRoleAssociation role = userManagementService.findByEntityRoleID(entityID);
					if (role == null) {
						version = "NE";
					}
				} else if (moduleType
						.equalsIgnoreCase(Constant.MasterModuleType.SITELAYOUT.getModuleType().toLowerCase())) {
					ModuleListing module = moduleService.getModuleListing(entityID);
					if (module == null) {
						version = "NE";
					}
				} else if (moduleType
						.equalsIgnoreCase(Constant.MasterModuleType.MANAGEUSERS.getModuleType().toLowerCase())) {
					JwsUser user = userManagementService.getJwsUser(entityID);
					if (user == null) {
						version = "NE";
					}
				} else if (moduleType
						.equalsIgnoreCase(Constant.MasterModuleType.MANAGEROLES.getModuleType().toLowerCase())) {
					JwsRole role = userManagementService.getJwsRole(entityID);
					if (role == null) {
						version = "NE";
					}
				} else if (moduleType
						.equalsIgnoreCase(Constant.MasterModuleType.HELPMANUAL.getModuleType().toLowerCase())) {
					ManualType manualType = iManualTypeRepository.findById(entityID).orElse(null);
					if (manualType == null) {
						version = "NE";
					}
				} else if (moduleType.equalsIgnoreCase(
						Constant.MasterModuleType.ADDITIONALDATASOURCE.getModuleType().toLowerCase())) {
					AdditionalDatasource ads = additionalDatasourceRepository.findById(entityID).orElse(null);
					if (ads == null) {
						version = "NE";
					}
				} else if (moduleType
						.equalsIgnoreCase(Constant.MasterModuleType.APICLIENTDETAILS.getModuleType().toLowerCase())) {
					ApiClientDetails acd = apiClientDetailsRepository.findById(entityID).orElse(null);
					if (acd == null) {
						version = "NE";
					}
				} else if (moduleType
						.equalsIgnoreCase(Constant.MasterModuleType.SCHEDULER.getModuleType().toLowerCase())) {
					JqScheduler scheduler = schedulerRepository.findById(entityID).orElse(null);
					if (scheduler == null) {
						version = "NE";
					}
				}
			} else {
				String entityName = EntityNameModuleTypeEnum.valueOf(moduleType.toUpperCase()).geTableName();
				version = String.valueOf(moduleVersionDAO.getVersionIdByEntityIdAndName(entityID, entityName));
				if (version == null || "".equals(version) || "null".equals(version)) {
					version = "NA";
				}

			}

			explrObject.put("existingVersion", version);
			versionMap.put(moduleType.toLowerCase() + entityID, version);
		}

		return versionMap;
	}

	public String importConfig(String exportedFormatObject, String importId, String moduleType, boolean isOnLoad, Date lastImportDate) throws Exception {
		try {
			Gson				g				= new Gson();
			// JSONObject imporatableDataJson = new JSONObject(imporatableData);
			//
			// JSONObject exportedFormatObject = (JSONObject)
			// imporatableDataJson.get("exportedFormatObject");
			Map<String, String>	entityStringMap	= g.fromJson(exportedFormatObject.toString(), Map.class);
			String				entityString	= entityStringMap.get(moduleType.toLowerCase() + importId);

			saveEntityOnLoad(moduleType, entityString, importId, isOnLoad);
			importId = importId.replace(moduleType, "");

			String version = String.valueOf(moduleVersionDAO.getVersionIdByEntityId(importId));
			if (version == null || "".equals(version) || "null".equals(version)) {
				version = "NA";
			}
			return version;
		} catch (Exception a_excep) {
			logger.error("Error while importing the configuration ", a_excep);
			if (a_excep.getMessage() != null && a_excep.getMessage().startsWith("Error while importing entity of ")) {
				throw new Exception("fail:" + a_excep.getMessage());
			} else {
				throw new Exception("fail:" + "Error while importing data");
			}
		}
	}

	public String importAll(String imporatableData, String importedIdJsonArray, boolean isOnLoad) throws Exception {

		try {
			Gson				g									= new Gson();
			JSONObject			imporatableDataJson					= new JSONObject(imporatableData);

			JSONObject			exportedModuleTypeObject			= (JSONObject) imporatableDataJson
					.get("exportedModuleTypeObject");
			Map<String, String>	moduleTypeMap						= g.fromJson(exportedModuleTypeObject.toString(),
					Map.class);

			JSONObject			exportedFormatObject				= (JSONObject) imporatableDataJson
					.get("exportedFormatObject");
			Map<String, String>	entityStringMap						= g.fromJson(exportedFormatObject.toString(),
					Map.class);

			List<String>		importedIdList						= g.fromJson(importedIdJsonArray, List.class);
			Collection<String>	moduleTypeList						= moduleTypeMap.values();

			Map<String, String>	pendingDashboardEntityStringMap		= new HashMap<>();
			Map<String, String>	pendingPermissionEntityStringMap	= new HashMap<>();
			Map<String, String>	pendingSchedulerEntityStringMap	= new HashMap<>();

			for (Entry<String, String> entry : entityStringMap.entrySet()) {
				String	importId		= entry.getKey();
				String	entityString	= entry.getValue();
				String	moduleType		= moduleTypeMap.get(importId);

				if (importedIdList == null || (importedIdList != null && !importedIdList.contains(importId))) {

					String					crcMapObject	= (String) imporatableDataJson.get("crcMap");
					Map<String, Boolean>	crcMap			= g.fromJson(crcMapObject, Map.class);
					if (crcMap != null && crcMap.containsKey(importId) && crcMap.get(importId) == true) {

						if (Constant.MasterModuleType.DASHBOARD.getModuleType().equalsIgnoreCase(moduleType)
								&& moduleTypeList
										.contains(Constant.MasterModuleType.DASHLET.getModuleType().toLowerCase())) {
							pendingDashboardEntityStringMap.put(importId, entityString);
							continue;
						}

						if (Constant.MasterModuleType.PERMISSION.getModuleType().equalsIgnoreCase(moduleType)) {
							pendingPermissionEntityStringMap.put(importId, entityString);
							continue;
						}

						if (Constant.MasterModuleType.SCHEDULER.getModuleType().equalsIgnoreCase(moduleType)) {
							pendingSchedulerEntityStringMap.put(importId, entityString);
							continue;
						}

						if (isOnLoad == false) {
//							saveEntity(moduleType, entityString, importId);
							saveEntityOnLoad(moduleType, entityString, importId, isOnLoad);
						} else {
							saveEntityOnLoad(moduleType, entityString, importId, isOnLoad);
						}

					}

				}
			}

			for (Entry<String, String> entry : pendingDashboardEntityStringMap.entrySet()) {
				String	importId		= entry.getKey();
				String	entityString	= entry.getValue();
				if (isOnLoad == false) {
//					saveEntity(Constant.MasterModuleType.DASHBOARD.getModuleType(), entityString, importId);
					saveEntityOnLoad(Constant.MasterModuleType.DASHBOARD.getModuleType(), entityString, importId, isOnLoad);
				} else {
					saveEntityOnLoad(Constant.MasterModuleType.DASHBOARD.getModuleType(), entityString, importId, isOnLoad);
				}
			}

			for (Entry<String, String> entry : pendingPermissionEntityStringMap.entrySet()) {
				String	importId		= entry.getKey();
				String	entityString	= entry.getValue();
				if (isOnLoad == false) {
//					saveEntity(Constant.MasterModuleType.PERMISSION.getModuleType(), entityString, importId);
					saveEntityOnLoad(Constant.MasterModuleType.PERMISSION.getModuleType(), entityString, importId, isOnLoad);
				} else {
					saveEntityOnLoad(Constant.MasterModuleType.PERMISSION.getModuleType(), entityString, importId, isOnLoad);
				}
			}

			for (Entry<String, String> entry : pendingSchedulerEntityStringMap.entrySet()) {
				String	importId		= entry.getKey();
				String	entityString	= entry.getValue();
				if (isOnLoad == false) {
//					saveEntity(Constant.MasterModuleType.SCHEDULER.getModuleType(), entityString, importId);
					saveEntityOnLoad(Constant.MasterModuleType.SCHEDULER.getModuleType(), entityString, importId, isOnLoad);
				} else {
					saveEntityOnLoad(Constant.MasterModuleType.SCHEDULER.getModuleType(), entityString, importId, isOnLoad);
				}
			}
		} catch (Exception a_excep) {
			logger.error("Error while importing the configuration ", a_excep);
			String errorMessage = a_excep.getMessage();
			if (a_excep.getMessage() != null && a_excep.getMessage().startsWith("Error while importing entity of ")) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				throw new Exception("fail:" + errorMessage);
			} else {
				throw new Exception("fail:" + "Error while importing data");
			}
		}
		return "success";
	}

	public void saveEntityOnLoad(String moduleType, String entityString, String importId, boolean isOnLoad) throws Exception {
		try {
			
			UserDetailsVO detailsVO = null;
			if ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes() != null) {
				detailsVO = detailsService.getUserDetails();
			} else {
				detailsVO = new UserDetailsVO("admin@jquiver.io", "admin", Arrays.asList("ADMIN"), "admin");
			}
			Date	date		= new Date();
			String	user		= detailsVO.getUserName();
			String	tableName	= "";
			if (!(moduleType.equalsIgnoreCase(Constant.MasterModuleType.FILEMANAGER.getModuleType().toLowerCase())
					|| moduleType.equalsIgnoreCase(Constant.MasterModuleType.PERMISSION.getModuleType().toLowerCase())
					|| moduleType.equalsIgnoreCase(Constant.MasterModuleType.SITELAYOUT.getModuleType().toLowerCase())
					|| moduleType.equalsIgnoreCase(Constant.MasterModuleType.MANAGEUSERS.getModuleType().toLowerCase())
					|| moduleType
							.equalsIgnoreCase(Constant.MasterModuleType.MANAGEROLES.getModuleType().toLowerCase())))
				tableName = EntityNameModuleTypeEnum.valueOf(moduleType.toUpperCase()).geTableName();

			Gson			g				= new Gson();
			ObjectMapper	objectMapper	= new ObjectMapper();
			if (Constant.MasterModuleType.GRID.getModuleType().equalsIgnoreCase(moduleType)) {
				GridDetails gridDetails = g.fromJson(entityString, GridDetails.class);
				
				GridDetails existingObject = gridDetailsDAO.findGridDetailsById(gridDetails.getGridId());
				
				if(isOnLoad == false || existingObject == null
						|| (isOnLoad && existingObject.getIsCustomUpdated() != null && existingObject.getIsCustomUpdated() == 0)) {
					if(isOnLoad == true) gridDetails.setIsCustomUpdated(0);
					
					gridDetailsDAO.saveGridDetails(gridDetails);
					GridDetailsJsonVO vo = convertGridEntityToVO(gridDetails);
					moduleVersionService.saveModuleVersion(vo, null, gridDetails.getGridId(), tableName,
						Constant.IMPORT_SOURCE_VERSION_TYPE);
				}
			} else if (Constant.MasterModuleType.AUTOCOMPLETE.getModuleType().equalsIgnoreCase(moduleType)) {
				Autocomplete	autocomplete	= g.fromJson(entityString, Autocomplete.class);
				Autocomplete existingObject = typeAheadDAO.findAutocomplete(autocomplete.getAutocompleteId());
				
				if(isOnLoad == false || existingObject == null 
						|| (isOnLoad &&  existingObject.getIsCustomUpdated() != null && existingObject.getIsCustomUpdated() == 0)) {
					if(isOnLoad == true) autocomplete.setIsCustomUpdated(0);
					
					AutocompleteVO	autocompleteVO	= typeAheadService.convertEntityToVO(autocomplete.getAutocompleteId(),
							autocomplete.getAutocompleteDesc(), autocomplete.getAutocompleteSelectQuery());
					typeAheadDAO.saveAutocomplete(autocomplete);
					moduleVersionService.saveModuleVersion(autocompleteVO, null, autocomplete.getAutocompleteId(),
						tableName, Constant.IMPORT_SOURCE_VERSION_TYPE);
				}
			} else if (Constant.MasterModuleType.RESOURCEBUNDLE.getModuleType().equalsIgnoreCase(moduleType)) {
				TypeReference<List<ResourceBundle>>	resourceBundleType		= new TypeReference<List<ResourceBundle>>() {
																			};
				List<ResourceBundle>				resourceBundleList		= objectMapper.readValue(entityString,
						resourceBundleType);

				
				List<ResourceBundleVO>				resourceBundleVOList	= new ArrayList<>();
				for (ResourceBundle entity : resourceBundleList) {
					ResourceBundle existingObject = resourceBundleDAO.findResourceBundle(entity.getId());
					if(isOnLoad == false || existingObject == null 
							|| (isOnLoad &&  existingObject.getIsCustomUpdated() != null && existingObject.getIsCustomUpdated() == 0)) {
						if(isOnLoad == true) entity.setIsCustomUpdated(0);
						resourceBundleDAO.saveResourceBundle(entity);
						ResourceBundleVO vo = resourceBundleService.convertResourceBundleEntityToVO(entity);
						resourceBundleVOList.add(vo);
					}
				}
				String resourceBundleKey = "";

				for (ResourceBundleVO vo : resourceBundleVOList) {
					resourceBundleKey = resourceBundleVOList.get(0).getResourceKey();
					moduleVersionService.saveModuleVersion(vo, null, resourceBundleKey, tableName,
							Constant.IMPORT_SOURCE_VERSION_TYPE);
				}

			} else if (Constant.MasterModuleType.DASHBOARD.getModuleType().equalsIgnoreCase(moduleType)) {
				// Dashboard dashboard = g.fromJson(entityString, Dashboard.class);
				//
				// Long existingObj =
				// dashboardDao.getDashboardCount(dashboard.getDashboardId());
				// if (existingObj == null || (existingObj != null && existingObj == 0)) {
				// dashboard.setCreatedBy(user);
				// dashboard.setCreatedDate(date);
				// }
				// List<DashboardRoleAssociation> dashboardRoleAssociations =
				// dashboard.getDashboardRoles();
				// List<DashboardDashletAssociation> dashboardDashlets =
				// dashboard.getDashboardDashlets();
				// dashboard.setDashboardRoles(null);
				// dashboard.setDashboardDashlets(null);
				//
				// dashboardCrudDAO.saveDashboard(dashboard, dashboardRoleAssociations,
				// dashboardDashlets);
				//
				// dashboard.setDashboardRoles(dashboardRoleAssociations);
				// dashboard.setDashboardDashlets(dashboardDashlets);
				// DashboardVO dashboardVO =
				// dashboardCrudService.convertDashboarEntityToVO(dashboard);
				// moduleVersionService.saveModuleVersion(dashboardVO, null,
				// dashboard.getDashboardId(), tableName,
				// Constant.IMPORT_SOURCE_VERSION_TYPE);
			} else if (Constant.MasterModuleType.NOTIFICATION.getModuleType().equalsIgnoreCase(moduleType)) {
				GenericUserNotification	notification	= g.fromJson(entityString, GenericUserNotification.class);
				GenericUserNotification existingObject = notificationDao
						.getNotificationDetails(notification.getNotificationId());
				if(isOnLoad == false  || existingObject == null 
						|| (isOnLoad && existingObject.getIsCustomUpdated() != null && existingObject.getIsCustomUpdated() == 0)) {
					if(isOnLoad == true) notification.setIsCustomUpdated(0);
					
					Long					existingObj		= notificationDao
							.getNotificationDetailsCount(notification.getNotificationId());
					if (existingObj == null || (existingObj != null && existingObj == 0)) {
						notification.setCreatedBy(user);
						notification.setCreationDate(date);
					}
					notification.setUpdatedBy(user);
					notification.setUpdatedDate(date);
	
					notificationDao.saveGenericUserNotification(notification);
					GenericUserNotificationJsonVO vo = convertNotificationEntityToVO(notification);
					moduleVersionService.saveModuleVersion(vo, null, notification.getNotificationId(), tableName,
						Constant.IMPORT_SOURCE_VERSION_TYPE);
				}
			} else if (Constant.MasterModuleType.DYNAREST.getModuleType().equalsIgnoreCase(moduleType)) {
				JwsDynamicRestDetail			dynarest					= g.fromJson(entityString,
						JwsDynamicRestDetail.class);
				JwsDynamicRestDetail existingObject = jwsDynarestDAO.getDynamicRestDetailsByName(dynarest.getJwsMethodName());
				if(isOnLoad == false || existingObject == null  
						|| (isOnLoad && existingObject.getIsCustomUpdated() != null && existingObject.getIsCustomUpdated() == 0)) {
					if(isOnLoad == true) dynarest.setIsCustomUpdated(0);
					
					List<JwsDynamicRestDaoDetail>	jwsDynamicRestDaoDetails	= dynarest.getJwsDynamicRestDaoDetails();
					dynarest.setJwsDynamicRestDaoDetails(null);
	
					jwsDynarestDAO.saveDynaRestDetail(dynarest, jwsDynamicRestDaoDetails);
	
					dynarest.setJwsDynamicRestDaoDetails(jwsDynamicRestDaoDetails);
					RestApiDetailsJsonVO vo = convertDynaRestEntityToVO(dynarest);
					moduleVersionService.saveModuleVersion(vo, null, dynarest.getJwsDynamicRestId(), tableName,
						Constant.IMPORT_SOURCE_VERSION_TYPE);
				}
			} else if (Constant.MasterModuleType.FILEMANAGER.getModuleType().equalsIgnoreCase(moduleType)) {
				FileUploadConfigImportEntity	fileConfigImportEntity	= g.fromJson(entityString,
						FileUploadConfigImportEntity.class);
				FileUploadConfig				fileConfig				= fileConfigImportEntity.getFileUploadConfig();
				FileUploadConfig existingObject = fileUploadConfigDAO.getFileUploadConfig(fileConfig.getFileBinId());
				if(isOnLoad == false || existingObject == null
						|| (isOnLoad && existingObject.getIsCustomUpdated() != null && existingObject.getIsCustomUpdated() == 0)) {
					if(isOnLoad == true) fileConfig.setIsCustomUpdated(0);
					
					fileConfig.setLastUpdatedBy(user);
					fileConfig.setLastUpdatedTs(date);
					if (fileConfig != null) {
						fileUploadConfigDAO.saveFileUploadConfig(fileConfig);
					}
	
					saveAndUploadFiles(fileConfigImportEntity.getFileUpload(), Constant.FILE_UPLOAD_DIRECTORY_NAME,
						fileConfig.getFileBinId());
				}
			} else if (Constant.MasterModuleType.PERMISSION.getModuleType().equalsIgnoreCase(moduleType)) {
				JwsEntityRoleAssociation role = g.fromJson(entityString, JwsEntityRoleAssociation.class);
				JwsEntityRoleAssociation existingObject = jwsEntityRoleAssociationDAO.findPermissionById(role.getEntityRoleId());
				if(isOnLoad == false || existingObject == null   
						|| (isOnLoad && existingObject.getIsCustomUpdated() != null && existingObject.getIsCustomUpdated() == 0)) {
					if(isOnLoad == true) role.setIsCustomUpdated(0);
					
					role.setLastUpdatedBy(user);
					role.setLastUpdatedDate(date);
	
					jwsEntityRoleAssociationDAO.saveJwsEntityRoleAssociation(role);
				}
			} else if (Constant.MasterModuleType.SITELAYOUT.getModuleType().equalsIgnoreCase(moduleType)) {
				ModuleListing			module				= g.fromJson(entityString, ModuleListing.class);
				ModuleListing existingObject = moduleDAO.findModuleListingById(module.getModuleId());
				if(isOnLoad == false || existingObject == null
						|| (isOnLoad && existingObject.getIsCustomUpdated() != null && existingObject.getIsCustomUpdated() == 0)) {
					if(isOnLoad == true) module.setIsCustomUpdated(0);
					
					List<ModuleListingI18n>	moduleListingI18ns	= module.getModuleListingI18ns();
					module.setModuleListingI18ns(null);
					moduleDAO.saveModuleListing(module, moduleListingI18ns);
				}
			} else if (Constant.MasterModuleType.APPLICATIONCONFIGURATION.getModuleType()
					.equalsIgnoreCase(moduleType)) {
				PropertyMaster propertyMaster = g.fromJson(entityString, PropertyMaster.class);
				PropertyMaster existingObject = propertyMasterService.findPropertyMasterByName(propertyMaster.getPropertyName());
				if(isOnLoad == false || existingObject == null   
						|| (isOnLoad && existingObject.getIsCustomUpdated() != null && existingObject.getIsCustomUpdated() == 0)) {
					if(isOnLoad == true) propertyMaster.setIsCustomUpdated(0);
					
					propertyMasterDAO.savePropertyMaster(propertyMaster);
					PropertyMasterJsonVO vo = convertPropertyMasterEntityToVO(propertyMaster);
					moduleVersionService.saveModuleVersion(vo, null, propertyMaster.getPropertyMasterId(), tableName,
						Constant.IMPORT_SOURCE_VERSION_TYPE);
				}
			} else if (Constant.MasterModuleType.MANAGEUSERS.getModuleType().equalsIgnoreCase(moduleType)) {
				JwsUser jwsUser = g.fromJson(entityString, JwsUser.class);
				JwsUser existingObject = userManagementDAO.findJwsUserById(jwsUser.getUserId());
				if(isOnLoad == false || existingObject == null   
						|| (isOnLoad && existingObject.getIsCustomUpdated() != null && existingObject.getIsCustomUpdated() == 0)) {
					if(isOnLoad == true) jwsUser.setIsCustomUpdated(0);
				
					userManagementDAO.saveJwsUser(jwsUser);
				}
			} else if (Constant.MasterModuleType.MANAGEROLES.getModuleType().equalsIgnoreCase(moduleType)) {
				JwsRole role = g.fromJson(entityString, JwsRole.class);
				JwsRole existingObject = userManagementDAO.findJwsRoleById(role.getRoleId());
				if(isOnLoad == false || existingObject == null   
						|| (isOnLoad && existingObject.getIsCustomUpdated() != null && existingObject.getIsCustomUpdated() == 0)) {
					if(isOnLoad == true) role.setIsCustomUpdated(0);
				
					userManagementDAO.saveJwsRole(role);
				}
			} else if (Constant.MasterModuleType.TEMPLATES.getModuleType().equalsIgnoreCase(moduleType)) {
				TemplateMaster	template	= g.fromJson(entityString, TemplateMaster.class);
				TemplateMaster existingObject = templateDAO.findTemplateById(template.getTemplateId());
				if(isOnLoad == false || existingObject == null   
						|| (isOnLoad && existingObject.getIsCustomUpdated() != null && existingObject.getIsCustomUpdated() == 0)) {
					if(isOnLoad == true) template.setIsCustomUpdated(0);
					
					Long			existingObj	= templateDAO.getTemplateCount(template.getTemplateId());
					if (existingObj == null || (existingObj != null && existingObj == 0)) {
						template.setCreatedBy(user);
					}
					template.setUpdatedBy(user);
					template.setUpdatedDate(date);
					if (template.getTemplateId() == null
							|| templateDAO.findTemplateById(template.getTemplateId()) == null) {
						templateDAO.saveTemplateData(template);
					} else {
						templateDAO.saveVelocityTemplateData(template);
					}
	
					TemplateVO templateVO = new TemplateVO(template.getTemplateId(), template.getTemplateName(),
							template.getTemplate(), new Date());
					moduleVersionService.saveModuleVersion(templateVO, null, template.getTemplateId(), tableName,
						Constant.IMPORT_SOURCE_VERSION_TYPE);
				}
			} else if (Constant.MasterModuleType.DASHLET.getModuleType().equalsIgnoreCase(moduleType)) {
				// Dashlet dashlet = g.fromJson(entityString, Dashlet.class);
				//
				// Long existingObj = dashboardDao.getDashletsCount(dashlet.getDashletId());
				// if (existingObj == null || (existingObj != null && existingObj == 0)) {
				// dashlet.setCreatedBy(user);
				// dashlet.setCreatedDate(date);
				// }
				// dashlet.setUpdatedBy(user);
				// dashlet.setLastUpdatedTs(date);
				// dashboardDao.saveDashlet(dashlet);
				//
				// DashletVO dashletVO =
				// dashletDownloadUploadModule.convertDashletEntityToVO(dashlet);
				// moduleVersionService.saveModuleVersion(dashletVO, null,
				// dashlet.getDashletId(), tableName,
				// Constant.IMPORT_SOURCE_VERSION_TYPE);
			} else if (Constant.MasterModuleType.DYNAMICFORM.getModuleType().equalsIgnoreCase(moduleType)) {
				DynamicForm	dynamicForm	= g.fromJson(entityString, DynamicForm.class);
				DynamicForm existingObject = dynamicFormCrudDAO.findDynamicFormById(dynamicForm.getFormId());
				if(isOnLoad == false || existingObject == null   
						|| (isOnLoad && existingObject.getIsCustomUpdated() != null && existingObject.getIsCustomUpdated() == 0)) {
					if(isOnLoad == true) dynamicForm.setIsCustomUpdated(0);
					
					Long		existingObj	= dynamicFormCrudDAO.getDynamicFormCount(dynamicForm.getFormId());
					if (existingObj == null || (existingObj != null && existingObj == 0)) {
						dynamicForm.setCreatedBy(user);
						dynamicForm.setCreatedDate(date);
					}
					List<DynamicFormSaveQuery> dynamicFormSaveQueries = dynamicForm.getDynamicFormSaveQueries();
					dynamicFormCrudDAO.saveDynamicForm(dynamicForm);
	
					dynamicForm.setDynamicFormSaveQueries(dynamicFormSaveQueries);
					DynamicFormVO dynamicFormVO = dynamicFormDownloadUploadModule.convertEntityToVO(dynamicForm);
					moduleVersionService.saveModuleVersion(dynamicFormVO, null, dynamicForm.getFormId(), tableName,
							Constant.IMPORT_SOURCE_VERSION_TYPE);
				}
			} else if (Constant.MasterModuleType.HELPMANUAL.getModuleType().equalsIgnoreCase(moduleType)) {
				HelpManual helpManual = g.fromJson(entityString, HelpManual.class);

				helpManualDAO.saveManualType(helpManual.getManualType());

				for (ManualEntryDetails med : helpManual.getManualEntryDetails()) {
					helpManualDAO.saveManualEntryDetails(med);
				}
				if (helpManual.getFileUploadConfig() != null) {
					fileUploadConfigDAO.saveFileUploadConfig(helpManual.getFileUploadConfig());
				}
				saveAndUploadFiles(helpManual.getFileUpload(), Constant.HELP_MANUAL_DIRECTORY_NAME,
						helpManual.getManualType().getName());
			} else if (Constant.MasterModuleType.ADDITIONALDATASOURCE.getModuleType().equalsIgnoreCase(moduleType)) {
				AdditionalDatasource additionalDatasource = g.fromJson(entityString, AdditionalDatasource.class);
				AdditionalDatasource existingObject = additionalDatasourceDAO.findAdditionalDatasourceById(additionalDatasource.getAdditionalDatasourceId());
				if(isOnLoad == false || existingObject == null   
						|| (isOnLoad && existingObject.getIsCustomUpdated() != null && existingObject.getIsCustomUpdated() == 0)) {
					if(isOnLoad == true) additionalDatasource.setIsCustomUpdated(0);
					
					additionalDatasourceDAO.saveAdditionalDatasource(additionalDatasource);
				}
			} else if (Constant.MasterModuleType.APICLIENTDETAILS.getModuleType().equalsIgnoreCase(moduleType)) {
				ApiClientDetails apiClientDetails = g.fromJson(entityString, ApiClientDetails.class);
				ApiClientDetails existingObject = apiClientDetailsDAO.findApiClientDetailsById(apiClientDetails.getClientId());
				if(isOnLoad == false || existingObject == null   
						|| (isOnLoad && existingObject.getIsCustomUpdated() != null && existingObject.getIsCustomUpdated() == 0)) {
					if(isOnLoad == true) apiClientDetails.setIsCustomUpdated(0);
					
					apiClientDetailsDAO.saveAdditionalDatasource(apiClientDetails);
				}
			} else if (Constant.MasterModuleType.SCHEDULER.getModuleType().equalsIgnoreCase(moduleType)) {
				JqScheduler scheduler = g.fromJson(entityString, JqScheduler.class);
				JqScheduler existingObject = schedulerDAO.findJqSchedulerById(scheduler.getSchedulerId());
				if(isOnLoad == false || existingObject == null   
						|| (isOnLoad && existingObject.getIsCustomUpdated() != null && existingObject.getIsCustomUpdated() == 0)) {
					if(isOnLoad == true) scheduler.setIsCustomUpdated(0);
					
					schedulerDAO.saveScheduler(scheduler);
				}
			}
		} catch (Exception exp) {
			exp.printStackTrace();
			logger.error("Error while importing entity of id: " + importId + ", module type: " + moduleType);
//			throw new Exception("Error while importing entity of id: " + importId + ", module type: " + moduleType);
		}
	}

	private JwsEntityRoleAssociationVO convertJwsEntityRoleAssociationEntityToVO(
			JwsEntityRoleAssociation entityRoleAssociation) {
		JwsEntityRoleAssociationVO vo = new JwsEntityRoleAssociationVO();
		return vo.convertEntityToVO(entityRoleAssociation);
	}

	private GridDetailsJsonVO convertGridEntityToVO(GridDetails grid) {
		GridDetailsJsonVO vo = new GridDetailsJsonVO();
		vo.setEntityName("jq_grid_details");
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

		vo.setEntityName("jq_generic_user_notification");
		vo.setFormId("e848b04c-f19b-11ea-9304-f48e38ab9348");
		vo.setFromDate(notitification.getMessageValidFrom());
		vo.setMessageText(notitification.getMessageText());
		vo.setMessageType(notitification.getMessageType());
		vo.setNotificationId(notitification.getNotificationId());
		vo.setPrimaryKey(notitification.getNotificationId());
		vo.setSelectionCriteria(notitification.getSelectionCriteria());
		vo.setToDate(notitification.getMessageValidTill());

		return vo;
	}

	private RestApiDetailsJsonVO convertDynaRestEntityToVO(JwsDynamicRestDetail dynaRest) throws Exception {
		RestApiDetailsJsonVO vo = new RestApiDetailsJsonVO();

		vo.setAllowFiles(dynaRest.getJwsAllowFiles() != null ? dynaRest.getJwsAllowFiles().toString() : "");

		JSONArray	daoDetailsId		= new JSONArray();
		JSONArray	queryDetails		= new JSONArray();
		JSONArray	variableNameDetails	= new JSONArray();
		JSONArray	queryTypes			= new JSONArray();
		if (dynaRest.getJwsDynamicRestDaoDetails() != null)
			for (JwsDynamicRestDaoDetail dao : dynaRest.getJwsDynamicRestDaoDetails()) {
				daoDetailsId.put(dao.getJwsDaoDetailsId());
				queryDetails.put(dao.getJwsDaoQueryTemplate());
				variableNameDetails.put(dao.getJwsResultVariableName());
				queryTypes.put(dao.getQueryType());
			}
		vo.setDaoDetailsIds(daoDetailsId.toString());
		vo.setDaoQueryDetails(queryDetails.toString());
		vo.setVariableName(variableNameDetails.toString());
		vo.setQueryType(queryTypes.toString());

		vo.setSaveUpdateQuery("");

		vo.setDynarestId(dynaRest.getJwsDynamicRestId());
		vo.setDynarestMethodDescription(dynaRest.getJwsMethodDescription());
		vo.setDynarestMethodName(dynaRest.getJwsMethodName());
		vo.setDynarestPlatformId(dynaRest.getJwsPlatformId() != null ? dynaRest.getJwsPlatformId().toString() : "");
		vo.setDynarestProdTypeId(
				dynaRest.getJwsResponseProducerTypeId() != null ? dynaRest.getJwsResponseProducerTypeId().toString()
						: "");
		vo.setDynarestRequestTypeId(
				dynaRest.getJwsRequestTypeId() != null ? dynaRest.getJwsRequestTypeId().toString() : "");
		vo.setDynarestUrl(dynaRest.getJwsDynamicRestUrl());
		vo.setEntityName("jq_dynamic_rest_details");
		vo.setFormId("8a80cb81749ab40401749ac2e7360000");
		vo.setIsEdit(dynaRest.getJwsRbacId() != null ? dynaRest.getJwsRbacId().toString() : "");
		vo.setPrimaryKey(dynaRest.getJwsDynamicRestId());
		vo.setServiceLogic(dynaRest.getJwsServiceLogic());

		return vo;
	}

	private PropertyMasterJsonVO convertPropertyMasterEntityToVO(PropertyMaster propertyMaster) {
		PropertyMasterJsonVO vo = new PropertyMasterJsonVO();

		vo.setEntityName("jq_property_master");
		vo.setFormId("8a80cb8174bf3b360174bfae9ac80006");
		vo.setAppVersion(propertyMaster.getAppVersion());
		vo.setComment(propertyMaster.getComments());
		vo.setOwnerId(propertyMaster.getOwnerId());
		vo.setOwnerType(propertyMaster.getOwnerType());
		vo.setPrimaryKey(propertyMaster.getPropertyMasterId());
		vo.setPropertyMasterId(propertyMaster.getPropertyMasterId());
		vo.setPropertyName(propertyMaster.getPropertyName());
		vo.setPropertyValue(propertyMaster.getPropertyValue());

		return vo;
	}

	private SchedulerVO convertSchedulerEntityToVO(JqScheduler scheduler) {
		SchedulerVO vo = new SchedulerVO();

		vo.setCronScheduler(scheduler.getCronScheduler());
		vo.setFailedNotificationParamJson(scheduler.getFailedNotificationParamJson());
		vo.setHeaderJson(scheduler.getHeaderJson());
		vo.setIsActive(scheduler.getIsActive());
		vo.setJwsDynamicRestId(scheduler.getJwsDynamicRestId());
		vo.setModifiedBy(scheduler.getModifiedBy());
		vo.setModifiedDate(scheduler.getModifiedDate());
		vo.setRequestParamJson(scheduler.getRequestParamJson());
		vo.setScheduler_name(scheduler.getScheduler_name());
		vo.setSchedulerId(scheduler.getSchedulerId());
		vo.setSchedulerTypeId(scheduler.getSchedulerTypeId());

		return vo;
	}

	private void saveAndUploadFiles(List<FileUpload> fileUploadList, String parentFolderName, String moduleName)
			throws Exception {
		LocalDate		localDate		= LocalDate.now();
		Integer			year			= localDate.getYear();
		Integer			month			= localDate.getMonthValue();
		Integer			todayDate		= localDate.getDayOfMonth();
		String			fileUploadDir	= propertyMasterService.findPropertyMasterValue("file-upload-location");
		StringJoiner	location		= new StringJoiner("" + File.separatorChar);
		location.add(fileUploadDir);
		location.add(year.toString()).add(month.toString()).add(todayDate.toString());
		if (Boolean.FALSE.equals(new File(location.toString()).exists()) && !fileUploadList.isEmpty()) {
			Files.createDirectories(Paths.get(location.toString()));
		}
		Path root = Paths.get(location.toString());

		for (FileUpload fileUpload : fileUploadList) {
			String manualPath = unZipFilePath + File.separator + parentFolderName + File.separator + moduleName
					+ File.separator + fileUpload.getPhysicalFileName();
			if (!new File(manualPath).exists()) {
				throw new RuntimeException("File Not found!!: " + manualPath);
			}

			FileInputStream fis = new FileInputStream(new File(manualPath));

			Files.deleteIfExists(root.resolve(fileUpload.getPhysicalFileName()));
			Files.copy(fis, root.resolve(fileUpload.getPhysicalFileName()));
			fileUpload.setFilePath(location.toString());
			ifileUploadRepository.save(fileUpload);
		}
	}

	public String getAdditionalDatasourceJson(String entityId) throws Exception {
		AdditionalDatasource	ads			= additionalDatasourceRepository.findById(entityId).orElse(null);
		String					jsonString	= "";
		if (ads != null) {
			ads = ads.getObject();

			AdditionalDatasourceVO	vo				= ads.convertEntityToVO(ads);
			Gson					gson			= new Gson();
			ObjectMapper			objectMapper	= new ObjectMapper();
			String					dbDateFormat	= propertyMasterService.getDateFormatByName(
					Constant.PROPERTY_MASTER_OWNER_TYPE, Constant.PROPERTY_MASTER_OWNER_ID,
					Constant.JWS_DATE_FORMAT_PROPERTY_NAME,
					com.trigyn.jws.dbutils.utils.Constant.JWS_JAVA_DATE_FORMAT_PROPERTY_NAME);
			DateFormat				dateFormat		= new SimpleDateFormat(dbDateFormat);
			objectMapper.setDateFormat(dateFormat);
			Map<String, Object> objectMap = objectMapper.convertValue(vo, TreeMap.class);
			jsonString = gson.toJson(objectMap);

		}
		return jsonString;
	}

	public String getApiClientDetailseJson(String entityId) throws Exception {
		ApiClientDetails	acd			= apiClientDetailsRepository.findById(entityId).orElse(null);
		String				jsonString	= "";
		if (acd != null) {
			acd = acd.getObject();

			ApiClientDetailsVO	vo				= acd.convertEntityToVO(acd);
			Gson				gson			= new Gson();
			ObjectMapper		objectMapper	= new ObjectMapper();
			String				dbDateFormat	= propertyMasterService.getDateFormatByName(
					Constant.PROPERTY_MASTER_OWNER_TYPE, Constant.PROPERTY_MASTER_OWNER_ID,
					Constant.JWS_DATE_FORMAT_PROPERTY_NAME,
					com.trigyn.jws.dbutils.utils.Constant.JWS_JAVA_DATE_FORMAT_PROPERTY_NAME);
			DateFormat			dateFormat		= new SimpleDateFormat(dbDateFormat);
			objectMapper.setDateFormat(dateFormat);
			Map<String, Object> objectMap = objectMapper.convertValue(vo, TreeMap.class);
			jsonString = gson.toJson(objectMap);

		}
		return jsonString;
	}

	public String getSchedulerJson(String entityId) throws Exception {
		JqScheduler	scheduler	= schedulerRepository.findById(entityId).orElse(null);
		String		jsonString	= "";
		if (scheduler != null) {
			scheduler = scheduler.getObject();

			SchedulerVO		vo				= convertSchedulerEntityToVO(scheduler);
			Gson			gson			= new Gson();
			ObjectMapper	objectMapper	= new ObjectMapper();
			String			dbDateFormat	= propertyMasterService.getDateFormatByName(
					Constant.PROPERTY_MASTER_OWNER_TYPE, Constant.PROPERTY_MASTER_OWNER_ID,
					Constant.JWS_DATE_FORMAT_PROPERTY_NAME,
					com.trigyn.jws.dbutils.utils.Constant.JWS_JAVA_DATE_FORMAT_PROPERTY_NAME);
			DateFormat		dateFormat		= new SimpleDateFormat(dbDateFormat);
			objectMapper.setDateFormat(dateFormat);
			Map<String, Object> objectMap = objectMapper.convertValue(vo, TreeMap.class);
			jsonString = gson.toJson(objectMap);

		}
		return jsonString;
	}

	public void importFileOnLoad(File file, boolean isDevMode) {
		try {
			Map<String, Object>	map				= importFile(file, isDevMode);

			MetadataXMLVO		metadataXmlvo	= (MetadataXMLVO) map.get("metadataVO");
			String				unZipFilePath	= (String) map.get("unZipFilePath");

			String				jsonArray		= getJsonArrayFromMetadataXMLVO(metadataXmlvo);
			Map<String, Object>	zipFileDataMap	= getXMLJsonDataMap(metadataXmlvo, unZipFilePath);

			Gson				gson			= new GsonBuilder().create();

			zipFileDataMap.put("completeZipJsonData", jsonArray);
			Map<String, String> versionMap = getLatestVersion(zipFileDataMap);
			zipFileDataMap.put("versionMap", gson.toJson(versionMap));

			Map<String, Boolean> crcMap = getLatestCRC(zipFileDataMap);
			zipFileDataMap.put("crcMap", gson.toJson(crcMap));
			String jsonString = gson.toJson(zipFileDataMap);
			importAll(jsonString, null, true);
		} catch (Exception a_exception) {a_exception.printStackTrace();
			logger.error("Error ", a_exception);
		}
	}

}
