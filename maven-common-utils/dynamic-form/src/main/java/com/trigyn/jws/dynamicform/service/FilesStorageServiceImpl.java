package com.trigyn.jws.dynamicform.service;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.stream.Stream;

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
			logger.error("Error while init of file stoarage ", execption.getMessage());
		}

	}

	@Override
	public String save(MultipartFile file) {
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
			FileUpload fileUpload = saveFileDetails(location.toString(), file.getOriginalFilename());
			Path root = Paths.get(location.toString());
			Files.copy(file.getInputStream(), root.resolve(fileUpload.getPhysicalFileName()));
			CryptoUtils.encrypt(JWS_SALT, root.resolve(fileUpload.getPhysicalFileName()).toFile(), root.resolve(fileUpload.getPhysicalFileName()).toFile());
			return fileUpload.getFileUploadId();
		} catch (Exception exception) {
			logger.error("Could not store the file. Error: ", exception);
			return null;
		}
	}

	private FileUpload saveFileDetails(String location, String originalFilename) {
		UserDetailsVO userDetailsVO = userdetailsService.getUserDetails();
		FileUpload fileUpload = new FileUpload();
		fileUpload.setFilePath(location);
		fileUpload.setOriginalFileName(originalFilename);
		fileUpload.setPhysicalFileName(UUID.randomUUID().toString());
		fileUpload.setUpdatedBy(userDetailsVO.getUserName());
		return fileUploadRepository.save(fileUpload);
	}

	@Override
	public File load(String fileUploadId) {
		try {
			FileUpload fileUploadDetails = fileUploadRepository.findById(fileUploadId)
						.orElseThrow(() -> new Exception("file not found with id : " + fileUploadId));
			Path root = Paths.get(fileUploadDetails.getFilePath());
			Path file = root.resolve(fileUploadDetails.getPhysicalFileName());
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				File file2 = File.createTempFile("aman", ".tmp");
				CryptoUtils.decrypt(JWS_SALT, resource.getFile(), file2);
				return file2;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (Exception e) {
			logger.error("Error: ", e.getMessage());
		}
		return null;
	}

	@Override
	public void deleteAll() {
		try {
			String fileUploadDir = propertyMasterService.findPropertyMasterValue("file-upload-location");
			Path root = Paths.get(fileUploadDir);
			FileSystemUtils.deleteRecursively(root.toFile());
		} catch (Exception e) {
			logger.error("Error: ", e.getMessage());
		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			String fileUploadDir = propertyMasterService.findPropertyMasterValue("file-upload-location");
			Path root = Paths.get(fileUploadDir);
			return Files.walk(root, 1).filter(path -> !path.equals(root)).map(root::relativize);
		} catch (Exception e) {
			logger.error("Could not load the files!");
		}
		return null;
	}

}
