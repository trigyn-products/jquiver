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
import com.trigyn.jws.webstarter.vo.ExportModule;
import com.trigyn.jws.webstarter.vo.Modules;
import com.trigyn.jws.webstarter.vo.Settings;
import com.trigyn.jws.webstarter.xml.MetadataXMLVO;
import com.trigyn.jws.webstarter.xml.XMLVO;

public class XMLUtil {

	public static void generateMetadataXML(Map<String, String> moduleListMap, Map<String, String> exportFolderData, 
			String downloadLocation, String version, String userName, String htmlTableJSON) throws Exception {
		MetadataXMLVO metaDataXMLVO = new MetadataXMLVO();

		Settings settings = new Settings();
		settings.setTimestamp(new Date());
		settings.setCurrentVersion(version);
		settings.setAuthor(userName);
		settings.setLeastSupportedVersion("1.0");
		
		ExportModule exportModule = new ExportModule();
		
		if(exportFolderData != null && !exportFolderData.isEmpty()) {
			List<Modules> exportModuleList = new ArrayList<>();
			
			for(Entry<String, String> entry : exportFolderData.entrySet()) {
				String moduleID = entry.getKey();
				String moduleName = entry.getValue();
	
				Modules module = new Modules();
				module.setModuleID(moduleID);
				module.setModuleName(moduleName);
				exportModuleList.add(module);
			}
			exportModule.setModule(exportModuleList);
			
			metaDataXMLVO.setSettings(settings);
			metaDataXMLVO.setExportModules(exportModule);
			marshaling(metaDataXMLVO, "medataData", downloadLocation);
			
		} else if(moduleListMap != null && !moduleListMap.isEmpty()) {
			List<Modules> exportModuleList = new ArrayList<>();
			
			for(Entry<String, String> entry : moduleListMap.entrySet()) {
				String moduleName = entry.getKey();
				String moduleType = entry.getValue();
	
				Modules module = new Modules();
				module.setModuleType(moduleType);
				module.setModuleName(moduleName);
				exportModuleList.add(module);
			}
			exportModule.setModule(exportModuleList);
			
			metaDataXMLVO.setSettings(settings);
			metaDataXMLVO.setExportModules(exportModule);
			metaDataXMLVO.setInfo(htmlTableJSON);
			marshaling(metaDataXMLVO, "medataData", downloadLocation);
			
		}
	}

	public static void marshaling(XMLVO xmlVO, String fileName, String downloadLocation) throws JAXBException {
	    JAXBContext jaxbContext = JAXBContext.newInstance(xmlVO.getClass());;
	    
	    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "Unicode");
	    jaxbMarshaller.setProperty(CharacterEscapeHandler.class.getName(), new CustomCharacterEscapeHandler());
	    jaxbMarshaller.marshal(xmlVO, new File(downloadLocation + File.separator + fileName.toLowerCase()+".xml"));
	}

	public static XMLVO unMarshaling(XMLVO xmlVO, String xmlFilePath) throws JAXBException {

        File xmlFile = new File(xmlFilePath);
         
	    JAXBContext jaxbContext = JAXBContext.newInstance(xmlVO.getClass());;
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	    XMLVO outputXMLVO = (XMLVO) jaxbUnmarshaller.unmarshal(xmlFile);
        
	    return outputXMLVO;
	}
	
	

}
