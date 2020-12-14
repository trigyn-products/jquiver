package com.trigyn.jws.dynamicform.service;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.vo.FileInfo;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynamicform.dao.FileUploadRepository;
import com.trigyn.jws.dynamicform.entities.FileUpload;
import com.trigyn.jws.dynamicform.utils.CryptoUtils;

@Component
@Qualifier(value = "file-system-storage")
@Transactional
public class FilesStorageServiceImpl implements FilesStorageService {

	private static final Logger logger = LogManager.getLogger(FilesStorageServiceImpl.class);

	@Autowired
	private PropertyMasterService propertyMasterService = null;
	
	@Autowired
	private IUserDetailsService userdetailsService 		= null;
	
	@Autowired
	private FileUploadRepository fileUploadRepository 	= null;
	
	private final static String JWS_SALT = "main alag duniya";

	@Override
	public void init() {
		String fileUploadDir = null;
		try {
			fileUploadDir = propertyMasterService.findPropertyMasterValue("file-upload-location");
			Files.createDirectory(Paths.get(fileUploadDir));
		} catch (Exception execption) {
			logger.warn("Error while init of file storage ", execption.getMessage());
		}

	}

	@Override
	public String save(MultipartFile file, String fileConfigId) {
		try {
			LocalDate localDate = LocalDate.now();
			Integer year = localDate.getYear();
			Integer month = localDate.getMonthValue();
			Integer date = localDate.getDayOfMonth();
			String fileUploadDir = propertyMasterService.findPropertyMasterValue("file-upload-location");
			StringJoiner location = new StringJoiner(""+File.separatorChar);
			location.add(fileUploadDir);
			location.add(year.toString()).add(month.toString()).add(date.toString());
			if(Boolean.FALSE.equals(new File(location.toString()).exists())) {
				Files.createDirectories(Paths.get(location.toString()));
			}
			FileUpload fileUpload = saveFileDetails(location.toString(), file.getOriginalFilename(), fileConfigId);
			Path root = Paths.get(location.toString());
			Files.copy(file.getInputStream(), root.resolve(fileUpload.getPhysicalFileName()));
			CryptoUtils.encrypt(JWS_SALT, root.resolve(fileUpload.getPhysicalFileName()).toFile(), root.resolve(fileUpload.getPhysicalFileName()).toFile());
			return fileUpload.getFileUploadId();
		} catch (Exception exception) {
			logger.error("Could not store the file. Error: ", exception);
			return null;
		}
	}

	private FileUpload saveFileDetails(String location, String originalFilename, String fileConfigId) {
		UserDetailsVO userDetailsVO = userdetailsService.getUserDetails();
		FileUpload fileUpload = new FileUpload();
		fileUpload.setFilePath(location);
		fileUpload.setOriginalFileName(originalFilename);
		fileUpload.setPhysicalFileName(UUID.randomUUID().toString());
		fileUpload.setUpdatedBy(userDetailsVO.getUserName());
		fileUpload.setFileConfigId(fileConfigId);
		return fileUploadRepository.save(fileUpload);
	}

	@Override
	public Map<String, Object> load(String fileUploadId) {
		try {
			Map<String, Object> details = new HashMap<>();
			FileUpload fileUploadDetails = fileUploadRepository.findById(fileUploadId)
						.orElseThrow(() -> new Exception("file not found with id : " + fileUploadId));
			Path root = Paths.get(fileUploadDetails.getFilePath());
			Path filePath = root.resolve(fileUploadDetails.getPhysicalFileName());
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists() || resource.isReadable()) {
				File newFile = resource.getFile();
				byte[] newFiles = CryptoUtils.decrypt(JWS_SALT, newFile, null);
				details.put("file", newFiles);
				details.put("fileName", fileUploadDetails.getOriginalFileName());
				return details;
			} else {
				String filePathStr = fileUploadDetails.getFilePath() + "/"+ fileUploadDetails.getPhysicalFileName();
				URL fileURL = FilesStorageServiceImpl.class.getResource(filePathStr);
				if (fileURL == null) {
					throw new RuntimeException("Could not read the file!");
				}else {
					URI fileURI = fileURL.toURI();
					File file = new File(fileURI);
					byte[] fileByteArray = CryptoUtils.decrypt(JWS_SALT, file, null);
					details.put("file", fileByteArray);
					details.put("fileName", fileUploadDetails.getOriginalFileName());
					return details;
				}
			}
		} catch (Exception e) {
			logger.error("Error: ", e.getMessage());
		}
		return null;
	}

	@Override
	public void deleteAll() {
		try {
			fileUploadRepository.deleteAll();
			String fileUploadDir = propertyMasterService.findPropertyMasterValue("file-upload-location");
			Path root = Paths.get(fileUploadDir);
			FileSystemUtils.deleteRecursively(root.toFile());
		} catch (Exception e) {
			logger.error("Error: ", e.getMessage());
		}
	}

	@Override
	public List<FileInfo> loadAll() {
		try {
			List<FileUpload> fileUploads = fileUploadRepository.findAll();
			List<FileInfo> fileInfos = fileUploads.stream().map(files -> {
				File file = new File(files.getFilePath());
				return new FileInfo(files.getFileUploadId(), files.getOriginalFileName(), file.length());
			}).collect(Collectors.toList());
			return fileInfos;
		} catch (Exception e) {
			logger.error("Could not load the files!");
		}
		return null;
	}

	@Override
	public List<FileInfo> getFileDetailsByIds(List<String> fileIdList) {
		List<FileUpload> fileUploads = fileUploadRepository.findAllByIds(fileIdList);
		List<FileInfo> fileInfos = fileUploads.stream().map(files -> {
			File file = new File(files.getFilePath());
			return new FileInfo(files.getFileUploadId(), files.getOriginalFileName(), file.length());
		}).collect(Collectors.toList());
		return fileInfos;
	}

	@Override
	public void deleteFileById(String fileId) throws Exception {
		FileUpload fileUploadDetails = fileUploadRepository.findById(fileId)
				.orElseThrow(() -> new Exception("file not found with id : " + fileId));
		Path root = Paths.get(fileUploadDetails.getFilePath());
		Path filePath = root.resolve(fileUploadDetails.getPhysicalFileName());
		Resource resource = new UrlResource(filePath.toUri());
		if (resource.exists() || resource.isReadable()) {
			fileUploadRepository.deleteById(fileId);
			resource.getFile().delete();
		}
	}

}
