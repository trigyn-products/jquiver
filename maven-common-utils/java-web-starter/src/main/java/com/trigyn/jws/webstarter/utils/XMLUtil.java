package com.trigyn.jws.webstarter.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;
import com.trigyn.jws.dbutils.utils.CustomCharacterEscapeHandler;
import com.trigyn.jws.dbutils.vo.xml.DashletExportVO;
import com.trigyn.jws.dbutils.vo.xml.DynaRestExportVO;
import com.trigyn.jws.dbutils.vo.xml.DynamicFormExportVO;
import com.trigyn.jws.dbutils.vo.xml.ExportModule;
import com.trigyn.jws.dbutils.vo.xml.FileUploadConfigExportVO;
import com.trigyn.jws.dbutils.vo.xml.FilesImportExportVO;
import com.trigyn.jws.dbutils.vo.xml.HelpManualTypeExportVO;
import com.trigyn.jws.dbutils.vo.xml.MetadataXMLVO;
import com.trigyn.jws.dbutils.vo.xml.Modules;
import com.trigyn.jws.dbutils.vo.xml.Settings;
import com.trigyn.jws.dbutils.vo.xml.TemplateExportVO;
import com.trigyn.jws.dbutils.vo.xml.XMLVO;

public class XMLUtil {

	public static void generateMetadataXML(Map<String, String> moduleListMap,
			Map<String, Map<String, Object>> exportFolderData, String downloadLocation, String version, String userName,
			String htmlTableJSON) throws Exception {
		MetadataXMLVO	metaDataXMLVO	= new MetadataXMLVO();

		Settings		settings		= new Settings();
		settings.setTimestamp(new Date());
		settings.setCurrentVersion(version);
		settings.setAuthor(userName);
		settings.setLeastSupportedVersion("1.0");

		ExportModule exportModule = new ExportModule();

		if (exportFolderData != null && !exportFolderData.isEmpty()) {
			List<Modules> exportModuleList = new ArrayList<>();

			for (Entry<String, Map<String, Object>> entry : exportFolderData.entrySet()) {
				String				moduleID	= entry.getKey();
				Map<String, Object>	map			= entry.getValue();

				Modules				module		= new Modules();
				module.setModuleID(moduleID);
				module.setModuleName((String) map.get("moduleName"));

				if (map.get("moduleObject") instanceof TemplateExportVO) {
					module.setTemplate((TemplateExportVO) map.get("moduleObject"));
				} else if (map.get("moduleObject") instanceof DashletExportVO) {
					module.setDashlet((DashletExportVO) map.get("moduleObject"));
				} else if (map.get("moduleObject") instanceof DynamicFormExportVO) {
					module.setDynamicForm((DynamicFormExportVO) map.get("moduleObject"));
				} else if (map.get("moduleObject") instanceof DynamicFormExportVO) {
					module.setDynamicForm((DynamicFormExportVO) map.get("moduleObject"));
				} else if (map.get("moduleObject") instanceof HelpManualTypeExportVO) {
					module.setHelpManual((HelpManualTypeExportVO) map.get("moduleObject"));
				} else if (map.get("moduleObject") instanceof FileUploadConfigExportVO) {
					module.setFileBin((FileUploadConfigExportVO) map.get("moduleObject"));
				} else if (map.get("moduleObject") instanceof DynaRestExportVO) {
					module.setDynaRestExportVO((DynaRestExportVO) map.get("moduleObject"));
				}else if (map.get("moduleObject") instanceof FilesImportExportVO) {
					module.setFiles((FilesImportExportVO) map.get("moduleObject"));
				}

				exportModuleList.add(module);
			}
			exportModule.setModule(exportModuleList);

			metaDataXMLVO.setSettings(settings);
			metaDataXMLVO.setExportModules(exportModule);
			marshaling(metaDataXMLVO, "metadata", downloadLocation);

		} else if (moduleListMap != null && !moduleListMap.isEmpty()) {
			List<Modules> exportModuleList = new ArrayList<>();

			for (Entry<String, String> entry : moduleListMap.entrySet()) {
				String	moduleName	= entry.getKey();
				String	moduleType	= entry.getValue();

				Modules	module		= new Modules();
				module.setModuleType(moduleType);
				module.setModuleName(moduleName);
				exportModuleList.add(module);
			}
			exportModule.setModule(exportModuleList);

			metaDataXMLVO.setSettings(settings);
			metaDataXMLVO.setExportModules(exportModule);
			metaDataXMLVO.setInfo(htmlTableJSON);
			marshaling(metaDataXMLVO, "metadata", downloadLocation);

		}
	}

	public static void marshaling(XMLVO xmlVO, String fileName, String downloadLocation) throws JAXBException {
		JAXBContext	jaxbContext		= JAXBContext.newInstance(xmlVO.getClass());

		Marshaller	jaxbMarshaller	= jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "Unicode");
		jaxbMarshaller.setProperty(CharacterEscapeHandler.class.getName(), new CustomCharacterEscapeHandler());
		jaxbMarshaller.marshal(xmlVO, new File(downloadLocation + File.separator + fileName.toLowerCase() + ".xml"));
	}

	public static XMLVO unMarshaling(Class xmlVOClass, String xmlFilePath) throws JAXBException {

		File		xmlFile		= new File(xmlFilePath);

		JAXBContext	jaxbContext	= JAXBContext.newInstance(xmlVOClass);
		Unmarshaller	jaxbUnmarshaller	= jaxbContext.createUnmarshaller();
		XMLVO			outputXMLVO			= (XMLVO) jaxbUnmarshaller.unmarshal(xmlFile);

		return outputXMLVO;
	}

}
