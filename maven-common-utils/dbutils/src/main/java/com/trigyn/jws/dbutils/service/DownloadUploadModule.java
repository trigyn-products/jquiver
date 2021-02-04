package com.trigyn.jws.dbutils.service;

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

import org.springframework.stereotype.Component;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;
import com.trigyn.jws.dbutils.utils.CustomCharacterEscapeHandler;
import com.trigyn.jws.dbutils.vo.xml.DashletExportVO;
import com.trigyn.jws.dbutils.vo.xml.DynamicFormExportVO;
import com.trigyn.jws.dbutils.vo.xml.ExportModule;
import com.trigyn.jws.dbutils.vo.xml.MetadataXMLVO;
import com.trigyn.jws.dbutils.vo.xml.Modules;
import com.trigyn.jws.dbutils.vo.xml.Settings;
import com.trigyn.jws.dbutils.vo.xml.TemplateExportVO;
import com.trigyn.jws.dbutils.vo.xml.XMLVO;

@Component
public interface DownloadUploadModule<T> {

	void downloadCodeToLocal(T object, String folderLocation) throws Exception;

	void uploadCodeToDB(String uploadFileName) throws Exception;

	void exportData(Object object, String folderLocation) throws Exception;

	Object importData(String folderLocation, String uploadFileName, String uploadID, Object importObject)
			throws Exception;

	public default void generateMetadataXML(MetadataXMLVO metaDataXMLVO,
			Map<String, Map<String, Object>> exportFolderData, String downloadLocation, String version, String userName)
			throws Exception {
		if (metaDataXMLVO == null) {
			metaDataXMLVO = new MetadataXMLVO();
			ExportModule exportModule = new ExportModule();
			exportModule.setModule(new ArrayList<>());
			metaDataXMLVO.setExportModules(exportModule);
		}

		Settings settings = new Settings();
		settings.setTimestamp(new Date());
		settings.setCurrentVersion(version);
		settings.setAuthor(userName);
		settings.setLeastSupportedVersion("1.0");

		ExportModule	exportModule		= metaDataXMLVO.getExportModules();
		List<Modules>	exportModuleList	= exportModule.getModule();

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
			}

			exportModuleList.add(module);
		}
		exportModule.setModule(exportModuleList);

		metaDataXMLVO.setSettings(settings);
		metaDataXMLVO.setExportModules(exportModule);
		marshaling(metaDataXMLVO, "metadata", downloadLocation);

	}

	public default void marshaling(XMLVO xmlVO, String fileName, String downloadLocation) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(xmlVO.getClass());
		;

		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "Unicode");
		jaxbMarshaller.setProperty(CharacterEscapeHandler.class.getName(), new CustomCharacterEscapeHandler());
		jaxbMarshaller.marshal(xmlVO, new File(downloadLocation + File.separator + fileName.toLowerCase() + ".xml"));
	}

	public default XMLVO unMarshaling(Class xmlVOClass, String xmlFilePath) throws JAXBException {

		File		xmlFile		= new File(xmlFilePath);

		JAXBContext	jaxbContext	= JAXBContext.newInstance(xmlVOClass);
		;
		Unmarshaller	jaxbUnmarshaller	= jaxbContext.createUnmarshaller();
		XMLVO			outputXMLVO			= (XMLVO) jaxbUnmarshaller.unmarshal(xmlFile);

		return outputXMLVO;
	}

}
