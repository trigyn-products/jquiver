package com.trigyn.jws.webstarter.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trigyn.jws.dashboard.entities.Dashboard;
import com.trigyn.jws.dashboard.entities.DashboardRoleAssociation;
import com.trigyn.jws.dashboard.entities.Dashlet;
import com.trigyn.jws.dashboard.service.DashletModule;
import com.trigyn.jws.dashboard.utility.Constants;
import com.trigyn.jws.dbutils.entities.AdditionalDatasource;
import com.trigyn.jws.dbutils.entities.ModuleListing;
import com.trigyn.jws.dbutils.entities.PropertyMaster;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dbutils.vo.xml.MetadataXMLVO;
import com.trigyn.jws.dbutils.vo.xml.Modules;
import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.dynamicform.entities.ManualType;
import com.trigyn.jws.dynamicform.service.DynamicFormModule;
import com.trigyn.jws.dynarest.entities.FileUpload;
import com.trigyn.jws.dynarest.entities.FileUploadConfig;
import com.trigyn.jws.dynarest.entities.JqApiClientDetails;
import com.trigyn.jws.dynarest.entities.JqScheduler;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDetail;
import com.trigyn.jws.dynarest.repository.FileUploadRepository;
import com.trigyn.jws.dynarest.service.DynaRestModule;
import com.trigyn.jws.gridutils.entities.GridDetails;
import com.trigyn.jws.resourcebundle.entities.ResourceBundle;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibraryDetails;
import com.trigyn.jws.templating.entities.TemplateMaster;
import com.trigyn.jws.templating.service.TemplateModule;
import com.trigyn.jws.typeahead.entities.Autocomplete;
import com.trigyn.jws.usermanagement.entities.JwsEntityRoleAssociation;
import com.trigyn.jws.usermanagement.entities.JwsRole;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.repository.JwsEntityRoleAssociationRepository;
import com.trigyn.jws.usermanagement.vo.JwsEntityRoleAssociationVO;
import com.trigyn.jws.webstarter.dao.CrudQueryStore;
import com.trigyn.jws.webstarter.dao.ImportExportCrudDAO;
import com.trigyn.jws.webstarter.utils.Constant;
import com.trigyn.jws.webstarter.utils.FileImportExportModule;
import com.trigyn.jws.webstarter.utils.FileUploadExportModule;
import com.trigyn.jws.webstarter.utils.FileUtil;
import com.trigyn.jws.webstarter.utils.HelpManualImportExportModule;
import com.trigyn.jws.webstarter.utils.ImportExportUtility;
import com.trigyn.jws.webstarter.utils.XMLUtil;
import com.trigyn.jws.webstarter.utils.ZipUtil;
import com.trigyn.jws.webstarter.vo.GenericUserNotification;
import com.trigyn.jws.webstarter.xml.AdditionalDatasourceXMLVO;
import com.trigyn.jws.webstarter.xml.ApiClientDetailsXMLVO;
import com.trigyn.jws.webstarter.xml.AutocompleteXMLVO;
import com.trigyn.jws.webstarter.xml.DashboardXMLVO;
import com.trigyn.jws.webstarter.xml.FileUploadXMLVO;
import com.trigyn.jws.webstarter.xml.GenericUserNotificationXMLVO;
import com.trigyn.jws.webstarter.xml.GridXMLVO;
import com.trigyn.jws.webstarter.xml.PermissionXMLVO;
import com.trigyn.jws.webstarter.xml.PropertyMasterXMLVO;
import com.trigyn.jws.webstarter.xml.ResourceBundleXMLVO;
import com.trigyn.jws.webstarter.xml.RoleXMLVO;
import com.trigyn.jws.webstarter.xml.SchedulerXMLVO;
import com.trigyn.jws.webstarter.xml.ScriptLibraryXMLVO;
import com.trigyn.jws.webstarter.xml.SiteLayoutXMLVO;
import com.trigyn.jws.webstarter.xml.UserXMLVO;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional(readOnly = false)
public class ExportService {

	private final static Logger logger = LogManager.getLogger(ExportService.class);

	@Autowired
	private ImportExportCrudDAO importExportCrudDAO = null;

	@Autowired
	private TemplateModule templateDownloadUploadModule = null;

	@Autowired
	private DynaRestModule dynaRestModule = null;

	@Autowired
	@Qualifier("dynamic-form")
	private DynamicFormModule dynamicFormDownloadUploadModule = null;

	@Autowired
	@Qualifier("dashlet")
	private DashletModule dashletDownloadUploadModule = null;

	@Autowired
	private DashboardCrudService dashboardCrudService = null;

	@Autowired
	private PropertyMasterDAO propertyMasterDAO = null;

	private Map<String, String> moduleListMap = null;

	@Autowired
	private IUserDetailsService detailsService = null;

	@Autowired
	private HelpManualImportExportModule helpManualImportExportModule = null;

	@Autowired
	private FileUploadExportModule fileUploadExportModule = null;

	@Autowired
	private FileImportExportModule fileImportExportModule = null;

	private String version = null;

	private String userName = null;

	@Autowired
	private FileUploadRepository fileUploadRepository = null;

	@Autowired
	private ImportExportUtility importExportUtility = null;

	@Autowired
	private JwsEntityRoleAssociationRepository entityRoleAssociationRepository = null;

	public List<Map<String, Object>> getAllCustomEntity() {
		return importExportCrudDAO.getAllCustomEntity();
	}

	public List<Map<String, Object>> getCustomEntityCount() {
		return importExportCrudDAO.getCustomEntityCount();
	}

	public List<Map<String, Object>> getAllEntityCount() {
		return importExportCrudDAO.getAllEntityCount();
	}

	public String exportConfigData(HttpServletRequest request, HttpServletResponse response, Map<String, String> out,
			boolean isExportToLocal) throws Exception {
		try {
			version = propertyMasterDAO.findPropertyMasterValue("system", "system", "version");
			UserDetailsVO detailsVO = detailsService.getUserDetails();
			userName = detailsVO.getUserName();

			String				systemPath		= System.getProperty("java.io.tmpdir");
			moduleListMap = new HashMap<>();

			Map<String, XMLVO>	xmlVOMap			= new HashMap<>();
			String				targetLocation	= null;
			JSONArray existingLocalData = new JSONArray();
			if (isExportToLocal == true) {
				targetLocation = propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
				
				MetadataXMLVO metadataXMLVO = importExportUtility.readMetaDataXML(targetLocation);
				if(metadataXMLVO != null && metadataXMLVO.getInfo() != null && metadataXMLVO.getInfo() != "" ) {
					existingLocalData = new JSONArray(metadataXMLVO.getInfo());
				}
				
				xmlVOMap = importExportUtility.readFiles(targetLocation, metadataXMLVO, moduleListMap);
			}
			
			String tempDownloadPath = FileUtil.generateTemporaryFilePath(Constant.EXPORTTEMPPATH,
					UUID.randomUUID().toString());
			new File(tempDownloadPath).mkdir();

			Map<String, List<String>>	exportTableMap	= new HashMap<>();
			String						htmlTableJSON	= "";
			for (Entry<String, String> obj : out.entrySet()) {
				String	moduleType	= obj.getKey();
				String	exportData	= obj.getValue();

				if ("htmlTableJSON".equals(moduleType)) {
					htmlTableJSON = StringEscapeUtils.unescapeXml("<![CDATA[" + exportData + "]]>");
					if (exportData != null && exportData != "") {
						JSONArray jsonArray = new JSONArray(exportData);
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject		explrObject	= jsonArray.getJSONObject(i);
							String			type		= explrObject.getString("moduleType");

							List<String>	list		= new ArrayList<>();
							if (exportTableMap.containsKey(type.toUpperCase())) {
								list = exportTableMap.get(type.toUpperCase());
							}
							list.add(explrObject.getString("moduleID"));
							exportTableMap.put(type.toUpperCase(), list);
						}
						
						if(isExportToLocal == true) {
							List<String> commonElementList = new ArrayList<>();

							for (int i = 0; i < existingLocalData.length(); i++) {
								String modType = existingLocalData.getJSONObject(i).getString("moduleType");
								String modId = existingLocalData.getJSONObject(i).getString("moduleID");
								for (int jCounter = 0; jCounter < jsonArray.length(); jCounter++) {
									if(modType.equals(jsonArray.getJSONObject(jCounter).getString("moduleType")) 
											&& modId.equals(jsonArray.getJSONObject(jCounter).getString("moduleID"))) {
										commonElementList.add(modType+":"+modId);
										break;
									}
								}
							}
							
							for (int i = 0; i < existingLocalData.length(); i++) { 
								JSONObject explrObject = existingLocalData.getJSONObject(i); 
								String modType = existingLocalData.getJSONObject(i).getString("moduleType");
								String modId = existingLocalData.getJSONObject(i).getString("moduleID");
								
								if(commonElementList.contains(modType+":"+modId) == false) {
									jsonArray.put(explrObject);
								}
							}
							
							htmlTableJSON = StringEscapeUtils.unescapeXml("<![CDATA[" + jsonArray.toString() + "]]>");
						}
					}
				}
			}

			for (Entry<String, String> obj : out.entrySet()) {
				XMLVO	xmlVO		= null;
				String	moduleType	= obj.getKey();
				String	exportData	= obj.getValue();

				if (!"htmlTableJSON".equals(moduleType)) {
					JSONObject jsonObject = new JSONObject(exportData);
					xmlVO = retrieveDBData(moduleType, jsonObject, tempDownloadPath, exportTableMap, xmlVOMap);
				}

				if (xmlVO != null)
					XMLUtil.marshaling(xmlVO, moduleType, tempDownloadPath);
			}

			XMLUtil.generateMetadataXML(moduleListMap, null, tempDownloadPath, version, userName, htmlTableJSON, null);

			if (isExportToLocal == false) {
				String zipFilePath = ZipUtil.zipDirectory(tempDownloadPath, systemPath);
				return zipFilePath;
			} else {
				return FileUtil.exportToLocal(tempDownloadPath, targetLocation);
			}
		} catch (Exception a_excep) {
			logger.error("Error while exporting the configuration ", a_excep);
			if (a_excep.getMessage() != null && a_excep.getMessage().startsWith("Data mismatch while exporting")) {
				return "fail:" + a_excep.getMessage();
			} else {
				return "fail:" + "Error occurred while exporting";
			}
		}
	}

	public void downloadExport(HttpServletRequest request, HttpServletResponse response, String filePath)
			throws Exception {
		FileUtil.downloadFile(request, response, filePath);
	}

	private XMLVO retrieveDBData(String moduleType, JSONObject jsonObject, String downloadFolderLocation,
			Map<String, List<String>> exportTableMap, Map<String, XMLVO> xmlVOMap) throws Exception {

		List<String> systemConfigIncludeList = new ArrayList<String>();
		List<String> customConfigExcludeList = new ArrayList<String>();

		JSONArray jsonArray = (JSONArray) jsonObject.get("systemConfigIncludeList");
		for (int i = 0; i < jsonArray.length(); i++) {
			systemConfigIncludeList.add(jsonArray.getString(i));
		}

		jsonArray = (JSONArray) jsonObject.get("customConfigExcludeList");
		for (int i = 0; i < jsonArray.length(); i++) {
			customConfigExcludeList.add(jsonArray.getString(i));
		}
		if (customConfigExcludeList.size() == 0) {
			customConfigExcludeList.add("");
		}

		if (moduleType.equals(Constant.MasterModuleType.GRID.getModuleType())) {
			return retrieveGridExportData(systemConfigIncludeList, customConfigExcludeList, moduleType,
					exportTableMap.get(Constant.MasterModuleType.GRID.getModuleType().toUpperCase()),
					xmlVOMap.get(Constant.MasterModuleType.GRID.getModuleType().toLowerCase() + ".xml"));
		} else if (moduleType.equals(Constant.MasterModuleType.SCRIPTLIBRARY.getModuleType())) {
			return retrieveScriptLibraryExportData(systemConfigIncludeList, customConfigExcludeList, moduleType,
					exportTableMap.get(Constant.MasterModuleType.SCRIPTLIBRARY.getModuleType().toUpperCase()),
					xmlVOMap.get(Constant.MasterModuleType.SCRIPTLIBRARY.getModuleType().toLowerCase() + ".xml"));
		} else if (moduleType.equals(Constant.MasterModuleType.RESOURCEBUNDLE.getModuleType())) {
			return retrieveRBExportData(systemConfigIncludeList, customConfigExcludeList, moduleType,
					exportTableMap.get(Constant.MasterModuleType.RESOURCEBUNDLE.getModuleType().toUpperCase()),
					xmlVOMap.get(Constant.MasterModuleType.RESOURCEBUNDLE.getModuleType().toLowerCase() + ".xml"));
		} else if (moduleType.equals(Constant.MasterModuleType.AUTOCOMPLETE.getModuleType())) {
			return retrieveAutocompleteExportData(systemConfigIncludeList, customConfigExcludeList, moduleType,
					exportTableMap.get(Constant.MasterModuleType.AUTOCOMPLETE.getModuleType().toUpperCase()),
					xmlVOMap.get(Constant.MasterModuleType.AUTOCOMPLETE.getModuleType().toLowerCase() + ".xml"));
		} else if (moduleType.equals(Constant.MasterModuleType.NOTIFICATION.getModuleType())) {
			return retrieveNotificationExportData(customConfigExcludeList, moduleType,
					exportTableMap.get(Constant.MasterModuleType.NOTIFICATION.getModuleType().toUpperCase()),
					xmlVOMap.get(Constant.MasterModuleType.NOTIFICATION.getModuleType().toLowerCase() + ".xml"));
		} else if (moduleType.equals(Constant.MasterModuleType.DASHBOARD.getModuleType())) {
			return downloadDashboardExportData(systemConfigIncludeList, customConfigExcludeList, moduleType,
					exportTableMap.get(Constant.MasterModuleType.DASHBOARD.getModuleType().toUpperCase()),
					xmlVOMap.get(Constant.MasterModuleType.DASHBOARD.getModuleType().toLowerCase() + ".xml"));
		} else if (moduleType.equals(Constant.MasterModuleType.FILEMANAGER.getModuleType())) {
			return retrieveFileManagerExportData(systemConfigIncludeList, customConfigExcludeList,
					downloadFolderLocation, moduleType,
					exportTableMap.get(Constant.MasterModuleType.FILEMANAGER.getModuleType().toUpperCase()),
					xmlVOMap.get(Constant.MasterModuleType.FILEMANAGER.getModuleType().toLowerCase()));
		} else if (moduleType.equals(Constant.MasterModuleType.DYNAREST.getModuleType())) {
			return downloadDynaRestExportData(systemConfigIncludeList, customConfigExcludeList, downloadFolderLocation,
					moduleType, exportTableMap.get(Constant.MasterModuleType.DYNAREST.getModuleType().toUpperCase()),
					xmlVOMap.get(Constant.MasterModuleType.DYNAREST.getModuleType().toLowerCase()));
		} else if (moduleType.equals(Constant.MasterModuleType.PERMISSION.getModuleType())) {
			return retrievePermissionExportData(systemConfigIncludeList, moduleType,
					exportTableMap.get(Constant.MasterModuleType.PERMISSION.getModuleType().toUpperCase()),
					xmlVOMap.get(Constant.MasterModuleType.PERMISSION.getModuleType().toLowerCase() + ".xml"));
		} else if (moduleType.equals(Constant.MasterModuleType.SITELAYOUT.getModuleType())) {
			return retrieveSiteLayoutExportData(systemConfigIncludeList, customConfigExcludeList, moduleType,
					exportTableMap.get(Constant.MasterModuleType.SITELAYOUT.getModuleType().toUpperCase()),
					xmlVOMap.get(Constant.MasterModuleType.SITELAYOUT.getModuleType().toLowerCase() + ".xml"));
		} else if (moduleType.equals(Constant.MasterModuleType.APPLICATIONCONFIGURATION.getModuleType())) {
			return retrieveAppConfigExportData(systemConfigIncludeList, moduleType,
					exportTableMap
							.get(Constant.MasterModuleType.APPLICATIONCONFIGURATION.getModuleType().toUpperCase()),
					xmlVOMap.get(
							Constant.MasterModuleType.APPLICATIONCONFIGURATION.getModuleType().toLowerCase() + ".xml"));
		} else if (moduleType.equals(Constant.MasterModuleType.MANAGEUSERS.getModuleType())) {
			return retrieveManageUsersExportData(systemConfigIncludeList, moduleType,
					exportTableMap.get(Constant.MasterModuleType.MANAGEUSERS.getModuleType().toUpperCase()),
					xmlVOMap.get(Constant.MasterModuleType.MANAGEUSERS.getModuleType().toLowerCase() + ".xml"));
		} else if (moduleType.equals(Constant.MasterModuleType.MANAGEROLES.getModuleType())) {
			return retrieveManageRolesExportData(systemConfigIncludeList, moduleType,
					exportTableMap.get(Constant.MasterModuleType.MANAGEROLES.getModuleType().toUpperCase()),
					xmlVOMap.get(Constant.MasterModuleType.MANAGEROLES.getModuleType().toLowerCase() + ".xml"));
		} else if (moduleType.equals(Constant.MasterModuleType.TEMPLATES.getModuleType())) {
			return downloadTemplateExportData(systemConfigIncludeList, customConfigExcludeList, downloadFolderLocation,
					moduleType, exportTableMap.get(Constant.MasterModuleType.TEMPLATES.getModuleType().toUpperCase()),
					xmlVOMap.get(Constant.MasterModuleType.TEMPLATES.getModuleType().toLowerCase()));
		} else if (moduleType.equals(Constant.MasterModuleType.DASHLET.getModuleType())) {
			return downloadDashletExportData(systemConfigIncludeList, customConfigExcludeList, downloadFolderLocation,
					moduleType, exportTableMap.get(Constant.MasterModuleType.DASHLET.getModuleType().toUpperCase()),
					xmlVOMap.get(Constant.MasterModuleType.DASHLET.getModuleType().toLowerCase()));
		} else if (moduleType.equals(Constant.MasterModuleType.DYNAMICFORM.getModuleType())) {
			return downloadDynamicFormExportData(systemConfigIncludeList, customConfigExcludeList,
					downloadFolderLocation, moduleType,
					exportTableMap.get(Constant.MasterModuleType.DYNAMICFORM.getModuleType().toUpperCase()),
					xmlVOMap.get(Constant.MasterModuleType.DYNAMICFORM.getModuleType().toLowerCase()));
		} else if (moduleType.equals(Constant.MasterModuleType.HELPMANUAL.getModuleType())) {
			return downloadHelpManualExportData(systemConfigIncludeList, customConfigExcludeList,
					downloadFolderLocation, moduleType,
					exportTableMap.get(Constant.MasterModuleType.HELPMANUAL.getModuleType().toUpperCase()),
					xmlVOMap.get(Constant.MasterModuleType.HELPMANUAL.getModuleType().toLowerCase()));
		} else if (moduleType.equals(Constant.MasterModuleType.APICLIENTDETAILS.getModuleType())) {
			return downloadApiClientExportData(systemConfigIncludeList, customConfigExcludeList, downloadFolderLocation,
					moduleType,
					exportTableMap.get(Constant.MasterModuleType.APICLIENTDETAILS.getModuleType().toUpperCase()),
					xmlVOMap.get(Constant.MasterModuleType.APICLIENTDETAILS.getModuleType().toLowerCase() + ".xml"));
		} else if (moduleType.equals(Constant.MasterModuleType.ADDITIONALDATASOURCE.getModuleType())) {
			return downloadAdditionalDatasourceExportData(systemConfigIncludeList, customConfigExcludeList,
					downloadFolderLocation, moduleType,
					exportTableMap.get(Constant.MasterModuleType.ADDITIONALDATASOURCE.getModuleType().toUpperCase()),
					xmlVOMap.get(
							Constant.MasterModuleType.ADDITIONALDATASOURCE.getModuleType().toLowerCase() + ".xml"));
		} else if (moduleType.equals(Constant.MasterModuleType.SCHEDULER.getModuleType())) {
			return retrieveSchedulerExportData(systemConfigIncludeList, customConfigExcludeList, moduleType,
					exportTableMap.get(Constant.MasterModuleType.SCHEDULER.getModuleType().toUpperCase()),
					xmlVOMap.get(Constant.MasterModuleType.SCHEDULER.getModuleType().toLowerCase() + ".xml"));
		} else if (moduleType.equals(Constant.MasterModuleType.FILEIMPEXPDETAILS.getModuleType())) {
			return retrieveExportFilesData(systemConfigIncludeList, customConfigExcludeList, moduleType,
					exportTableMap.get(Constant.MasterModuleType.FILEIMPEXPDETAILS.getModuleType().toUpperCase()),
					downloadFolderLocation,
					xmlVOMap.get(Constant.MasterModuleType.FILEIMPEXPDETAILS.getModuleType().toLowerCase()));
		} else {
			return null;
		}
	}

	private XMLVO retrieveGridExportData(List<String> systemConfigIncludeList, List<String> customConfigExcludeList,
			String moduleType, List<String> exportedList, XMLVO xmlVO) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_GRID_DATA_FOR_EXPORT, systemConfigIncludeList, 2,
				customConfigExcludeList, 1);

		validate(exportableList, exportedList, "Grid");

		GridXMLVO gridXMLVO = (xmlVO == null) ? null : (GridXMLVO) xmlVO;

		
		if (exportableList != null && !exportableList.isEmpty()) {
			gridXMLVO = (gridXMLVO == null) ? new GridXMLVO() : gridXMLVO;
			for (Object obj : exportableList) {
				if (!exportedList.contains(((GridDetails) obj).getGridId())) {
					throw new Exception("Data mismatch while exporting Grid.");
				}
				Map<String, Integer> positionMap = new HashMap<>();
				if (gridXMLVO != null && gridXMLVO.getGridDetails().isEmpty() == false) {
					int counter = 0;
					for (GridDetails grid : gridXMLVO.getGridDetails()) {
						positionMap.put(grid.getGridId(), counter);
						counter = counter + 1;
					}
				}

				GridDetails gridDetails = ((GridDetails) obj).getObject();
				if (positionMap.containsKey(gridDetails.getGridId())) {
					List<GridDetails> moduleList = gridXMLVO.getGridDetails();
					int o = positionMap.get(((GridDetails) obj).getGridId());
					moduleList.remove(o);
					gridXMLVO.setGridDetails(moduleList);
				}
				gridXMLVO.getGridDetails().add(gridDetails);
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return gridXMLVO;
	}

	private XMLVO retrieveRBExportData(List<String> systemConfigIncludeList, List<String> customConfigExcludeList,
			String moduleType, List<String> exportedList, XMLVO xmlVO) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getRBExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_RESOURCE_BUNDLE_DATA_FOR_EXPORT, systemConfigIncludeList,
				customConfigExcludeList);

		validate(exportableList, exportedList, "Resource Bundle");

		ResourceBundleXMLVO resourceBundleXMLVO = (xmlVO == null) ? null : (ResourceBundleXMLVO) xmlVO;
		Map<String, Integer> positionMap = new HashMap<>();
		if (resourceBundleXMLVO != null && resourceBundleXMLVO.getResourceBundleDetails().isEmpty() == false) {
			int counter = 0;
			for (ResourceBundle resourceBundleObj : resourceBundleXMLVO.getResourceBundleDetails()) {
				positionMap.put(resourceBundleObj.getId().getResourceKey(), counter);
				counter = counter + 1;
			}
		}

		if (exportableList != null && !exportableList.isEmpty()) {
			resourceBundleXMLVO = (resourceBundleXMLVO == null) ? new ResourceBundleXMLVO() : resourceBundleXMLVO;
			for (Object obj : exportableList) {
				if (!exportedList.contains(((ResourceBundle) obj).getId().getResourceKey())) {
					throw new Exception("Data mismatch while exporting Resource Bundle.");
				}
				
				if (resourceBundleXMLVO != null && resourceBundleXMLVO.getResourceBundleDetails().isEmpty() == false && positionMap.isEmpty() ==false ) {
					int counter = 0;
					for (ResourceBundle resourceBundleObj : resourceBundleXMLVO.getResourceBundleDetails()) {
						positionMap.put(resourceBundleObj.getId().getResourceKey(), counter);
						counter = counter + 1;
					}
				}
				ResourceBundle resourceBundle = ((ResourceBundle) obj).getObject();
				if (positionMap.containsKey(resourceBundle.getId().getResourceKey())) {
					List<ResourceBundle> moduleList = resourceBundleXMLVO.getResourceBundleDetails();
					int o = positionMap.get(resourceBundle.getId().getResourceKey());
					moduleList.remove(o);
					resourceBundleXMLVO.setResourceBundleDetails(moduleList);
				}
				resourceBundleXMLVO.getResourceBundleDetails().add(resourceBundle);
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return resourceBundleXMLVO;
	}

	private XMLVO retrieveAutocompleteExportData(List<String> systemConfigIncludeList,
			List<String> customConfigExcludeList, String moduleType, List<String> exportedList, XMLVO xmlVO)
			throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_AUTOCOMPLETE_DATA_FOR_EXPORT, systemConfigIncludeList, 2,
				customConfigExcludeList, 1);

		validate(exportableList, exportedList, "Autocomplete");

		AutocompleteXMLVO autocompleteXMLVO = (xmlVO == null) ? null : (AutocompleteXMLVO) xmlVO;

		

		if (exportableList != null && !exportableList.isEmpty()) {
			autocompleteXMLVO = (autocompleteXMLVO == null) ? new AutocompleteXMLVO() : autocompleteXMLVO;
			for (Object obj : exportableList) {
				if (!exportedList.contains(((Autocomplete) obj).getAutocompleteId())) {
					throw new Exception("Data mismatch while exporting Autocomplete.");
				}
				
				Map<String, Integer> positionMap = new HashMap<>();
				if (autocompleteXMLVO != null && autocompleteXMLVO.getAutocompleteDetails().isEmpty() == false) {
					int counter = 0;
					for (Autocomplete auto : autocompleteXMLVO.getAutocompleteDetails()) {
						positionMap.put(auto.getAutocompleteId(), counter);
						counter = counter + 1;
					}
				}

				Autocomplete autocomplete = ((Autocomplete) obj).getObject();
				if (positionMap.containsKey(autocomplete.getAutocompleteId())) {
					List<Autocomplete> moduleList = autocompleteXMLVO.getAutocompleteDetails();
					int o = positionMap.get(autocomplete.getAutocompleteId());
					moduleList.remove(o);
					autocompleteXMLVO.setAutocompleteDetails(moduleList);
				}
				autocompleteXMLVO.getAutocompleteDetails().add(autocomplete);
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return autocompleteXMLVO;
	}

	private XMLVO retrieveSchedulerExportData(List<String> systemConfigIncludeList,
			List<String> customConfigExcludeList, String moduleType, List<String> exportedList, XMLVO xmlVO)
			throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_SCHEDULER_DATA_FOR_EXPORT, systemConfigIncludeList, 2,
				customConfigExcludeList, 1);

		validate(exportableList, exportedList, "Scheduler");

		SchedulerXMLVO schedulerXMLVO = (xmlVO == null) ? null : (SchedulerXMLVO) xmlVO;

	

		if (exportableList != null && !exportableList.isEmpty()) {
			schedulerXMLVO = (schedulerXMLVO == null) ? new SchedulerXMLVO() : schedulerXMLVO;
			for (Object obj : exportableList) {
				if (!exportedList.contains(((JqScheduler) obj).getSchedulerId())) {
					throw new Exception("Data mismatch while exporting Scheduler.");
				}
				
				Map<String, Integer> positionMap = new HashMap<>();
				if (schedulerXMLVO != null && schedulerXMLVO.getSchedulerDetails().isEmpty() == false) {
					int counter = 0;
					for (JqScheduler sch : schedulerXMLVO.getSchedulerDetails()) {
						positionMap.put(sch.getSchedulerId(), counter);
						counter = counter + 1;
					}
				}
				JqScheduler scheduler = ((JqScheduler) obj).getObject();
				if (positionMap.containsKey(scheduler.getSchedulerId())) {
					List<JqScheduler> moduleList = schedulerXMLVO.getSchedulerDetails();
					int o = positionMap.get(scheduler.getSchedulerId());
					moduleList.remove(o);
					schedulerXMLVO.setSchedulerDetails(moduleList);
				}
				schedulerXMLVO.getSchedulerDetails().add(scheduler);
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return schedulerXMLVO;
	}

	private XMLVO retrieveNotificationExportData(List<String> customConfigExcludeList, String moduleType,
			List<String> exportedList, XMLVO xmlVO) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_NOTIFICATAION_DATA_FOR_EXPORT, null, null, customConfigExcludeList,
				null);

		validate(exportableList, exportedList, "Notification");

		GenericUserNotificationXMLVO genericUserNotificationXMLVO = (xmlVO == null) ? null
				: (GenericUserNotificationXMLVO) xmlVO;

	

		if (exportableList != null && !exportableList.isEmpty()) {
			genericUserNotificationXMLVO = (genericUserNotificationXMLVO == null) ? new GenericUserNotificationXMLVO()
					: genericUserNotificationXMLVO;
			for (Object obj : exportableList) {
				if (!exportedList.contains(((GenericUserNotification) obj).getNotificationId())) {
					throw new Exception("Data mismatch while exporting Notification.");
				}
				
				Map<String, Integer> positionMap = new HashMap<>();
				if (genericUserNotificationXMLVO != null
						&& genericUserNotificationXMLVO.getGenericUserNotificationDetails().isEmpty() == false) {
					int counter = 0;
					for (GenericUserNotification gnd : genericUserNotificationXMLVO.getGenericUserNotificationDetails()) {
						positionMap.put(gnd.getNotificationId(), counter);
						counter = counter + 1;
					}
				}
				GenericUserNotification gnd = ((GenericUserNotification) obj).getObject();
				if (positionMap.containsKey(gnd.getNotificationId())) {
					List<GenericUserNotification> moduleList = genericUserNotificationXMLVO
							.getGenericUserNotificationDetails();
					int o = positionMap.get(gnd.getNotificationId());
					moduleList.remove(o);
					genericUserNotificationXMLVO.setGenericUserNotificationDetails(moduleList);
				}
				genericUserNotificationXMLVO.getGenericUserNotificationDetails().add(gnd);
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return genericUserNotificationXMLVO;
	}

	private XMLVO retrieveScriptLibraryExportData(List<String> systemConfigIncludeList,
			List<String> customConfigExcludeList, String moduleType,
			List<String> exportedList, XMLVO xmlVO) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_SCRIPT_LIBRARY_DATA_FOR_EXPORT, null, null, customConfigExcludeList,
				null);

		validate(exportableList, exportedList, "ScriptLibrary");

		ScriptLibraryXMLVO scriptLibraryXMLVO = (xmlVO == null) ? null
				: (ScriptLibraryXMLVO) xmlVO;

		if (exportableList != null && !exportableList.isEmpty()) {
			scriptLibraryXMLVO = (scriptLibraryXMLVO == null) ? new ScriptLibraryXMLVO()
					: scriptLibraryXMLVO;
			for (Object obj : exportableList) {
				if (!exportedList.contains(((ScriptLibraryDetails) obj).getScriptLibId())) {
					throw new Exception("Data mismatch while exporting Script Library.");
				}
				
				Map<String, Integer> positionMap = new HashMap<>();
				if (scriptLibraryXMLVO != null
						&& scriptLibraryXMLVO.getScriptLibraryDetails().isEmpty() == false) {
					int counter = 0;
					for (ScriptLibraryDetails scrlib : scriptLibraryXMLVO.getScriptLibraryDetails()) {
						positionMap.put(scrlib.getScriptLibId(), counter);
						counter = counter + 1;
					}
				}
				ScriptLibraryDetails scrlib = ((ScriptLibraryDetails) obj).getObject();
				if (positionMap.containsKey(scrlib.getScriptLibId())) {
					List<ScriptLibraryDetails> moduleList = scriptLibraryXMLVO.getScriptLibraryDetails();
							
					int o = positionMap.get(scrlib.getScriptLibId());
					moduleList.remove(o);
					scriptLibraryXMLVO.setScriptLibraryDetails(moduleList);
				}
				scriptLibraryXMLVO.getScriptLibraryDetails().add(scrlib);
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return scriptLibraryXMLVO;
	}
	
	private XMLVO retrieveFileManagerExportData(List<String> systemConfigIncludeList,
			List<String> customConfigExcludeList, String downloadFolderLocation, String moduleType,
			List<String> exportedList, XMLVO xmlVO) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_FILE_MANAGER_DATA_FOR_EXPORT, null, null, customConfigExcludeList,
				null);

		validate(exportableList, exportedList, "File Bin");

		MetadataXMLVO metadataXMLVO = (MetadataXMLVO) xmlVO;
		fileUploadExportModule.setModuleDetailsMap(new HashMap<>());

		if (exportableList != null && !exportableList.isEmpty()) {
			for (Object obj : exportableList) {
				if (!exportedList.contains(((FileUploadConfig) obj).getFileBinId())) {
					throw new Exception("Data mismatch while exporting File Bin.");
				}
				
				Map<String, Integer> positionMap = new HashMap<>();
				if ((MetadataXMLVO) xmlVO != null && ((MetadataXMLVO) xmlVO).getExportModules() != null
						&& ((MetadataXMLVO) xmlVO).getExportModules().getModule().isEmpty() == false) {
					int counter = 0;
					for (Modules module : metadataXMLVO.getExportModules().getModule()) {
						positionMap.put(module.getModuleID(), counter);
						counter = counter + 1;
					}
				}
				FileUploadConfig fileUploadObj = (FileUploadConfig) obj;
				if (positionMap.containsKey(fileUploadObj.getFileBinId())) {
					List<Modules> moduleList = metadataXMLVO.getExportModules().getModule();
					int o = positionMap.get(fileUploadObj.getFileBinId());
					moduleList.remove(o);
					metadataXMLVO.getExportModules().setModule(moduleList);
				}

				fileUploadExportModule.exportData(fileUploadObj, downloadFolderLocation);
			}
			moduleListMap.put(moduleType, Constant.FOLDER_EXPORT_TYPE);
			XMLUtil.generateMetadataXML(null, fileUploadExportModule.getModuleDetailsMap(),
					downloadFolderLocation + File.separator + Constant.FILE_BIN_UPLOAD_DIRECTORY_NAME, version,
					userName, "", null);
			fileUploadExportModule.setModuleDetailsMap(new HashMap<>());
		}

		return null;

	}


	private XMLVO downloadDynaRestExportData(List<String> systemConfigIncludeList, List<String> customConfigExcludeList,
			String downloadFolderLocation, String moduleType, List<String> exportedList, XMLVO xmlVO) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_DYNA_REST_DATA_FOR_EXPORT, systemConfigIncludeList, 2,
				customConfigExcludeList, 1);

		validate(exportableList, exportedList, "Dyna Rest");

		MetadataXMLVO metadataXMLVO = (MetadataXMLVO) xmlVO;
		dynaRestModule.setModuleDetailsMap(new HashMap<>());
		

		if (exportableList != null && !exportableList.isEmpty()) {
			for (Object obj : exportableList) {
				if (!exportedList.contains(((JwsDynamicRestDetail) obj).getJwsDynamicRestId())) {
					throw new Exception("Data mismatch while exporting Rest API.");
				}
				
				Map<String, Integer> positionMap = new HashMap<>();
				if ((MetadataXMLVO) xmlVO != null && ((MetadataXMLVO) xmlVO).getExportModules() != null
						&& ((MetadataXMLVO) xmlVO).getExportModules().getModule().isEmpty() == false) {
					int counter = 0;
					for (Modules module : metadataXMLVO.getExportModules().getModule()) {
						positionMap.put(module.getModuleID(), counter);
						counter = counter + 1;
					}
				}
				
				JwsDynamicRestDetail dynaRest = (JwsDynamicRestDetail) obj;
				if (positionMap.containsKey(dynaRest.getJwsDynamicRestId())) {
					List<Modules> moduleList = metadataXMLVO.getExportModules().getModule();
					int o = positionMap.get(dynaRest.getJwsDynamicRestId());
					moduleList.remove(o);
					metadataXMLVO.getExportModules().setModule(moduleList);
				}

				dynaRestModule.exportData(dynaRest, downloadFolderLocation);
			}
			moduleListMap.put(moduleType, Constant.FOLDER_EXPORT_TYPE);

			XMLUtil.generateMetadataXML(null, dynaRestModule.getModuleDetailsMap(),
					downloadFolderLocation + File.separator
							+ com.trigyn.jws.dynarest.utils.Constants.DYNAMIC_REST_DIRECTORY_NAME,
					version, userName, "", null);
			dynaRestModule.setModuleDetailsMap(new HashMap<>());
		}
		return null;
	}

	private XMLVO retrievePermissionExportData(List<String> systemConfigIncludeList, String moduleType,
			List<String> exportedList, XMLVO xmlVO) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_PERMISSION_FOR_EXPORT, systemConfigIncludeList, null, null, null);

		validate(exportableList, exportedList, "Permission");

		PermissionXMLVO permissionXMLVO = (xmlVO == null) ? null : (PermissionXMLVO) xmlVO;

	

		if (exportableList != null && !exportableList.isEmpty()) {
			permissionXMLVO = (permissionXMLVO == null) ? new PermissionXMLVO() : permissionXMLVO;
			for (Object obj : exportableList) {
				if (!exportedList.contains(((JwsEntityRoleAssociation) obj).getEntityRoleId())) {
					throw new Exception("Data mismatch while exporting Permission.");
				}
				
				Map<String, Integer> positionMap = new HashMap<>();
				if (permissionXMLVO != null && permissionXMLVO.getJwsRoleDetails().isEmpty() == false) {
					int counter = 0;
					for (JwsEntityRoleAssociation permission : permissionXMLVO.getJwsRoleDetails()) {
						positionMap.put(permission.getEntityRoleId(), counter);
						counter = counter + 1;
					}
				}
				
				JwsEntityRoleAssociation permission = ((JwsEntityRoleAssociation) obj).getObject();
				if (positionMap.containsKey(permission.getEntityRoleId())) {
					List<JwsEntityRoleAssociation> moduleList = permissionXMLVO.getJwsRoleDetails();
					int o = positionMap.get(permission.getEntityRoleId());
					moduleList.remove(o);
					permissionXMLVO.setJwsRoleDetails(moduleList);
				}
				permissionXMLVO.getJwsRoleDetails().add(permission);
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return permissionXMLVO;
	}

	private XMLVO retrieveSiteLayoutExportData(List<String> systemConfigIncludeList,
			List<String> customConfigExcludeList, String moduleType, List<String> exportedList, XMLVO xmlVO)
			throws Exception {

		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_SITE_LAYOUT_DATA_FOR_EXPORT, systemConfigIncludeList, 2,
				customConfigExcludeList, 1);

		validate(exportableList, exportedList, "SIte Layout");

		SiteLayoutXMLVO siteLayoutXMLVO = (xmlVO == null) ? null : (SiteLayoutXMLVO) xmlVO;

		
		if (exportableList != null && !exportableList.isEmpty()) {
			siteLayoutXMLVO = (siteLayoutXMLVO == null) ? new SiteLayoutXMLVO() : siteLayoutXMLVO;
			for (Object obj : exportableList) {
				if (!exportedList.contains(((ModuleListing) obj).getModuleId())) {
					throw new Exception("Data mismatch while exporting Site Layout.");
				}
				
				Map<String, Integer> positionMap = new HashMap<>();
				if (siteLayoutXMLVO != null && siteLayoutXMLVO.getModuleListingDetails().isEmpty() == false) {
					int counter = 0;
					for (ModuleListing moduleListing : siteLayoutXMLVO.getModuleListingDetails()) {
						positionMap.put(moduleListing.getModuleId(), counter);
						counter = counter + 1;
					}
				}
				
				ModuleListing moduleListing = ((ModuleListing) obj).getObject();
				if (positionMap.containsKey(moduleListing.getModuleId())) {
					List<ModuleListing> moduleList = siteLayoutXMLVO.getModuleListingDetails();
					int o = positionMap.get(moduleListing.getModuleId());
					moduleList.remove(o);
					siteLayoutXMLVO.setModuleListingDetails(moduleList);
				}
				siteLayoutXMLVO.getModuleListingDetails().add(moduleListing);
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return siteLayoutXMLVO;
	}

	private XMLVO retrieveAppConfigExportData(List<String> systemConfigIncludeList, String moduleType,
			List<String> exportedList, XMLVO xmlVO) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_APP_CONFIG_DATA_FOR_EXPORT, systemConfigIncludeList, null, null,
				null);

		validate(exportableList, exportedList, "Application Configuration");

		PropertyMasterXMLVO propertyMasterXMLVO = (xmlVO == null) ? null : (PropertyMasterXMLVO) xmlVO;

	

		if (exportableList != null && !exportableList.isEmpty()) {
			propertyMasterXMLVO = (propertyMasterXMLVO == null) ? new PropertyMasterXMLVO() : propertyMasterXMLVO;
			for (Object obj : exportableList) {
				if (!exportedList.contains(((PropertyMaster) obj).getPropertyMasterId())) {
					throw new Exception("Data mismatch while exporting Application Configuration.");
				}
				
				Map<String, Integer> positionMap = new HashMap<>();
				if (propertyMasterXMLVO != null && propertyMasterXMLVO.getApplicationConfiguration().isEmpty() == false) {
					int counter = 0;
					for (PropertyMaster propertyMaster : propertyMasterXMLVO.getApplicationConfiguration()) {
						positionMap.put(propertyMaster.getPropertyMasterId(), counter);
						counter = counter + 1;
					}
				}
				PropertyMaster propertyMaster = ((PropertyMaster) obj).getObject();
				if (positionMap.containsKey(propertyMaster.getPropertyMasterId())) {
					List<PropertyMaster> moduleList = propertyMasterXMLVO.getApplicationConfiguration();
					int o = positionMap.get(propertyMaster.getPropertyMasterId());
					moduleList.remove(o);
					propertyMasterXMLVO.setApplicationConfiguration(moduleList);
				}
				propertyMasterXMLVO.getApplicationConfiguration().add(propertyMaster);
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return propertyMasterXMLVO;
	}

	private XMLVO retrieveManageUsersExportData(List<String> systemConfigIncludeList, String moduleType,
			List<String> exportedList, XMLVO xmlVO) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_MANAGE_USERS_DATA_FOR_EXPORT, systemConfigIncludeList, null, null,
				null);

		validate(exportableList, exportedList, "Users");

		UserXMLVO userXMLVO = (xmlVO == null) ? null : (UserXMLVO) xmlVO;

	

		if (exportableList != null && !exportableList.isEmpty()) {
			userXMLVO = (userXMLVO == null) ? new UserXMLVO() : userXMLVO;
			for (Object obj : exportableList) {
				if (!exportedList.contains(((JwsUser) obj).getUserId())) {
					throw new Exception("Data mismatch while exporting Users.");
				}
				
				Map<String, Integer> positionMap = new HashMap<>();
				if (userXMLVO != null && userXMLVO.getUserDetails().isEmpty() == false) {
					int counter = 0;
					for (JwsUser user : userXMLVO.getUserDetails()) {
						positionMap.put(user.getUserId(), counter);
						counter = counter + 1;
					}
				}
				
				JwsUser user = ((JwsUser) obj).getObject();
				if (positionMap.containsKey(user.getUserId())) {
					List<JwsUser> moduleList = userXMLVO.getUserDetails();
					int o = positionMap.get(user.getUserId());
					moduleList.remove(o);
					userXMLVO.setUserDetails(moduleList);
				}
				userXMLVO.getUserDetails().add(user);
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return userXMLVO;
	}

	private XMLVO retrieveManageRolesExportData(List<String> systemConfigIncludeList, String moduleType,
			List<String> exportedList, XMLVO xmlVO) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_MANAGE_ROLES_DATA_FOR_EXPORT, systemConfigIncludeList, null, null,
				null);

		validate(exportableList, exportedList, "Roles");

		RoleXMLVO roleXMLVO = (xmlVO == null) ? null : (RoleXMLVO) xmlVO;

	

		if (exportableList != null && !exportableList.isEmpty()) {
			roleXMLVO = (roleXMLVO == null) ? new RoleXMLVO() : roleXMLVO;
			for (Object obj : exportableList) {
				if (!exportedList.contains(((JwsRole) obj).getRoleName())) {
					throw new Exception("Data mismatch while exporting Roles.");
				}
				
				Map<String, Integer> positionMap = new HashMap<>();
				if (roleXMLVO != null && roleXMLVO.getRoleDetails().isEmpty() == false) {
					int counter = 0;
					for (JwsRole role : roleXMLVO.getRoleDetails()) {
						positionMap.put(role.getRoleId(), counter);
						counter = counter + 1;
					}
				}
				
				JwsRole role = ((JwsRole) obj).getObject();
				if (positionMap.containsKey(role.getRoleId())) {
					List<JwsRole> moduleList = roleXMLVO.getRoleDetails();
					int o = positionMap.get(role.getRoleId());
					moduleList.remove(o);
					roleXMLVO.setRoleDetails(moduleList);
				}
				roleXMLVO.getRoleDetails().add(role);
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return roleXMLVO;
	}

	private XMLVO downloadTemplateExportData(List<String> systemConfigIncludeList, List<String> customConfigExcludeList,
			String downloadFolderLocation, String moduleType, List<String> exportedList, XMLVO xmlVO) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_TEMPLATE_DATA_FOR_EXPORT, systemConfigIncludeList, 2,
				customConfigExcludeList, 1);

		validate(exportableList, exportedList, "Templates");

		MetadataXMLVO metadataXMLVO = (MetadataXMLVO) xmlVO;
		templateDownloadUploadModule.setModuleDetailsMap(new HashMap<>());

		if (exportableList != null && !exportableList.isEmpty()) {
			for (Object obj : exportableList) {
				if (!exportedList.contains(((TemplateMaster) obj).getTemplateId())) {
					throw new Exception("Data mismatch while exporting Templates.");
				}

				Map<String, Integer> positionMap = new HashMap<>();
				if ((MetadataXMLVO) xmlVO != null && ((MetadataXMLVO) xmlVO).getExportModules() != null
						&& ((MetadataXMLVO) xmlVO).getExportModules().getModule().isEmpty() == false) {
					int counter = 0;
					for (Modules module : metadataXMLVO.getExportModules().getModule()) {
						positionMap.put(module.getModuleID(), counter);
						counter = counter + 1;
					}
				}
				TemplateMaster template = (TemplateMaster) obj;
				if (positionMap.containsKey(template.getTemplateId())) {
					List<Modules> moduleList = metadataXMLVO.getExportModules().getModule();
					int o = positionMap.get(template.getTemplateId());
					moduleList.remove(o);
					metadataXMLVO.getExportModules().setModule(moduleList);
				}

				templateDownloadUploadModule.exportData(template, downloadFolderLocation);
			}
			moduleListMap.put(moduleType, Constant.FOLDER_EXPORT_TYPE);

			XMLUtil.generateMetadataXML(null, templateDownloadUploadModule.getModuleDetailsMap(),
					downloadFolderLocation + File.separator
							+ com.trigyn.jws.templating.utils.Constant.TEMPLATE_DIRECTORY_NAME,
					version, userName, "", (xmlVO != null ? metadataXMLVO.getExportModules().getModule() : null));
			templateDownloadUploadModule.setModuleDetailsMap(new HashMap<>());
		}
		return null;
	}

	private XMLVO downloadDashboardExportData(List<String> systemConfigIncludeList,
			List<String> customConfigExcludeList, String moduleType, List<String> exportedList, XMLVO xmlVO)
			throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_DASHBOARD_DATA_FOR_EXPORT, systemConfigIncludeList, 2,
				customConfigExcludeList, 1);

		validate(exportableList, exportedList, "Dashboard");

		DashboardXMLVO dashboardXMLVO = (xmlVO == null) ? null : (DashboardXMLVO) xmlVO;

		

		if (exportableList != null && !exportableList.isEmpty()) {
			dashboardXMLVO = (dashboardXMLVO == null) ? new DashboardXMLVO() : dashboardXMLVO;
			for (Object obj : exportableList) {
				if (!exportedList.contains(((Dashboard) obj).getDashboardId())) {
					throw new Exception("Data mismatch while exporting Dashboard.");
				}
				
				Map<String, Integer> positionMap = new HashMap<>();
				if (dashboardXMLVO != null && dashboardXMLVO.getDashboardDetails().isEmpty() == false) {
					int counter = 0;
					for (Dashboard dashboard : dashboardXMLVO.getDashboardDetails()) {
						positionMap.put(dashboard.getDashboardId(), counter);
						counter = counter + 1;
					}

				}
				List<DashboardRoleAssociation> dashletRoleAssociation = dashboardCrudService
						.findDashboardRoleByDashboardId(((Dashboard) obj).getDashboardId());
				if (!CollectionUtils.isEmpty(dashletRoleAssociation)) {
					((Dashboard) obj).setDashboardRoles(dashletRoleAssociation);
				}

				Dashboard dashboard = ((Dashboard) obj).getObject();
				if (positionMap.containsKey(dashboard.getDashboardId())) {
					List<Dashboard> moduleList = dashboardXMLVO.getDashboardDetails();
					int o = positionMap.get(dashboard.getDashboardId());
					moduleList.remove(o);
					dashboardXMLVO.setDashboardDetails(moduleList);
				}
				dashboardXMLVO.getDashboardDetails().add(dashboard);
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return dashboardXMLVO;
	}

	private XMLVO downloadDashletExportData(List<String> systemConfigIncludeList, List<String> customConfigExcludeList,
			String downloadFolderLocation, String moduleType, List<String> exportedList, XMLVO xmlVO) throws Exception {
		/*List<Object> exportableList = importExportCrudDAO.getAllExportableData(CrudQueryStore.HQL_QUERY_TO_FETCH_DASHLET_DATA_FOR_EXPORT,
				systemConfigIncludeList, 2, customConfigExcludeList, 1);

		validate(exportableList, exportedList, "Dashlets");

		if (exportableList != null && !exportableList.isEmpty()) {
			for (Object obj : exportableList) {
				if (!exportedList.contains(((Dashlet) obj).getDashletId())) {
					throw new Exception("Data mismatch while exporting Dashlet.");
				}
				List<DashletProperties> dpOtr = new ArrayList<>();
				if (((Dashlet) obj).getProperties() != null && !((Dashlet) obj).getProperties().isEmpty()) {
					for (DashletProperties objProp : ((Dashlet) obj).getProperties()) {
						dpOtr.add(objProp.getObj());
					}
					((Dashlet) obj).setProperties(dpOtr);
				} else
					((Dashlet) obj).setProperties(null);
				
				dashletDownloadUploadModule.exportData(((Dashlet) obj), downloadFolderLocation);
			}
			moduleListMap.put(moduleType, Constant.FOLDER_EXPORT_TYPE);
			XMLUtil.generateMetadataXML(null, dashletDownloadUploadModule.getModuleDetailsMap(),
					downloadFolderLocation + File.separator + Constants.DASHLET_DIRECTORY_NAME, version, userName, "",(xmlVO != null ? metadataXMLVO.getExportModules().getModule() : null));
			dashletDownloadUploadModule.setModuleDetailsMap(new HashMap<>());
		}
		return null;*/
		
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_DASHLET_DATA_FOR_EXPORT, systemConfigIncludeList, 2,
				customConfigExcludeList, 1);

		validate(exportableList, exportedList, "Dashlets");

		MetadataXMLVO metadataXMLVO = (MetadataXMLVO) xmlVO;
		dashletDownloadUploadModule.setModuleDetailsMap(new HashMap<>());

		if (exportableList != null && !exportableList.isEmpty()) {

			for (Object obj : exportableList) {
				if (!exportedList.contains(((Dashlet) obj).getDashletId())) {
					throw new Exception("Data mismatch while exporting Dashlet.");
				}
				Map<String, Integer> positionMap = new HashMap<>();
				if ((MetadataXMLVO) xmlVO != null && ((MetadataXMLVO) xmlVO).getExportModules() != null
						&& ((MetadataXMLVO) xmlVO).getExportModules().getModule().isEmpty() == false) {
					int counter = 0;
					for (Modules module : metadataXMLVO.getExportModules().getModule()) {
						positionMap.put(module.getModuleID(), counter);
						counter = counter + 1;
					}
				}
				Dashlet dashlet = (Dashlet) obj;
				if (positionMap.containsKey(dashlet.getDashletId())) {
					List<Modules> moduleList = metadataXMLVO.getExportModules().getModule();
					int o = positionMap.get(dashlet.getDashletId());
					moduleList.remove(o);
					metadataXMLVO.getExportModules().setModule(moduleList);
				}

				dashletDownloadUploadModule.exportData(dashlet, downloadFolderLocation);
			}
			moduleListMap.put(moduleType, Constant.FOLDER_EXPORT_TYPE);
			XMLUtil.generateMetadataXML(null, dashletDownloadUploadModule.getModuleDetailsMap(),
					downloadFolderLocation + File.separator + Constants.DASHLET_DIRECTORY_NAME, version, userName, "",
					(xmlVO != null ? metadataXMLVO.getExportModules().getModule() : null));
			dashletDownloadUploadModule.setModuleDetailsMap(new HashMap<>());
		}
		return null;
	}

	private XMLVO downloadDynamicFormExportData(List<String> systemConfigIncludeList,
			List<String> customConfigExcludeList, String downloadFolderLocation, String moduleType,
			List<String> exportedList, XMLVO xmlVO) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_DYNAMIC_FORM_DATA_FOR_EXPORT, systemConfigIncludeList, 2,
				customConfigExcludeList, 1);

		validate(exportableList, exportedList, "Dynamic Form");

		MetadataXMLVO metadataXMLVO = (MetadataXMLVO) xmlVO;
		dynamicFormDownloadUploadModule.setModuleDetailsMap(new HashMap<>());

		if (exportableList != null && !exportableList.isEmpty()) {

			for (Object obj : exportableList) {
				if (!exportedList.contains(((DynamicForm) obj).getFormId())) {
					throw new Exception("Data mismatch while exporting Dynamic Form.");
				}
				
				Map<String, Integer> positionMap = new HashMap<>();
				if ((MetadataXMLVO) xmlVO != null && ((MetadataXMLVO) xmlVO).getExportModules() != null
						&& ((MetadataXMLVO) xmlVO).getExportModules().getModule().isEmpty() == false) {
					int counter = 0;
					for (Modules module : metadataXMLVO.getExportModules().getModule()) {
						positionMap.put(module.getModuleID(), counter);
						counter = counter + 1;
					}
				}
				DynamicForm dynamicForm = (DynamicForm) obj;
				if (positionMap.containsKey(dynamicForm.getFormId())) {
					List<Modules> moduleList = metadataXMLVO.getExportModules().getModule();
					int o = positionMap.get(dynamicForm.getFormId());
					moduleList.remove(o);
					metadataXMLVO.getExportModules().setModule(moduleList);
				}

				dynamicFormDownloadUploadModule.exportData(dynamicForm, downloadFolderLocation);
			}
			moduleListMap.put(moduleType, Constant.FOLDER_EXPORT_TYPE);
			XMLUtil.generateMetadataXML(null, dynamicFormDownloadUploadModule.getModuleDetailsMap(),
					downloadFolderLocation + File.separator
							+ com.trigyn.jws.dynamicform.utils.Constant.DYNAMIC_FORM_DIRECTORY_NAME,
					version, userName, "", (xmlVO != null ? metadataXMLVO.getExportModules().getModule() : null));
			dynamicFormDownloadUploadModule.setModuleDetailsMap(new HashMap<>());
		}
		return null;
	}

	private XMLVO downloadHelpManualExportData(List<String> systemConfigIncludeList,
			List<String> customConfigExcludeList, String downloadFolderLocation, String moduleType,
			List<String> exportedList, XMLVO xmlVO) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_HELP_MANUAL_DATA_FOR_EXPORT, systemConfigIncludeList, 2,
				customConfigExcludeList, 1);

		validate(exportableList, exportedList, "Help Manual");

		MetadataXMLVO metadataXMLVO = (MetadataXMLVO) xmlVO;
		helpManualImportExportModule.setModuleDetailsMap(new HashMap<>());

		if (exportableList != null && !exportableList.isEmpty()) {
			for (Object obj : exportableList) {
				if (!exportedList.contains(((ManualType) obj).getManualId())) {
					throw new Exception("Data mismatch while exporting Help Manual.");
				}
				
				Map<String, Integer> positionMap = new HashMap<>();
				if ((MetadataXMLVO) xmlVO != null && ((MetadataXMLVO) xmlVO).getExportModules() != null
						&& ((MetadataXMLVO) xmlVO).getExportModules().getModule().isEmpty() == false) {
					int counter = 0;
					for (Modules module : metadataXMLVO.getExportModules().getModule()) {
						positionMap.put(module.getModuleID(), counter);
						counter = counter + 1;
					}
				}
				
				ManualType manualType = (ManualType) obj;
				if (positionMap.containsKey(manualType.getManualId())) {
					List<Modules> moduleList = metadataXMLVO.getExportModules().getModule();
					int o = positionMap.get(manualType.getManualId());
					moduleList.remove(o);
					metadataXMLVO.getExportModules().setModule(moduleList);
				}

				helpManualImportExportModule.exportData(manualType, downloadFolderLocation);
			}
			moduleListMap.put(moduleType, Constant.FOLDER_EXPORT_TYPE);
			XMLUtil.generateMetadataXML(null, helpManualImportExportModule.getModuleDetailsMap(),
					downloadFolderLocation + File.separator + Constant.HELP_MANUAL_DIRECTORY_NAME, version, userName,
					"", (xmlVO != null ? metadataXMLVO.getExportModules().getModule() : null));
			helpManualImportExportModule.setModuleDetailsMap(new HashMap<>());
		}

		return null;
	}

	private XMLVO downloadApiClientExportData(List<String> systemConfigIncludeList,
			List<String> customConfigExcludeList, String downloadFolderLocation, String moduleType,
			List<String> exportedList, XMLVO xmlVO) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_API_CLIENT_DETAILS_FOR_EXPORT, null, null, customConfigExcludeList,
				null);

		validate(exportableList, exportedList, "Api Clients")	;

		ApiClientDetailsXMLVO apiClientDetailsXMLVO = (xmlVO == null) ? null : (ApiClientDetailsXMLVO) xmlVO;

	

		if (exportableList != null && !exportableList.isEmpty()) {
			apiClientDetailsXMLVO = (apiClientDetailsXMLVO == null) ? new ApiClientDetailsXMLVO()
					: apiClientDetailsXMLVO;
			for (Object obj : exportableList) {
				if (!exportedList.contains(((JqApiClientDetails) obj).getClientId())) {
					throw new Exception("Data mismatch while exporting Api Clients.");
				}
				
				Map<String, Integer> positionMap = new HashMap<>();
				if (apiClientDetailsXMLVO != null && apiClientDetailsXMLVO.getApiClientDetails().isEmpty() == false) {
					int counter = 0;
					for (JqApiClientDetails apiClientDetails : apiClientDetailsXMLVO.getApiClientDetails()) {
						positionMap.put(apiClientDetails.getClientId(), counter);
						counter = counter + 1;
					}
				}
				
				JqApiClientDetails apiClientDetails = ((JqApiClientDetails) obj).getObject();
				if (positionMap.containsKey(apiClientDetails.getClientId())) {
					List<JqApiClientDetails> moduleList = apiClientDetailsXMLVO.getApiClientDetails();
					int o = positionMap.get(apiClientDetails.getClientId());
					moduleList.remove(o);
					apiClientDetailsXMLVO.setApiClientDetails(moduleList);
				}
				apiClientDetailsXMLVO.getApiClientDetails().add(apiClientDetails);
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);

		}
		return apiClientDetailsXMLVO;
	}

	private XMLVO downloadAdditionalDatasourceExportData(List<String> systemConfigIncludeList,
			List<String> customConfigExcludeList, String downloadFolderLocation, String moduleType,
			List<String> exportedList, XMLVO xmlVO) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_ADDITIONAL_DATASOURCE_FOR_EXPORT, null, null, customConfigExcludeList,
				null);

		validate(exportableList, exportedList, "Additional Datasource");
		AdditionalDatasourceXMLVO additionalDatasourceXMLVO = (xmlVO == null) ? null
				: (AdditionalDatasourceXMLVO) xmlVO;

	

		if (exportableList != null && !exportableList.isEmpty()) {
			additionalDatasourceXMLVO = (additionalDatasourceXMLVO == null) ? new AdditionalDatasourceXMLVO()
					: additionalDatasourceXMLVO;
			for (Object obj : exportableList) {
				if (!exportedList.contains(((AdditionalDatasource) obj).getAdditionalDatasourceId())) {
					throw new Exception("Data mismatch while exporting Additional Datasource.");
				}
				Map<String, Integer> positionMap = new HashMap<>();
				if (additionalDatasourceXMLVO != null
						&& additionalDatasourceXMLVO.getAdditionalDatasource().isEmpty() == false) {
					int counter = 0;
					for (AdditionalDatasource additionalDatasource : additionalDatasourceXMLVO.getAdditionalDatasource()) {
						positionMap.put(additionalDatasource.getAdditionalDatasourceId(), counter);
						counter = counter + 1;
					}
				}
				AdditionalDatasource addDS = ((AdditionalDatasource) obj).getObject();
				if (positionMap.containsKey(addDS.getAdditionalDatasourceId())) {
					List<AdditionalDatasource> moduleList = additionalDatasourceXMLVO.getAdditionalDatasource();
					int o = positionMap.get(addDS.getAdditionalDatasourceId());
					moduleList.remove(o);
					additionalDatasourceXMLVO.setAdditionalDatasource(moduleList);
				}
				additionalDatasourceXMLVO.getAdditionalDatasource().add(addDS);
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return additionalDatasourceXMLVO;
	}

	private void validate(List<Object> exportableList, List<String> exportedList, String moduleType) throws Exception {

		if (exportableList != null && exportableList.isEmpty() && exportedList != null && !exportedList.isEmpty()) {
			throw new Exception("Data mismatch while exporting " + moduleType + ".");
		} else if (exportableList != null && !exportableList.isEmpty() && exportedList != null
				&& exportedList.isEmpty()) {
			throw new Exception("Data mismatch while exporting " + moduleType + ".");
		} else if (exportableList == null && exportedList != null && !exportedList.isEmpty()) {
			throw new Exception("Data mismatch while exporting " + moduleType + ".");
		} else if (exportedList == null && exportableList != null && !exportableList.isEmpty()) {
			throw new Exception("Data mismatch while exporting " + moduleType + ".");
		}

	}

	private XMLVO retrieveExportFilesData(List<String> systemConfigIncludeList, List<String> customConfigExcludeList,
			String moduleType, List<String> exportedList, String downloadFolderLocation, XMLVO xmlVO) throws Exception {
		if (exportedList != null && exportedList.isEmpty() == false) {
			List<Object> exportableList = importExportCrudDAO.getAllExportableData(
					CrudQueryStore.HQL_QUERY_TO_FETCH_FILES_DATA_FOR_EXPORT, exportedList, null, null, null);
			validate(exportableList, exportedList, Constant.MasterModuleType.FILEIMPEXPDETAILS.getModuleType());

			Map<String, List<FileUpload>> uploadMap = new HashMap();
			if (exportableList != null && !exportableList.isEmpty()) {

				for (Object obj : exportableList) {
					FileUpload fileUpload = ((FileUpload) obj);

					if (fileUpload != null && uploadMap != null && uploadMap.containsKey(fileUpload.getFileBinId())) {
						uploadMap.get(fileUpload.getFileBinId()).add(fileUpload);
					} else {
						List<FileUpload> fileUploads = new ArrayList<>();
						fileUploads.add(((FileUpload) obj));
						uploadMap.put(fileUpload.getFileBinId(), fileUploads);
					}
				}
			}
			FileUploadXMLVO fileUploadExportXMLVO = null;
			for (Entry<String, List<FileUpload>> entry : uploadMap.entrySet()) {
				String key = entry.getKey();
				List<FileUpload> files = entry.getValue();
				if (key != null && files.isEmpty() == false) {
					fileUploadExportXMLVO = new FileUploadXMLVO();
					fileUploadExportXMLVO.setFileUploadDetails(files);

					fileImportExportModule.exportData(fileUploadExportXMLVO, downloadFolderLocation);
					moduleListMap.put(moduleType, Constant.FOLDER_EXPORT_TYPE);
					XMLUtil.generateMetadataXML(null, fileImportExportModule.getModuleDetailsMap(),
							downloadFolderLocation + File.separator + Constant.FILES_UPLOAD_DIRECTORY_NAME, version,
							userName, "", null);

				}
			}
		}
		return null;

	}

	/**
	 * This Method gets entity permission by entityId and entityName.
	 * 
	 * @param entityId entityId
	 * @param moduleId moduleId
	 * @return list of JwsEntityRoleAssociationVO
	 */
	public List<JwsEntityRoleAssociationVO> getEntityPermissions(String entityId, String moduleId) {
		List<JwsEntityRoleAssociation> entityRoleAssociations = entityRoleAssociationRepository.getEntityRoles(entityId,
				moduleId);
		List<JwsEntityRoleAssociationVO> roleList = new ArrayList<>();
		if (entityRoleAssociations != null) {
			for (Iterator iterator = entityRoleAssociations.iterator(); iterator.hasNext();) {
				JwsEntityRoleAssociation jwsEntityRoleAssociation = (JwsEntityRoleAssociation) iterator.next();
				jwsEntityRoleAssociation = jwsEntityRoleAssociation.getObject();
				JwsEntityRoleAssociationVO vo = new JwsEntityRoleAssociationVO();
				vo = vo.convertEntityToVO(jwsEntityRoleAssociation);
				roleList.add(vo);
			}

		}
		return roleList;
	}

}
