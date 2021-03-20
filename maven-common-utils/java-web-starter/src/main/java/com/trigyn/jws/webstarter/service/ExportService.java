package com.trigyn.jws.webstarter.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.trigyn.jws.dashboard.entities.Dashboard;
import com.trigyn.jws.dashboard.entities.DashboardRoleAssociation;
import com.trigyn.jws.dashboard.entities.Dashlet;
import com.trigyn.jws.dashboard.service.DashletModule;
import com.trigyn.jws.dashboard.utility.Constants;
import com.trigyn.jws.dbutils.entities.ModuleListing;
import com.trigyn.jws.dbutils.entities.PropertyMaster;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.dynamicform.entities.FileUploadConfig;
import com.trigyn.jws.dynamicform.entities.ManualType;
import com.trigyn.jws.dynamicform.service.DynamicFormModule;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDetail;
import com.trigyn.jws.gridutils.entities.GridDetails;
import com.trigyn.jws.notification.entities.GenericUserNotification;
import com.trigyn.jws.resourcebundle.entities.ResourceBundle;
import com.trigyn.jws.templating.entities.TemplateMaster;
import com.trigyn.jws.templating.service.TemplateModule;
import com.trigyn.jws.typeahead.entities.Autocomplete;
import com.trigyn.jws.usermanagement.entities.JwsEntityRoleAssociation;
import com.trigyn.jws.usermanagement.entities.JwsRole;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.webstarter.dao.CrudQueryStore;
import com.trigyn.jws.webstarter.dao.ImportExportCrudDAO;
import com.trigyn.jws.webstarter.utils.Constant;
import com.trigyn.jws.webstarter.utils.FileUploadExportModule;
import com.trigyn.jws.webstarter.utils.FileUtil;
import com.trigyn.jws.webstarter.utils.HelpManualImportExportModule;
import com.trigyn.jws.webstarter.utils.XMLUtil;
import com.trigyn.jws.webstarter.utils.ZipUtil;
import com.trigyn.jws.webstarter.xml.AutocompleteXMLVO;
import com.trigyn.jws.webstarter.xml.DashboardXMLVO;
import com.trigyn.jws.webstarter.xml.DynaRestXMLVO;
import com.trigyn.jws.webstarter.xml.GenericUserNotificationXMLVO;
import com.trigyn.jws.webstarter.xml.GridXMLVO;
import com.trigyn.jws.webstarter.xml.PermissionXMLVO;
import com.trigyn.jws.webstarter.xml.PropertyMasterXMLVO;
import com.trigyn.jws.webstarter.xml.ResourceBundleXMLVO;
import com.trigyn.jws.webstarter.xml.RoleXMLVO;
import com.trigyn.jws.webstarter.xml.SiteLayoutXMLVO;
import com.trigyn.jws.webstarter.xml.UserXMLVO;

@Service
@Transactional(readOnly = false)
public class ExportService {

	private final static Logger				logger							= LogManager.getLogger(ExportService.class);

	@Autowired
	private ImportExportCrudDAO				importExportCrudDAO				= null;

	@Autowired
	private TemplateModule					templateDownloadUploadModule	= null;

	@Autowired
	@Qualifier("dynamic-form")
	private DynamicFormModule				dynamicFormDownloadUploadModule	= null;

	@Autowired
	@Qualifier("dashlet")
	private DashletModule					dashletDownloadUploadModule		= null;

	@Autowired
	private DashboardCrudService			dashboardCrudService			= null;

	@Autowired
	private PropertyMasterDAO				propertyMasterDAO				= null;

	private Map<String, String>				moduleListMap					= null;

	@Autowired
	private IUserDetailsService				detailsService					= null;

	@Autowired
	private HelpManualImportExportModule	helpManualImportExportModule	= null;

	@Autowired
	private FileUploadExportModule			fileUploadExportModule			= null;

	private String							version							= null;

	private String							userName						= null;

	public List<Map<String, Object>> getAllCustomEntity() {
		return importExportCrudDAO.getAllCustomEntity();
	}

	public List<Map<String, Object>> getCustomEntityCount() {
		return importExportCrudDAO.getCustomEntityCount();
	}

	public List<Map<String, Object>> getAllEntityCount() {
		return importExportCrudDAO.getAllEntityCount();
	}

	public String exportConfigData(HttpServletRequest request, HttpServletResponse response, Map<String, String> out)
			throws Exception {
		try {
			version = propertyMasterDAO.findPropertyMasterValue("system", "system", "version");
			UserDetailsVO detailsVO = detailsService.getUserDetails();
			userName = detailsVO.getUserName();

			String	systemPath			= System.getProperty("user.dir");
			String	tempDownloadPath	= FileUtil.generateTemporaryFilePath(Constant.EXPORTTEMPPATH);
			new File(tempDownloadPath).mkdir();
			moduleListMap = new HashMap<>();

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

					}
				}
			}

			for (Entry<String, String> obj : out.entrySet()) {
				XMLVO	xmlVO		= null;
				String	moduleType	= obj.getKey();
				String	exportData	= obj.getValue();

				if (!"htmlTableJSON".equals(moduleType)) {
					JSONObject jsonObject = new JSONObject(exportData);
					xmlVO = retrieveDBData(moduleType, jsonObject, tempDownloadPath, exportTableMap);
				}

				if (xmlVO != null)
					XMLUtil.marshaling(xmlVO, moduleType, tempDownloadPath);
			}

			XMLUtil.generateMetadataXML(moduleListMap, null, tempDownloadPath, version, userName, htmlTableJSON);
			String zipFilePath = ZipUtil.zipDirectory(tempDownloadPath, systemPath);
			return zipFilePath;
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
			Map<String, List<String>> exportTableMap) throws Exception {

		List<String>	systemConfigIncludeList	= new ArrayList<String>();
		List<String>	customConfigExcludeList	= new ArrayList<String>();

		JSONArray		jsonArray				= (JSONArray) jsonObject.get("systemConfigIncludeList");
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
					exportTableMap.get(Constant.MasterModuleType.GRID.getModuleType().toUpperCase()));
		} else if (moduleType.equals(Constant.MasterModuleType.RESOURCEBUNDLE.getModuleType())) {
			return retrieveRBExportData(systemConfigIncludeList, customConfigExcludeList, moduleType,
					exportTableMap.get(Constant.MasterModuleType.RESOURCEBUNDLE.getModuleType().toUpperCase()));
		} else if (moduleType.equals(Constant.MasterModuleType.AUTOCOMPLETE.getModuleType())) {
			return retrieveAutocompleteExportData(systemConfigIncludeList, customConfigExcludeList, moduleType,
					exportTableMap.get(Constant.MasterModuleType.AUTOCOMPLETE.getModuleType().toUpperCase()));
		} else if (moduleType.equals(Constant.MasterModuleType.NOTIFICATION.getModuleType())) {
			return retrieveNotificationExportData(customConfigExcludeList, moduleType,
					exportTableMap.get(Constant.MasterModuleType.NOTIFICATION.getModuleType().toUpperCase()));
		} else if (moduleType.equals(Constant.MasterModuleType.DASHBOARD.getModuleType())) {
			return downloadDashboardExportData(systemConfigIncludeList, customConfigExcludeList, moduleType,
					exportTableMap.get(Constant.MasterModuleType.DASHBOARD.getModuleType().toUpperCase()));
		} else if (moduleType.equals(Constant.MasterModuleType.FILEMANAGER.getModuleType())) {
			return retrieveFileManagerExportData(systemConfigIncludeList, customConfigExcludeList,
					downloadFolderLocation, moduleType,
					exportTableMap.get(Constant.MasterModuleType.FILEMANAGER.getModuleType().toUpperCase()));
		} else if (moduleType.equals(Constant.MasterModuleType.DYNAREST.getModuleType())) {
			return downloadDynaRestExportData(systemConfigIncludeList, customConfigExcludeList, moduleType,
					exportTableMap.get(Constant.MasterModuleType.DYNAREST.getModuleType().toUpperCase()));
		} else if (moduleType.equals(Constant.MasterModuleType.PERMISSION.getModuleType())) {
			return retrievePermissionExportData(systemConfigIncludeList, moduleType,
					exportTableMap.get(Constant.MasterModuleType.PERMISSION.getModuleType().toUpperCase()));
		} else if (moduleType.equals(Constant.MasterModuleType.SITELAYOUT.getModuleType())) {
			return retrieveSiteLayoutExportData(systemConfigIncludeList, moduleType,
					exportTableMap.get(Constant.MasterModuleType.SITELAYOUT.getModuleType().toUpperCase()));
		} else if (moduleType.equals(Constant.MasterModuleType.APPLICATIONCONFIGURATION.getModuleType())) {
			return retrieveAppConfigExportData(systemConfigIncludeList, moduleType, exportTableMap
					.get(Constant.MasterModuleType.APPLICATIONCONFIGURATION.getModuleType().toUpperCase()));
		} else if (moduleType.equals(Constant.MasterModuleType.MANAGEUSERS.getModuleType())) {
			return retrieveManageUsersExportData(systemConfigIncludeList, moduleType,
					exportTableMap.get(Constant.MasterModuleType.MANAGEUSERS.getModuleType().toUpperCase()));
		} else if (moduleType.equals(Constant.MasterModuleType.MANAGEROLES.getModuleType())) {
			return retrieveManageRolesExportData(systemConfigIncludeList, moduleType,
					exportTableMap.get(Constant.MasterModuleType.MANAGEROLES.getModuleType().toUpperCase()));
		} else if (moduleType.equals(Constant.MasterModuleType.TEMPLATES.getModuleType())) {
			return downloadTemplateExportData(systemConfigIncludeList, customConfigExcludeList, downloadFolderLocation,
					moduleType, exportTableMap.get(Constant.MasterModuleType.TEMPLATES.getModuleType().toUpperCase()));
		} else if (moduleType.equals(Constant.MasterModuleType.DASHLET.getModuleType())) {
			return downloadDashletExportData(systemConfigIncludeList, customConfigExcludeList, downloadFolderLocation,
					moduleType, exportTableMap.get(Constant.MasterModuleType.DASHLET.getModuleType().toUpperCase()));
		} else if (moduleType.equals(Constant.MasterModuleType.DYNAMICFORM.getModuleType())) {
			return downloadDynamicFormExportData(systemConfigIncludeList, customConfigExcludeList,
					downloadFolderLocation, moduleType,
					exportTableMap.get(Constant.MasterModuleType.DYNAMICFORM.getModuleType().toUpperCase()));
		} else if (moduleType.equals(Constant.MasterModuleType.HELPMANUAL.getModuleType())) {
			return downloadHelpManualExportData(systemConfigIncludeList, customConfigExcludeList,
					downloadFolderLocation, moduleType,
					exportTableMap.get(Constant.MasterModuleType.HELPMANUAL.getModuleType().toUpperCase()));
		} else {
			return null;
		}
	}

	private XMLVO retrieveGridExportData(List<String> systemConfigIncludeList, List<String> customConfigExcludeList,
			String moduleType, List<String> exportedList) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_GRID_DATA_FOR_EXPORT, systemConfigIncludeList, 2,
				customConfigExcludeList, 1);

		validate(exportableList, exportedList, "Grid");

		GridXMLVO gridXMLVO = null;
		if (exportableList != null && !exportableList.isEmpty()) {
			gridXMLVO = new GridXMLVO();
			for (Object obj : exportableList) {
				if (!exportedList.contains(((GridDetails) obj).getGridId())) {
					throw new Exception("Data mismatch while exporting Grid.");
				}

				gridXMLVO.getGridDetails().add(((GridDetails) obj).getObject());
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return gridXMLVO;
	}

	private XMLVO retrieveRBExportData(List<String> systemConfigIncludeList, List<String> customConfigExcludeList,
			String moduleType, List<String> exportedList) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getRBExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_RESOURCE_BUNDLE_DATA_FOR_EXPORT, systemConfigIncludeList,
				customConfigExcludeList);

		validate(exportableList, exportedList, "Resource Bundle");

		ResourceBundleXMLVO resourceBundleXMLVO = null;
		if (exportableList != null && !exportableList.isEmpty()) {
			resourceBundleXMLVO = new ResourceBundleXMLVO();
			for (Object obj : exportableList) {
				if (!exportedList.contains(((ResourceBundle) obj).getId().getResourceKey())) {
					throw new Exception("Data mismatch while exporting Resource Bundle.");
				}
				resourceBundleXMLVO.getResourceBundleDetails().add(((ResourceBundle) obj).getObject());
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return resourceBundleXMLVO;
	}

	private XMLVO retrieveAutocompleteExportData(List<String> systemConfigIncludeList,
			List<String> customConfigExcludeList, String moduleType, List<String> exportedList) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_AUTOCOMPLETE_DATA_FOR_EXPORT, systemConfigIncludeList, 2,
				customConfigExcludeList, 1);

		validate(exportableList, exportedList, "Autocomplete");

		AutocompleteXMLVO autocompleteXMLVO = null;
		if (exportableList != null && !exportableList.isEmpty()) {
			autocompleteXMLVO = new AutocompleteXMLVO();
			for (Object obj : exportableList) {
				if (!exportedList.contains(((Autocomplete) obj).getAutocompleteId())) {
					throw new Exception("Data mismatch while exporting Autocomplete.");
				}
				autocompleteXMLVO.getAutocompleteDetails().add(((Autocomplete) obj).getObject());
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return autocompleteXMLVO;
	}

	private XMLVO retrieveNotificationExportData(List<String> customConfigExcludeList, String moduleType,
			List<String> exportedList) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_NOTIFICATAION_DATA_FOR_EXPORT, null, null, customConfigExcludeList,
				null);

		validate(exportableList, exportedList, "Notification");

		GenericUserNotificationXMLVO genericUserNotificationXMLVO = null;
		if (exportableList != null && !exportableList.isEmpty()) {
			genericUserNotificationXMLVO = new GenericUserNotificationXMLVO();
			for (Object obj : exportableList) {
				if (!exportedList.contains(((GenericUserNotification) obj).getNotificationId())) {
					throw new Exception("Data mismatch while exporting Notification.");
				}
				genericUserNotificationXMLVO.getGenericUserNotificationDetails()
						.add(((GenericUserNotification) obj).getObject());
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return genericUserNotificationXMLVO;
	}

	private XMLVO downloadDashboardExportData(List<String> systemConfigIncludeList,
			List<String> customConfigExcludeList, String moduleType, List<String> exportedList) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_DASHBOARD_DATA_FOR_EXPORT, systemConfigIncludeList, 2,
				customConfigExcludeList, 1);

		validate(exportableList, exportedList, "Dashboard");

		DashboardXMLVO dashboardXMLVO = null;
		if (exportableList != null && !exportableList.isEmpty()) {
			dashboardXMLVO = new DashboardXMLVO();
			for (Object obj : exportableList) {
				if (!exportedList.contains(((Dashboard) obj).getDashboardId())) {
					throw new Exception("Data mismatch while exporting Dashboard.");
				}
				List<DashboardRoleAssociation> dashletRoleAssociation = dashboardCrudService
						.findDashboardRoleByDashboardId(((Dashboard) obj).getDashboardId());
				if (!CollectionUtils.isEmpty(dashletRoleAssociation)) {
					((Dashboard) obj).setDashboardRoles(dashletRoleAssociation);
				}

				dashboardXMLVO.getDashboardDetails().add(((Dashboard) obj).getObject());
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return dashboardXMLVO;
	}

	private XMLVO retrieveFileManagerExportData(List<String> systemConfigIncludeList,
			List<String> customConfigExcludeList, String downloadFolderLocation, String moduleType,
			List<String> exportedList) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_FILE_MANAGER_DATA_FOR_EXPORT, null, null, customConfigExcludeList,
				null);

		validate(exportableList, exportedList, "File Bin");

		if (exportableList != null && !exportableList.isEmpty()) {
			for (Object obj : exportableList) {
				if (!exportedList.contains(((FileUploadConfig) obj).getFileBinId())) {
					throw new Exception("Data mismatch while exporting File Bin.");
				}
				fileUploadExportModule.exportData(((FileUploadConfig) obj), downloadFolderLocation);
			}
			moduleListMap.put(moduleType, Constant.FOLDER_EXPORT_TYPE);
			XMLUtil.generateMetadataXML(null, fileUploadExportModule.getModuleDetailsMap(),
					downloadFolderLocation + File.separator + Constant.FILE_UPLOAD_DIRECTORY_NAME, version, userName,
					"");
		}

		return null;

	}

	private XMLVO downloadDynaRestExportData(List<String> systemConfigIncludeList, List<String> customConfigExcludeList,
			String moduleType, List<String> exportedList) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_DYNA_REST_DATA_FOR_EXPORT, systemConfigIncludeList, 2,
				customConfigExcludeList, 1);

		validate(exportableList, exportedList, "Dyna Rest");

		DynaRestXMLVO dynaRestXMLVO = null;
		if (exportableList != null && !exportableList.isEmpty()) {
			dynaRestXMLVO = new DynaRestXMLVO();
			for (Object obj : exportableList) {
				if (!exportedList.contains(((JwsDynamicRestDetail) obj).getJwsDynamicRestId())) {
					throw new Exception("Data mismatch while exporting Dyna Rest.");
				}
				dynaRestXMLVO.getDynaRestDetails().add(((JwsDynamicRestDetail) obj).getObject());
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return dynaRestXMLVO;
	}

	private XMLVO retrievePermissionExportData(List<String> systemConfigIncludeList, String moduleType,
			List<String> exportedList) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_PERMISSION_FOR_EXPORT, systemConfigIncludeList, null, null, null);

		validate(exportableList, exportedList, "Permission");

		PermissionXMLVO permissionXMLVO = null;
		if (exportableList != null && !exportableList.isEmpty()) {
			permissionXMLVO = new PermissionXMLVO();
			for (Object obj : exportableList) {
				if (!exportedList.contains(((JwsEntityRoleAssociation) obj).getEntityRoleId())) {
					throw new Exception("Data mismatch while exporting Permission.");
				}
				permissionXMLVO.getJwsRoleDetails().add(((JwsEntityRoleAssociation) obj).getObject());
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return permissionXMLVO;
	}

	private XMLVO retrieveSiteLayoutExportData(List<String> systemConfigIncludeList, String moduleType,
			List<String> exportedList) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_SITE_LAYOUT_DATA_FOR_EXPORT, systemConfigIncludeList, null, null,
				null);

		validate(exportableList, exportedList, "SIte Layout");

		SiteLayoutXMLVO siteLayoutXMLVO = null;
		if (exportableList != null && !exportableList.isEmpty()) {
			siteLayoutXMLVO = new SiteLayoutXMLVO();
			for (Object obj : exportableList) {
				if (!exportedList.contains(((ModuleListing) obj).getModuleId())) {
					throw new Exception("Data mismatch while exporting Site Layout.");
				}
				siteLayoutXMLVO.getModuleListingDetails().add(((ModuleListing) obj).getObject());
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return siteLayoutXMLVO;
	}

	private XMLVO retrieveAppConfigExportData(List<String> systemConfigIncludeList, String moduleType,
			List<String> exportedList) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_APP_CONFIG_DATA_FOR_EXPORT, systemConfigIncludeList, null, null,
				null);

		validate(exportableList, exportedList, "Application Configuration");

		PropertyMasterXMLVO propertyMasterXMLVO = null;
		if (exportableList != null && !exportableList.isEmpty()) {
			propertyMasterXMLVO = new PropertyMasterXMLVO();
			for (Object obj : exportableList) {
				if (!exportedList.contains(((PropertyMaster) obj).getPropertyMasterId())) {
					throw new Exception("Data mismatch while exporting Application Configuration.");
				}
				propertyMasterXMLVO.getApplicationConfiguration().add(((PropertyMaster) obj).getObject());
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return propertyMasterXMLVO;
	}

	private XMLVO retrieveManageUsersExportData(List<String> systemConfigIncludeList, String moduleType,
			List<String> exportedList) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_MANAGE_USERS_DATA_FOR_EXPORT, systemConfigIncludeList, null, null,
				null);

		validate(exportableList, exportedList, "Users");

		UserXMLVO userXMLVO = null;
		if (exportableList != null && !exportableList.isEmpty()) {
			userXMLVO = new UserXMLVO();
			for (Object obj : exportableList) {
				if (!exportedList.contains(((JwsUser) obj).getUserId())) {
					throw new Exception("Data mismatch while exporting Users.");
				}
				userXMLVO.getUserDetails().add(((JwsUser) obj).getObject());
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return userXMLVO;
	}

	private XMLVO retrieveManageRolesExportData(List<String> systemConfigIncludeList, String moduleType,
			List<String> exportedList) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_MANAGE_ROLES_DATA_FOR_EXPORT, systemConfigIncludeList, null, null,
				null);

		validate(exportableList, exportedList, "Roles");

		RoleXMLVO roleXMLVO = null;
		if (exportableList != null && !exportableList.isEmpty()) {
			roleXMLVO = new RoleXMLVO();
			for (Object obj : exportableList) {
				if (!exportedList.contains(((JwsRole) obj).getRoleName())) {
					throw new Exception("Data mismatch while exporting Roles.");
				}
				roleXMLVO.getRoleDetails().add(((JwsRole) obj).getObject());
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return roleXMLVO;
	}

	private XMLVO downloadTemplateExportData(List<String> systemConfigIncludeList, List<String> customConfigExcludeList,
			String downloadFolderLocation, String moduleType, List<String> exportedList) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_TEMPLATE_DATA_FOR_EXPORT, systemConfigIncludeList, 2,
				customConfigExcludeList, 1);

		validate(exportableList, exportedList, "Templates");

		if (exportableList != null && !exportableList.isEmpty()) {
			for (Object obj : exportableList) {
				if (!exportedList.contains(((TemplateMaster) obj).getTemplateId())) {
					throw new Exception("Data mismatch while exporting Templates.");
				}
				templateDownloadUploadModule.exportData(((TemplateMaster) obj), downloadFolderLocation);
			}
			moduleListMap.put(moduleType, Constant.FOLDER_EXPORT_TYPE);

			XMLUtil.generateMetadataXML(null, templateDownloadUploadModule.getModuleDetailsMap(), downloadFolderLocation
					+ File.separator + com.trigyn.jws.templating.utils.Constant.TEMPLATE_DIRECTORY_NAME, version,
					userName, "");
		}
		return null;
	}

	private XMLVO downloadDashletExportData(List<String> systemConfigIncludeList, List<String> customConfigExcludeList,
			String downloadFolderLocation, String moduleType, List<String> exportedList) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_DASHLET_DATA_FOR_EXPORT, systemConfigIncludeList, 2,
				customConfigExcludeList, 1);

		validate(exportableList, exportedList, "Dashlets");

		if (exportableList != null && !exportableList.isEmpty()) {
			for (Object obj : exportableList) {
				if (!exportedList.contains(((Dashlet) obj).getDashletId())) {
					throw new Exception("Data mismatch while exporting Dashlet.");
				}
				dashletDownloadUploadModule.exportData(((Dashlet) obj), downloadFolderLocation);
			}
			moduleListMap.put(moduleType, Constant.FOLDER_EXPORT_TYPE);
			XMLUtil.generateMetadataXML(null, dashletDownloadUploadModule.getModuleDetailsMap(),
					downloadFolderLocation + File.separator + Constants.DASHLET_DIRECTORY_NAME, version, userName, "");
		}
		return null;
	}

	private XMLVO downloadDynamicFormExportData(List<String> systemConfigIncludeList,
			List<String> customConfigExcludeList, String downloadFolderLocation, String moduleType,
			List<String> exportedList) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_DYNAMIC_FORM_DATA_FOR_EXPORT, systemConfigIncludeList, 2,
				customConfigExcludeList, 1);

		validate(exportableList, exportedList, "Dynamic Form");

		if (exportableList != null && !exportableList.isEmpty()) {
			for (Object obj : exportableList) {
				if (!exportedList.contains(((DynamicForm) obj).getFormId())) {
					throw new Exception("Data mismatch while exporting Dynamic Form.");
				}
				dynamicFormDownloadUploadModule.exportData(((DynamicForm) obj), downloadFolderLocation);
			}
			moduleListMap.put(moduleType, Constant.FOLDER_EXPORT_TYPE);
			XMLUtil.generateMetadataXML(null, dynamicFormDownloadUploadModule.getModuleDetailsMap(),
					downloadFolderLocation + File.separator
							+ com.trigyn.jws.dynamicform.utils.Constant.DYNAMIC_FORM_DIRECTORY_NAME,
					version, userName, "");
		}
		return null;
	}

	private XMLVO downloadHelpManualExportData(List<String> systemConfigIncludeList,
			List<String> customConfigExcludeList, String downloadFolderLocation, String moduleType,
			List<String> exportedList) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(
				CrudQueryStore.HQL_QUERY_TO_FETCH_HELP_MANUAL_DATA_FOR_EXPORT, systemConfigIncludeList, 2,
				customConfigExcludeList, 1);

		validate(exportableList, exportedList, "Help Manual");

		if (exportableList != null && !exportableList.isEmpty()) {
			for (Object obj : exportableList) {
				if (!exportedList.contains(((ManualType) obj).getManualId())) {
					throw new Exception("Data mismatch while exporting Help Manual.");
				}
				helpManualImportExportModule.exportData(((ManualType) obj), downloadFolderLocation);
			}
			moduleListMap.put(moduleType, Constant.FOLDER_EXPORT_TYPE);
			XMLUtil.generateMetadataXML(null, helpManualImportExportModule.getModuleDetailsMap(),
					downloadFolderLocation + File.separator + Constant.HELP_MANUAL_DIRECTORY_NAME, version, userName,
					"");
		}

		return null;
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

}
