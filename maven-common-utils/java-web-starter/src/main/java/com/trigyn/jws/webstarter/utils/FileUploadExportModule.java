package com.trigyn.jws.webstarter.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import com.trigyn.jws.dbutils.vo.xml.FileUploadConfigExportVO;
import com.trigyn.jws.dbutils.vo.xml.FileUploadExportVO;
import com.trigyn.jws.dynarest.dao.FileUploadConfigDAO;
import com.trigyn.jws.dynarest.dao.JwsDynarestDAO;
import com.trigyn.jws.dynarest.entities.FileUpload;
import com.trigyn.jws.dynarest.entities.FileUploadConfig;
import com.trigyn.jws.dynarest.repository.FileUploadRepository;
import com.trigyn.jws.dynarest.service.FilesStorageServiceImpl;
import com.trigyn.jws.webstarter.vo.FileUploadConfigImportEntity;

@Component
public class FileUploadExportModule {

	private Map<String, Map<String, Object>>	moduleDetailsMap		= new HashMap<>();

	@Autowired
	private FileUploadRepository				fileUploadRepository	= null;
	
	@Autowired
	private FileUploadConfigDAO fileUploadConfigDAO = null;

	public void exportData(Object object, String folderLocation) throws Exception {
		FileUploadConfig fileUploadConfig = (FileUploadConfig) object;

		if (!new File(folderLocation + File.separator + Constant.FILE_BIN_UPLOAD_DIRECTORY_NAME).exists()) {
			File fileDirectory = new File(folderLocation + File.separator + Constant.FILE_BIN_UPLOAD_DIRECTORY_NAME);
			fileDirectory.mkdirs();
		}

		List<FileUpload>			fileUploads				= fileUploadRepository
				.findAllByFileBinId(fileUploadConfig.getFileBinId());
		List<FileUploadExportVO>	fileUploadExportVOList	= new ArrayList<>();
		for (FileUpload fu : fileUploads) {
			fileUploadExportVOList.add(new FileUploadExportVO(fu.getFileUploadId(), fu.getPhysicalFileName(),
					fu.getOriginalFileName(), fu.getFilePath(), fu.getUpdatedBy(), fu.getLastUpdatedTs(),
					fu.getFileBinId(), fu.getFileAssociationId()));

			if (!new File(folderLocation + File.separator + Constant.FILE_BIN_UPLOAD_DIRECTORY_NAME + File.separator
					+ fileUploadConfig.getFileBinId()).exists()) {
				File fileDirectory = new File(folderLocation + File.separator + Constant.FILE_BIN_UPLOAD_DIRECTORY_NAME
						+ File.separator + fileUploadConfig.getFileBinId());
				fileDirectory.mkdirs();
			}

			Path		downloadPath	= Paths.get(folderLocation + File.separator
					+ Constant.FILE_BIN_UPLOAD_DIRECTORY_NAME + File.separator + fileUploadConfig.getFileBinId());

			Path		fileRoot		= Paths.get(fu.getFilePath());
			Path		filePath		= fileRoot.resolve(fu.getPhysicalFileName());
			Resource	resource		= new UrlResource(filePath.toUri());
			InputStream	in;
			if (resource.exists() || resource.isReadable()) {
				File newFile = resource.getFile();
				in = new FileInputStream(newFile);
			} else {
				String filePathStr = fu.getFilePath() + File.separator + fu.getPhysicalFileName();
				in = FilesStorageServiceImpl.class.getResourceAsStream(filePathStr);
				if (in == null) {
					throw new RuntimeException("Could not read the file! Location: " + filePathStr);
				}
			}
			Files.deleteIfExists(downloadPath.resolve(fu.getPhysicalFileName()));
			Files.copy(in, downloadPath.resolve(fu.getPhysicalFileName()));
		}
		List<String> uploadScriptLibIdList = fileUploadConfigDAO.getFileBinScriptLibId("upload_"+fileUploadConfig.getFileBinId());
		List<String> viewScriptLibIdList   = fileUploadConfigDAO.getFileBinScriptLibId("view_"+fileUploadConfig.getFileBinId());
		List<String> deleteScriptLibIdList = fileUploadConfigDAO.getFileBinScriptLibId("delete_"+fileUploadConfig.getFileBinId());
		List<String>					scriptLibUploadId	= new ArrayList<>();
		List<String>					scriptLibViewId		= new ArrayList<>();
		List<String>					scriptLibDeleteId	= new ArrayList<>();
		for(int iScriptUploadCounter = 0 ; iScriptUploadCounter<uploadScriptLibIdList.size(); iScriptUploadCounter++) {
			scriptLibUploadId.add("\"" +uploadScriptLibIdList.get(iScriptUploadCounter)+ "\"");
			fileUploadConfig.setUploadScriptLibraryId(scriptLibUploadId.toString());
		}
		for(int iScriptViewCounter = 0 ; iScriptViewCounter<viewScriptLibIdList.size(); iScriptViewCounter++) {
			scriptLibViewId.add("\"" +viewScriptLibIdList.get(iScriptViewCounter)+ "\"");
			fileUploadConfig.setViewScriptLibraryId(scriptLibViewId.toString());
		}
		for(int iScriptDeleteCounter = 0 ; iScriptDeleteCounter<deleteScriptLibIdList.size(); iScriptDeleteCounter++) {
			scriptLibDeleteId.add("\"" +deleteScriptLibIdList.get(iScriptDeleteCounter)+ "\"");
			fileUploadConfig.setDeleteScriptLibraryId(scriptLibDeleteId.toString());
		}
		FileUploadConfigExportVO	fileUploadConfigExportVO	= new FileUploadConfigExportVO(
				fileUploadConfig.getFileBinId(), fileUploadConfig.getFileTypSupported(),
				fileUploadConfig.getMaxFileSize(), fileUploadConfig.getNoOfFiles(),
				StringEscapeUtils.unescapeXml("<![CDATA[" + fileUploadConfig.getUploadQueryContent().trim() + "]]>"),
				StringEscapeUtils.unescapeXml("<![CDATA[" + fileUploadConfig.getViewQueryContent().trim() + "]]>"),
				StringEscapeUtils.unescapeXml("<![CDATA[" + fileUploadConfig.getDeleteQueryContent().trim() + "]]>"),
				fileUploadConfig.getDatasourceId(),fileUploadConfig.getIsDeleted(),fileUploadConfig.getCreatedBy(),fileUploadConfig.getCreatedDate(),
				fileUploadConfig.getLastUpdatedBy(),fileUploadConfig.getLastUpdatedTs(),fileUploadConfig.getUploadQueryType()
				,fileUploadConfig.getDeleteQueryType(),fileUploadConfig.getViewQueryType(),fileUploadConfig.getDatasourceUploadValidator()
				,fileUploadConfig.getDatasourceDeleteValidator(),fileUploadConfig.getDatasourceViewValidator(),fileUploadConfig.getIsCustomUpdated(),
				fileUploadConfig.getUploadScriptLibraryId(),fileUploadConfig.getViewScriptLibraryId(),fileUploadConfig.getDeleteScriptLibraryId()
				);

		Map<String, Object>			map							= new HashMap<>();
		map.put("moduleName", fileUploadConfig.getFileBinId());
		map.put("moduleObject", fileUploadConfigExportVO);
		moduleDetailsMap.put(fileUploadConfig.getFileBinId(), map);

	}

	public FileUploadConfigImportEntity importData(String folderLocation, String uploadFileName, String uploadID,
			Object importObject) {
		FileUploadConfigImportEntity	fileUploadConfigImportEntity	= null;

		FileUploadConfigExportVO		fileUploadConfigVO				= (FileUploadConfigExportVO) importObject;
		FileUploadConfig				fileUploadConfig				= new FileUploadConfig(
				fileUploadConfigVO.getFileBinId(), fileUploadConfigVO.getFileTypSupported(),
				fileUploadConfigVO.getMaxFileSize(), fileUploadConfigVO.getNoOfFiles(),
				fileUploadConfigVO.getUploadQueryContent(), fileUploadConfigVO.getViewQueryContent(),
				fileUploadConfigVO.getDeleteQueryContent(), fileUploadConfigVO.getDataSourceId(),
				fileUploadConfigVO.getIsDeleted(), fileUploadConfigVO.getCreatedBy(),
				fileUploadConfigVO.getCreatedDate(), fileUploadConfigVO.getUpdatedBy(),fileUploadConfigVO.getUpdatedDate(),
				fileUploadConfigVO.getUploadQueryType(),fileUploadConfigVO.getViewQueryType(),fileUploadConfigVO.getDeleteQueryType(),
				fileUploadConfigVO.getDatasourceViewValidator(),fileUploadConfigVO.getDatasourceUploadValidator(),
				fileUploadConfigVO.getDatasourceDeleteValidator(),fileUploadConfigVO.getIsCustomUpdated(),fileUploadConfigVO.getUploadScriptLibraryId(),fileUploadConfigVO.getViewScriptLibraryId(),fileUploadConfigVO.getDeleteScriptLibraryId());

		List<FileUpload>				fileUploads						= new ArrayList<>();

		fileUploadConfigImportEntity = new FileUploadConfigImportEntity(fileUploadConfig, fileUploads);

		return fileUploadConfigImportEntity;
	}

	public Map<String, Map<String, Object>> getModuleDetailsMap() {
		return moduleDetailsMap;
	}

	public void setModuleDetailsMap(Map<String, Map<String, Object>> moduleDetailsMap) {
		this.moduleDetailsMap = moduleDetailsMap;
	}

}
