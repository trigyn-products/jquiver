package com.trigyn.jws.webstarter.utils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Component;

import com.trigyn.jws.dashboard.utility.Constants;
import com.trigyn.jws.dbutils.vo.xml.MetadataXMLVO;
import com.trigyn.jws.dbutils.vo.xml.Modules;
import com.trigyn.jws.dbutils.vo.xml.XMLVO;
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

@Component
public class ImportExportUtility {
	
	public Map<String, XMLVO> readFiles(String targetLocation, MetadataXMLVO metadataXMLVO, 
			Map<String, String> moduleListMap) throws JAXBException {
		Map<String, File>	fileMap = new HashMap<>();
		File	directory	= new File(targetLocation);
		File[]	files		= null;

		if (directory.isDirectory()) {
			files = directory.listFiles();
			for (File file : files) {
				if (file.isFile() && !file.getName().equals("metadata.xml")) {
					fileMap.put(file.getName(), file);
				}
			}
		}
		
		Map<String, XMLVO> xmlVOMap = new HashMap<>();
		if(moduleListMap == null) moduleListMap = new HashMap<>();
		
		if(metadataXMLVO != null && metadataXMLVO.getExportModules() != null && metadataXMLVO.getExportModules().getModule() != null) {
		
			List<Modules>	modules			= metadataXMLVO.getExportModules().getModule();
	
			for (Modules module : modules) {
				String	moduleName	= module.getModuleName();
				String	moduleType	= module.getModuleType();
				XMLVO xmlVO = null;
				String			folderPath = null;
				if (Constant.XML_EXPORT_TYPE.equals(moduleType)) {
					File	file		= fileMap.get(moduleName.toLowerCase() + ".xml");
					String	fileName	= file.getName();
					
					if (fileName.toLowerCase().startsWith(Constant.MasterModuleType.GRID.getModuleType().toLowerCase())) {
						xmlVO = XMLUtil.unMarshaling(GridXMLVO.class, file.getAbsolutePath());
					} else if (fileName.toLowerCase().startsWith(Constant.MasterModuleType.AUTOCOMPLETE.getModuleType().toLowerCase())) {
						xmlVO = XMLUtil.unMarshaling(AutocompleteXMLVO.class, file.getAbsolutePath());
					} else if (fileName.toLowerCase().startsWith(Constant.MasterModuleType.RESOURCEBUNDLE.getModuleType().toLowerCase())) {
						xmlVO = XMLUtil.unMarshaling(ResourceBundleXMLVO.class, file.getAbsolutePath());
					} else if (fileName.toLowerCase().startsWith(Constant.MasterModuleType.DASHBOARD.getModuleType().toLowerCase())) {
						xmlVO = XMLUtil.unMarshaling(DashboardXMLVO.class, file.getAbsolutePath());
					} else if (fileName.toLowerCase().startsWith(Constant.MasterModuleType.NOTIFICATION.getModuleType().toLowerCase())) {
						xmlVO = XMLUtil.unMarshaling(GenericUserNotificationXMLVO.class, file.getAbsolutePath());
					} else if (fileName.toLowerCase().startsWith(Constant.MasterModuleType.PERMISSION.getModuleType().toLowerCase())) {
						xmlVO = XMLUtil.unMarshaling(PermissionXMLVO.class, file.getAbsolutePath());
					} else if (fileName.toLowerCase().startsWith(Constant.MasterModuleType.SITELAYOUT.getModuleType().toLowerCase())) {
						xmlVO = XMLUtil.unMarshaling(SiteLayoutXMLVO.class, file.getAbsolutePath());
					} else if (fileName.toLowerCase().startsWith(Constant.MasterModuleType.APPLICATIONCONFIGURATION.getModuleType().toLowerCase())) {
						xmlVO = XMLUtil.unMarshaling(PropertyMasterXMLVO.class, file.getAbsolutePath());
					} else if (fileName.toLowerCase().startsWith(Constant.MasterModuleType.MANAGEUSERS.getModuleType().toLowerCase())) {
						xmlVO = XMLUtil.unMarshaling(UserXMLVO.class, file.getAbsolutePath());
					} else if (fileName.toLowerCase().startsWith(Constant.MasterModuleType.MANAGEROLES.getModuleType().toLowerCase())) {
						xmlVO = XMLUtil.unMarshaling(RoleXMLVO.class, file.getAbsolutePath());
					} else if (fileName.toLowerCase().startsWith(Constant.MasterModuleType.APICLIENTDETAILS.getModuleType().toLowerCase())) {
						xmlVO = XMLUtil.unMarshaling(ApiClientDetailsXMLVO.class, file.getAbsolutePath());
					} else if (fileName.toLowerCase().startsWith(Constant.MasterModuleType.ADDITIONALDATASOURCE.getModuleType().toLowerCase())) {
						xmlVO = XMLUtil.unMarshaling(AdditionalDatasourceXMLVO.class, file.getAbsolutePath());
					} else if (fileName.toLowerCase().startsWith(Constant.MasterModuleType.SCHEDULER.getModuleType().toLowerCase())) {
						xmlVO = XMLUtil.unMarshaling(SchedulerXMLVO.class, file.getAbsolutePath());
					}
	
					if(xmlVO != null) {
						xmlVOMap.put(fileName, xmlVO);
						moduleListMap.put(moduleName, Constant.XML_EXPORT_TYPE);
					}
				} else if (Constant.FOLDER_EXPORT_TYPE.equals(moduleType)) {
					if (Constant.MasterModuleType.TEMPLATES.getModuleType().equalsIgnoreCase(moduleName)) {
						folderPath	= targetLocation + File.separator
								+ com.trigyn.jws.templating.utils.Constant.TEMPLATE_DIRECTORY_NAME;
					}  else if (Constant.MasterModuleType.DASHLET.getModuleType().equalsIgnoreCase(moduleName)) {
						folderPath	= targetLocation + File.separator + Constants.DASHLET_DIRECTORY_NAME;
					} else if (Constant.MasterModuleType.DYNAMICFORM.getModuleType().equalsIgnoreCase(moduleName)) {
						folderPath	= targetLocation + File.separator + com.trigyn.jws.dynamicform.utils.Constant.DYNAMIC_FORM_DIRECTORY_NAME;
					} else if (Constant.MasterModuleType.HELPMANUAL.getModuleType().equalsIgnoreCase(moduleName)) {
						folderPath	= targetLocation + File.separator + Constant.HELP_MANUAL_DIRECTORY_NAME;
					} else if (Constant.MasterModuleType.FILEMANAGER.getModuleType().equalsIgnoreCase(moduleName)) {
						folderPath	= targetLocation + File.separator + Constant.FILE_BIN_UPLOAD_DIRECTORY_NAME;
					} else if (Constant.MasterModuleType.DYNAREST.getModuleType().equalsIgnoreCase(moduleName)) {
						folderPath	= targetLocation + File.separator + com.trigyn.jws.dynarest.utils.Constants.DYNAMIC_REST_DIRECTORY_NAME;
					}else if (Constant.MasterModuleType.FILEIMPEXPDETAILS.getModuleType().equalsIgnoreCase(moduleName)) {
						folderPath	= targetLocation + File.separator + Constant.FILES_UPLOAD_DIRECTORY_NAME;
					}
					xmlVO 	= readMetaDataXML(folderPath);
					if(xmlVO != null) {
						xmlVOMap.put(moduleName.toLowerCase(), xmlVO);
						moduleListMap.put(moduleName, Constant.FOLDER_EXPORT_TYPE);
					}
				}
			}
			
		}
		
		return xmlVOMap;
	}


	public MetadataXMLVO readMetaDataXML(String filePath) throws JAXBException {
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
	
}
