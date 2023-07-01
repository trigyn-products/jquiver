package com.trigyn.jws.webstarter.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import com.trigyn.jws.dbutils.service.DownloadUploadModule;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.vo.xml.FileUploadExportVO;
import com.trigyn.jws.dynarest.entities.FileUpload;
import com.trigyn.jws.dynarest.service.FilesStorageServiceImpl;
import com.trigyn.jws.webstarter.vo.FileUploadImportEntity;
import com.trigyn.jws.webstarter.xml.FileUploadXMLVO;

/**
 * This class is used for File Import Export
 * @author Shrinath.Halki
 */
@Component("fileImportExportModule")
public class FileImportExportModule implements DownloadUploadModule<FileUpload> {

	private Map<String, Map<String, Object>>	moduleDetailsMap		= new HashMap<>();

	@Autowired
	private PropertyMasterService				propertyMasterService	= null;

	@Override
	public void downloadCodeToLocal(FileUpload object, String folderLocation) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void uploadCodeToDB(String uploadFileName) throws Exception {

	}

	@Override
	public void exportData(Object object, String folderLocation) throws Exception {

		if (object instanceof FileUploadXMLVO) {

			FileUploadXMLVO				fileUploadXMLVO			= (FileUploadXMLVO) object;
			List<FileUpload>			fileUploadDetails		= fileUploadXMLVO.getFileUploadDetails();
			SimpleDateFormat			formattedDate			= new SimpleDateFormat("yyyy/MM/dd");
			List<FileUploadExportVO>	fileUploadExportVOList	= new ArrayList<>();
			String fileBinId = null;
			for (FileUpload fu : fileUploadDetails) {
				int		filePathIdx			= -1;
				String	exportPath			= null;
				String	targetFolder		= propertyMasterService.findPropertyMasterValue("file-upload-location");
				Path	targetFolderpath	= Paths.get(targetFolder);
				Path	actFilepath			= Paths.get(fu.getFilePath());
				if (actFilepath.isAbsolute() == false) {
					exportPath = fu.getFilePath();
				} else if (actFilepath.toString().contains(targetFolderpath.toString())
						&& actFilepath.isAbsolute() == true) {
					int folderPathIdx = fu.getFilePath().indexOf(targetFolder);
					if (folderPathIdx > -1) {
						filePathIdx	= folderPathIdx + targetFolder.length();
						exportPath	= fu.getFilePath().substring(filePathIdx);
					} else {
						String strFormattedDate = formattedDate.format(fu.getLastUpdatedTs());
						exportPath = Paths.get(strFormattedDate).toString();
					}
				} else {
					String strFormattedDate = formattedDate.format(fu.getLastUpdatedTs());
					exportPath = Paths.get(strFormattedDate).toString();
				}

				FileUploadExportVO fileUploadExportVO = new FileUploadExportVO(fu.getFileUploadId(),
						fu.getPhysicalFileName(), fu.getOriginalFileName(), exportPath, fu.getUpdatedBy(),
						fu.getLastUpdatedTs(), fu.getFileBinId(), fu.getFileAssociationId());
				fileUploadExportVOList.add(fileUploadExportVO);
				if (new File(folderLocation + File.separator + Constant.FILES_UPLOAD_DIRECTORY_NAME + File.separator
						+ exportPath + File.separator).exists() == false) {
					File fileDirectory = new File(folderLocation + File.separator + Constant.FILES_UPLOAD_DIRECTORY_NAME
							+ File.separator + exportPath + File.separator);
					fileDirectory.mkdirs();
				}
				Path		downloadPath	= Paths.get(folderLocation + File.separator
						+ Constant.FILES_UPLOAD_DIRECTORY_NAME + File.separator + exportPath + File.separator);

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
				}
				if (in != null) {
					Files.deleteIfExists(downloadPath.resolve(fu.getPhysicalFileName()));
					Files.copy(in, downloadPath.resolve(fu.getPhysicalFileName()));
				}
				fileBinId = fu.getFileBinId();
				
			}
			Map<String, Object>	map				= new HashMap<>();
			map.put("moduleName", Constant.MasterModuleType.FILEIMPEXPDETAILS.getModuleType());
			map.put("moduleObject", fileUploadExportVOList);
			moduleDetailsMap.put(fileBinId, map);
		}
	}

	@Override
	public Object importData(String folderLocation, String uploadFileName, String uploadID, Object importObject)
			throws Exception {
		FileUploadImportEntity	fileUploadImportEntity	= null;
		List<FileUpload>		fileUploads				= new ArrayList<>();
		List<FileUploadExportVO>		filsImpExpVOs			= (List<FileUploadExportVO>) importObject;
		for (FileUploadExportVO fueVO : filsImpExpVOs) {
			FileUpload fu = new FileUpload(fueVO.getFileUploadId(), fueVO.getPhysicalFileName(),
					fueVO.getOriginalFileName(), fueVO.getFilePath(), fueVO.getUpdatedBy(), fueVO.getFileBinId(),
					fueVO.getFileAssociationId());
			fileUploads.add(fu);
			fileUploadImportEntity = new FileUploadImportEntity(fileUploads);
		}
		return fileUploadImportEntity;
	}

	public Map<String, Map<String, Object>> getModuleDetailsMap() {
		return moduleDetailsMap;
	}

	public void setModuleDetailsMap(Map<String, Map<String, Object>> moduleDetailsMap) {
		this.moduleDetailsMap = moduleDetailsMap;
	}
	
	private Boolean checkFileExist(Object obj) throws MalformedURLException, IOException, FileNotFoundException {
		FileUpload				fu			= (FileUpload) obj;
		Path		fileRoot		= Paths.get(fu.getFilePath());
		Path		filePath		= fileRoot.resolve(fu.getPhysicalFileName());
		Resource	resource		= new UrlResource(filePath.toUri());
		InputStream	in;
		if (resource.exists() || resource.isReadable()) {
			File newFile = resource.getFile();
			in = new FileInputStream(newFile);
		} else {
			String filePathStr = fu.getFilePath() + "/" + fu.getPhysicalFileName();
			in = FilesStorageServiceImpl.class.getResourceAsStream(filePathStr);
		}
		if (in != null) {
			return Boolean.TRUE;
		}else {
			return Boolean.FALSE;
		}
	}

}
