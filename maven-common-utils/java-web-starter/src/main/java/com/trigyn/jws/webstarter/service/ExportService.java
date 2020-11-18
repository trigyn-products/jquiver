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

import com.trigyn.jws.dashboard.entities.Dashboard;
import com.trigyn.jws.dashboard.entities.Dashlet;
import com.trigyn.jws.dashboard.service.DashletModule;
import com.trigyn.jws.dashboard.utility.Constants;
import com.trigyn.jws.dbutils.entities.ModuleListing;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.dynamicform.entities.FileUploadConfig;
import com.trigyn.jws.dynamicform.service.DynamicFormModule;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDetail;
import com.trigyn.jws.gridutils.entities.GridDetails;
import com.trigyn.jws.notification.entities.GenericUserNotification;
import com.trigyn.jws.resourcebundle.entities.ResourceBundle;
import com.trigyn.jws.templating.entities.TemplateMaster;
import com.trigyn.jws.templating.service.TemplateModule;
import com.trigyn.jws.typeahead.entities.Autocomplete;
import com.trigyn.jws.usermanagement.entities.JwsEntityRoleAssociation;
import com.trigyn.jws.webstarter.dao.CrudQueryStore;
import com.trigyn.jws.webstarter.dao.ImportExportCrudDAO;
import com.trigyn.jws.webstarter.utils.Constant;
import com.trigyn.jws.webstarter.utils.FileUtil;
import com.trigyn.jws.webstarter.utils.XMLUtil;
import com.trigyn.jws.webstarter.utils.ZipUtil;
import com.trigyn.jws.webstarter.xml.AutocompleteXMLVO;
import com.trigyn.jws.webstarter.xml.DashboardXMLVO;
import com.trigyn.jws.webstarter.xml.DynaRestXMLVO;
import com.trigyn.jws.webstarter.xml.FileManagerXMLVO;
import com.trigyn.jws.webstarter.xml.GenericUserNotificationXMLVO;
import com.trigyn.jws.webstarter.xml.GridXMLVO;
import com.trigyn.jws.webstarter.xml.PermissionXMLVO;
import com.trigyn.jws.webstarter.xml.ResourceBundleXMLVO;
import com.trigyn.jws.webstarter.xml.SiteLayoutXMLVO;
import com.trigyn.jws.webstarter.xml.XMLVO;


@Service
@Transactional(readOnly = false)
public class ExportService {
	
	private final static Logger logger = LogManager.getLogger(ExportService.class);

    @Autowired
    private ImportExportCrudDAO 	importExportCrudDAO              	= null;

	@Autowired
	private TemplateModule 			templateDownloadUploadModule 		= null;

	@Autowired
	@Qualifier("dynamic-form")
	private DynamicFormModule 		dynamicFormDownloadUploadModule 	= null;

    @Autowired
	@Qualifier("dashlet")
	private DashletModule 			dashletDownloadUploadModule 		= null;

	@Autowired
	private PropertyMasterDAO 		propertyMasterDAO 					= null;
	
	private Map<String, String> 	moduleListMap						= null;

	@Autowired
	private IUserDetailsService 	detailsService 						= null;

	private String 					version 							= null; 
	
	private String 					userName							= null;
	
	public List<Map<String, Object>> getAllCustomEntity() {
		return importExportCrudDAO.getAllCustomEntity();
	}
	
	public String exportConfigData(HttpServletRequest request, HttpServletResponse response, Map<String, String> out) throws Exception {

		version 		= propertyMasterDAO.findPropertyMasterValue("system", "system", "version");
        UserDetailsVO detailsVO = detailsService.getUserDetails(); 
        userName = detailsVO.getUserName();
		
		String systemPath = System.getProperty("user.dir");
		String tempDownloadPath= FileUtil.generateTemporaryFilePath(Constant.EXPORTTEMPPATH);
		new File(tempDownloadPath).mkdir();
		moduleListMap = new HashMap<>();
		
		String htmlTableJSON = "";
		for(Entry<String,String> obj : out.entrySet()) {
			XMLVO xmlVO = null;
			String moduleType = obj.getKey();
			String exportData = obj.getValue();
			
			if("htmlTableJSON".equals(moduleType)) {
				htmlTableJSON = StringEscapeUtils.unescapeXml("<![CDATA["+ exportData +"]]>");
			} else {
				JSONObject jsonObject = new JSONObject(exportData);
				xmlVO =retrieveDBData(moduleType, jsonObject, tempDownloadPath);
			}
			
			if(xmlVO != null)
				XMLUtil.marshaling(xmlVO, moduleType, tempDownloadPath);
		}
		
		XMLUtil.generateMetadataXML(moduleListMap, null, tempDownloadPath, version, userName, htmlTableJSON);
		String zipFilePath = ZipUtil.zipDirectory(tempDownloadPath, systemPath);
		return zipFilePath;
	}
	
	public void downloadExport(HttpServletRequest request, HttpServletResponse response, String filePath) throws Exception {
		FileUtil.downloadFile(request, response, filePath);
	}
	
	private XMLVO retrieveDBData(String moduleType, JSONObject jsonObject, String downloadFolderLocation) throws Exception {
		
		List<String> systemConfigIncludeList = new ArrayList<String>();
		List<String> customConfigExcludeList = new ArrayList<String>();
		
		JSONArray jsonArray = (JSONArray) jsonObject.get("systemConfigIncludeList");
	    for(int i=0; i < jsonArray.length(); i++) {
	    	systemConfigIncludeList.add(jsonArray.getString(i));
	    }

		jsonArray = (JSONArray) jsonObject.get("customConfigExcludeList");
	    for(int i=0; i < jsonArray.length(); i++) {
	    	customConfigExcludeList.add(jsonArray.getString(i));
	    }
	    if(customConfigExcludeList.size() == 0) {
	    	customConfigExcludeList.add("");
	    }
	    
		if (moduleType.equals(Constant.MasterModuleType.GRID.getModuleType())) {
			return retrieveGridExportData(systemConfigIncludeList, customConfigExcludeList, moduleType);
		} else if (moduleType.equals(Constant.MasterModuleType.RESOURCEBUNDLE.getModuleType())) {
			return retrieveRBExportData(systemConfigIncludeList, customConfigExcludeList, moduleType);
		} else if (moduleType.equals(Constant.MasterModuleType.AUTOCOMPLETE.getModuleType())) {
			return retrieveAutocompleteExportData(systemConfigIncludeList, customConfigExcludeList, moduleType);
		} else if (moduleType.equals(Constant.MasterModuleType.NOTIFICATION.getModuleType())) {
			return retrieveNotificationExportData(customConfigExcludeList, moduleType);
		} else if (moduleType.equals(Constant.MasterModuleType.DASHBOARD.getModuleType())) {
			return downloadDashboardExportData(systemConfigIncludeList, customConfigExcludeList, moduleType);
		} else if (moduleType.equals(Constant.MasterModuleType.FILEMANAGER.getModuleType())) {
			return retrieveFileManagerExportData(customConfigExcludeList, moduleType);
		} else if (moduleType.equals(Constant.MasterModuleType.DYNAREST.getModuleType())) {
			return downloadDynaRestExportData(systemConfigIncludeList, customConfigExcludeList, moduleType);
		} else if (moduleType.equals(Constant.MasterModuleType.PERMISSION.getModuleType())) {
			return retrievePermissionExportData(systemConfigIncludeList, moduleType);
		} else if (moduleType.equals(Constant.MasterModuleType.SITE_LAYOUT.getModuleType())) {
			return retrieveSiteLayoutExportData(systemConfigIncludeList, moduleType);
		} else if (moduleType.equals(Constant.MasterModuleType.TEMPLATE.getModuleType())) {
			return downloadTemplateExportData(systemConfigIncludeList, customConfigExcludeList, downloadFolderLocation, moduleType);
		} else if (moduleType.equals(Constant.MasterModuleType.DASHLET.getModuleType())) {
			return downloadDashletExportData(systemConfigIncludeList, customConfigExcludeList, downloadFolderLocation, moduleType);
		} else if (moduleType.equals(Constant.MasterModuleType.DYNAMICFORM.getModuleType())) {
			return downloadDynamicFormExportData(systemConfigIncludeList, customConfigExcludeList, downloadFolderLocation, moduleType);
		} else {
			return null;
		}
	}

	private XMLVO retrieveGridExportData(List<String> systemConfigIncludeList, List<String> customConfigExcludeList, String moduleType)
			throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(CrudQueryStore.HQL_QUERY_TO_FETCH_GRID_DATA_FOR_EXPORT, 
				systemConfigIncludeList, 2, customConfigExcludeList, 1);

		GridXMLVO gridXMLVO = null;		
		if(exportableList != null && !exportableList.isEmpty()) {
			gridXMLVO = new GridXMLVO();
			for(Object obj : exportableList) {
				gridXMLVO.getGridDetails().add(((GridDetails)obj).getObject());
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return gridXMLVO;
	}

	private XMLVO retrieveRBExportData(List<String> systemConfigIncludeList, List<String> customConfigExcludeList, String moduleType)
			throws Exception {
		List<Object> exportableList = importExportCrudDAO.getRBExportableData(CrudQueryStore.HQL_QUERY_TO_FETCH_RESOURCE_BUNDLE_DATA_FOR_EXPORT, 
				systemConfigIncludeList, customConfigExcludeList);

		ResourceBundleXMLVO resourceBundleXMLVO = null;
		if(exportableList != null && !exportableList.isEmpty()) {
			resourceBundleXMLVO = new ResourceBundleXMLVO();
			for(Object obj : exportableList) {
				resourceBundleXMLVO.getResourceBundleDetails().add(((ResourceBundle)obj).getObject());
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return resourceBundleXMLVO;
	}

	private XMLVO retrieveAutocompleteExportData(List<String> systemConfigIncludeList,
			List<String> customConfigExcludeList, String moduleType) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(CrudQueryStore.HQL_QUERY_TO_FETCH_AUTOCOMPLETE_DATA_FOR_EXPORT, 
				systemConfigIncludeList, 2, customConfigExcludeList, 1);

		AutocompleteXMLVO autocompleteXMLVO = null;
		if(exportableList != null && !exportableList.isEmpty()) {
			autocompleteXMLVO = new AutocompleteXMLVO();
			for(Object obj : exportableList) {
				autocompleteXMLVO.getAutocompleteDetails().add(((Autocomplete)obj).getObject());
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return autocompleteXMLVO;
	}

	private XMLVO retrieveNotificationExportData(List<String> customConfigExcludeList, String moduleType) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(CrudQueryStore.HQL_QUERY_TO_FETCH_NOTIFICATAION_DATA_FOR_EXPORT, 
				null, null, customConfigExcludeList, null);

		GenericUserNotificationXMLVO genericUserNotificationXMLVO = null;
		if(exportableList != null && !exportableList.isEmpty()) {
			genericUserNotificationXMLVO = new GenericUserNotificationXMLVO();
			for(Object obj : exportableList) {
				genericUserNotificationXMLVO.getGenericUserNotificationDetails()
					.add(((GenericUserNotification)obj).getObject());
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return genericUserNotificationXMLVO;
	}

	private XMLVO downloadDashboardExportData(List<String> systemConfigIncludeList,
			List<String> customConfigExcludeList, String moduleType) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(CrudQueryStore.HQL_QUERY_TO_FETCH_DASHBOARD_DATA_FOR_EXPORT, 
				systemConfigIncludeList, 2, customConfigExcludeList, 1);

		DashboardXMLVO dashboardXMLVO = null;
		if(exportableList != null && !exportableList.isEmpty()) {
			dashboardXMLVO = new DashboardXMLVO();
			for(Object obj : exportableList) {
				dashboardXMLVO.getDashboardDetails().add(((Dashboard)obj).getObject());
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return dashboardXMLVO;
	}

	private XMLVO retrieveFileManagerExportData(List<String> customConfigExcludeList, String moduleType) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(CrudQueryStore.HQL_QUERY_TO_FETCH_FILE_MANAGER_DATA_FOR_EXPORT, 
				null, null, customConfigExcludeList, null);

		FileManagerXMLVO fileManagerXMLVO = null;
		if(exportableList != null && !exportableList.isEmpty()) {
			fileManagerXMLVO = new FileManagerXMLVO();
			for(Object obj : exportableList) {
				fileManagerXMLVO.getFileUploadDetails().add(((FileUploadConfig)obj).getObject());
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return fileManagerXMLVO;
	}

	private XMLVO downloadDynaRestExportData(List<String> systemConfigIncludeList,
			List<String> customConfigExcludeList, String moduleType) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(CrudQueryStore.HQL_QUERY_TO_FETCH_DYNA_REST_DATA_FOR_EXPORT, 
				systemConfigIncludeList, 2, customConfigExcludeList, 1);
		DynaRestXMLVO dynaRestXMLVO = null;
		if(exportableList != null && !exportableList.isEmpty()) {
			dynaRestXMLVO = new DynaRestXMLVO();
			for(Object obj : exportableList) {
				dynaRestXMLVO.getDynaRestDetails().add(((JwsDynamicRestDetail)obj).getObject());
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return dynaRestXMLVO;
	}

	private XMLVO retrievePermissionExportData(List<String> systemConfigIncludeList, String moduleType) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(CrudQueryStore.HQL_QUERY_TO_FETCH_PERMISSION_FOR_EXPORT, 
				systemConfigIncludeList, null, null, null);

		PermissionXMLVO permissionXMLVO = null;
		if(exportableList != null && !exportableList.isEmpty()) {
			permissionXMLVO = new PermissionXMLVO();
			for(Object obj : exportableList) {
				permissionXMLVO.getJwsRoleDetails().add(((JwsEntityRoleAssociation)obj).getObject());
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return permissionXMLVO;
	}

	private XMLVO retrieveSiteLayoutExportData(List<String> systemConfigIncludeList, String moduleType) throws Exception {
		List<Object> exportableList;
		exportableList = importExportCrudDAO.getAllExportableData(CrudQueryStore.HQL_QUERY_TO_FETCH_SITE_LAYOUT_DATA_FOR_EXPORT, 
				systemConfigIncludeList, null, null, null);

		SiteLayoutXMLVO siteLayoutXMLVO = null;
		if(exportableList != null && !exportableList.isEmpty()) {
			siteLayoutXMLVO = new SiteLayoutXMLVO();
			for(Object obj : exportableList) {
				siteLayoutXMLVO.getModuleListingDetails().add(((ModuleListing)obj).getObject());
			}
			moduleListMap.put(moduleType, Constant.XML_EXPORT_TYPE);
		}
		return siteLayoutXMLVO;
	}

	private XMLVO downloadTemplateExportData(List<String> systemConfigIncludeList, List<String> customConfigExcludeList, 
			String downloadFolderLocation, String moduleType) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(CrudQueryStore.HQL_QUERY_TO_FETCH_TEMPLATE_DATA_FOR_EXPORT, 
				systemConfigIncludeList, 2, customConfigExcludeList, 1);
		if(exportableList != null && !exportableList.isEmpty()) {
			for(Object obj : exportableList) {
				templateDownloadUploadModule.downloadCodeToLocal(((TemplateMaster)obj), downloadFolderLocation);
			}
			moduleListMap.put(moduleType, Constant.FOLDER_EXPORT_TYPE);
			
			XMLUtil.generateMetadataXML(null, templateDownloadUploadModule.getModuleDetailsMap(), downloadFolderLocation+File.separator+
					com.trigyn.jws.templating.utils.Constant.TEMPLATE_DIRECTORY_NAME, version, userName,"");
		}
		return null;
	}

	private XMLVO downloadDashletExportData(List<String> systemConfigIncludeList, List<String> customConfigExcludeList, 
			String downloadFolderLocation, String moduleType) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(CrudQueryStore.HQL_QUERY_TO_FETCH_DASHLET_DATA_FOR_EXPORT, 
				systemConfigIncludeList, 2, customConfigExcludeList, 1);
		if(exportableList != null && !exportableList.isEmpty()) {
			for(Object obj : exportableList) {
				dashletDownloadUploadModule.downloadCodeToLocal(((Dashlet)obj), downloadFolderLocation);
			}
			moduleListMap.put(moduleType, Constant.FOLDER_EXPORT_TYPE);
			XMLUtil.generateMetadataXML(null, dashletDownloadUploadModule.getModuleDetailsMap(), 
					downloadFolderLocation+File.separator+Constants.DASHLET_DIRECTORY_NAME, version, userName,"");
		}
		return null;
	}
	
	private XMLVO downloadDynamicFormExportData(List<String> systemConfigIncludeList, List<String> customConfigExcludeList, 
			String downloadFolderLocation, String moduleType) throws Exception {
		List<Object> exportableList = importExportCrudDAO.getAllExportableData(CrudQueryStore.HQL_QUERY_TO_FETCH_DYNAMIC_FORM_DATA_FOR_EXPORT, 
				systemConfigIncludeList, 2, customConfigExcludeList, 1);
		if(exportableList != null && !exportableList.isEmpty()) {
			for(Object obj : exportableList) {
				dynamicFormDownloadUploadModule.downloadCodeToLocal(((DynamicForm)obj), downloadFolderLocation);
			}
			moduleListMap.put(moduleType, Constant.FOLDER_EXPORT_TYPE);
			XMLUtil.generateMetadataXML(null, dynamicFormDownloadUploadModule.getModuleDetailsMap(), downloadFolderLocation+File.separator+
						com.trigyn.jws.dynamicform.utils.Constant.DYNAMIC_FORM_DIRECTORY_NAME, version, userName,"");
		}
		return null;
	}
	
}
